-- Habilita extensão para gerar UUIDs
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-- Criação da tabela usuario
CREATE TABLE usuario (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    senha VARCHAR(255) NOT NULL,
    tipo VARCHAR(20) CHECK (tipo IN ('ADMINISTRADOR', 'TESOUREIRA')) NOT NULL
);

-- Criação da tabela categoria
CREATE TABLE categoria (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    nome VARCHAR(100) NOT NULL,
    tipo VARCHAR(10) CHECK (tipo IN ('ENTRADA', 'SAIDA')) NOT NULL
);

-- Criação da tabela lancamento
CREATE TABLE lancamento (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    descricao VARCHAR(255),
    valor NUMERIC(10,2) NOT NULL,
    data DATE NOT NULL,
    tipo VARCHAR(10) CHECK (tipo IN ('ENTRADA', 'SAIDA')) NOT NULL,
    categoria_id UUID REFERENCES categoria(id),
    usuario_id UUID REFERENCES usuario(id)
);

-- Inserção de categorias padrão
INSERT INTO categoria (id, nome, tipo) VALUES
(gen_random_uuid(), 'Oferta', 'ENTRADA'),
(gen_random_uuid(), 'Dízimo', 'ENTRADA'),
(gen_random_uuid(), 'Energia Elétrica', 'SAIDA'),
(gen_random_uuid(), 'Aluguel', 'SAIDA');

-- Inserção de usuário administrador
INSERT INTO usuario (id, nome, email, senha, tipo) VALUES
(gen_random_uuid(), 'Wyston Frank', 'wfrankms2@gmail.com', '$wfrankms2', 'ADMINISTRADOR');
