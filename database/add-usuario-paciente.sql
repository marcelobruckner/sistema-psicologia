-- =====================================================
-- ADICIONAR RELACIONAMENTO USUÁRIO-PACIENTE
-- =====================================================

-- Adicionar coluna usuario_id na tabela pacientes
ALTER TABLE pacientes 
ADD COLUMN usuario_id UUID REFERENCES usuarios(id);

-- Atualizar pacientes existentes para o usuário admin (temporário)
UPDATE pacientes 
SET usuario_id = (SELECT id FROM usuarios WHERE username = 'admin' LIMIT 1)
WHERE usuario_id IS NULL;

-- Tornar a coluna obrigatória após atualizar dados existentes
ALTER TABLE pacientes 
ALTER COLUMN usuario_id SET NOT NULL;

-- Criar índice para performance
CREATE INDEX IF NOT EXISTS idx_pacientes_usuario ON pacientes(usuario_id);

-- Verificar as alterações
SELECT p.nome_completo, u.username as criado_por 
FROM pacientes p 
JOIN usuarios u ON p.usuario_id = u.id 
ORDER BY p.nome_completo;