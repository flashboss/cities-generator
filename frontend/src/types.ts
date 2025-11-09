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
}

export interface CountryInfo {
  code: string;
  name: string;
  file: string;
}
