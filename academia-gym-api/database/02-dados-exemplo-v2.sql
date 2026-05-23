USE academia_db;

-- Grupos para controle de permissões da aplicação.
INSERT INTO grupos_usuarios (nome, descricao) VALUES
('ADMIN', 'Administrador do sistema'),
('RECEPCAO', 'Usuário da recepção, responsável por alunos, matrículas e pagamentos'),
('PROFESSOR', 'Professor vinculado a uma modalidade'),
('ALUNO', 'Perfil de aluno, com acesso apenas às informações permitidas');

INSERT INTO modalidades (nome, descricao) VALUES
('Musculação', 'Treino com aparelhos e pesos'),
('Jiu-jitsu', 'Arte marcial com foco em defesa pessoal'),
('Boxe', 'Treino de luta e condicionamento físico'),
('Yoga', 'Atividade com foco em mobilidade e respiração');

-- Usuários do sistema
INSERT INTO usuarios (nome, email, telefone, cargo, grupo_usuario_id, modalidade_id) VALUES
('Administrador Geral', 'admin@academia.com', '62999990001', 'Administrador', 1, NULL),
('Recepção Academia', 'recepcao@academia.com', '62999990002', 'Recepcionista', 2, NULL),
('Carlos Lima', 'carlos.usuario@academia.com', '62988887777', 'Professor de Musculação', 3, 1),
('Mariana Alves', 'mariana.usuario@academia.com', '62977776666', 'Professora de Jiu-jitsu', 3, 2);

INSERT INTO professores (nome, email, telefone, modalidade_id) VALUES
('Carlos Lima', 'carlos@email.com', '62988887777', 1),
('Mariana Alves', 'mariana@email.com', '62977776666', 2),
('João Pereira', 'joao@email.com', '62966665555', 3);

INSERT INTO planos (nome, duracao_dias, valor) VALUES
('Mensal', 30, 99.90),
('Trimestral', 90, 269.90),
('Semestral', 180, 499.90),
('Anual', 365, 899.90);

INSERT INTO alunos (nome, cpf, email, telefone, data_nascimento, sexo, endereco) VALUES
('Ana Souza', '11122233344', 'ana@email.com', '62999990000', '2000-05-10', 'FEMININO', 'Rua A, 123'),
('Pedro Santos', '55566677788', 'pedro@email.com', '62999991111', '1998-08-20', 'MASCULINO', 'Rua B, 456'),
('Lucas Oliveira', '99988877766', 'lucas@email.com', '62999992222', '2002-01-15', 'MASCULINO', 'Rua C, 789');

-- Matrícula criada pela procedure, já com pagamento inicial.
CALL sp_registrar_matricula(1, 1, 'PIX', @mensagem);
SELECT @mensagem AS mensagem_procedure;

-- Matrícula antiga para demonstrar trigger de vencimento e geração de inadimplência.
INSERT INTO matriculas (aluno_id, plano_id, data_inicio, data_fim, status)
VALUES (2, 1, DATE_SUB(CURDATE(), INTERVAL 45 DAY), DATE_SUB(CURDATE(), INTERVAL 15 DAY), 'ATIVA');

-- Força uma atualização para a trigger verificar se a matrícula venceu.
UPDATE matriculas
SET status = status
WHERE aluno_id = 2;
