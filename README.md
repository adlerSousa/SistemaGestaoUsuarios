# Sistema de Gestão de Usuários

Este projeto consiste em um sistema desktop desenvolvido em Java para o gerenciamento de usuários, perfis e notificações. O sistema foi projetado seguindo a arquitetura MVP (Model-View-Presenter) na abordagem *Passive View* e utiliza SQLite para persistência de dados.

## 🚀 Tecnologias Utilizadas

* Linguagem: Java 17
* Gerenciador de Projetos: Maven
* Interface Gráfica: Java Swing (Arquivos `.form` do NetBeans)
* Banco de Dados: SQLite (JDBC)
* Arquitetura: Model-View-Presenter (MVP)
* Bibliotecas Externas (via JitPack):
    * `LoggerLib` (Geração de logs em CSV/JSONL)
    * `ValidadorSenha` (Validação de complexidade de senhas)

## ⚙️ Funcionalidades Principais

1.  **Gestão de Usuários:**
    * Cadastro de usuários (Administrador e Padrão).
    * Autocadastro (com fluxo de autorização pendente).
    * Edição de perfil e exclusão de usuários.
    * Listagem com estatísticas de notificações.
2.  **Segurança:**
    * Autenticação (Login/Senha).
    * Autorização de novos cadastros pelo Administrador.
    * Validação forte de senhas.
3.  **Comunicação:**
    * Envio de notificações para múltiplos usuários.
    * Leitura e marcação de notificações.
4.  **Auditoria e Manutenção:**
    * Logs detalhados de operações (Sucesso e Falha).
    * Configuração do formato de Log (CSV ou JSONL).
    * **Restauração do Sistema:** Reset completo da base de dados para o estado inicial.

## 🔧 Como Executar o Projeto

### Pré-requisitos
* Java JDK 17 instalado.
* Maven instalado.
* Git instalado.

### Passo a Passo

1.  **Clone o repositório:**
    ```bash
    git clone https://github.com/adlerSousa/SistemaGestaoUsuarios.git
    ```

2.  **Compile o projeto e baixe as dependências:**
    ```bash
    mvn clean install
    ```

3.  **Execute a aplicação:**
    Você pode executar diretamente pela sua IDE (NetBeans/IntelliJ/Eclipse) rodando a classe `com.ufes.sistema.Main` ou via linha de comando (se o plugin jar estiver configurado):
    ```bash
    java -jar target/sistema-usuarios-1.0-SNAPSHOT.jar
    ```

## 🏗️ Decisões Arquiteturais

* Padrão Repository: Utilizado para isolar a camada de acesso a dados (DAO) da lógica de negócio.
* injeção de Dependência: As dependências (Repositories, Views) são injetadas nos Presenters, facilitando testes e manutenção.
* Passive View: A View não possui lógica de negócio; ela apenas notifica o Presenter sobre eventos (cliques) e exibe dados formatados pelo Presenter.

## 👥 Autores

* **Guilherme Cardoso Martins** - https://github.com/GuiCardosoMartins
* **Adler Amorim de Sousa** - https://github.com/adlerSousa
