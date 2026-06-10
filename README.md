# 🛒 E-Commerce RESTful API — Spring Boot & JDBC Avançado

### Contexto Acadêmico
* **Instituição:** IBMEC Rio de Janeiro
* **Disciplina:** Técnicas Avançadas de Programação
* **Professor:** Thiago Souza
* **Aluno:** João Pedro Borges Souza Santana

---

## Sobre o Projeto

Este projeto consiste em uma **API RESTful de alta performance** desenvolvida em **Java com Spring Boot**, projetada especificamente para gerenciar o ecossistema de vendas de um e-commerce corporativo (Categorias, Produtos, Pedidos e Itens de Carrinho). 

O grande diferencial técnico e arquitetural desta aplicação é o **descarrilamento intencional de ORMs tradicionais (como Spring Data JPA / Hibernate)**. Em conformidade com as diretrizes acadêmicas puristas de Engenharia de Software, todo o acesso, mapeamento objeto-relacional e controle transacional de tabelas acopladas foram implementados **manualmente via JDBC estruturado** (`NamedParameterJdbcOperations`), garantindo total soberania sobre as instruções SQL enviadas à nuvem.

---

## Tecnologias, Bibliotecas e Infraestrutura

O ecossistema do projeto foi blindado utilizando ferramentas modernas que elevam a performance e a governança da aplicação:

* **Linguagem Principal:** Java 17+ (uso estratégico de Streams e tipagem estática moderna)
* **Framework Base:** Spring Boot 3+ (utilizado estritamente para o roteamento HTTP, injeção de dependência e ciclo de vida dos controllers)
* **Redução de Boilerplate:** Lombok (anotações como `@Data`, `@Builder`, `@NoArgsConstructor` e `@AllArgsConstructor` para garantir entidades e DTOs limpos e imutáveis)
* **Pool de Conexões:** **HikariCP** (Configurado sob medida para controle rígido de vazamento de recursos e limite estrito de conexões simultâneas)
* **Documentação Interativa:** SpringDoc OpenAPI 2+ (Swagger UI) para mapeamento de contratos, payloads JSON e execução de testes funcionais
* **Banco de Dados Relacional:** MySQL 8.0 (Instância cloud ativa hospedada via **Clever Cloud**)
* **Ambiente de Produção (Hospedagem):** **Render** (Containerização via Dockerfile multi-stage otimizada para limites estritos de memória RAM em instâncias gratuitas)

---

## Engenharia de Software, Design Patterns e Clean Code

Para combater o acoplamento e garantir a manutenibilidade, a API adota uma separação rígida de responsabilidades em camadas (`Controller -> DAO -> Database`). Abaixo detalham-se as técnicas aplicadas:

### 1. Padrão Data Access Object (DAO) Isolado
A persistência de dados é totalmente agnóstica às regras de negócio ou de transporte HTTP. Cada entidade possui sua interface de contrato (`ProdutoDAO`, `PedidoDAO`) e sua respectiva classe de implementação JDBC (`ProdutoDaoImpl`, `PedidoDaoImpl`), mantendo as consultas SQL isoladas de outras camadas do sistema.

### 2. Controle de Chave Transacional Mestre-Detalhe (`@Transactional`)
O maior desafio arquitetural do projeto reside no salvamento do **Pedido** e de sua lista de **Itens**.
* **Como funciona:** O método `salvar` na classe `PedidoDaoImpl` é blindado com a anotação `@Transactional`. A API primeiro dispara o `INSERT` do cabeçalho do pedido. Através do componente `GeneratedKeyHolder`, o Java intercepta o ID auto-incrementado gerado pelo MySQL na nuvem, injeta esse ID em cada item de carrinho em tempo de execução e, em seguida, dispara o lote de inserções dos itens na tabela vinculada.
* **Consistência Atômica (ACID):** Se qualquer inserção de item falhar no laço de repetição, a transação inteira sofre *rollback* automático pelo Spring, impedindo a existência de pedidos órfãos ou dados corrompidos no banco.

### 3. Otimização contra Memory Leaks e Conexões Zumbis
* **Ajuste de Pool do Hikari:** Para rodar de forma saudável em planos cloud gratuitos, o pool de conexões foi travado em `maximum-pool-size=2`. Isso força a API a reutilizar conexões em sockets de forma extremamente eficiente, evitando erros de estouro de sessões simultâneas (`max_user_connections`).
* **RowMapper Manual:** Toda a conversão do cursor bruto do banco de dados (`ResultSet`) para objetos Java ricos foi programada de forma explícita, mapeando campos snake_case do banco (ex: `status_pedido`, `categoria_id`) para atributos camelCase do Java com validações preventivas para valores nulos.

---

## Estrutura do Banco de Dados

O sistema utiliza um banco SQL relacional com as seguintes tabelas e relações:

* `categoria` (1:N com produtos)
* `produto` (N:1 com categorias e 1:N com item_pedido)
* `pedido` (1:N com item_pedido)
* `item_pedido` (Tabela associativa / Itens do carrinho que amarram o pedido ao produto)

### Detalhamento das Regras de Integridade:
* **Categoria ── (1:N) ── Produto:** Uma categoria específica pode conter múltiplos produtos vinculados (ex: a categoria 'Gamer' tem mouses, teclados, etc.), mas cada produto pertence a apenas uma categoria. (*Mapeado via chave estrangeira `categoria_id` na tabela produto*).
* **Pedido ── (1:N) ── Item_Pedido ── (N:1) ── Produto:** Um pedido pode conter vários produtos em quantidades diferentes, e um produto pode aparecer em vários pedidos de clientes distintos. Essa relação Muitos-para-Muitos é resolvida através da tabela associativa `item_pedido`.
