-- Banco do sistema de biblioteca. Rode este arquivo no HeidiSQL (ou mysql -u root < biblioteca.sql).

CREATE DATABASE IF NOT EXISTS biblioteca;
USE biblioteca;

CREATE TABLE IF NOT EXISTS pessoa (
    id INT AUTO_INCREMENT PRIMARY KEY,
    tipo VARCHAR(20) NOT NULL,
    nome VARCHAR(100) NOT NULL,
    documento VARCHAR(50) NOT NULL UNIQUE,
    curso VARCHAR(100) NULL,
    ano_entrada INT NULL,
    departamento VARCHAR(100) NULL
);

CREATE TABLE IF NOT EXISTS livro (
    id INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(200) NOT NULL UNIQUE,
    autor VARCHAR(100) NOT NULL,
    ano INT NOT NULL,
    exemplares INT NOT NULL,
    exemplares_disponiveis INT NOT NULL
);

CREATE TABLE IF NOT EXISTS emprestimo (
    id INT AUTO_INCREMENT PRIMARY KEY,
    pessoa_id INT NOT NULL,
    livro_id INT NOT NULL,
    data_emprestimo DATE NOT NULL,
    data_prevista DATE NOT NULL,
    data_devolucao DATE NULL,
    FOREIGN KEY (pessoa_id) REFERENCES pessoa(id),
    FOREIGN KEY (livro_id) REFERENCES livro(id)
);

-- Dados de teste
INSERT INTO pessoa (tipo, nome, documento, curso, ano_entrada) VALUES
('ALUNO', 'Maria Silva', 'RA123', 'Ciencia da Computacao', 2024),
('ALUNO', 'Joao Souza', 'RA456', 'Sistemas de Informacao', 2025);

INSERT INTO pessoa (tipo, nome, documento, departamento) VALUES
('PROFESSOR', 'Carlos Pereira', 'MAT789', 'Exatas');

INSERT INTO livro (titulo, autor, ano, exemplares, exemplares_disponiveis) VALUES
('Clean Code', 'Robert C. Martin', 2008, 3, 3),
('Refatoracao', 'Martin Fowler', 2019, 2, 2);
