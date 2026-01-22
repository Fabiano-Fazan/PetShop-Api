# 🐾 PetShop API

Uma API RESTful para o gerenciamento de um PetShop. O sistema orquestra desde o controle de estoque e fluxo de vendas até o agendamento de consultas veterinárias e gestão financeira automatizada.

## 📋 Sobre o Projeto

Este projeto foi desenvolvido para testar meus estudos. O sistema não apenas realiza CRUDs básicos, mas gerencia o ciclo de vida das operações comerciais:

* **Vendas:** Ao finalizar uma venda, o sistema automaticamente baixa o estoque e gera os registros financeiros (contas a receber).
* **Agendamentos:** Validação inteligente de conflitos de horários para veterinários.
* **Financeiro:** Geração automática de parcelas e controle de pagamentos parciais ou totais.

## 🚀 Tecnologias Utilizadas

* **Linguagem:** Java 21
* **Framework:** Spring Boot 3 (Web, Data JPA, Validation)
* **Segurança:** Spring Security + JWT (JSON Web Token)
* **Banco de Dados:** PostgreSQL (Produção/Dev)
* **Build Tool:** Gradle
* **Mapeamento:** MapStruct
* **Utilitários:** Lombok
* **Containerização:** Docker & Docker Compose
* **Testes:** JUnit 5, Mockito, AssertJ

## 📦 Como Rodar o Projeto

### Pré-requisitos
* Java 21+ instalado.
* Docker e Docker Compose (Opcional, mas recomendado para o Banco de Dados).

### Passo a Passo

1.  **Clone o repositório:**
    ```bash
    git clone [https://github.com/seu-usuario/petshop-api.git](https://github.com/seu-usuario/petshop-api.git)
    cd petshop-api
    ```

2.  **Configuração do Banco de Dados:**
    O projeto já possui um arquivo `docker-compose.yml`. Para subir o PostgreSQL:
    ```bash
    docker-compose up -d
    ```

3.  **Execute a aplicação:**
    Utilize o wrapper do Gradle (não é necessário ter o Gradle instalado globalmente).
    * **Windows:**
     ```cmd
     gradlew.bat bootRun
     ```

A API estará disponível em: `http://localhost:8080`

## 🧪 Rodando os Testes

O projeto conta com uma suíte abrangente de testes unitários cobrindo Services, Generators e Validadores.

Para executar os testes:

```bash
./gradlew test
