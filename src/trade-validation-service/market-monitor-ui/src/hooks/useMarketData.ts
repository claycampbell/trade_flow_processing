import { useState, useEffect, useRef } from 'react';
import { MarketData, MarketState, SymbolDetails } from '../types/MarketTypes';
import { MarketDataService } from '../services/MarketDataService';

interface UseMarketDataOptions {
  pollInterval?: number;
}

interface UseMarketDataReturn {
  marketData: MarketData | null;
  symbolData: Record<string, SymbolDetails>;
  marketState: MarketState | null;
  error: string | null;
  isLoading: boolean;
  availableSymbols: string[];
  subscribeToSymbol: (symbol: string) => void;
  unsubscribeFromSymbol: (symbol: string) => void;
}

export function useMarketData(options: UseMarketDataOptions = {}): UseMarketDataReturn {
  const [marketData, setMarketData] = useState<MarketData | null>(null);
  const [symbolData, setSymbolData] = useState<Record<string, SymbolDetails>>({});
  const [marketState, setMarketState] = useState<MarketState | null>(null);
  const [error, setError] = useState<string | null>(null);
  const [isLoading, setIsLoading] = useState(true);
  const [availableSymbols, setAvailableSymbols] = useState<string[]>([]);

  // Using ref to avoid issues with cleanup functions and stale closures
  const symbolUnsubscribers = useRef(new Map<string, () => void>());

  const service = MarketDataService.getInstance();

  useEffect(() => {
    if (options.pollInterval) {
      service.setPollInterval(options.pollInterval);
    }

    // Initialize available symbols
    service.getAvailableSymbols()
      .then(symbols => setAvailableSymbols(symbols))
      .catch(() => setError('Failed to fetch available symbols'));

    // Subscribe to market state updates
    const unsubscribe = service.subscribeToMarketState((data: MarketData | { error: string }) => {
      if ('error' in data) {
        setError(data.error);
        return;
      }
      setMarketData(data);
      setMarketState(data.state);
      setError(null);
      setIsLoading(false);
    });

    return () => {
      unsubscribe();
    };
  }, [options.pollInterval]);

  const subscribeToSymbol = (symbol: string) => {
    if (symbolData[symbol]) return; // Already subscribed

    const unsubscribe = service.subscribeToSymbol(symbol, (data: SymbolDetails | { error: string }) => {
      if ('error' in data) {
        setError(`Failed to fetch data for symbol ${symbol}`);
        return;
      }
      setSymbolData((prev: Record<string, SymbolDetails>) => ({
        ...prev,
        [symbol]: data
      }));
      setError(null);
    });

    // Store unsubscribe function
    symbolUnsubscribers.current.set(symbol, unsubscribe);
  };

  const unsubscribeFromSymbol = (symbol: string) => {
    const unsubscribe = symbolUnsubscribers.current.get(symbol);
    if (unsubscribe) {
      unsubscribe();
      symbolUnsubscribers.current.delete(symbol);
      setSymbolData((prev: Record<string, SymbolDetails>) => {
        const newData = { ...prev };
        delete newData[symbol];
        return newData;
      });
    }
  };

  // Cleanup subscriptions on unmount
  useEffect(() => {
    return () => {
      symbolUnsubscribers.current.forEach(unsubscribe => unsubscribe());
    };
  }, []);

  return {
    marketData,
    symbolData,
    marketState,
    error,
    isLoading,
    availableSymbols,
    subscribeToSymbol,
    unsubscribeFromSymbol
  };
}