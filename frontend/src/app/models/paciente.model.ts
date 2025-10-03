export interface Paciente {
  id?: string;
  nomeCompleto: string;
  cpf: string;
  rg?: string;
  dataNascimento: string;
  telefone: string;
  email?: string;
  endereco?: string;
  profissao?: string;
  estadoCivil?: string;
  contatoEmergencia?: string;
  telefoneEmergencia?: string;
  observacoes?: string;
  ativo?: boolean;
  dataCriacao?: string;
  dataAtualizacao?: string;
}

export interface PacienteRequest {
  nomeCompleto: string;
  cpf: string;
  rg?: string;
  dataNascimento: string;
  telefone: string;
  email?: string;
  endereco?: string;
  profissao?: string;
  estadoCivil?: string;
  contatoEmergencia?: string;
  telefoneEmergencia?: string;
  observacoes?: string;
}

export interface PacientePage {
  content: Paciente[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
}