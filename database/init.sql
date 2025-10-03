-- =====================================================
-- SCRIPT DE INICIALIZAÇÃO - SISTEMA PSICOLOGIA
-- =====================================================

-- Criar extensão para UUID (se não existir)
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- =====================================================
-- TABELA DE USUÁRIOS
-- =====================================================
CREATE TABLE IF NOT EXISTS usuarios (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    nome_completo VARCHAR(150) NOT NULL,
    ativo BOOLEAN DEFAULT true,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- =====================================================
-- TABELA DE PACIENTES
-- =====================================================
CREATE TABLE IF NOT EXISTS pacientes (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    nome_completo VARCHAR(150) NOT NULL,
    cpf VARCHAR(14) UNIQUE,
    rg VARCHAR(20),
    data_nascimento DATE,
    telefone VARCHAR(20),
    email VARCHAR(100),
    endereco TEXT,
    profissao VARCHAR(100),
    estado_civil VARCHAR(20),
    contato_emergencia VARCHAR(150),
    telefone_emergencia VARCHAR(20),
    observacoes TEXT,
    ativo BOOLEAN DEFAULT true,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- =====================================================
-- TABELA DE PRONTUÁRIOS
-- =====================================================
CREATE TABLE IF NOT EXISTS prontuarios (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    paciente_id UUID NOT NULL REFERENCES pacientes(id),
    usuario_id UUID NOT NULL REFERENCES usuarios(id),
    data_sessao TIMESTAMP NOT NULL,
    tipo_sessao VARCHAR(50) DEFAULT 'Consulta',
    observacoes TEXT,
    diagnostico TEXT,
    tratamento TEXT,
    proxima_sessao TIMESTAMP,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- =====================================================
-- ÍNDICES PARA PERFORMANCE
-- =====================================================
CREATE INDEX IF NOT EXISTS idx_usuarios_username ON usuarios(username);
CREATE INDEX IF NOT EXISTS idx_usuarios_email ON usuarios(email);
CREATE INDEX IF NOT EXISTS idx_pacientes_cpf ON pacientes(cpf);
CREATE INDEX IF NOT EXISTS idx_pacientes_nome ON pacientes(nome_completo);
CREATE INDEX IF NOT EXISTS idx_prontuarios_paciente ON prontuarios(paciente_id);
CREATE INDEX IF NOT EXISTS idx_prontuarios_usuario ON prontuarios(usuario_id);
CREATE INDEX IF NOT EXISTS idx_prontuarios_data ON prontuarios(data_sessao);

-- =====================================================
-- TRIGGER PARA ATUALIZAR DATA_ATUALIZACAO
-- =====================================================
CREATE OR REPLACE FUNCTION update_data_atualizacao()
RETURNS TRIGGER AS $$
BEGIN
    NEW.data_atualizacao = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Aplicar trigger nas tabelas
DROP TRIGGER IF EXISTS trigger_usuarios_update ON usuarios;
CREATE TRIGGER trigger_usuarios_update
    BEFORE UPDATE ON usuarios
    FOR EACH ROW EXECUTE FUNCTION update_data_atualizacao();

DROP TRIGGER IF EXISTS trigger_pacientes_update ON pacientes;
CREATE TRIGGER trigger_pacientes_update
    BEFORE UPDATE ON pacientes
    FOR EACH ROW EXECUTE FUNCTION update_data_atualizacao();

DROP TRIGGER IF EXISTS trigger_prontuarios_update ON prontuarios;
CREATE TRIGGER trigger_prontuarios_update
    BEFORE UPDATE ON prontuarios
    FOR EACH ROW EXECUTE FUNCTION update_data_atualizacao();

-- =====================================================
-- DADOS INICIAIS - USUÁRIOS DE TESTE
-- =====================================================

-- Usuário Admin (senha: admin123)
INSERT INTO usuarios (username, password, email, nome_completo) 
VALUES (
    'admin', 
    '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 
    'admin@psicologia.com', 
    'Administrador do Sistema'
) ON CONFLICT (username) DO NOTHING;

-- Usuário Psicólogo (senha: psicologo123)
INSERT INTO usuarios (username, password, email, nome_completo) 
VALUES (
    'psicologo', 
    '$2a$10$N9qo8uLOickgx2ZMRZoMye7VFnjZcHzP3.K9n/qeGTT2QbO/1cxBm', 
    'psicologo@psicologia.com', 
    'Dr. João Silva'
) ON CONFLICT (username) DO NOTHING;

-- Usuário Teste (senha: teste123)
INSERT INTO usuarios (username, password, email, nome_completo) 
VALUES (
    'teste', 
    '$2a$10$DowJonesLocker2019SALT.O19n.GQXRZ2YFXfokUdOWfFEog/4q', 
    'teste@psicologia.com', 
    'Usuário de Teste'
) ON CONFLICT (username) DO NOTHING;

-- =====================================================
-- DADOS DE TESTE - PACIENTES
-- =====================================================
INSERT INTO pacientes (nome_completo, cpf, data_nascimento, telefone, email, endereco, profissao, estado_civil) 
VALUES 
    ('Maria Santos Silva', '123.456.789-01', '1985-03-15', '(11) 99999-1234', 'maria@email.com', 'Rua das Flores, 123', 'Professora', 'Casada'),
    ('João Pedro Oliveira', '987.654.321-02', '1990-07-22', '(11) 88888-5678', 'joao@email.com', 'Av. Principal, 456', 'Engenheiro', 'Solteiro'),
    ('Ana Carolina Costa', '456.789.123-03', '1978-12-10', '(11) 77777-9012', 'ana@email.com', 'Rua do Centro, 789', 'Médica', 'Divorciada')
ON CONFLICT (cpf) DO NOTHING;

-- =====================================================
-- VERIFICAÇÃO DOS DADOS
-- =====================================================
SELECT 'Usuários criados:' as info, count(*) as total FROM usuarios;
SELECT 'Pacientes criados:' as info, count(*) as total FROM pacientes;
SELECT 'Prontuários criados:' as info, count(*) as total FROM prontuarios;

-- =====================================================
-- INFORMAÇÕES DE LOGIN
-- =====================================================
SELECT 
    '=== CREDENCIAIS DE TESTE ===' as info,
    '' as separador
UNION ALL
SELECT 
    'Usuário: admin | Senha: admin123' as info,
    'Usuário: psicologo | Senha: psicologo123' as separador
UNION ALL
SELECT 
    'Usuário: teste | Senha: teste123' as info,
    '' as separador;