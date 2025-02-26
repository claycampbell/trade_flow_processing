import axios, { AxiosInstance } from 'axios';
import { MarketData, MarketState, SymbolDetails } from '../types/MarketTypes';

export class MarketDataService {
  private static instance: MarketDataService;
  private readonly client: AxiosInstance;
  private pollInterval: number = 1000; // Default 1 second
  private pollingTimers: Map<string, NodeJS.Timeout> = new Map();
  private subscribers: Map<string, Set<(data: any) => void>> = new Map();

  private constructor() {
    this.client = axios.create({
      baseURL: process.env.REACT_APP_API_URL || 'http://localhost:8081',
      timeout: 5000,
      headers: {
        'Content-Type': 'application/json',
      },
    });
  }

  public static getInstance(): MarketDataService {
    if (!MarketDataService.instance) {
      MarketDataService.instance = new MarketDataService();
    }
    return MarketDataService.instance;
  }

  public setPollInterval(interval: number): void {
    this.pollInterval = interval;
    // Restart all active polling with new interval
    this.pollingTimers.forEach((timer, endpoint) => {
      clearInterval(timer);
      if (this.subscribers.get(endpoint)?.size) {
        this.startPolling(endpoint);
      }
    });
  }

  public subscribe(endpoint: string, callback: (data: any) => void): () => void {
    if (!this.subscribers.has(endpoint)) {
      this.subscribers.set(endpoint, new Set());
    }
    this.subscribers.get(endpoint)?.add(callback);

    // Start polling if this is the first subscriber
    if (this.subscribers.get(endpoint)?.size === 1) {
      this.startPolling(endpoint);
    }

    // Return unsubscribe function
    return () => {
      this.subscribers.get(endpoint)?.delete(callback);
      // Stop polling if no more subscribers
      if (this.subscribers.get(endpoint)?.size === 0) {
        this.stopPolling(endpoint);
      }
    };
  }

  private startPolling(endpoint: string): void {
    if (this.pollingTimers.has(endpoint)) {
      return;
    }

    const poll = async () => {
      try {
        const response = await this.client.get(endpoint);
        this.subscribers.get(endpoint)?.forEach(callback => callback(response.data));
      } catch (error) {
        console.error(`Polling error for ${endpoint}:`, error);
        // Notify subscribers of error
        this.subscribers.get(endpoint)?.forEach(callback => 
          callback({ error: 'Failed to fetch market data' })
        );
      }
    };

    // Initial poll
    poll();
    // Set up interval
    const timer = setInterval(poll, this.pollInterval);
    this.pollingTimers.set(endpoint, timer);
  }

  private stopPolling(endpoint: string): void {
    const timer = this.pollingTimers.get(endpoint);
    if (timer) {
      clearInterval(timer);
      this.pollingTimers.delete(endpoint);
    }
  }

  // Convenience methods for specific market data
  public subscribeToMarketState(callback: (data: MarketData) => void): () => void {
    return this.subscribe('/api/v1/market/state', callback);
  }

  public subscribeToSymbol(symbol: string, callback: (data: SymbolDetails) => void): () => void {
    return this.subscribe(`/api/v1/market/symbol/${symbol}`, callback);
  }

  public async getAvailableSymbols(): Promise<string[]> {
    try {
      const response = await this.client.get('/api/v1/market/symbols');
      return response.data;
    } catch (error) {
      console.error('Failed to fetch available symbols:', error);
      return [];
    }
  }

  public async getCurrentMarketState(): Promise<MarketState | null> {
    try {
      const response = await this.client.get('/api/v1/market/state');
      return response.data.state;
    } catch (error) {
      console.error('Failed to fetch market state:', error);
      return null;
    }
  }
}