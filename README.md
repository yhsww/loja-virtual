# Loja Virtual

Sistema de loja virtual desktop desenvolvido em **Java 21**, utilizando **Java Swing**, **Maven** e **Gson**, com integração a um chatbot inteligente chamado **Íris**.

O projeto foi desenvolvido com foco em **Programação Orientada a Objetos**, organização de responsabilidades e integração entre uma aplicação desktop Java e uma API desenvolvida em Python.

## Sobre o projeto

A Loja Virtual permite realizar o fluxo completo de uma compra, desde a consulta de produtos até a finalização do pedido.

O sistema possui funcionalidades como:

- Cadastro e gerenciamento de produtos;
- Cadastro de compradores;
- Controle de estoque;
- Carrinho de compras;
- Cálculo de valores da compra;
- Aplicação de descontos;
- Finalização de pedidos;
- Diferentes formas de pagamento;
- Histórico de compras;
- Área administrativa;
- Persistência dos dados em arquivos JSON;
- Chatbot integrado para atendimento ao cliente.

## Diferencial: Chatbot Íris

O principal diferencial do projeto é a integração com a **Íris**, uma assistente virtual desenvolvida para interagir com os clientes da loja.

A Íris utiliza **Google Gemini** para compreender as perguntas e gerar respostas em linguagem natural.

A integração é realizada por meio de uma API desenvolvida com **FastAPI**, permitindo que a aplicação Java se comunique com o serviço Python através de requisições HTTP.

A assistente consegue utilizar informações relacionadas ao cliente para responder perguntas sobre:

- Produtos disponíveis;
- Preços;
- Estoque;
- Compras realizadas;
- Histórico de pedidos;
- Informações relacionadas ao atendimento.

O contexto enviado à IA é construído dinamicamente a partir dos dados da aplicação, evitando que a assistente responda utilizando informações de outros clientes.

## Arquitetura

O projeto é dividido em duas aplicações principais:

**Aplicação Java**
- Interface gráfica;
- Regras de negócio;
- Produtos;
- Compradores;
- Carrinho;
- Compras;
- Pagamentos;
- Persistência dos dados;
- Comunicação com a API da Íris.

**Backend da Íris**
- API REST com FastAPI;
- Integração com Google Gemini;
- Construção do contexto do cliente;
- Processamento das perguntas;
- Retorno das respostas para a aplicação Java.

A comunicação entre as duas partes ocorre através de HTTP:

```text
Aplicação Java
      |
      | HTTP / JSON
      v
Backend da Íris
      |
      | API
      v
Google Gemini
```

## Tecnologias

| Tecnologia | Utilização |
|---|---|
| Java 21 | Aplicação principal |
| Java Swing | Interface gráfica |
| Maven | Gerenciamento do projeto |
| Gson | Manipulação dos arquivos JSON |
| Python | Backend da Íris |
| FastAPI | API do chatbot |
| Uvicorn | Servidor da API |
| Google Gemini | Inteligência artificial |

## Pré-requisitos

Antes de executar o projeto, é necessário ter instalado:

- JDK 21;
- Maven;
- Python 3;
- Chave da API do Google Gemini.

Verifique as instalações:

```bash
java -version
mvn -version
python --version
```

## Configuração da Íris

Acesse a pasta do chatbot:

```bash
cd chat-bot
```

Crie o ambiente virtual:

```bash
python -m venv .venv
```

No Windows PowerShell:

```powershell
.\.venv\Scripts\Activate.ps1
```

Instale as dependências:

```bash
python -m pip install -r requirements.txt
```

Crie o arquivo `.env`:

```powershell
Copy-Item .env.example .env
```

Configure a chave do Gemini:

```env
GEMINI_API_KEY=sua_chave_aqui
GEMINI_MODEL=gemini-3.5-flash
```

## Execução

### 1. Iniciar a API da Íris

Na pasta `chat-bot`:

```bash
python -m uvicorn servidor:app --host 127.0.0.1 --port 5000
```

A API ficará disponível em:

```text
http://127.0.0.1:5000
```

Para verificar se o servidor está funcionando:

```text
http://127.0.0.1:5000/health
```

A resposta esperada é:

```json
{
  "status": "ok"
}
```

### 2. Executar a Loja Virtual

Abra outro terminal na pasta principal do projeto e execute:

```bash
mvn clean compile
```

Depois:

```bash
mvn exec:java
```

A aplicação Java será iniciada e poderá utilizar a Íris através da API.

## Fluxo de execução

```text
Cliente
   |
   v
Loja Virtual
(Java + Swing)
   |
   | Requisição HTTP
   v
API da Íris
(Python + FastAPI)
   |
   | Solicitação
   v
Google Gemini
   |
   | Resposta
   v
API da Íris
   |
   | JSON
   v
Loja Virtual
   |
   v
Resposta exibida ao cliente
```

## Programação Orientada a Objetos

O projeto aplica conceitos fundamentais de POO, incluindo:

- Encapsulamento;
- Abstração;
- Herança;
- Polimorfismo;
- Interfaces;
- Composição;
- Separação de responsabilidades;
- Tratamento de exceções.

A estrutura do sistema também busca separar as responsabilidades entre interface, regras de negócio, serviços, persistência e comunicação com a API.

## Persistência

Os dados da aplicação são armazenados em arquivos JSON, permitindo manter informações de produtos, compradores e compras sem a necessidade de um banco de dados externo.

A biblioteca **Gson** é utilizada para realizar a conversão entre objetos Java e JSON.

## Projeto acadêmico

Projeto desenvolvido para fins acadêmicos na disciplina de **Programação Orientada a Objetos (POO)**.
