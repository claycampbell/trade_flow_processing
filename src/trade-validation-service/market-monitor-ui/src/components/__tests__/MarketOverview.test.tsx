import { render, screen, waitFor } from '@testing-library/react';
import '@testing-library/jest-dom';
import { jest, describe, expect, it, beforeEach } from '@jest/globals';
import { MarketOverview } from '../MarketOverview';
import { MarketDataService } from '../../services/MarketDataService';
import type { MarketState } from '../../types/MarketTypes';

// Mock MUI components
jest.mock('@mui/material', () => ({
  CircularProgress: () => <div role="progressbar">Loading...</div>,
  Alert: ({ children, severity }: { children: React.ReactNode; severity?: string }) => (
    <div role="alert" data-severity={severity}>
      {children}
    </div>
  ),
  Card: ({ children }: { children: React.ReactNode }) => (
    <div role="article">{children}</div>
  ),
  Grid: ({ children }: { children: React.ReactNode }) => (
    <div role="grid">{children}</div>
  ),
}));

// Mock the service
const mockService = {
  subscribeToMarketState: jest.fn(() => jest.fn()),
  subscribeToSymbol: jest.fn(() => jest.fn()),
  getAvailableSymbols: jest.fn().mockResolvedValue(['AAPL', 'GOOGL', 'MSFT']),
};

jest.mock('../../services/MarketDataService', () => ({
  getInstance: () => mockService,
}));

describe('MarketOverview Component', () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  it('should render loading state initially', () => {
    render(<MarketOverview />);
    expect(screen.getByRole('progressbar')).toBeInTheDocument();
  });

  it('should subscribe to market state on mount', () => {
    render(<MarketOverview />);
    expect(mockService.subscribeToMarketState).toHaveBeenCalled();
  });

  it('should subscribe to available symbols', async () => {
    render(<MarketOverview />);
    
    await waitFor(() => {
      expect(mockService.subscribeToSymbol).toHaveBeenCalledTimes(3);
    });
  });

  it('should display error state when market data fails', async () => {
    const errorMessage = 'Failed to fetch market data';
    mockService.subscribeToMarketState.mockImplementation((callback: (data: any) => void) => {
      callback({ error: errorMessage });
      return jest.fn();
    });

    render(<MarketOverview />);

    await waitFor(() => {
      expect(screen.getByText(errorMessage)).toBeInTheDocument();
    });
  });

  it('should unsubscribe from all data on unmount', () => {
    const unsubscribeMarket = jest.fn();
    const unsubscribeSymbol = jest.fn();

    mockService.subscribeToMarketState.mockReturnValue(unsubscribeMarket);
    mockService.subscribeToSymbol.mockReturnValue(unsubscribeSymbol);
    mockService.getAvailableSymbols.mockResolvedValue(['AAPL', 'GOOGL']);

    const { unmount } = render(<MarketOverview />);
    unmount();

    expect(unsubscribeMarket).toHaveBeenCalled();
    expect(unsubscribeSymbol).toHaveBeenCalledTimes(2);
  });

  it('should update market state when receiving new data', () => {
    const marketData = {
      state: 'NORMAL' as MarketState,
      volatility: 0.15,
      trendStrength: 0.8,
      timestamp: Date.now(),
    };

    mockService.subscribeToMarketState.mockImplementation((callback: (data: any) => void) => {
      callback(marketData);
      return jest.fn();
    });

    render(<MarketOverview />);
    expect(screen.getByText('Market State: NORMAL')).toBeInTheDocument();
  });
});