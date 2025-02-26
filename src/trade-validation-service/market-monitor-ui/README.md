# Market Monitor UI

Real-time market monitoring dashboard for the trade validation service.

## Features

- Real-time market state visualization
- Price movement charts
- Market event monitoring
- Dark/light theme support
- Responsive design
- Symbol-specific detailed views

## Setup

1. Install Node.js (v14+ recommended)
2. Clone the repository
3. Navigate to the project directory:
   ```bash
   cd src/trade-validation-service/market-monitor-ui
   ```
4. Make the setup script executable:
   ```bash
   chmod +x setup.sh
   ```
5. Run the setup script:
   ```bash
   ./setup.sh
   ```

## Development

Start the development server:
```bash
npm start
```

The application will be available at http://localhost:3000

## Building for Production

Create a production build:
```bash
npm run build
```

The build artifacts will be stored in the `build/` directory.

## Integration with Backend

The UI connects to the trade validation service endpoints:

- Market state: `/api/v1/market/state`
- Symbol details: `/api/v1/market/symbol/{symbol}`
- Available symbols: `/api/v1/market/symbols`
- Market events: `/api/v1/market/event`

The proxy is configured to forward requests to the backend service running on port 8081.

## Technologies Used

- React 18
- TypeScript 4.9+
- Material-UI 5
- Recharts
- Axios
- Date-fns

## Project Structure

```
market-monitor-ui/
├── public/
│   ├── index.html
│   └── manifest.json
├── src/
│   ├── components/           # React components
│   ├── types/               # TypeScript types
│   ├── styles.ts           # Global styles
│   ├── App.tsx             # Main application component
│   └── index.tsx           # Application entry point
├── package.json
├── tsconfig.json
└── setup.sh                # Dependency installation script
```

## Contributing

1. Follow TypeScript best practices
2. Use Material-UI components for consistency
3. Update documentation when adding new features
4. Add appropriate tests for new components

## Running Tests

```bash
npm test
```

## Known Issues

See the Issues section in the repository for current known issues and feature requests.