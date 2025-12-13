/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ufes.sistema.repository.sqlite;

import com.ufes.sistema.repository.IConfiguracaoRepository;
import com.ufes.sistema.model.Configuracao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Adler
 */
public class ConfiguracaoRepositorySQLite implements IConfiguracaoRepository {
    
    private static final int CONFIG_ID = 1;

    public ConfiguracaoRepositorySQLite() {
        inicializarTabela();
    }
    
    private void inicializarTabela() {
        try (Connection conn = DatabaseConnection.getInstance().getConnection(); 
             Statement stmt = conn.createStatement()) {

            String sqlConfiguracao = """
                CREATE TABLE IF NOT EXISTS configuracao (
                    id INTEGER PRIMARY KEY,
                    formato_log TEXT NOT NULL
                );
            """;
            stmt.execute(sqlConfiguracao);

       
            String sqlCheck = "SELECT COUNT(*) FROM configuracao WHERE id = ?";
            try (PreparedStatement psCheck = conn.prepareStatement(sqlCheck)) {
                psCheck.setInt(1, CONFIG_ID);
                ResultSet rs = psCheck.executeQuery();
                if (rs.next() && rs.getInt(1) == 0) {
                    String sqlInsertDefault = "INSERT INTO configuracao (id, formato_log) VALUES (?, ?)";
                    try (PreparedStatement psInsert = conn.prepareStatement(sqlInsertDefault)) {
                        psInsert.setInt(1, CONFIG_ID);
                        psInsert.setString(2, "CSV"); 
                        psInsert.executeUpdate();
                    }
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao criar tabela de configuracao: " + e.getMessage());
        }
    }
    
    @Override
    public Configuracao buscarConfiguracao() throws SQLException {
        String sql = "SELECT formato_log FROM configuracao WHERE id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, CONFIG_ID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Configuracao(rs.getString("formato_log"));
                }
            }
        }
        return new Configuracao("CSV"); 
    }

    @Override
    public void salvar(Configuracao config) throws SQLException {
        String sql = "INSERT OR REPLACE INTO configuracao (id, formato_log) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, CONFIG_ID);
            ps.setString(2, config.getFormatoLog());
            ps.executeUpdate();
        }
    }
}
    

