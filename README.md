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

* **Linguagem Principal:** Java 17+
* **Framework Base:** Spring Boot 3+
* **Redução de Boilerplate:** Lombok 
* **Documentação Interativa:** SpringDoc OpenAPI 2+ (Swagger UI)
* **Banco de Dados Relacional: MySQL 8.0 
* **Ambiente de Produção (Hospedagem):** Render(API) e Clever Cloud(Banco)

---

## Princípios de Engenharia de Software e Boas Práticas Aplicadas

Para garantir que a aplicação seja escalável, sustentável e de fácil manutenção, o desenvolvimento foi guiado pelos principais pilares de **Clean Code**, **S.O.L.I.D.** e **Design Patterns**:

### 1. Princípios de Clean Code (Código Limpo)
* **Prevenção de Resource Leaks (Vazamento de Recursos):** Ao utilizar o `NamedParameterJdbcOperations` do Spring, o gerenciamento de cursores, buffers e fechamento de conexões com o banco na nuvem é automatizado. Isso elimina o risco de *Memory Leaks* ou tabelas travadas por transações órfãs.
* **Expressividade do Domínio:** Variáveis genéricas e sintaxes SQL confusas foram substituídas por nomenclatura clara e contextualizada (ex: `sqlPedido`, `idPedidoGerado`, `itemPedidoRowMapper`), tornando o código autoexplicativo.
* **Padrão Builder para Imutabilidade:** Uso do padrão *Fluent Builder* (via Lombok) nos `RowMappers` manuais. Isso garante a criação de objetos complexos e imutáveis sem a necessidade de construtores telescópicos e confusos.

### 2. Princípios do S.O.L.I.D.
* **S — Single Responsibility Principle (Princípio da Responsabilidade Única):** Separação total de conceitos. Os *Controllers* cuidam exclusivamente do protocolo HTTP e payloads JSON, enquanto as classes *DAO* isolam estritamente a sintaxe e persistência SQL.
* **O — Open/Closed Principle (Princípio Aberto/Fechado):** A arquitetura foi desenhada utilizando interfaces de contrato (ex: `PedidoDAO`). O sistema está aberto para expansão (podendo trocar o MySQL por outro banco ou por JPA no futuro), mas totalmente fechado para modificação nas camadas superiores.
* **D — Dependency Inversion Principle (Princípio da Inversão de Dependência):** Os *Controllers* não conhecem as classes concretas de banco de dados (como `PedidoDaoImpl`). Eles dependem unicamente das interfaces (*abstrações*), delegando ao Spring Boot a injeção da implementação correta em tempo de execução.

### 3. Design Patterns (Padrões de Projeto)
* **Data Access Object (DAO):** Padrão arquitetural utilizado para encapsular toda a lógica de acesso ao banco de dados. A camada de negócio interage apenas com entidades Java puras, permanecendo agnóstica a tabelas e dialetos SQL.
* **Mestre-Detalhe Transacional:** Implementado no salvamento atômico de pedidos. A anotação `@Transactional` gerencia a consistência **ACID** do banco: ou o cabeçalho do pedido e todos os seus itens associados são persistidos com sucesso, ou a transação inteira sofre *rollback* em bloco caso ocorra alguma falha no laço, impedindo a corrupção de dados.

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
