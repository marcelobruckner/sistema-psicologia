-- Atualizar senha do admin para 'admin' (senha simples)
UPDATE usuarios 
SET password = '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iKXIrbZlSQy6kHbEpRFaODdnhzDu' 
WHERE username = 'admin';