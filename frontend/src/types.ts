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
  remoteUrl?: string;
  username?: string;
  password?: string;
  placeholder?: string;
  country?: string;
}

export interface CountryInfo {
  code: string;
  name: string;
  file: string;
}
