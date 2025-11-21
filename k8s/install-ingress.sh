#!/bin/bash

echo "Instalando NGINX Ingress Controller..."

# Para minikube
if command -v minikube &> /dev/null; then
    echo "Detectado Minikube - habilitando addon ingress"
    minikube addons enable ingress
    echo "✅ Ingress habilitado no Minikube"
    echo "Para obter IP: minikube ip"
    exit 0
fi

# Para kind
if command -v kind &> /dev/null; then
    echo "Detectado Kind - instalando NGINX Ingress"
    kubectl apply -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/main/deploy/static/provider/kind/deploy.yaml
    echo "Aguardando Ingress Controller..."
    kubectl wait --namespace ingress-nginx --for=condition=ready pod --selector=app.kubernetes.io/component=controller --timeout=90s
    echo "✅ Ingress instalado no Kind"
    exit 0
fi

# Para Docker Desktop
echo "Instalando NGINX Ingress Controller genérico..."
kubectl apply -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/controller-v1.8.2/deploy/static/provider/cloud/deploy.yaml

echo "Aguardando Ingress Controller..."
kubectl wait --namespace ingress-nginx --for=condition=ready pod --selector=app.kubernetes.io/component=controller --timeout=90s

echo "✅ Ingress Controller instalado!"
echo "Adicione ao /etc/hosts: 127.0.0.1 psicologia.local"