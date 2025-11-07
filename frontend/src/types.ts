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
  dataSource: 'local' | 'remote';
  localPath?: string;
  remoteUrl?: string;
  username?: string;
  password?: string;
  startFromCountry?: boolean;
}

export interface CountryInfo {
  code: string;
  name: string;
  file: string;
}
