# Sistema de Controle de Pacientes - Psicologia

Sistema completo para gestão de pacientes e prontuários médicos para psicólogos.

## Arquitetura

- **Backend**: Spring Boot 3.2.0 + PostgreSQL + JWT Authentication
- **Frontend**: Angular 17 + Angular Material
- **Containerização**: Docker + Docker Compose
- **Java**: Versão 17

## Estrutura do Projeto

```
sistema-psicologia/
├── backend/          # API REST Spring Boot
│   ├── src/main/java/com/psicologia/
│   │   ├── config/           # Configurações (Security, JWT)
│   │   ├── controller/       # Controllers REST
│   │   ├── dto/             # Data Transfer Objects
│   │   ├── entity/          # Entidades JPA
│   │   ├── exception/       # Tratamento de exceções
│   │   ├── repository/      # Repositórios JPA
│   │   ├── service/         # Lógica de negócio
│   │   └── PsicologiaApplication.java
│   ├── pom.xml
│   └── Dockerfile
├── frontend/         # SPA Angular
│   ├── src/app/
│   │   ├── components/      # Componentes reutilizáveis
│   │   ├── guards/          # Guards de autenticação
│   │   ├── models/          # Interfaces TypeScript
│   │   ├── pages/           # Páginas da aplicação
│   │   └── services/        # Serviços HTTP
│   ├── package.json
│   └── Dockerfile
├── shared/           # Tipos compartilhados
├── docker/           # Configurações Docker
└── docs/             # Documentação
```

## Dependências Principais

### Backend (pom.xml)
- Spring Boot 3.2.0 (Web, Data JPA, Security, Validation)
- PostgreSQL Driver
- JWT (jjwt 0.11.5)
- Spring Boot Test

### Frontend (package.json)
- Angular 17.0.0
- Angular Material 17.0.0
- RxJS 7.8.0
- TypeScript 5.2.0

## Configuração do Backend

### application.yml
```yaml
server:
  port: 8080

spring:
  application:
    name: sistema-psicologia
  
  datasource:
    url: jdbc:postgresql://localhost:5432/psicologia
    username: admin
    password: admin123
    driver-class-name: org.postgresql.Driver
  
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect
        format_sql: true
  
  security:
    jwt:
      secret: minha-chave-secreta-super-segura-para-jwt-tokens
      expiration: 86400000 # 24 horas

logging:
  level:
    com.psicologia: DEBUG
    org.springframework.security: DEBUG
```

## Docker Compose

```yaml
version: '3.8'

services:
  postgres:
    image: postgres:15
    container_name: psicologia-db
    environment:
      POSTGRES_DB: psicologia
      POSTGRES_USER: admin
      POSTGRES_PASSWORD: admin123
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data
    networks:
      - psicologia-network

  backend:
    build: ./backend
    container_name: psicologia-backend
    ports:
      - "8080:8080"
    environment:
      SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/psicologia
      SPRING_DATASOURCE_USERNAME: admin
      SPRING_DATASOURCE_PASSWORD: admin123
    depends_on:
      - postgres
    networks:
      - psicologia-network

  frontend:
    build: ./frontend
    container_name: psicologia-frontend
    ports:
      - "4200:80"
    depends_on:
      - backend
    networks:
      - psicologia-network

volumes:
  postgres_data:

networks:
  psicologia-network:
    driver: bridge
```

## Configuração do Banco de Dados

### PostgreSQL via Docker

```bash
# Executar PostgreSQL
docker run -d \
  --name psicologia-db \
  -e POSTGRES_DB=psicologia \
  -e POSTGRES_USER=admin \
  -e POSTGRES_PASSWORD=admin123 \
  -p 5432:5432 \
  -v psicologia_data:/var/lib/postgresql/data \
  postgres:15
```

### Comandos Úteis do Banco

```bash
# Parar o banco
docker stop psicologia-db

# Iniciar o banco
docker start psicologia-db

# Ver logs
docker logs psicologia-db

# Conectar via psql
docker exec -it psicologia-db psql -U admin -d psicologia

# Remover completamente (cuidado: apaga dados)
docker rm -f psicologia-db
docker volume rm psicologia_data
```

## Como executar

### Desenvolvimento Local
```bash
# 1. Iniciar banco de dados
docker run -d --name psicologia-db -e POSTGRES_DB=psicologia -e POSTGRES_USER=admin -e POSTGRES_PASSWORD=admin123 -p 5432:5432 -v psicologia_data:/var/lib/postgresql/data postgres:15

# 2. Executar backend
cd backend && mvn spring-boot:run

# 3. Executar frontend
cd frontend && npm install && ng serve
```

### Docker Compose (Ambiente completo)
```bash
docker-compose up -d
```

### Containers Docker Individuais

#### 1. Criar rede e volume
```bash
docker network create psicologia-network
docker volume create psicologia_data
```

#### 2. PostgreSQL Database
```bash
docker run -d \
  --name psicologia-db \
  --network psicologia-network \
  -p 5432:5432 \
  -e POSTGRES_DB=psicologia \
  -e POSTGRES_USER=admin \
  -e POSTGRES_PASSWORD=admin123 \
  -v psicologia_data:/var/lib/postgresql/data \
  postgres:15
```

#### 3. Backend Spring Boot
```bash
docker run -d \
  --name psicologia-backend \
  --network psicologia-network \
  -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://psicologia-db:5432/psicologia \
  -e SPRING_DATASOURCE_USERNAME=admin \
  -e SPRING_DATASOURCE_PASSWORD=admin123 \
  -e SPRING_DATASOURCE_DRIVER_CLASS_NAME=org.postgresql.Driver \
  marcelobruckner/sistema-psicologia-backend:1.0
```

#### 4. Frontend Angular
```bash
docker run -d \
  --name psicologia-frontend \
  --network psicologia-network \
  -p 4200:80 \
  marcelobruckner/sistema-psicologia-frontend:1.0
```

### Acessar a aplicação
- **Frontend**: http://localhost:4200
- **Backend API**: http://localhost:8080
- **PostgreSQL**: localhost:5432

## URLs

- **Frontend**: http://localhost:4200
- **Backend API**: http://localhost:8080
- **PostgreSQL**: localhost:5432

## Endpoints Disponíveis

### Endpoints Públicos
- **GET** `/api/health` - Status da API
- **GET** `/api/auth/test` - Teste do endpoint de autenticação
- **POST** `/api/auth/login` - Login com JWT

### Exemplo de Uso
```bash
# Testar health
curl http://localhost:8080/api/health

# Testar auth
curl http://localhost:8080/api/auth/test

# Login (ainda sem usuários cadastrados)
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"senha123"}'
```

## Frontend Angular

### Estrutura Implementada
- **AuthService** - Gerenciamento de autenticação e estado do usuário
- **AuthGuard** - Proteção de rotas autenticadas
- **LoginComponent** - Tela de login com Angular Material
- **DashboardComponent** - Tela principal após autenticação
- **Models TypeScript** - Interfaces para tipagem

### Funcionalidades
- 🔐 Sistema de login com JWT
- 🛡️ Rotas protegidas por guard
- 📱 Interface responsiva com Angular Material
- 🔄 Gerenciamento de estado reativo
- 💾 Persistência de sessão no localStorage

### Como executar o Frontend
```bash
cd frontend
npm install
ng serve
```

## Credenciais de Usuários

### Usuários Disponíveis
- **Admin**: `admin` / Senha: `123` (ADMIN - apenas visualização)
- **Psicólogo 1**: `Carolina` / Senha: `123` (PSICOLOGO - CRUD completo)
- **Psicólogo 2**: `Heloisa` / Senha: `123` (PSICOLOGO - CRUD completo)

### Diferenças de Perfil
- **ADMIN**: Pode apenas visualizar pacientes (sem botões de ação)
- **PSICOLOGO**: Pode criar, editar e excluir pacientes

## Estado Atual

- ✅ Estrutura base criada com pastas organizadas
- ✅ Configurações de banco e segurança definidas
- ✅ Docker configurado para todos os serviços
- ✅ Configuração JWT + CORS implementada
- ✅ Endpoints básicos funcionando
- ✅ Frontend Angular com autenticação implementado
- ✅ Tela de login e dashboard funcionais
- ✅ Sistema de permissões por perfil implementado
- ✅ Containers Docker individuais funcionando
- ✅ Manifestos Kubernetes criados
- ⏳ Pronto para deploy em produção

O projeto está estruturado para ser um sistema completo de gestão de pacientes e prontuários médicos para psicólogos, com autenticação JWT e interface moderna em Angular Material.