-- =====================================================
-- ADICIONAR SISTEMA DE ROLES/PERFIS
-- =====================================================

-- Adicionar coluna role na tabela usuarios
ALTER TABLE usuarios 
ADD COLUMN role VARCHAR(20) NOT NULL DEFAULT 'PSICOLOGO';

-- Atualizar usuário admin existente para ter role ADMIN
UPDATE usuarios 
SET role = 'ADMIN' 
WHERE username = 'admin';

-- Verificar as alterações
SELECT id, username, email, nome_completo, role, ativo 
FROM usuarios 
ORDER BY username;