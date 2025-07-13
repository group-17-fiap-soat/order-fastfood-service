# 🍔 FastFood Backend API

API REST para gerenciamento de pedidos (Order) e produtos (Product) de um sistema de fast food.

## Sumário
- [Visão Geral](#visão-geral)
- [Tecnologias](#tecnologias)
- [Como rodar o projeto](#como-rodar-o-projeto)
- [Banco de Dados](#banco-de-dados)
- [Endpoints principais](#endpoints-principais)
- [Exemplos de uso](#exemplos-de-uso)
- [Documentação Swagger](#documentação-swagger)

## Visão Geral
Esta API permite:
- Cadastrar, listar, atualizar e remover produtos
- Criar, listar, atualizar status e consultar pedidos

## Tecnologias
- Kotlin
- Spring Boot
- PostgreSQL
- Flyway (migração de banco)
- Docker

## Como rodar o projeto

### Pré-requisitos
- Docker e Docker Compose instalados

### Subindo a infraestrutura (PostgreSQL)

```sh
docker-compose up -d
```

### Rodando a aplicação

```sh
./gradlew bootRun
```

A aplicação estará disponível em: http://localhost:8082

## Banco de Dados
- Banco: `fastfood`
- As tabelas são criadas automaticamente via Flyway.

## Endpoints principais

### Produtos
- `POST   /products` — Cadastrar produto
- `GET    /products` — Listar produtos
- `GET    /products/{id}` — Detalhar produto
- `PUT    /products/{id}` — Atualizar produto
- `DELETE /products/{id}` — Remover produto

### Pedidos
- `POST   /orders` — Criar pedido
- `GET    /orders` — Listar pedidos
- `GET    /orders/{id}` — Detalhar pedido
- `PUT    /orders/{id}/status` — Atualizar status do pedido

## Exemplos de uso

### Criar produto
```json
POST /products
{
  "name": "Hambúrguer",
  "description": "Pão, carne, queijo",
  "price": 19.90,
  "category": "Lanche"
}
```

### Criar pedido
```json
POST /orders
{
  "customerId": "uuid-do-cliente",
  "items": [
    { "productId": "uuid-produto", "quantity": 2 }
  ]
}
```

## Documentação Swagger
Acesse a documentação interativa em:

- http://localhost:8082/swagger-ui

---

> Para dúvidas ou sugestões, abra uma issue.
