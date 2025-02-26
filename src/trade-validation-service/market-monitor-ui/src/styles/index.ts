export const marketStateColors = {
  NORMAL: '#4caf50',       // Green
  VOLATILE: '#ff9800',     // Orange
  TRENDING_UP: '#2196f3',  // Blue
  TRENDING_DOWN: '#f44336', // Red
  LOW_LIQUIDITY: '#9e9e9e', // Grey
  HIGH_LIQUIDITY: '#00bcd4', // Cyan
  MARKET_EVENT: '#e91e63',  // Pink
};

export type MarketStateColor = keyof typeof marketStateColors;

// Theme-specific color variations
export const marketStateAlphas = {
  hover: '20',  // 20% opacity
  selected: '40', // 40% opacity
  background: '10', // 10% opacity
  border: '50',  // 50% opacity
};

// Helper function to get color with alpha
export const getMarketStateColor = (
  state: MarketStateColor,
  alpha: keyof typeof marketStateAlphas = 'background'
) => `${marketStateColors[state]}${marketStateAlphas[alpha]}`;

// Common styles
export const commonStyles = {
  flexCenter: {
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
  },
  flexBetween: {
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'space-between',
  },
  flexColumn: {
    display: 'flex',
    flexDirection: 'column' as const,
  },
  paper: {
    padding: '16px',
    margin: '8px',
    borderRadius: '4px',
  },
  errorText: {
    color: marketStateColors.TRENDING_DOWN,
  },
  successText: {
    color: marketStateColors.NORMAL,
  },
  warningText: {
    color: marketStateColors.VOLATILE,
  },
  infoText: {
    color: marketStateColors.TRENDING_UP,
  },
};