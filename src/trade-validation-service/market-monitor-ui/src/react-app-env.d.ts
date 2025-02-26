/// <reference types="react-scripts" />

declare module '*.svg' {
  import * as React from 'react';
  export const ReactComponent: React.FunctionComponent<React.SVGProps<SVGSVGElement>>;
  const src: string;
  export default src;
}

declare module '*.png' {
  const content: string;
  export default content;
}

declare module '*.jpg' {
  const content: string;
  export default content;
}

declare module '*.gif' {
  const content: string;
  export default content;
}

declare module '*.json' {
  const content: { [key: string]: any };
  export default content;
}

declare namespace NodeJS {
  interface ProcessEnv {
    NODE_ENV: 'development' | 'production' | 'test';
    PUBLIC_URL: string;
    REACT_APP_API_URL: string;
  }
}

declare module 'styled-components' {
  export interface DefaultTheme {
    colors?: {
      primary: string;
      secondary: string;
      background: string;
      text: string;
    };
    spacing?: {
      small: string;
      medium: string;
      large: string;
    };
    breakpoints?: {
      mobile: string;
      tablet: string;
      desktop: string;
    };
  }
}