# Portfolio Backend API

![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![MongoDB](https://img.shields.io/badge/MongoDB-%234ea94b.svg?style=for-the-badge&logo=mongodb&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-black?style=for-the-badge&logo=JSON%20web%20tokens)

Backend API para um sistema de gerenciamento de portfólio, construído com Spring Boot e MongoDB.

## Funcionalidades

- **Gerenciamento de Projetos** - Operações CRUD para projetos do portfólio.
- **Autenticação** - Autenticação segura baseada em JWT.
- **Upload de Arquivos** - Suporte para upload de imagens dos projetos.
- **API RESTful** - Endpoints limpos e padronizados.
- **Integração com MongoDB** - Banco de dados NoSQL para armazenamento flexível de dados.

## Tecnologias

- Java 17
- Spring Boot 3.1.5
- Spring Data MongoDB
- Spring Security
- Autenticação JWT
- Maven

## Instruções de Configuração

### Pré-requisitos

- JDK 17 ou superior
- Conta no MongoDB Atlas ou instância local do MongoDB
- Maven 3.8 ou superior

### Instalação

1. Clone o repositório:
   ```bash
   git clone https://github.com/Moost999/BackendPortfol.git
   cd BackendPortfol
   ```

2. Configure as variáveis de ambiente:
   Crie um arquivo `.env` no diretório raiz com o seguinte conteúdo:
   ```env
   DATABASE_URL=mongodb+srv://usuario:senha@cluster.mongodb.net/database?retryWrites=true&w=majority
   SPRING_DATABASE=nome_do_seu_banco_de_dados
   JWT_SECRET=sua_chave_secreta_jwt
   ```

3. Construa e execute o projeto:
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

## Documentação da API

A API estará disponível em `http://localhost:8080`.

### Autenticação

| Endpoint | Método | Descrição |
|----------|--------|------------|
| `/api/auth/signin` | POST | Login do usuário |
| `/api/auth/signup` | POST | Registro de usuário |

### Projetos

| Endpoint | Método | Descrição |
|----------|--------|------------|
| `/api/projects` | GET | Obter todos os projetos |
| `/api/projects` | POST | Criar novo projeto |
| `/api/projects/{id}` | GET | Obter projeto por ID |
| `/api/projects/{id}` | PUT | Atualizar projeto |
| `/api/projects/{id}` | DELETE | Deletar projeto |

## Implantação

### Render.com

1. Crie um novo Web Service.
2. Conecte seu repositório GitHub.
3. Configure as variáveis de ambiente:
   - `DATABASE_URL`
   - `SPRING_DATABASE`
   - `JWT_SECRET`
4. Defina o comando de build: `mvn clean install`
5. Defina o comando de start: `java -jar target/portfolio.jar`

## Contribuição

Pull requests são bem-vindos. Para mudanças significativas, por favor, abra uma issue primeiro para discutir o que você gostaria de alterar.

## Licença

MIT
