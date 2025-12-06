/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ufes.sistema.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Adler
 */
public class DatabaseUtil {

    public static void inicializarTabelas() {

        Connection conn = DatabaseConnection.getInstance().getConnection();

        try (Statement stmt = conn.createStatement()) {

            String sqlUsuario = """
                CREATE TABLE IF NOT EXISTS usuario (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    nome TEXT NOT NULL,
                    login TEXT UNIQUE NOT NULL,
                    senha TEXT NOT NULL,
                    admin BOOLEAN NOT NULL DEFAULT 0,
                    autorizado BOOLEAN NOT NULL DEFAULT 0,
                    data_cadastro TEXT NOT NULL
                );
            """;

            stmt.execute(sqlUsuario);
            System.out.println("Tabelas verificadas/criadas com sucesso.");

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao criar tabelas: " + e.getMessage());
        }
    }
}
