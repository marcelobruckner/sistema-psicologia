# Exclusões para Code Review

Excluir os seguintes diretórios e arquivos das análises de código:

## Diretórios de Cache e Build
- `frontend/.angular/cache/`
- `frontend/node_modules/`
- `frontend/dist/`
- `backend/target/`
- `backend/.mvn/`

## Arquivos de Dependências
- `frontend/package-lock.json`
- `backend/pom.xml` (apenas vulnerabilidades de dependências)

## Arquivos de Documentação
- `README.md` (credenciais são apenas exemplos de documentação)

Focar apenas no código fonte da aplicação:
- `backend/src/`
- `frontend/src/`
- Arquivos de configuração principais