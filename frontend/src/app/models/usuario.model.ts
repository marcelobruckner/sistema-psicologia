export interface Usuario {
  id?: string;
  username: string;
  email: string;
  nomeCompleto: string;
  role: Role;
  ativo?: boolean;
  dataCriacao?: string;
  dataAtualizacao?: string;
}

export interface UsuarioRequest {
  username: string;
  password: string;
  email: string;
  nomeCompleto: string;
  role: Role;
}

export interface UsuarioPage {
  content: Usuario[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
}

export enum Role {
  ADMIN = 'ADMIN',
  PSICOLOGO = 'PSICOLOGO'
}