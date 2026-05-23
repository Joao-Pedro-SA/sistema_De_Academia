# GYM - API de Gerenciamento de Academia

Projeto simples em Java + Spring Boot + Maven + MySQL para a disciplina de Laboratório de Banco de Dados.

O sistema foi simplificado para ser fácil de explicar em sala. Ele gerencia:

- Alunos
- Professores
- Modalidades
- Planos
- Matrículas
- Pagamentos
- Inadimplências
- Relatórios por views do MySQL

## Tecnologias

- Java 17
- Spring Boot 3.3.5
- Maven
- Spring Web
- Spring Data JPA
- MySQL
- application.yaml

## Estrutura principal

```text
src/main/java/com/projetoUCB/GYM
├── controller
├── dto
├── model
├── repository
└── service
```

## Como rodar

### 1. Criar o banco

Abra o MySQL Workbench ou outro cliente MySQL e execute, nesta ordem:

```text
database/01-create-database.sql
database/02-dados-exemplo.sql
```

O primeiro script cria o banco `academia_db` e o usuário local:

```text
usuário: acad_admin
senha: admin123
```

Caso queira usar outro usuário/senha, altere também o arquivo:

```text
src/main/resources/application.yaml
```

### 2. Rodar a API

Na pasta raiz do projeto, execute:

```bash
mvn spring-boot:run
```

A API ficará disponível em:

```text
http://localhost:8080
```

## Endpoints principais

### Alunos

```http
POST /alunos
GET /alunos
GET /alunos/{id}
GET /alunos/buscar?nome=Ana
PUT /alunos/{id}
DELETE /alunos/{id}
```

Exemplo de cadastro de aluno:

```json
{
  "nome": "Ana Souza",
  "cpf": "11122233344",
  "email": "ana@email.com",
  "telefone": "62999990000",
  "dataNascimento": "2000-05-10",
  "sexo": "FEMININO",
  "endereco": "Rua A, 123"
}
```

### Modalidades

```http
POST /modalidades
GET /modalidades
GET /modalidades/{id}
PUT /modalidades/{id}
DELETE /modalidades/{id}
```

Exemplo:

```json
{
  "nome": "Musculação",
  "descricao": "Treino com aparelhos e pesos"
}
```

### Professores

```http
POST /professores
GET /professores
GET /professores/{id}
PUT /professores/{id}
DELETE /professores/{id}
```

Exemplo:

```json
{
  "nome": "Carlos Lima",
  "email": "carlos@email.com",
  "telefone": "62988887777",
  "modalidadeId": 1
}
```

### Planos

```http
POST /planos
GET /planos
GET /planos/{id}
PUT /planos/{id}
DELETE /planos/{id}
```

Exemplo:

```json
{
  "nome": "Mensal",
  "duracaoDias": 30,
  "valor": 99.90
}
```

### Matrículas

A matrícula é criada usando a procedure `sp_registrar_matricula` do banco.

```http
POST /matriculas
GET /matriculas
GET /matriculas/{id}
PUT /matriculas/{id}/cancelar
PUT /matriculas/verificar-vencidas
```

Exemplo:

```json
{
  "alunoId": 1,
  "planoId": 1,
  "formaPagamento": "PIX"
}
```

### Pagamentos

```http
POST /pagamentos
GET /pagamentos
GET /pagamentos/{id}
```

Exemplo:

```json
{
  "matriculaId": 1,
  "valor": 99.90,
  "formaPagamento": "PIX",
  "status": "PAGO"
}
```

### Inadimplências

```http
GET /inadimplencias
GET /inadimplencias/pendentes
GET /inadimplencias/{id}
PUT /inadimplencias/{id}/resolver
```

### Relatórios via views

```http
GET /relatorios/alunos-ativos
GET /relatorios/inadimplencias-pendentes
```

## Como explicar o projeto ao professor

1. O `model` representa as tabelas do banco.
2. O `repository` faz a comunicação com o banco usando Spring Data JPA.
3. O `service` concentra as regras de negócio.
4. O `controller` recebe as requisições HTTP e chama o service.
5. Os `dto` são usados para entrada e saída de dados, evitando expor diretamente as entidades.
6. O MySQL possui triggers, procedure, function, views e índices para atender à proposta da disciplina.
7. O check-in foi removido para reduzir a complexidade e focar no fluxo principal: aluno → plano → matrícula → pagamento → inadimplência.

## Frontend simples

O projeto também possui um frontend simples usando apenas:

- HTML
- CSS
- JavaScript puro
- Fetch API

Os arquivos ficam em:

```text
src/main/resources/static
├── index.html
├── style.css
└── script.js
```

Como o frontend foi colocado dentro da pasta `static` do Spring Boot, basta rodar a API normalmente:

```bash
mvn spring-boot:run
```

Depois, acesse no navegador:

```text
http://localhost:8080
```

ou:

```text
http://localhost:8080/index.html
```

O frontend permite:

- cadastrar, listar, editar e excluir alunos;
- cadastrar, listar, editar e excluir modalidades;
- cadastrar, listar, editar e excluir professores;
- cadastrar, listar, editar e excluir planos;
- criar, listar, filtrar, cancelar e verificar matrículas vencidas;
- registrar e listar pagamentos;
- consultar alunos ativos;
- consultar e resolver inadimplências pendentes.

Esse frontend foi feito de forma simples para apresentação acadêmica. Ele não usa autenticação, não tem build, não tem framework JavaScript e não precisa de deploy.
