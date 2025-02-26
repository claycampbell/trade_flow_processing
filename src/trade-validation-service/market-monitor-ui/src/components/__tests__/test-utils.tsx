import React, { ReactElement } from 'react';
import { render as rtlRender } from '@testing-library/react';
import type { RenderResult, RenderOptions } from '@testing-library/react';

function render(
  ui: ReactElement,
  options?: Omit<RenderOptions, 'wrapper'>
): RenderResult {
  return rtlRender(ui, options);
}

// Re-export everything
export * from '@testing-library/react';
export { render };