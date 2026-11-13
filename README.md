# Loja Virtual

Sistema de loja virtual desenvolvido em **Java 21**, utilizando **Programação Orientada a Objetos (POO)**, **Swing** para a interface gráfica e **Gson** para persistência dos dados em arquivos JSON.

O projeto implementa uma aplicação desktop com funcionalidades de gerenciamento de produtos, compradores, carrinho de compras, compras, histórico e diferentes formas de pagamento.

---

## Sobre o projeto

A **Loja Virtual** é uma aplicação desktop desenvolvida em Java com arquitetura organizada em camadas, separando as responsabilidades de:

- regras de negócio;
- serviços;
- persistência de dados;
- interface gráfica;
- tratamento de exceções;
- gerenciamento de carrinho e compras.

A aplicação é executada por meio de uma interface gráfica desenvolvida com **Java Swing**.

---

### Funcionalidades

O sistema possui componentes para:

### Compradores

- Cadastro e gerenciamento de compradores;
- Autenticação/login;
- Sessão do usuário;
- Acesso às informações relacionadas às compras.

### Produtos

- Cadastro de produtos;
- Consulta de produtos;
- Gerenciamento de produtos;
- Controle relacionado ao estoque;
- Tratamento de produtos duplicados ou não encontrados.

### Carrinho

- Adição de produtos ao carrinho;
- Gerenciamento dos itens do carrinho;
- Controle de quantidade;
- Validação de quantidade;
- Verificação de disponibilidade em estoque.

### Pagamentos

O projeto possui diferentes implementações de formas de pagamento:

- PIX;
- cartão de crédito;
- cartão de débito.

Também existe tratamento específico para situações em que o pagamento é recusado.

### Compras

- Criação de compras;
- Registro dos itens comprados;
- Processamento da compra;
- Controle de estoque;
- Histórico de compras.

### Persistência

Os dados são persistidos utilizando **arquivos JSON**, com a biblioteca Gson.

O projeto possui repositórios específicos para:

- compradores;
- produtos;
- compras.

---

## Tecnologias utilizadas

| Tecnologia | Utilização |
|---|---|
| **Java 21** | Linguagem e plataforma |
| **Java Swing** | Interface gráfica |
| **Maven** | Gerenciamento do projeto e dependências |
| **Gson 2.11.0** | Serialização e desserialização JSON |
| **Git/GitHub** | Versionamento do código |

---

## Arquitetura do projeto

O projeto está organizado em pacotes de acordo com suas responsabilidades:

```text
src/
└── main/
    └── java/
        ├── module-info.java
        │
        └── kyj/
            └── poo/
                ├── Main.java
                │
                ├── persistencia/
                │   ├── ArquivoJson.java
                │   ├── CompraRepositorio.java
                │   ├── CompraRepositorioJson.java
                │   ├── CompradorRepositorio.java
                │   ├── CompradorRepositorioJson.java
                │   ├── ProdutoRepositorio.java
                │   └── ProdutoRepositorioJson.java
                │
                ├── regras/
                │   ├── Comprador.java
                │   ├── Produto.java
                │   │
                │   ├── carrinho/
                │   │   ├── Carrinho.java
                │   │   └── ItemDoCarrinho.java
                │   │
                │   ├── compra/
                │   │   ├── Compra.java
                │   │   └── ItemDaCompra.java
                │   │
                │   ├── excecao/
                │   │   ├── EstoqueInsuficienteException.java
                │   │   ├── PagamentoRecusadoException.java
                │   │   ├── PersistenciaException.java
                │   │   ├── ProdutoDuplicadoException.java
                │   │   ├── ProdutoNaoEncontradoException.java
                │   │   └── QuantidadeInvalidaException.java
                │   │
                │   └── pagamento/
                │       ├── FormaPagamento.java
                │       ├── PagamentoComCartaoDeCredito.java
                │       ├── PagamentoComCartaoDeDebito.java
                │       └── PagamentoComPix.java
                │
                ├── servico/
                │   ├── CompraService.java
                │   ├── CompradorService.java
                │   └── ProdutoService.java
                │
                └── ui/
                    ├── Aplicacao.java
                    ├── Botao.java
                    ├── Carrossel.java
                    ├── Cartao.java
                    ├── PainelGradiente.java
                    ├── Sessao.java
                    ├── Tela.java
                    ├── Tema.java
                    │
                    └── tela/
                        ├── TelaAdmin.java
                        ├── TelaCarrinho.java
                        ├── TelaCheckout.java
                        ├── TelaComprador.java
                        ├── TelaHistorico.java
                        ├── TelaLogin.java
                        └── TelaLoja.java
```

### Organização das camadas

**`regras/`**

Contém as entidades e regras relacionadas ao domínio da aplicação, como produtos, compradores, carrinho, compras e pagamentos.

**`servico/`**

Contém os serviços responsáveis por intermediar as operações de negócio, como:

- `ProdutoService`;
- `CompradorService`;
- `CompraService`.

**`persistencia/`**

Responsável pelo armazenamento e recuperação dos dados utilizando JSON.

**`ui/`**

Contém a interface gráfica desenvolvida com Swing, incluindo telas, componentes visuais, tema e gerenciamento de sessão.

**`regras/excecao/`**

Contém exceções específicas do domínio, permitindo tratar situações como estoque insuficiente, quantidade inválida, produto inexistente e pagamento recusado.

---

## Pré-requisitos

Antes de executar o projeto, é necessário ter instalado:

- **Java JDK 21 ou superior**;
- **Maven**;
- Git, caso o projeto seja obtido pelo GitHub.

Verifique as instalações:

```bash
java -version
mvn -version
```

O projeto está configurado para utilizar **Java 21**.

---

## Instalação

Clone o repositório:

```bash
git clone https://github.com/yhsww/loja-virtual.git
```

Entre na pasta:

```bash
cd loja-virtual
```

---

## Compilação

Para compilar o projeto utilizando Maven:

```bash
mvn clean compile
```

O Maven fará o gerenciamento da dependência do Gson definida no `pom.xml`.

---

## Execução

A classe principal da aplicação é:

```text
kyj.poo.Main
```

Ela configura o ambiente visual do Swing e inicia a aplicação.

Com Maven, você pode compilar o projeto e executar a classe principal pela sua IDE ou por uma configuração de execução Java.

Caso esteja utilizando uma IDE como IntelliJ IDEA, Eclipse ou VS Code, configure a execução utilizando:

```text
kyj.poo.Main
```

---

## Chatbot da Íris

O projeto também possui um chatbot integrado à interface Swing. O backend da assistente Íris está em Python, utilizando FastAPI, Uvicorn e a API do Google Gemini.

### Estrutura do chatbot

chat-bot/
├── chatbot.py
├── servidor.py
├── requirements.txt
└── .env.example

Os dados utilizados pelo chatbot permanecem na pasta dados/: compradores.json, produtos.json e compras.json.

O backend localiza essa pasta automaticamente a partir da localização do próprio chat-bot, portanto não é necessário configurar um caminho manual para os arquivos JSON.

### Configuração

Entre na pasta do backend:

cd chat-bot

Crie o ambiente virtual:

python -m venv .venv

Ative:

.\.venv\Scripts\Activate.ps1

Instale as dependências:

python -m pip install -r requirements.txt

Crie o arquivo chat-bot/.env a partir do exemplo:

Copy-Item .env.example .env

Depois, edite o .env e informe sua chave do Gemini:

GEMINI_API_KEY=sua_chave
GEMINI_MODEL=gemini-3.5-flash

Não envie o arquivo .env para o GitHub. Ele já está protegido pelo .gitignore.

### Executando a API

O Maven deve ser executado a partir da pasta loja-virtual, e o servidor Python a partir da pasta chat-bot.

Na pasta chat-bot:

python -m uvicorn servidor:app --host 0.0.0.0 --port 5000

A API ficará disponível em http://127.0.0.1:5000.

Teste a saúde da API:

Invoke-RestMethod -Uri http://127.0.0.1:5000/health

Resposta esperada: status=ok.

### Testando o chatbot

Use uma requisição POST para /chat. Exemplo no PowerShell:

$body = @{ nome = "Fulano"; cpf = "12345678910"; email = "fulano@email.com"; pergunta = "Olá" } | ConvertTo-Json
Invoke-RestMethod -Uri http://127.0.0.1:5000/chat -Method Post -ContentType "application/json; charset=utf-8" -Body ([System.Text.Encoding]::UTF8.GetBytes($body))

O chatbot utiliza o CPF e o e-mail enviados na requisição para localizar somente o histórico de compras correspondente ao cliente.

### Problemas comuns

Porta 5000 já está em uso: verifique com netstat -ano | findstr :5000 e encerre somente o PID que você confirmou ser do servidor anterior.

Erro ao executar Maven: o pom.xml está na raiz de loja-virtual. Use cd loja-virtual e depois mvn clean compile. Não execute Maven dentro de chat-bot.

Erro do Gemini: o backend registra no terminal o tipo e a mensagem do erro retornado pelo Gemini. Confirme GEMINI_API_KEY, GEMINI_MODEL e a disponibilidade do modelo.


## Persistência de dados

A aplicação utiliza **Gson 2.11.0** para trabalhar com dados em formato JSON.

A camada de persistência possui implementações específicas para os principais objetos do sistema:

```text
CompraRepositorioJson
CompradorRepositorioJson
ProdutoRepositorioJson
```

Isso permite separar as regras de negócio da forma como os dados são armazenados.

---

## Tratamento de exceções

O projeto possui exceções específicas para diferentes situações:

```text
EstoqueInsuficienteException
PagamentoRecusadoException
PersistenciaException
ProdutoDuplicadoException
ProdutoNaoEncontradoException
QuantidadeInvalidaException
```

Essa estrutura permite que situações específicas da aplicação sejam identificadas e tratadas de maneira apropriada.

---

## Formas de pagamento

O projeto utiliza uma estrutura baseada na abstração:

```text
FormaPagamento
```

com implementações para:

```text
PagamentoComPix
PagamentoComCartaoDeCredito
PagamentoComCartaoDeDebito
```

Essa organização permite adicionar ou alterar formas de pagamento sem concentrar toda a lógica em uma única classe.

---

## Interface gráfica

A interface da aplicação foi desenvolvida utilizando **Java Swing**.

Entre as telas disponíveis no projeto estão:

- Tela de login;
- Tela da loja;
- Tela do comprador;
- Tela do carrinho;
- Tela de checkout;
- Tela de histórico;
- Tela administrativa.

Também foram criados componentes visuais reutilizáveis, como botões, cartões, carrossel, painéis e gerenciamento de tema.

---

## Testes e validação

Para verificar se o projeto compila corretamente:

```bash
mvn clean compile
```

Também é recomendado executar a aplicação e verificar os principais fluxos:

1. Login;
2. Cadastro/consulta de produtos;
3. Adição de produtos ao carrinho;
4. Alteração da quantidade;
5. Checkout;
6. Seleção da forma de pagamento;
7. Registro da compra;
8. Consulta do histórico;
9. Operações administrativas.

> O projeto disponibilizado não apresenta uma suíte de testes automatizados configurada no conteúdo analisado. Por isso, a compilação e os testes funcionais da aplicação devem ser utilizados para validação, caso não sejam adicionados testes automatizados posteriormente.

---

## Conceitos de POO utilizados

O projeto foi estruturado utilizando conceitos de **Programação Orientada a Objetos**, incluindo:

- encapsulamento;
- abstração;
- interfaces;
- composição;
- separação de responsabilidades;
- organização em camadas;
- tratamento de exceções;
- polimorfismo nas formas de pagamento.

A separação entre regras de negócio, serviços, persistência e interface facilita a manutenção e evolução do sistema.

---

## Arquivos importantes

### `pom.xml`

Arquivo de configuração do Maven. Define:

- identificação do projeto;
- versão do Java;
- dependência do Gson;
- configuração do compilador.

### `module-info.java`

Define o módulo Java:

```text
kyj.poo
```

e suas dependências, incluindo:

```text
java.desktop
com.google.gson
```

### `Main.java`

É o ponto de entrada da aplicação.

---

Projeto desenvolvido como parte de atividades acadêmicas relacionadas à disciplina de **Programação Orientada a Objetos**.

---

## Licença

Este projeto é destinado a fins acadêmicos.
