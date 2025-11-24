import React from 'react';
import { DropdownConfig } from './types';
import { CitiesDropdownModel0, CitiesDropdownModel1 } from './models';
import './CitiesDropdown.css';

interface CitiesDropdownProps extends DropdownConfig {
  data?: import('./types').Nodes;
  onSelect?: (node: import('./types').Node) => void;
  className?: string;
}

export const CitiesDropdown: React.FC<CitiesDropdownProps> = ({
  model = 0,
  ...props
}) => {
  // Route to the appropriate model component
  switch (model) {
    case 1:
      return <CitiesDropdownModel1 {...props} />;
    case 0:
    default:
      return <CitiesDropdownModel0 {...props} />;
  }
};
