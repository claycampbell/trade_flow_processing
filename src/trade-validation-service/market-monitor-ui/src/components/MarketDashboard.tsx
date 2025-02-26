import React, { useEffect, useState } from 'react';
import { Box, Grid, Paper, Typography, CircularProgress, Alert } from '@mui/material';
import { styled } from '@mui/material/styles';
import { MarketData, MarketState, MarketStats } from '../types/MarketTypes';
import { LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, Legend } from 'recharts';

const StyledPaper = styled(Paper)(({ theme }) => ({
  padding: theme.spacing(2),
  height: '100%',
  display: 'flex',
  flexDirection: 'column',
}));

const MarketStateColors: Record<MarketState, string> = {
  NORMAL: '#4caf50',
  VOLATILE: '#ff9800',
  TRENDING_UP: '#2196f3',
  TRENDING_DOWN: '#f44336',
  LOW_LIQUIDITY: '#9c27b0',
  HIGH_LIQUIDITY: '#00bcd4',
  MARKET_EVENT: '#e91e63'
};

const MarketDashboard: React.FC = () => {
  const [marketData, setMarketData] = useState<MarketData | null>(null);
  const [marketStats, setMarketStats] = useState<MarketStats | null>(null);
  const [error, setError] = useState<string | null>(null);
  const [loading, setLoading] = useState<boolean>(true);

  useEffect(() => {
    const fetchMarketData = async () => {
      try {
        const response = await fetch('/api/v1/market/state');
        if (!response.ok) {
          throw new Error('Failed to fetch market data');
        }
        const data = await response.json();
        setMarketData(data);
        setError(null);
      } catch (err) {
        setError('Failed to fetch market data: ' + (err as Error).message);
      } finally {
        setLoading(false);
      }
    };

    fetchMarketData();
    const interval = setInterval(fetchMarketData, 1000);
    return () => clearInterval(interval);
  }, []);

  const calculateMarketStats = (data: MarketData): MarketStats => {
    const prices = Object.values(data.currentPrices);
    return {
      totalVolume: data.tradingVolume,
      averagePrice: prices.reduce((a, b) => a + b, 0) / prices.length,
      highPrice: Math.max(...prices),
      lowPrice: Math.min(...prices),
      volatilityIndex: data.volatility,
      trendDirection: data.trendStrength,
      lastUpdate: data.timestamp
    };
  };

  if (loading) {
    return (
      <Box display="flex" justifyContent="center" alignItems="center" height="100vh">
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

  if (!marketData) {
    return (
      <Box m={2}>
        <Alert severity="info">No market data available</Alert>
      </Box>
    );
  }

  const stats = calculateMarketStats(marketData);

  return (
    <Box p={3}>
      {/* Market State Overview */}
      <StyledPaper sx={{ mb: 3, backgroundColor: MarketStateColors[marketData.state] + '20' }}>
        <Typography variant="h4" gutterBottom>
          Market Status: {marketData.state}
        </Typography>
        <Typography>
          Volatility: {(marketData.volatility * 100).toFixed(2)}% | 
          Trend: {(marketData.trendStrength * 100).toFixed(2)}% | 
          Volume: {marketData.tradingVolume.toFixed(2)}
        </Typography>
      </StyledPaper>

      <Grid container spacing={3}>
        {/* Price Chart */}
        <Grid item xs={12} md={8}>
          <StyledPaper>
            <Typography variant="h6" gutterBottom>Price Movement</Typography>
            <LineChart width={800} height={400} data={Object.entries(marketData.currentPrices)
              .map(([symbol, price]) => ({ symbol, price }))}>
              <CartesianGrid strokeDasharray="3 3" />
              <XAxis dataKey="symbol" />
              <YAxis />
              <Tooltip />
              <Legend />
              <Line type="monotone" dataKey="price" stroke="#8884d8" />
            </LineChart>
          </StyledPaper>
        </Grid>

        {/* Market Statistics */}
        <Grid item xs={12} md={4}>
          <StyledPaper>
            <Typography variant="h6" gutterBottom>Market Statistics</Typography>
            <Box>
              <Typography>Average Price: ${stats.averagePrice.toFixed(2)}</Typography>
              <Typography>High: ${stats.highPrice.toFixed(2)}</Typography>
              <Typography>Low: ${stats.lowPrice.toFixed(2)}</Typography>
              <Typography>Volume: {stats.totalVolume.toFixed(2)}</Typography>
              <Typography>Volatility Index: {(stats.volatilityIndex * 100).toFixed(2)}%</Typography>
              <Typography>Trend Direction: {(stats.trendDirection * 100).toFixed(2)}%</Typography>
            </Box>
          </StyledPaper>
        </Grid>

        {/* Symbol Grid */}
        <Grid item xs={12}>
          <Grid container spacing={2}>
            {Object.entries(marketData.currentPrices).map(([symbol, price]) => (
              <Grid item xs={12} sm={6} md={4} lg={3} key={symbol}>
                <StyledPaper>
                  <Typography variant="h6">{symbol}</Typography>
                  <Typography variant="h4">${price.toFixed(2)}</Typography>
                  <Typography color={marketData.priceChanges[symbol] >= 0 ? 'success.main' : 'error.main'}>
                    {(marketData.priceChanges[symbol] * 100).toFixed(2)}%
                  </Typography>
                </StyledPaper>
              </Grid>
            ))}
          </Grid>
        </Grid>
      </Grid>
    </Box>
  );
};

export default MarketDashboard;