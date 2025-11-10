export interface Node {
  id: string;
  name: string;
  level: number;
  zones?: Node[];
}

export interface Nodes {
  zones: Node[];
}

export interface DropdownConfig {
  dataUrl?: string;
  country?: string;
  language?: string;
  placeholder?: string;
  username?: string;
  password?: string;
  enableSearch?: boolean;
  searchPlaceholder?: string;
}

export interface CountryInfo {
  code: string;
  name: string;
  file: string;
}
