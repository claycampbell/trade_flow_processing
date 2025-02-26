import React from 'react';
import { Box, Paper, Typography, Grid, CircularProgress, Alert } from '@mui/material';
import { useMarketData } from '../hooks/useMarketData';
import { marketStateColors } from '../styles';

const POLL_INTERVAL = 1000; // 1 second

export const MarketOverview: React.FC = () => {
  const {
    marketData,
    symbolData,
    marketState,
    error,
    isLoading,
    availableSymbols,
    subscribeToSymbol,
    unsubscribeFromSymbol
  } = useMarketData({ pollInterval: POLL_INTERVAL });

  React.useEffect(() => {
    // Subscribe to first 5 available symbols
    availableSymbols.slice(0, 5).forEach(symbol => {
      subscribeToSymbol(symbol);
    });

    return () => {
      availableSymbols.forEach(symbol => {
        unsubscribeFromSymbol(symbol);
      });
    };
  }, [availableSymbols]);

  if (isLoading) {
    return (
      <Box display="flex" justifyContent="center" alignItems="center" minHeight="200px">
        <CircularProgress />
      </Box>
    );
  }

  if (error) {
    return (
      <Box m={2}>
        <Alert severity="error">{error}</Alert>
      </Box>
    );
  }

  if (!marketData || !marketState) {
    return (
      <Box m={2}>
        <Alert severity="info">No market data available</Alert>
      </Box>
    );
  }

  return (
    <Box p={3}>
      <Paper
        sx={{
          p: 2,
          mb: 3,
          backgroundColor: marketStateColors[marketState] + '20',
          transition: 'background-color 0.3s ease'
        }}
      >
        <Typography variant="h5" gutterBottom>
          Market State: {marketState}
        </Typography>
        <Typography>
          Volatility: {(marketData.volatility * 100).toFixed(2)}%
        </Typography>
        <Typography>
          Trend Strength: {(marketData.trendStrength * 100).toFixed(2)}%
        </Typography>
        <Typography>
          Trading Volume: {marketData.tradingVolume.toFixed(2)}
        </Typography>
      </Paper>

      <Typography variant="h6" gutterBottom>
        Active Symbols
      </Typography>
      <Grid container spacing={2}>
        {Object.entries(symbolData).map(([symbol, data]) => (
          <Grid item xs={12} sm={6} md={4} key={symbol}>
            <Paper sx={{ p: 2 }}>
              <Typography variant="h6">{symbol}</Typography>
              <Typography variant="h4">
                ${data.price.toFixed(2)}
              </Typography>
              <Typography
                color={data.change >= 0 ? 'success.main' : 'error.main'}
              >
                {(data.change * 100).toFixed(2)}%
              </Typography>
              <Typography variant="caption" display="block">
                Last Update: {new Date(data.timestamp).toLocaleTimeString()}
              </Typography>
            </Paper>
          </Grid>
        ))}
      </Grid>
    </Box>
  );
};