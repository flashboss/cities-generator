export interface Node {
  id: string;
  name: string;
  level: number;
  zones?: Node[];
}

export interface Nodes {
  zones: Node[];
}

