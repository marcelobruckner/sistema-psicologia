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

```bash
# 1. Iniciar banco de dados
docker run -d --name psicologia-db -e POSTGRES_DB=psicologia -e POSTGRES_USER=admin -e POSTGRES_PASSWORD=admin123 -p 5432:5432 -v psicologia_data:/var/lib/postgresql/data postgres:15

# 2. Executar backend
cd backend && ./mvnw spring-boot:run

# 3. Executar frontend
cd frontend && ng serve

# OU ambiente completo via Docker Compose
docker-compose up -d
```

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

## Estado Atual

- ✅ Estrutura base criada com pastas organizadas
- ✅ Configurações de banco e segurança definidas
- ✅ Docker configurado para todos os serviços
- ✅ Configuração JWT + CORS implementada
- ✅ Endpoints básicos funcionando
- ⏳ Pronto para implementação das entidades e funcionalidades específicas

O projeto está estruturado para ser um sistema completo de gestão de pacientes e prontuários médicos para psicólogos, com autenticação JWT e interface moderna em Angular Material.