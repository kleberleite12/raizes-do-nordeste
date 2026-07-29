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

Importe o arquivo de coleção disponível na raiz do repositório: Raizes do Nordeste.postman_collection

### Ordem sugerida para executar os testes:

1. T01 - Fazer login (POST /auth/login) — copie o token retornado
2. Coloque o token no campo Bearer Token de cada requisição
3. T03 - Criar produto (POST /produtos)
4. T12 - Criar estoque para o produto (PUT /estoque/entrada)
5. T02 - Criar unidade (POST /unidades) — se ainda não existir
6. T04 - Criar pedido com itens (POST /pedidos)
7. T05 - Registrar pagamento mock aprovado (POST /pagamentos)
8. T11 - Inscrever cliente no programa de fidelidade (POST /fidelidade/inscricao)
9. T07 - Testar acesso sem token (GET /pedidos sem token)
10. T08 - Testar acesso com perfil errado (GET /usuarios com token de CLIENTE)
11. T09 - Testar pedido sem canalPedido (POST /pedidos sem canalPedido)
12. T10 - Testar estoque insuficiente (POST /pedidos com quantidade alta)
13. T14 - Testar pagamento recusado (POST /pagamentos com valor acima de R$10.000)

### Observações:
- O token JWT expira em 24 horas. Se der erro 403, faça login novamente.
- O campo canalPedido aceita apenas: APP, TOTEM, BALCAO, PICKUP, WEB
- Pagamentos acima de R$10.000 são recusados automaticamente pelo mock

## Endpoints disponíveis

- POST /auth/login - autenticar usuário
- POST /auth/registro - cadastrar novo usuário
- GET /usuarios - listar usuários (ADMIN)
- POST /usuarios - cadastrar usuário
- GET /unidades - listar unidades
- POST /unidades - cadastrar unidade (ADMIN)
- GET /produtos - listar produtos
- POST /produtos - cadastrar produto (ADMIN)
- GET /pedidos - listar pedidos (aceita ?canalPedido=APP)
- POST /pedidos - criar pedido
- PUT /pedidos/{id}/status - atualizar status
- DELETE /pedidos/{id} - cancelar pedido
- GET /pagamentos - listar pagamentos
- POST /pagamentos - registrar pagamento mock
- GET /estoque - consultar estoque
- POST /estoque - registrar estoque
- PUT /estoque/entrada - entrada de estoque
- PUT /estoque/saida - saida de estoque
- POST /fidelidade/inscricao - inscrever no programa
- GET /fidelidade/{clienteId} - consultar pontos
- POST /fidelidade/adicionar-pontos - adicionar pontos
- POST /fidelidade/resgatar-pontos - resgatar pontos

## Repositório

https://github.com/kleberleite12/raizes-do-nordeste