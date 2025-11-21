#!/bin/bash

echo "Configurando port-forward para acesso local..."

# Port-forward do frontend
echo "Frontend: http://localhost:4200"
kubectl port-forward -n psicologia service/frontend-service 4200:80 &

# Port-forward do backend  
echo "Backend API: http://localhost:8080"
kubectl port-forward -n psicologia service/backend-service 8080:8080 &

# Port-forward do PostgreSQL
echo "PostgreSQL: localhost:5432"
kubectl port-forward -n psicologia service/postgres-service 5432:5432 &

echo ""
echo "✅ Port-forwards configurados:"
echo "- Frontend: http://localhost:4200"
echo "- Backend: http://localhost:8080/api/health"
echo "- PostgreSQL: localhost:5432"
echo ""
echo "Para parar: pkill -f 'kubectl port-forward'"

wait