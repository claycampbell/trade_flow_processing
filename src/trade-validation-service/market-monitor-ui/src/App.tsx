import React from 'react';
import { ThemeProvider, createTheme, CssBaseline } from '@mui/material';
import { Box, Container, AppBar, Toolbar, Typography, IconButton } from '@mui/material';
import { Brightness4, Brightness7 } from '@mui/icons-material';
import MarketDashboard from './components/MarketDashboard';

const App: React.FC = () => {
  const [darkMode, setDarkMode] = React.useState<boolean>(false);

  const theme = createTheme({
    palette: {
      mode: darkMode ? 'dark' : 'light',
      primary: {
        main: '#1976d2',
      },
      secondary: {
        main: '#dc004e',
      },
      background: {
        default: darkMode ? '#303030' : '#f5f5f5',
        paper: darkMode ? '#424242' : '#ffffff',
      },
    },
    typography: {
      h4: {
        fontWeight: 600,
      },
      h6: {
        fontWeight: 600,
      },
    },
    components: {
      MuiPaper: {
        styleOverrides: {
          root: {
            transition: 'all 0.3s ease-in-out',
          },
        },
      },
    },
  });

  const toggleDarkMode = () => {
    setDarkMode(!darkMode);
  };

  return (
    <ThemeProvider theme={theme}>
      <CssBaseline />
      <Box sx={{ flexGrow: 1, height: '100vh', overflow: 'hidden' }}>
        <AppBar position="static" color="primary">
          <Toolbar>
            <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
              Market Monitor
            </Typography>
            <IconButton
              edge="end"
              color="inherit"
              onClick={toggleDarkMode}
              aria-label="toggle dark mode"
            >
              {darkMode ? <Brightness7 /> : <Brightness4 />}
            </IconButton>
          </Toolbar>
        </AppBar>
        <Container maxWidth="xl" sx={{ mt: 3, height: 'calc(100vh - 64px)', overflow: 'auto' }}>
          <MarketDashboard />
        </Container>
      </Box>
    </ThemeProvider>
  );
};

export default App;