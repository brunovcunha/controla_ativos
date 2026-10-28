# 🚀 Controle de Ativos — Backend

**API REST para gerenciamento de ativos de TI, colaboradores, localizações, empréstimos e manutenções.**

Este projeto faz parte de uma aplicação full-stack voltada ao controle de ativos corporativos, com foco na organização dos equipamentos, rastreabilidade e gerenciamento de operações.

<p align="center">
  <img src="https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java 21" />
  <img src="https://img.shields.io/badge/Spring%20Boot-4.1.1-6DB33F?style=for-the-badge&logo=springboot&logoColor=white" alt="Spring Boot" />
  <img src="https://img.shields.io/badge/Maven-Build-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white" alt="Maven" />
  <img src="https://img.shields.io/badge/API-REST-005571?style=for-the-badge" alt="REST API" />
</p>

---

## 📌 Sobre o projeto

O **Controle de Ativos** tem como objetivo centralizar o gerenciamento de equipamentos e recursos corporativos, permitindo organizar informações e operações por meio de uma API REST.

A aplicação está estruturada em módulos, facilitando a manutenção do código e a evolução das funcionalidades.

### 🎯 Principais objetivos

- 📦 Gerenciar o cadastro e o status dos ativos.
- 👥 Manter o cadastro de colaboradores e departamentos.
- 📍 Organizar as localizações dos equipamentos.
- 📅 Controlar agendamentos e operações de empréstimo de ativos.
- 🔧 Gerenciar informações de manutenção.
- 🔗 Disponibilizar endpoints para integração com o frontend.

## 🖥️ Frontend

A interface da aplicação é desenvolvida em um projeto separado.

<p align="center">
  <a href="https://github.com/brunovcunha/controla_ativos_front">
    <img src="https://img.shields.io/badge/Acessar%20reposit%C3%B3rio-Frontend-3178C6?style=for-the-badge&logo=github&logoColor=white" alt="Acessar repositório frontend" />
  </a>
</p>

🔗 **Repositório:** [controla_ativos_front](https://github.com/brunovcunha/controla_ativos_front)

## 🛠️ Tecnologias utilizadas

| Tecnologia | Finalidade |
|---|---|
| Java 21 | Linguagem de programação |
| Spring Boot | Estrutura da aplicação e criação da API |
| Spring Data JPA | Persistência e acesso aos dados |
| Jakarta Validation | Validação de dados de entrada |
| Maven | Gerenciamento de dependências e build |
| PostgreSQL | Banco de dados relacional |
| JUnit 5 | Testes automatizados |
| Mockito | Simulação de dependências nos testes |
| Lombok | Redução de código repetitivo |

## 🧩 Módulos da aplicação

| Módulo | Responsabilidade |
|---|---|
| `asset` | Cadastro e gerenciamento de ativos |
| `department` | Gerenciamento de departamentos |
| `employee` | Cadastro e gerenciamento de colaboradores |
| `location` | Controle de localizações |
| `loan` | Agendamento e controle de empréstimos |
| `maintenance` | Gerenciamento de manutenções |
| `config` | Configurações e componentes transversais da aplicação |

## 🏗️ Arquitetura

O backend utiliza uma organização modular em camadas, separando as responsabilidades da aplicação.

```text
src/main/java/
└── br/com/brunov/controla_ativos/controla_ativos/
    ├── asset/
    │   ├── controller/
    │   ├── dto/
    │   ├── entity/
    │   ├── repository/
    │   └── service/
    ├── department/
    ├── employee/
    ├── location/
    ├── loan/
    ├── maintenance/
    └── config/
```

*Observação: a estrutura interna ilustrada é conceitual; os nomes e a distribuição reais dos pacotes podem variar conforme a implementação.*

### Responsabilidades das camadas

- **Controller:** recebe as requisições HTTP e retorna as respostas da API.
- **DTO:** define os dados recebidos e retornados pelos endpoints.
- **Service:** concentra as regras de negócio e as validações.
- **Repository:** realiza as operações de persistência por meio do Spring Data JPA.
- **Entity:** representa as entidades persistidas no banco de dados.
- **Config:** reúne configurações compartilhadas da aplicação.

## ⚙️ Pré-requisitos

Antes de executar o projeto, verifique se o ambiente possui:

- [Java JDK 21](https://adoptium.net/)
- [PostgreSQL](https://www.postgresql.org/)
- [Git](https://git-scm.com/)
- Maven ou o Maven Wrapper incluído no projeto.

## 🚀 Como executar

### 1. Clonar o repositório

Substitua o endereço abaixo pela URL real do repositório backend.

```bash
git clone https://github.com/brunovcunha/controla_ativos.git
cd controla_ativos
```

### 2. Configurar o banco de dados

Crie o banco de dados PostgreSQL destinado à aplicação.

Em seguida, configure a conexão no arquivo `src/main/resources/application.properties`, conforme as propriedades utilizadas pelo projeto.

Exemplo de configuração:

```properties
spring.datasource.url=${DB_URL:jdbc:postgresql://localhost:5432/controle_ativos}
spring.datasource.username=${DB_USERNAME:postgres}
spring.datasource.password=${DB_PASSWORD}

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
```

> **Importante:** ajuste as propriedades de acordo com a configuração real do projeto. O exemplo de `ddl-auto=update` é apropriado apenas se essa for a estratégia escolhida para o ambiente; em produção, prefira um processo controlado de migração do banco. Não versione senhas ou credenciais reais.

### 3. Executar a aplicação

No Windows, utilizando o Maven Wrapper:

```powershell
.\mvnw.cmd spring-boot:run
```

No Linux ou macOS:

```bash
./mvnw spring-boot:run
```

Se o Maven estiver instalado globalmente, também é possível executar:

```bash
mvn spring-boot:run
```

Após a inicialização, a API ficará disponível no endereço configurado. Em uma configuração local padrão, acesse:

```text
http://localhost:8080
```

## 🔌 Endpoints da API

Os endpoints abaixo ilustram as operações de gerenciamento de localizações implementadas no projeto.

| Método | Endpoint | Descrição |
|---|---|---|
| `POST` | `/api/locations` | Cadastra uma localização |
| `GET` | `/api/locations` | Lista as localizações |
| `GET` | `/api/locations/{id}` | Consulta uma localização |
| `PATCH` | `/api/locations/{id}` | Atualiza parcialmente uma localização |
| `DELETE` | `/api/locations/{id}` | Executa a operação de exclusão definida pelo serviço |

Os demais módulos disponibilizam operações conforme seus respectivos controllers e regras de negócio.

## 🧪 Testes automatizados

O projeto utiliza **JUnit 5** e **Mockito** para testar as regras de negócio e simular dependências durante os testes.

Executar todos os testes:

```bash
.\mvnw.cmd test
```

Executar a verificação completa do build:

```bash
.\mvnw.cmd verify
```

No Linux ou macOS, substitua `.\mvnw.cmd` por `./mvnw`.

## 🔐 Tratamento de erros

A aplicação possui exceções personalizadas para representar situações de negócio, como:

- Recurso não encontrado.
- Conflito com dados já existentes.
- Operação incompatível com as regras de negócio.

A validação de entrada e o tratamento centralizado de exceções ajudam a manter as respostas da API consistentes.

## 🔗 Integração com o frontend

Para conectar o frontend à API, configure a variável de ambiente `VITE_API_URL` no projeto de interface.

Exemplo de `.env` para execução local:

```env
VITE_API_URL=http://localhost:8080/api
```

Verifique também a configuração de CORS do backend para permitir a origem utilizada pelo frontend.

> **Atenção:** a URL, as portas e as origens permitidas devem corresponder ao ambiente em que cada aplicação estiver sendo executada.

## 📈 Status do projeto

🚧 **Em desenvolvimento**

O projeto está sendo desenvolvido de forma modular, com evolução gradual das funcionalidades, validações de negócio e testes automatizados.

## 👨‍💻 Desenvolvimento

Este repositório contém exclusivamente o backend da aplicação Controle de Ativos.

Para acessar a interface, consulte o [repositório do frontend](https://github.com/brunovcunha/controla_ativos_front).

---

<p align="center">
  Desenvolvido para facilitar o gerenciamento de ativos e recursos corporativos.
</p>
