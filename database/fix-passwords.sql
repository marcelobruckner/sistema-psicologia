-- =====================================================
-- CORREÇÃO DAS SENHAS BCRYPT
-- =====================================================

-- Atualizar senha do admin (admin123)
UPDATE usuarios 
SET password = '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi'
WHERE username = 'admin';

-- Atualizar senha do psicologo (psicologo123) 
UPDATE usuarios 
SET password = '$2a$10$N9qo8uLOickgx2ZMRZoMye7VFnjZcHzP3.K9n/qeGTT2QbO/1cxBm'
WHERE username = 'psicologo';

-- Atualizar senha do teste (teste123)
UPDATE usuarios 
SET password = '$2a$10$DowJonesLocker2019SALT.O19n.GQXRZ2YFXfokUdOWfFEog/4q'
WHERE username = 'teste';

-- Verificar as senhas atualizadas
SELECT username, password FROM usuarios;