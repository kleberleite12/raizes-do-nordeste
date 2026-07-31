DELETE FROM usuarios WHERE id IN (1, 2) OR email IN ('admin@raizes.com', 'maria@email.com');

INSERT INTO usuarios (id, nome, email, senha, perfil) VALUES
(1, 'Administrador', 'admin@raizes.com', '$2a$10$0e5f2PDer1M2gin5AdmUsOutFXKVoqKovw7Et7WpxgkgM50O5fXDa', 'ADMIN'),
(2, 'Maria Silva', 'maria@email.com', '$2a$10$BXcGx2wkggobpPj80m1xJe3TqdgBHn2Jk1NYCzHWr6gCsx6ieZmGe', 'CLIENTE');

SELECT setval(pg_get_serial_sequence('usuarios', 'id'), (SELECT MAX(id) FROM usuarios));