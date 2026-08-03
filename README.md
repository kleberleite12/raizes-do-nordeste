# Raízes do Nordeste - API Back-end

Projeto desenvolvido para a disciplina de Projeto Multidisciplinar da UNINTER - Trilha Back-end.

## Requisitos

- Java 21
- Maven
- PostgreSQL 17

## Configuração do banco de dados

1. Instale o PostgreSQL e crie um banco de dados vazio com o nome `raizesdonordeste`.

   Pelo pgAdmin: botão direito em Databases > Create > Database, e informe o nome.

   Ou pelo terminal:

```sql
   CREATE DATABASE raizesdonordeste;
```

2. As tabelas são criadas automaticamente pelo Hibernate na primeira execução.

## Variáveis de ambiente

A senha do banco não fica gravada no código. Ela é lida da variável de ambiente DB_PASSWORD.

O arquivo `.env.example` na raiz do repositório mostra qual variável é usada.

Formas de configurar:

- **IntelliJ IDEA:** Run > Edit Configurations > campo "Environment variables" > `DB_PASSWORD=sua_senha`
- **PowerShell:** `$env:DB_PASSWORD="sua_senha"` antes de iniciar a API
- **Linux/Mac:** `export DB_PASSWORD=sua_senha`

Se a variável não for configurada, a aplicação usa `postgres` como senha padrão.

## Como instalar as dependências

Windows:

```
.\mvnw clean install -DskipTests
```

Linux/Mac:

```
./mvnw clean install -DskipTests
```

## Como iniciar a API

Windows:

```
.\mvnw spring-boot:run
```

Linux/Mac:

```
./mvnw spring-boot:run
```

A API sobe na porta 8080.

## Seed (dados iniciais)

O arquivo `src/main/resources/data.sql` é executado automaticamente ao iniciar a API e cadastra dois usuários para permitir os testes:

| Perfil | E-mail | Senha |
|---|---|---|
| ADMIN | admin@raizes.com | Admin@2026 |
| CLIENTE | maria@email.com | Cliente@2026 |

As demais entidades (unidades, produtos e estoque) são cadastradas pelos próprios testes da coleção Postman, na ordem indicada abaixo.

## Documentação Swagger

Com a API rodando, acesse:

http://localhost:8080/swagger-ui/index.html

## Testando com Postman

Importe o arquivo de coleção disponível na raiz do repositório: `Raizes do Nordeste.postman_collection.json`

A coleção está organizada nas pastas: Auth, Cadastros, Pedidos, Pagamentos e Erros.

### Como executar

1. Execute o T01 (Auth) para fazer login e copie o token retornado.
2. Cole o token no campo Bearer Token (aba Authorization) das demais requisições.
3. Execute os testes na ordem abaixo — os quatro primeiros criam os dados usados pelos seguintes.

### Ordem de execução

**Fluxo principal (positivos)**

| ID | Cenário | Endpoint | Esperado |
|---|---|---|---|
| T01 | Login válido | POST /auth/login | 200 + token |
| T02 | Criar unidade | POST /unidades | 201 |
| T03 | Criar produto | POST /produtos | 201 |
| T04 | Cadastrar estoque | POST /estoque | 201 |
| T05 | Criar pedido | POST /pedidos | 201 + total calculado |
| T06 | Pagamento aprovado | POST /pagamentos | 200 + APROVADO |
| T07 | Atualizar status | PUT /pedidos/1/status?status=EM_PREPARO | 200 + log no console |
| T08 | Filtrar por canal | GET /pedidos?canalPedido=APP | 200 + lista paginada |
| T16 | Inscrição fidelidade | POST /fidelidade/inscricao | 201 |

**Cenários de erro (negativos)**

| ID | Cenário | Endpoint | Esperado |
|---|---|---|---|
| T09 | Pagamento recusado | POST /pagamentos (valor > 10000) | 200 + RECUSADO |
| T10 | Acesso sem token | GET /pedidos | 401 |
| T11 | Perfil sem permissão | POST /produtos (token CLIENTE) | 403 |
| T12 | canalPedido ausente | POST /pedidos | 422 |
| T13 | Produto inexistente | POST /pedidos (produtoId 9999) | 404 |
| T14 | Quantidade inválida | POST /pedidos (quantidade -5) | 422 |
| T15 | Estoque insuficiente | POST /pedidos (quantidade 999) | 409 |

### Observações

- O token JWT expira em 24 horas. Se receber 401, faça login novamente.
- O campo canalPedido aceita apenas: APP, TOTEM, BALCAO, PICKUP, WEB
- Pagamentos acima de R$ 10.000,00 são recusados automaticamente pelo mock
- O T11 exige o token do usuário CLIENTE (maria@email.com)
- As listagens são paginadas e aceitam os parâmetros `?page=1&limit=10`

## Testes automatizados

Não foram implementados testes automatizados. A validação da API é feita pela coleção Postman descrita acima.

## Endpoints disponíveis

**Autenticação**
- POST /auth/login - autenticar usuário e retornar token
- POST /auth/registro - cadastrar novo usuário

**Usuários**
- GET /usuarios - listar usuários paginados (ADMIN)
- POST /usuarios - cadastrar usuário

**Unidades**
- GET /unidades - listar unidades paginadas
- POST /unidades - cadastrar unidade (ADMIN)

**Produtos**
- GET /produtos - listar produtos paginados
- POST /produtos - cadastrar produto (ADMIN)

**Pedidos**
- GET /pedidos - listar pedidos paginados (aceita ?canalPedido=APP)
- POST /pedidos - criar pedido
- PUT /pedidos/{id}/status - atualizar status
- DELETE /pedidos/{id} - cancelar pedido

**Pagamentos**
- GET /pagamentos - listar pagamentos paginados
- POST /pagamentos - registrar pagamento mock

**Estoque**
- GET /estoque - consultar estoque paginado
- POST /estoque - registrar estoque
- PUT /estoque/entrada - entrada de estoque
- PUT /estoque/saida - saída de estoque

**Fidelidade**
- POST /fidelidade/inscricao - inscrever no programa
- GET /fidelidade/{clienteId} - consultar pontos
- POST /fidelidade/adicionar-pontos - adicionar pontos
- POST /fidelidade/resgatar-pontos - resgatar pontos

## Repositório

https://github.com/kleberleite12/raizes-do-nordeste