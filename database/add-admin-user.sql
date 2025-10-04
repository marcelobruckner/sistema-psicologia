-- Script para adicionar usuário admin
-- Senha: teste123 (hash BCrypt)

INSERT INTO usuarios (
    id, 
    username, 
    password, 
    email, 
    nome_completo, 
    role, 
    ativo, 
    data_criacao, 
    data_atualizacao
) VALUES (
    gen_random_uuid(),
    'admin',
    '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iKXIrbZlSQy6kHbEpRFaODdnhzDu',
    'admin@psicologia.com',
    'Administrador do Sistema',
    'ADMIN',
    true,
    NOW(),
    NOW()
) ON CONFLICT (username) DO NOTHING;