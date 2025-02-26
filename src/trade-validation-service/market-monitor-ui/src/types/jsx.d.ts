import * as React from 'react';

declare global {
  namespace JSX {
    interface IntrinsicElements {
      [elemName: string]: any;
    }
  }

  interface Window {
    ResizeObserver: typeof ResizeObserver;
    IntersectionObserver: typeof IntersectionObserver;
  }
}

declare module 'react' {
  interface HTMLAttributes<T> extends AriaAttributes, DOMAttributes<T> {
    // Add custom attributes here
    severity?: string;
    role?: string;
  }
}

declare module '@testing-library/react' {
  export * from '@testing-library/react/types';
}

declare module '@testing-library/jest-dom' {
  export * from '@testing-library/jest-dom/matchers';
}

declare module '@jest/globals' {
  export const jest: typeof import('@jest/globals').jest;
  export const describe: typeof import('@jest/globals').describe;
  export const expect: typeof import('@jest/globals').expect;
  export const it: typeof import('@jest/globals').it;
  export const beforeEach: typeof import('@jest/globals').beforeEach;
  export const afterEach: typeof import('@jest/globals').afterEach;
  export const beforeAll: typeof import('@jest/globals').beforeAll;
  export const afterAll: typeof import('@jest/globals').afterAll;
}

declare module '@mui/material' {
  export interface AlertProps extends React.HTMLAttributes<HTMLDivElement> {
    severity?: 'error' | 'warning' | 'info' | 'success';
    children?: React.ReactNode;
  }
  
  export interface CircularProgressProps extends React.HTMLAttributes<HTMLDivElement> {
    size?: number | string;
    thickness?: number;
    value?: number;
    variant?: 'determinate' | 'indeterminate';
  }
}