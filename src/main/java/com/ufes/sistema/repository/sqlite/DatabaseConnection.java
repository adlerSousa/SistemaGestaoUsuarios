package com.ufes.sistema.repository.sqlite;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Gerenciador de Conexão com SQLite usando padrão Singleton.
 * Garante que apenas uma instância do gerenciador exista.
 */
public class DatabaseConnection {

    // Instância única da classe (Singleton)
    private static DatabaseConnection instance;
    
    // Objeto de conexão do Java SQL
    private Connection connection;
    
    // RNF04: Caminho relativo na raiz do projeto
    private static final String URL = "jdbc:sqlite:sistema_usuarios.db";

    // Construtor privado para impedir 'new DatabaseConnection()' fora daqui
    private DatabaseConnection() {
        // Deixamos vazio, a conexão será aberta sob demanda no getConnection
    }

    // Método estático para pegar a instância única do gerenciador
    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    // Método que entrega a conexão (e reconecta se necessário)
    public Connection getConnection() {
        try {
            // Se a conexão não existe OU foi fechada, abrimos uma nova
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(URL);
                System.out.println("Conexão com SQLite estabelecida/restaurada.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro fatal ao conectar no banco de dados: " + e.getMessage());
        }
        return connection;
    }
    
    // Método opcional para fechar explicitamente (bom para quando fechar o app)
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Conexão fechada.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}