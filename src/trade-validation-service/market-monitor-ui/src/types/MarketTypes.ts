export type MarketState =
  | 'NORMAL'
  | 'VOLATILE'
  | 'TRENDING_UP'
  | 'TRENDING_DOWN'
  | 'LOW_LIQUIDITY'
  | 'HIGH_LIQUIDITY'
  | 'MARKET_EVENT';

export interface SymbolDetails {
  symbol: string;
  price: number;
  change: number;
  volume: number;
  timestamp: number;
  bid: number;
  ask: number;
  lastTradeSize: number;
}

export interface MarketData {
  state: MarketState;
  timestamp: number;
  volatility: number;
  trendStrength: number;
  tradingVolume: number;
  currentPrices: Record<string, number>;
  priceChanges: Record<string, number>;
  symbolDetails: Record<string, SymbolDetails>;
}

export interface MarketEvent {
  type: string;
  severity: 'INFO' | 'WARNING' | 'CRITICAL';
  message: string;
  timestamp: number;
  affectedSymbols: string[];
  metadata: Record<string, any>;
}

export interface MarketStatus {
  isOpen: boolean;
  nextOpenTime?: number;
  nextCloseTime?: number;
  tradingDay: string;
  timezone: string;
}

export interface MarketStats {
  totalVolume: number;
  averagePrice: number;
  highPrice: number;
  lowPrice: number;
  volatilityIndex: number;
  trendDirection: number;
  lastUpdate: number;
}

export interface TradingSession {
  sessionId: string;
  startTime: number;
  endTime?: number;
  volume: number;
  trades: number;
  vwap: number; // Volume Weighted Average Price
}

// API Response Types
export interface MarketDataResponse {
  success: boolean;
  data: MarketData;
  timestamp: number;
}

export interface MarketEventResponse {
  success: boolean;
  events: MarketEvent[];
  timestamp: number;
}

export interface MarketStatusResponse {
  success: boolean;
  status: MarketStatus;
  timestamp: number;
}

export interface ApiError {
  success: false;
  error: string;
  code: string;
  timestamp: number;
}