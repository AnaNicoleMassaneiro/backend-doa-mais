# Backend do Projeto DOA+

Este é o repositório do backend do Projeto DOA+, uma aplicação desenvolvida em Quarkus.

## Pré-requisitos

Antes de começar, verifique se você tem os seguintes requisitos instalados em seu sistema:

- Java Development Kit (JDK) 11 ou superior
- Maven
- PostgreSQL (ou outro banco de dados de sua escolha)
- IDE de sua preferência (recomendamos o IntelliJ IDEA ou o Eclipse)

## Configuração do Banco de Dados

Certifique-se de ter o PostgreSQL instalado e configurado em seu sistema. Crie um banco de dados vazio para o projeto.

Acesse o arquivo `application.properties` localizado em `src/main/resources` e atualize as informações de conexão com o banco de dados de acordo com sua configuração:

```
quarkus.datasource.url=jdbc:postgresql://localhost:5432/meu_banco_de_dados
quarkus.datasource.username=seu_usuario
quarkus.datasource.password=sua_senha
```

## Executando o Backend

1. Clone este repositório em seu ambiente de desenvolvimento:

```
git clone https://github.com/AnaNicoleMassaneiro/backend-doa-mais
```

2. Abra o projeto em sua IDE e aguarde a importação das dependências.

3. Execute a aplicação usando o Maven:

```
mvn compile quarkus:dev
```

O servidor Quarkus será iniciado e estará pronto para receber solicitações.

## Documentação da API

Acesse a documentação da API do backend em [http://localhost:8080/swagger-ui](http://localhost:8080/swagger-ui) para obter informações sobre os endpoints disponíveis e como utilizá-los.

## Licença

Este projeto está licenciado sob a licença MIT. Consulte o arquivo LICENSE para obter mais detalhes.

## Contato

Em caso de dúvidas ou sugestões, entre em contato comigo através do email [ana.nicole02@gmail.com](mailto:ana.nicole02@gmail.com).

