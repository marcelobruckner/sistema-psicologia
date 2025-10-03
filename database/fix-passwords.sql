-- =====================================================
-- CORREÇÃO DAS SENHAS BCRYPT
-- =====================================================
-- IMPORTANTE: Gere os hashes BCrypt usando a aplicação Spring Boot
-- ou uma ferramenta externa antes de executar este script

-- Exemplo de como gerar hash BCrypt em Java:
-- BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
-- String hash = encoder.encode("sua_senha_aqui");

-- Atualizar senha do admin
-- SUBSTITUA ${ADMIN_PASSWORD_HASH} pelo hash BCrypt gerado
UPDATE usuarios 
SET password = '${ADMIN_PASSWORD_HASH}'
WHERE username = 'admin';

-- Atualizar senha do psicologo
-- SUBSTITUA ${PSICOLOGO_PASSWORD_HASH} pelo hash BCrypt gerado
UPDATE usuarios 
SET password = '${PSICOLOGO_PASSWORD_HASH}'
WHERE username = 'psicologo';

-- Atualizar senha do teste
-- SUBSTITUA ${TESTE_PASSWORD_HASH} pelo hash BCrypt gerado
UPDATE usuarios 
SET password = '${TESTE_PASSWORD_HASH}'
WHERE username = 'teste';

-- Verificar as senhas atualizadas
SELECT username, password FROM usuarios;