# Atividade avaliativa (15%) - Projeto de Sistemas de Software - Criação e Manutenção de usuários

## 👥 Integrantes da Equipe
* **Guilherme Cardoso Martins**
* **Adler Amorim de Sousa**

## 🛠️ Instruções de Build e Execução

Este projeto utiliza **Java 17** e **Maven**. Siga os passos abaixo para compilar e rodar a aplicação.

### Pré-requisitos
* **Java JDK 17** (Obrigatório conforme Regra 7)
* **Maven** (Gerenciador de dependências)
* **Git**
* Conexão com a internet (para baixar dependências do JitPack)

### Comandos para Compilação e Execução

1.  **Clonar o repositório:**
    ```bash
    git clone https://github.com/adlerSousa/SistemaGestaoUsuarios.git
    ```

2.  **Compilar e baixar dependências:**
    Execute o comando abaixo na raiz do projeto (onde está o arquivo `pom.xml`):
    ```bash
    mvn clean install
    ```

3.  **Executar a aplicação:**
    Após o build com sucesso, execute via linha de comando:
    ```bash
    java -jar target/sistema-usuarios-1.0-SNAPSHOT.jar
    ```
    *Alternativamente, o projeto pode ser aberto no NetBeans e executado pressionando F6, desde que o ambiente esteja configurado com JDK 17.*

### Versões das Ferramentas
* **Java:** 17
* **Maven:** 3.8+
* **SQLite:** JDBC 3.x (Gerenciado pelo Maven)

---

## 📂 Estrutura de Pastas do Projeto

A estrutura segue o padrão Maven, organizada em pacotes conforme a arquitetura **MVP (Model-View-Presenter)**:

* `src/main/java/com/ufes/sistema`
    * `model/`: Contém as classes de domínio (Usuario, Notificacao, Configuracao).
    * `view/`: Contém as classes da interface gráfica (`.java` e `.form` do Swing).
    * `presenter/`: Contém a lógica de apresentação e interação entre Model e View.
    * `repository/`: Interfaces e implementações (SQLite) para persistência de dados.
    * `Main.java`: Classe principal de entrada da aplicação.
* `target/`: Diretório gerado automaticamente após o build (contém o `.jar`).
* `sistema.log`: Arquivo de registros de Logs.
* `sistema_usuarios.db`: Arquivo do banco de dados SQLite (gerado na raiz após a primeira execução).
* `pom.xml`: Arquivo de configuração do Maven e dependências.

---

## 📝 Descrição da Atividade

Sistema de gestão de usuários desenvolvido em Java Swing utilizando arquitetura MVP - Passive View. O sistema permite cadastro de usuários, gestão de perfis (Administrador/Padrão), envio e leitura de notificações, logs de auditoria e restauração completa do sistema.
