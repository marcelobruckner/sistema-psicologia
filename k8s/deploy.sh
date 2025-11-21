#!/bin/bash

echo "Deploying Sistema Psicologia to Kubernetes..."

# Aplicar namespace
kubectl apply -f namespace.yaml

# Aplicar secrets e configmaps
kubectl apply -f secret.yaml
kubectl apply -f configmap.yaml
kubectl apply -f postgres-init-configmap.yaml

# Aplicar banco de dados
kubectl apply -f postgres.yaml

# Aguardar postgres estar pronto
echo "Aguardando PostgreSQL..."
kubectl wait --for=condition=ready pod -l app=postgres -n psicologia --timeout=300s

# Verificar se postgres service está respondendo
echo "Testando conectividade do PostgreSQL..."
kubectl run postgres-test --image=postgres:15-alpine --rm -i --restart=Never -n psicologia -- pg_isready -h postgres-service -p 5432

# Aplicar backend
kubectl apply -f backend.yaml

# Aguardar backend estar pronto
echo "Aguardando Backend..."
kubectl wait --for=condition=ready pod -l app=backend -n psicologia --timeout=600s

# Aplicar frontend
kubectl apply -f frontend.yaml

# Aplicar ingress
kubectl apply -f ingress.yaml

echo "Deploy concluído!"
echo "Acesse: http://psicologia.local"
echo "Adicione ao /etc/hosts: <INGRESS_IP> psicologia.local"