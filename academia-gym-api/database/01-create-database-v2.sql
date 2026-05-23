DROP DATABASE IF EXISTS academia_db;
CREATE DATABASE academia_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE academia_db;

CREATE TABLE grupos_usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    codigo_grupo VARCHAR(40) NOT NULL UNIQUE,
    nome VARCHAR(80) NOT NULL UNIQUE,
    descricao VARCHAR(255),
    ativo BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE modalidades (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(80) NOT NULL UNIQUE,
    descricao VARCHAR(255),
    ativa BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    codigo_usuario VARCHAR(40) NOT NULL UNIQUE,
    nome VARCHAR(120) NOT NULL,
    email VARCHAR(120) NOT NULL UNIQUE,
    telefone VARCHAR(20),
    cargo VARCHAR(80),
    grupo_usuario_id INT NOT NULL,
    modalidade_id INT NULL,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT fk_usuario_grupo
        FOREIGN KEY (grupo_usuario_id) REFERENCES grupos_usuarios(id),
    CONSTRAINT fk_usuario_modalidade
        FOREIGN KEY (modalidade_id) REFERENCES modalidades(id)
);

CREATE TABLE professores (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(120) NOT NULL,
    email VARCHAR(120) NOT NULL UNIQUE,
    telefone VARCHAR(20),
    modalidade_id INT NOT NULL,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT fk_professor_modalidade
        FOREIGN KEY (modalidade_id) REFERENCES modalidades(id)
);

CREATE TABLE planos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(80) NOT NULL UNIQUE,
    duracao_dias INT NOT NULL,
    valor DECIMAL(10,2) NOT NULL,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT chk_planos_duracao CHECK (duracao_dias > 0),
    CONSTRAINT chk_planos_valor CHECK (valor >= 0)
);

CREATE TABLE alunos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    codigo_aluno VARCHAR(40) NOT NULL UNIQUE,
    nome VARCHAR(120) NOT NULL,
    cpf VARCHAR(14) NOT NULL UNIQUE,
    email VARCHAR(120) NOT NULL UNIQUE,
    telefone VARCHAR(20),
    data_nascimento DATE,
    sexo ENUM('MASCULINO', 'FEMININO', 'OUTRO') DEFAULT 'OUTRO',
    endereco VARCHAR(255),
    ativo BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE matriculas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    codigo_matricula VARCHAR(40) NOT NULL UNIQUE,
    aluno_id INT NOT NULL,
    plano_id INT NOT NULL,
    data_inicio DATE NOT NULL,
    data_fim DATE NOT NULL,
    status ENUM('ATIVA', 'VENCIDA', 'CANCELADA') NOT NULL DEFAULT 'ATIVA',
    criado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_matricula_aluno
        FOREIGN KEY (aluno_id) REFERENCES alunos(id),
    CONSTRAINT fk_matricula_plano
        FOREIGN KEY (plano_id) REFERENCES planos(id)
);

CREATE TABLE pagamentos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    codigo_pagamento VARCHAR(40) NOT NULL UNIQUE,
    matricula_id INT NOT NULL,
    valor DECIMAL(10,2) NOT NULL,
    data_pagamento DATE,
    forma_pagamento ENUM('DINHEIRO', 'PIX', 'CARTAO', 'BOLETO') NOT NULL DEFAULT 'PIX',
    status ENUM('PENDENTE', 'PAGO', 'CANCELADO') NOT NULL DEFAULT 'PAGO',
    CONSTRAINT fk_pagamento_matricula
        FOREIGN KEY (matricula_id) REFERENCES matriculas(id),
    CONSTRAINT chk_pagamentos_valor CHECK (valor >= 0)
);

CREATE TABLE inadimplencias (
    id INT AUTO_INCREMENT PRIMARY KEY,
    codigo_inadimplencia VARCHAR(40) NOT NULL UNIQUE,
    matricula_id INT NOT NULL,
    valor DECIMAL(10,2) NOT NULL,
    dias_atraso INT NOT NULL,
    status ENUM('PENDENTE', 'RESOLVIDA') NOT NULL DEFAULT 'PENDENTE',
    data_registro DATE NOT NULL DEFAULT (CURRENT_DATE),
    CONSTRAINT fk_inadimplencia_matricula
        FOREIGN KEY (matricula_id) REFERENCES matriculas(id),
    CONSTRAINT uk_inadimplencia_matricula UNIQUE (matricula_id)
);



CREATE INDEX idx_grupos_nome ON grupos_usuarios(nome);
CREATE INDEX idx_usuarios_nome ON usuarios(nome);
CREATE INDEX idx_usuarios_grupo ON usuarios(grupo_usuario_id);
CREATE INDEX idx_alunos_nome ON alunos(nome);
CREATE INDEX idx_professores_nome ON professores(nome);
CREATE INDEX idx_mat_aluno ON matriculas(aluno_id);
CREATE INDEX idx_mat_status_fim ON matriculas(status, data_fim);
CREATE INDEX idx_pag_matricula ON pagamentos(matricula_id, status);


DELIMITER //

CREATE FUNCTION fn_codigo_base(p_prefixo VARCHAR(10))
RETURNS VARCHAR(40)
NOT DETERMINISTIC
NO SQL
BEGIN
    RETURN CONCAT(
        p_prefixo,
        '-',
        DATE_FORMAT(NOW(), '%Y%m%d'),
        '-',
        UPPER(SUBSTRING(REPLACE(UUID(), '-', ''), 1, 12))
    );
END//

CREATE FUNCTION fn_gerar_codigo_grupo()
RETURNS VARCHAR(40)
NOT DETERMINISTIC
NO SQL
BEGIN
    RETURN fn_codigo_base('GRP');
END//

CREATE FUNCTION fn_gerar_codigo_usuario()
RETURNS VARCHAR(40)
NOT DETERMINISTIC
NO SQL
BEGIN
    RETURN fn_codigo_base('USR');
END//

CREATE FUNCTION fn_gerar_codigo_aluno()
RETURNS VARCHAR(40)
NOT DETERMINISTIC
NO SQL
BEGIN
    RETURN fn_codigo_base('ALU');
END//

CREATE FUNCTION fn_gerar_codigo_matricula()
RETURNS VARCHAR(40)
NOT DETERMINISTIC
NO SQL
BEGIN
    RETURN fn_codigo_base('MAT');
END//

CREATE FUNCTION fn_gerar_codigo_pagamento()
RETURNS VARCHAR(40)
NOT DETERMINISTIC
NO SQL
BEGIN
    RETURN fn_codigo_base('PAG');
END//

CREATE FUNCTION fn_gerar_codigo_inadimplencia()
RETURNS VARCHAR(40)
NOT DETERMINISTIC
NO SQL
BEGIN
    RETURN fn_codigo_base('INA');
END//

CREATE FUNCTION fn_dias_restantes(p_data_fim DATE, p_data_base DATE)
RETURNS INT
DETERMINISTIC
NO SQL
BEGIN
    RETURN DATEDIFF(p_data_fim, p_data_base);
END//

-- TRIGGERS

CREATE TRIGGER trg_codigo_grupo_usuario
BEFORE INSERT ON grupos_usuarios
FOR EACH ROW
BEGIN
    IF NEW.codigo_grupo IS NULL OR NEW.codigo_grupo = '' THEN
        SET NEW.codigo_grupo = fn_gerar_codigo_grupo();
    END IF;
END//

CREATE TRIGGER trg_codigo_usuario
BEFORE INSERT ON usuarios
FOR EACH ROW
BEGIN
    IF NEW.codigo_usuario IS NULL OR NEW.codigo_usuario = '' THEN
        SET NEW.codigo_usuario = fn_gerar_codigo_usuario();
    END IF;
END//

CREATE TRIGGER trg_codigo_aluno
BEFORE INSERT ON alunos
FOR EACH ROW
BEGIN
    IF NEW.codigo_aluno IS NULL OR NEW.codigo_aluno = '' THEN
        SET NEW.codigo_aluno = fn_gerar_codigo_aluno();
    END IF;
END//

CREATE TRIGGER trg_preparar_matricula
BEFORE INSERT ON matriculas
FOR EACH ROW
BEGIN
    DECLARE v_duracao INT DEFAULT 0;

    IF NEW.codigo_matricula IS NULL OR NEW.codigo_matricula = '' THEN
        SET NEW.codigo_matricula = fn_gerar_codigo_matricula();
    END IF;

    SELECT duracao_dias INTO v_duracao
    FROM planos
    WHERE id = NEW.plano_id;

    IF NEW.data_inicio IS NULL THEN
        SET NEW.data_inicio = CURDATE();
    END IF;

    IF NEW.data_fim IS NULL THEN
        SET NEW.data_fim = DATE_ADD(NEW.data_inicio, INTERVAL v_duracao DAY);
    END IF;

    IF NEW.status IS NULL THEN
        SET NEW.status = 'ATIVA';
    END IF;
END//

CREATE TRIGGER trg_codigo_pagamento
BEFORE INSERT ON pagamentos
FOR EACH ROW
BEGIN
    IF NEW.codigo_pagamento IS NULL OR NEW.codigo_pagamento = '' THEN
        SET NEW.codigo_pagamento = fn_gerar_codigo_pagamento();
    END IF;
END//

CREATE TRIGGER trg_codigo_inadimplencia
BEFORE INSERT ON inadimplencias
FOR EACH ROW
BEGIN
    IF NEW.codigo_inadimplencia IS NULL OR NEW.codigo_inadimplencia = '' THEN
        SET NEW.codigo_inadimplencia = fn_gerar_codigo_inadimplencia();
    END IF;
END//

CREATE TRIGGER trg_vencer_matricula
BEFORE UPDATE ON matriculas
FOR EACH ROW
BEGIN
    IF NEW.status = 'ATIVA' AND NEW.data_fim < CURDATE() THEN
        SET NEW.status = 'VENCIDA';
    END IF;
END//

CREATE TRIGGER trg_gerar_inadimplencia
AFTER UPDATE ON matriculas
FOR EACH ROW
BEGIN
    DECLARE v_valor DECIMAL(10,2) DEFAULT 0;

    IF OLD.status = 'ATIVA' AND NEW.status = 'VENCIDA' THEN
        SELECT valor INTO v_valor
        FROM planos
        WHERE id = NEW.plano_id;

        IF NOT EXISTS (
            SELECT 1
            FROM inadimplencias
            WHERE matricula_id = NEW.id
        ) THEN
            INSERT INTO inadimplencias (matricula_id, valor, dias_atraso, status, data_registro)
            VALUES (NEW.id, v_valor, DATEDIFF(CURDATE(), NEW.data_fim), 'PENDENTE', CURDATE());
        END IF;
    END IF;
END//

-- PROCEDURE
-- Registra matrícula e pagamento inicial em uma única operação.


CREATE PROCEDURE sp_registrar_matricula(
    IN p_aluno_id INT,
    IN p_plano_id INT,
    IN p_forma_pagamento VARCHAR(20),
    OUT p_mensagem VARCHAR(255)
)
BEGIN
    DECLARE v_aluno_existe INT DEFAULT 0;
    DECLARE v_plano_existe INT DEFAULT 0;
    DECLARE v_tem_matricula_ativa INT DEFAULT 0;
    DECLARE v_duracao_dias INT DEFAULT 0;
    DECLARE v_valor DECIMAL(10,2) DEFAULT 0;
    DECLARE v_matricula_id INT DEFAULT 0;

    SELECT COUNT(*) INTO v_aluno_existe
    FROM alunos
    WHERE id = p_aluno_id AND ativo = TRUE;

    SELECT COUNT(*) INTO v_plano_existe
    FROM planos
    WHERE id = p_plano_id AND ativo = TRUE;

    SELECT COUNT(*) INTO v_tem_matricula_ativa
    FROM matriculas
    WHERE aluno_id = p_aluno_id
      AND status = 'ATIVA'
      AND data_fim >= CURDATE();

    IF v_aluno_existe = 0 THEN
        SET p_mensagem = 'Aluno não encontrado ou inativo.';
    ELSEIF v_plano_existe = 0 THEN
        SET p_mensagem = 'Plano não encontrado ou inativo.';
    ELSEIF v_tem_matricula_ativa > 0 THEN
        SET p_mensagem = 'Aluno já possui uma matrícula ativa.';
    ELSE
        SELECT duracao_dias, valor
        INTO v_duracao_dias, v_valor
        FROM planos
        WHERE id = p_plano_id;

        START TRANSACTION;

        INSERT INTO matriculas (aluno_id, plano_id, data_inicio, data_fim, status)
        VALUES (p_aluno_id, p_plano_id, CURDATE(), DATE_ADD(CURDATE(), INTERVAL v_duracao_dias DAY), 'ATIVA');

        SET v_matricula_id = LAST_INSERT_ID();

        INSERT INTO pagamentos (matricula_id, valor, data_pagamento, forma_pagamento, status)
        VALUES (v_matricula_id, v_valor, CURDATE(), p_forma_pagamento, 'PAGO');

        COMMIT;

        SET p_mensagem = CONCAT('Matrícula cadastrada com sucesso. ID: ', v_matricula_id);
    END IF;
END//

DELIMITER ;

-- VIEWS

CREATE VIEW vw_alunos_ativos AS
SELECT
    a.id AS aluno_id,
    a.codigo_aluno,
    a.nome AS aluno_nome,
    a.cpf,
    a.email,
    a.telefone,
    m.codigo_matricula,
    p.nome AS plano_nome,
    m.data_inicio,
    m.data_fim,
    fn_dias_restantes(m.data_fim, CURDATE()) AS dias_restantes
FROM alunos a
JOIN matriculas m ON m.aluno_id = a.id
JOIN planos p ON p.id = m.plano_id
WHERE a.ativo = TRUE
  AND m.status = 'ATIVA';

CREATE VIEW vw_inadimplencias_pendentes AS
SELECT
    i.id AS inadimplencia_id,
    i.codigo_inadimplencia,
    a.id AS aluno_id,
    a.codigo_aluno,
    a.nome AS aluno_nome,
    a.email,
    a.telefone,
    m.codigo_matricula,
    p.nome AS plano_nome,
    i.valor,
    i.dias_atraso,
    i.data_registro
FROM inadimplencias i
JOIN matriculas m ON m.id = i.matricula_id
JOIN alunos a ON a.id = m.aluno_id
JOIN planos p ON p.id = m.plano_id
WHERE i.status = 'PENDENTE';

-- USUARIOS MYSQL E CONTROLE DE ACESSO
-- Execute esta parte com usuario administrador. A aplicação local usa acad_admin.
-- Os outros usuarios existem para demonstrar níveis diferentes de acesso no MySQL.

CREATE USER IF NOT EXISTS 'acad_admin'@'localhost' IDENTIFIED BY 'admin123';
CREATE USER IF NOT EXISTS 'acad_recepcao'@'localhost' IDENTIFIED BY 'recepcao123';
CREATE USER IF NOT EXISTS 'acad_aluno'@'localhost' IDENTIFIED BY 'aluno123';

GRANT ALL PRIVILEGES ON academia_db.* TO 'acad_admin'@'localhost';

GRANT SELECT ON academia_db.modalidades TO 'acad_recepcao'@'localhost';
GRANT SELECT ON academia_db.planos TO 'acad_recepcao'@'localhost';
GRANT SELECT ON academia_db.professores TO 'acad_recepcao'@'localhost';
GRANT SELECT, INSERT, UPDATE ON academia_db.alunos TO 'acad_recepcao'@'localhost';
GRANT SELECT, INSERT, UPDATE ON academia_db.matriculas TO 'acad_recepcao'@'localhost';
GRANT SELECT, INSERT, UPDATE ON academia_db.pagamentos TO 'acad_recepcao'@'localhost';
GRANT SELECT ON academia_db.inadimplencias TO 'acad_recepcao'@'localhost';
GRANT SELECT ON academia_db.vw_alunos_ativos TO 'acad_recepcao'@'localhost';
GRANT SELECT ON academia_db.vw_inadimplencias_pendentes TO 'acad_recepcao'@'localhost';
GRANT EXECUTE ON PROCEDURE academia_db.sp_registrar_matricula TO 'acad_recepcao'@'localhost';

GRANT SELECT ON academia_db.vw_alunos_ativos TO 'acad_aluno'@'localhost';

FLUSH PRIVILEGES;
