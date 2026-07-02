# Raízes do Nordeste - API Back-end

Projeto desenvolvido para a disciplina de Projeto Multidisciplinar da UNINTER - Trilha Back-end.

## Requisitos

- Java 21
- Maven
- PostgreSQL 17

## Configuração do banco de dados

1. Instale o PostgreSQL e crie um banco de dados com o nome: raizesdonordeste
2. Abra o arquivo src/main/resources/application.properties
3. Coloque sua senha do postgres no campo spring.datasource.password

## Como instalar as dependências

.\mvnw clean install -DskipTests

## Como iniciar a API

.\mvnw spring-boot:run

A API vai subir na porta 8080. As tabelas são criadas automaticamente pelo Hibernate na primeira execução.

## Documentação Swagger

Com a API rodando, acesse:
http://localhost:8080/swagger-ui/index.html

## Testando com Postman

Importe o arquivo de coleção disponível na pasta postman do repositório.
A ordem sugerida para testar é:
1. Criar usuário (POST /usuarios)
2. Criar unidade (POST /unidades)
3. Criar produto (POST /produtos)
4. Criar pedido (POST /pedidos)
5. Registrar pagamento mock (POST /pagamentos)

## Endpoints disponíveis

- POST /usuarios - cadastrar usuário
- GET /usuarios - listar usuários
- POST /unidades - cadastrar unidade
- GET /unidades - listar unidades
- POST /produtos - cadastrar produto
- GET /produtos - listar produtos
- POST /pedidos - criar pedido
- GET /pedidos - listar pedidos
- PUT /pedidos/{id}/status - atualizar status do pedido
- POST /pagamentos - registrar pagamento mock
- GET /estoque - consultar estoque

## Repositório

https://github.com/kleberleite12/raizes-do-nordeste