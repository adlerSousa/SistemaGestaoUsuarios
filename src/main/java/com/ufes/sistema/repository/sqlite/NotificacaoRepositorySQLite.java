/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ufes.sistema.repository.sqlite;

import com.ufes.sistema.repository.INotificacaoRepository;
import com.ufes.sistema.model.Notificacao;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author Adler
 */
public class NotificacaoRepositorySQLite implements INotificacaoRepository {
   
    public NotificacaoRepositorySQLite() {
        inicializarTabela();
    }
    
    private void inicializarTabela() {
        try (Connection conn = DatabaseConnection.getInstance().getConnection(); Statement stmt = conn.createStatement()) {
            
            String sqlNotificacao = """
            CREATE TABLE IF NOT EXISTS notificacao (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                conteudo TEXT NOT NULL,
                id_remetente INTEGER NOT NULL,
                id_destinatario INTEGER NOT NULL,
                lida BOOLEAN NOT NULL DEFAULT 0,
                data_envio TEXT NOT NULL,
                FOREIGN KEY(id_remetente) REFERENCES usuario(id),
                FOREIGN KEY(id_destinatario) REFERENCES usuario(id)
            );
        """;
        stmt.execute(sqlNotificacao);

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao criar tabela: " + e.getMessage());
        }
    }
    
    
    
    @Override
    public void enviar(Notificacao notificacao) {
        String sql = "INSERT INTO notificacao (conteudo, id_remetente, id_destinatario, lida, data_envio) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, notificacao.getConteudo());
            ps.setInt(2, notificacao.getIdRemetente());
            ps.setInt(3, notificacao.getIdDestinatario());
            ps.setBoolean(4, notificacao.isLida());
            ps.setString(5, notificacao.getDataEnvio().toString());
            
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao enviar notificação: " + e.getMessage());
        }
    }

    @Override
    public List<Notificacao> buscarPorDestinatario(int idDestinatario) {
        List<Notificacao> lista = new ArrayList<>();
        String sql = "SELECT * FROM notificacao WHERE id_destinatario = ? ORDER BY data_envio DESC";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, idDestinatario);
            ResultSet rs = ps.executeQuery();
            
            while(rs.next()) {
                lista.add(new Notificacao(
                    rs.getInt("id"),
                    rs.getString("conteudo"),
                    rs.getInt("id_remetente"),
                    rs.getInt("id_destinatario"),
                    rs.getBoolean("lida"),
                    LocalDateTime.parse(rs.getString("data_envio"))
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    @Override
    public void marcarComoLida(int idNotificacao) {
        String sql = "UPDATE notificacao SET lida = 1 WHERE id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idNotificacao);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao ler notificação: " + e.getMessage());
        }
    }
    
    @Override
    public int contarNaoLidas(int idDestinatario) {
        String sql = "SELECT COUNT(*) FROM notificacao WHERE id_destinatario = ? AND lida = 0";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idDestinatario);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    
}
