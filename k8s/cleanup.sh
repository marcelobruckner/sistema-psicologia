#!/bin/bash

echo "Removendo recursos do Sistema Psicologia do Kubernetes..."

# Remover ingress
kubectl delete -f ingress.yaml --ignore-not-found=true

# Remover frontend
kubectl delete -f frontend.yaml --ignore-not-found=true

# Remover backend
kubectl delete -f backend.yaml --ignore-not-found=true

# Remover postgres
kubectl delete -f postgres.yaml --ignore-not-found=true

# Remover configmaps e secrets
kubectl delete -f postgres-init-configmap.yaml --ignore-not-found=true
kubectl delete -f configmap.yaml --ignore-not-found=true
kubectl delete -f secret.yaml --ignore-not-found=true

# Remover namespace (isso remove tudo dentro dele)
kubectl delete -f namespace.yaml --ignore-not-found=true

echo "✅ Todos os recursos foram removidos!"
echo "Para verificar: kubectl get all -n psicologia"