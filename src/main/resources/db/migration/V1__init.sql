-- Criação de tabela usuario
CREATE TABLE usuario (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    senha VARCHAR(255) NOT NULL,
    tipo VARCHAR(20) CHECK (tipo IN ('ADMINISTRADOR', 'TESOUREIRA')) NOT NULL
);

-- Criação de tabela categoria
CREATE TABLE categoria (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    tipo VARCHAR(10) CHECK (tipo IN ('ENTRADA', 'SAIDA')) NOT NULL
);

-- Criação de tabela lancamento
CREATE TABLE lancamento (
    id BIGSERIAL PRIMARY KEY,
    descricao VARCHAR(255),
    valor NUMERIC(10,2) NOT NULL,
    data DATE NOT NULL,
    tipo VARCHAR(10) CHECK (tipo IN ('ENTRADA', 'SAIDA')) NOT NULL,
    categoria_id BIGINT REFERENCES categoria(id),
    usuario_id BIGINT REFERENCES usuario(id)
);

-- Inserção de dados iniciais
INSERT INTO categoria (nome, tipo) VALUES
('Oferta', 'ENTRADA'),
('Dízimo', 'ENTRADA'),
('Energia Elétrica', 'SAIDA'),
('Aluguel', 'SAIDA');

INSERT INTO usuario (nome, email, senha, tipo) VALUES
('Admin Principal', 'admin@igreja.com', '1234', 'ADMINISTRADOR');
