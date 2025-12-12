package com.ufes.sistema.repository.sqlite;

import com.ufes.sistema.model.Usuario;
import com.ufes.sistema.repository.IUsuarioRepository;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UsuarioRepositorySQLite implements IUsuarioRepository {

    public UsuarioRepositorySQLite() {
        inicializarTabela();
    }

    private void inicializarTabela() {
        try (Connection conn = DatabaseConnection.getInstance().getConnection(); Statement stmt = conn.createStatement()) {

            String sql = """
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

            stmt.execute(sql);

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao criar tabela: " + e.getMessage());
        }
    }

    @Override
    public void cadastrarUsuario(Usuario usuario) {
        String sql = "INSERT INTO usuario (nome, login, senha, admin, autorizado, data_cadastro) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getInstance().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, usuario.getNome());
            ps.setString(2, usuario.getLogin());
            ps.setString(3, usuario.getSenha());
            ps.setBoolean(4, usuario.isAdmin());
            ps.setBoolean(5, usuario.isAutorizado());
            ps.setString(6, usuario.getDataCadastro().toString());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar usuário: " + e.getMessage());
        }
    }

    @Override
    public int contarUsuarios() {
        String sql = "SELECT COUNT(*) FROM usuario";
        try (Connection conn = DatabaseConnection.getInstance().getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public boolean existeUsuarioComLogin(String login) {
        String sql = "SELECT 1 FROM usuario WHERE login = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, login);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Usuario buscarPorLogin(String login) {
        String sql = "SELECT * FROM usuario WHERE login = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, login);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // Reconstrói o objeto Usuario vindo do banco
                    Usuario u = new Usuario(
                            rs.getString("nome"),
                            rs.getString("login"),
                            rs.getString("senha"),
                            rs.getBoolean("admin"),
                            rs.getBoolean("autorizado"),
                            java.time.LocalDate.parse(rs.getString("data_cadastro"))
                    );
                    u.setId(rs.getInt("id")); // Importante pegar o ID
                    return u;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    @Override
public List<Usuario> buscarTodos() {
    List<Usuario> usuarios = new ArrayList<>();
    String sql = "SELECT * FROM usuario ORDER BY nome ASC";
    
    try (Connection conn = DatabaseConnection.getInstance().getConnection();
         PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {
        
        while (rs.next()) {
            Usuario u = new Usuario(
                rs.getString("nome"),
                rs.getString("login"),
                rs.getString("senha"),
                rs.getBoolean("admin"),
                rs.getBoolean("autorizado"),
                java.time.LocalDate.parse(rs.getString("data_cadastro"))
            );
            u.setId(rs.getInt("id")); 
            usuarios.add(u);
        }
        
    } catch (SQLException e) {
        e.printStackTrace();
        
    }
    return usuarios;
}

}
