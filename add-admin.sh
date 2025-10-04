#!/bin/bash

# Script para adicionar usuário admin
# Uso: ./add-admin.sh

echo "Adicionando usuário admin..."

# Executar via Docker (se o container estiver rodando)
if docker ps | grep -q "psicologia-db"; then
    echo "Executando via Docker..."
    docker exec -i psicologia-db psql -U admin -d psicologia < database/add-admin-user.sql
    echo "✅ Usuário admin adicionado via Docker!"
    echo "Login: admin"
    echo "Senha: teste123"
else
    echo "Container do banco não encontrado. Tentando conexão local..."
    # Executar via psql local (se disponível)
    if command -v psql &> /dev/null; then
        PGPASSWORD=admin123 psql -h localhost -U admin -d psicologia -f database/add-admin-user.sql
        echo "✅ Usuário admin adicionado via psql local!"
        echo "Login: admin"
        echo "Senha: teste123"
    else
        echo "❌ Nem Docker nem psql encontrados."
        echo "Execute manualmente:"
        echo "docker exec -i psicologia-db psql -U admin -d psicologia < database/add-admin-user.sql"
    fi
fi