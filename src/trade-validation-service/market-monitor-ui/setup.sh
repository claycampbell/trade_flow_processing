#!/bin/bash

# Create React app with TypeScript template if not already created
if [ ! -f "package.json" ]; then
  npx create-react-app . --template typescript
fi

# Install dependencies
npm install @mui/material @emotion/react @emotion/styled
npm install @mui/icons-material
npm install @types/react @types/react-dom @types/node
npm install recharts @types/recharts
npm install axios
npm install @mui/lab
npm install date-fns

# Install dev dependencies
npm install --save-dev @testing-library/react @testing-library/jest-dom @testing-library/user-event
npm install --save-dev @types/jest

# Fix potential vulnerabilities
npm audit fix

echo "Dependencies installed successfully!"