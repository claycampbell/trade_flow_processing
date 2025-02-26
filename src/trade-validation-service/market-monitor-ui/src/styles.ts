import { Theme } from '@mui/material/styles';

export const globalStyles = (theme: Theme) => ({
  '*': {
    boxSizing: 'border-box',
    margin: 0,
    padding: 0,
  },
  'html, body': {
    height: '100%',
    width: '100%',
    fontFamily: 'Roboto, sans-serif',
    WebkitFontSmoothing: 'antialiased',
    MozOsxFontSmoothing: 'grayscale',
  },
  '#root': {
    height: '100%',
    width: '100%',
  },
  '.recharts-wrapper': {
    width: '100% !important',
    height: '100% !important',
  },
  '.recharts-surface': {
    width: '100% !important',
    height: '100% !important',
  },
  '.MuiCard-root': {
    transition: 'transform 0.2s ease-in-out',
    '&:hover': {
      transform: 'translateY(-2px)',
    },
  },
  '::-webkit-scrollbar': {
    width: '8px',
    height: '8px',
  },
  '::-webkit-scrollbar-track': {
    background: theme.palette.mode === 'dark' ? '#424242' : '#f1f1f1',
    borderRadius: '4px',
  },
  '::-webkit-scrollbar-thumb': {
    background: theme.palette.mode === 'dark' ? '#686868' : '#888',
    borderRadius: '4px',
    '&:hover': {
      background: theme.palette.mode === 'dark' ? '#828282' : '#555',
    },
  },
  '@keyframes pulse': {
    '0%': {
      opacity: 1,
    },
    '50%': {
      opacity: 0.5,
    },
    '100%': {
      opacity: 1,
    },
  },
  '.market-event-flash': {
    animation: 'pulse 1s infinite',
  },
});

export const marketStateColors = {
  NORMAL: '#4caf50',
  VOLATILE: '#ff9800',
  TRENDING_UP: '#2196f3',
  TRENDING_DOWN: '#f44336',
  LOW_LIQUIDITY: '#9c27b0',
  HIGH_LIQUIDITY: '#00bcd4',
  MARKET_EVENT: '#e91e63',
};