/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ufes.sistema.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Adler
 */
public class DatabaseConnection {

    private static DatabaseConnection singleInstance = null;
    private Connection connection;
    private static final String URL = "jdbc:sqlite:sistema_usuarios.db";

    private DatabaseConnection() {
        try {
            connection = DriverManager.getConnection(URL);
            System.out.println("Conexão com SQLite estabelecida.");
        } catch (SQLException e) {
            throw new RuntimeException("Erro fatal ao conectar no banco de dados: " + e.getMessage());
        }
    }

    public static DatabaseConnection getInstance() {
        if (singleInstance == null) {
            singleInstance = new DatabaseConnection();
        } else {
            try {
                if (singleInstance.getConnection().isClosed()) {
                    singleInstance = new DatabaseConnection();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return singleInstance;
    }

    public Connection getConnection() {
        return connection;
    }

}
