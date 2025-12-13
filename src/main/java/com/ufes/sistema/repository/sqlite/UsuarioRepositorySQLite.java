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
                    nome_usuario TEXT UNIQUE NOT NULL, 
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
        String sql = "INSERT INTO usuario (nome, nome_usuario, senha, admin, autorizado, data_cadastro) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getInstance().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, usuario.getNome());
            ps.setString(2, usuario.getNomeUsuario());
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
    public boolean existeUsuarioComNomeUsuario(String nomeUsuario) {
        String sql = "SELECT 1 FROM usuario WHERE nome_usuario = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nomeUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Usuario buscarPorNomeUsuario(String nomeUsuario) {
        String sql = "SELECT * FROM usuario WHERE nome_usuario = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nomeUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Usuario u = new Usuario(
                            rs.getString("nome"),
                            rs.getString("nome_usuario"),
                            rs.getString("senha"),
                            rs.getBoolean("admin"),
                            rs.getBoolean("autorizado"),
                            java.time.LocalDate.parse(rs.getString("data_cadastro"))
                    );
                    u.setId(rs.getInt("id"));
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
        List<Usuario> lista = new ArrayList<>();
        
        String sql = """
            SELECT u.*, 
            (SELECT COUNT(*) FROM notificacao n WHERE n.id_destinatario = u.id) as total_msg,
            (SELECT COUNT(*) FROM notificacao n WHERE n.id_destinatario = u.id AND n.lida = 1) as lidas_msg
            FROM usuario u
        """;
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Usuario u = new Usuario(
                    rs.getString("nome"),
                    rs.getString("nome_usuario"),
                    rs.getString("senha"),
                    rs.getBoolean("admin"),
                    rs.getBoolean("autorizado"),
                    java.time.LocalDate.parse(rs.getString("data_cadastro"))
                );
                u.setId(rs.getInt("id"));

                u.setNotificacoesEnviadas(rs.getInt("total_msg"));
                u.setNotificacoesLidas(rs.getInt("lidas_msg"));
                
                lista.add(u);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    @Override
    public void atualizarSenha(int idUsuario, String novaSenha) throws SQLException {
        String sql = "UPDATE usuario SET senha = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, novaSenha);
            ps.setInt(2, idUsuario);
            ps.executeUpdate();
        }
    }
    
    @Override
    public void atualizar(Usuario usuario) {
        String sql = "UPDATE usuario SET nome=?, nome_usuario=?, senha=?, admin=?, autorizado=? WHERE id=?";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, usuario.getNome());
            ps.setString(2, usuario.getNomeUsuario());
            ps.setString(3, usuario.getSenha());
            ps.setBoolean(4, usuario.isAdmin());
            ps.setBoolean(5, usuario.isAutorizado());
            ps.setInt(6, usuario.getId());
            
            ps.executeUpdate();
            
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar usuário: " + e.getMessage());
        }
    }

    @Override
    public void deletar(int id) {
        String sql = "DELETE FROM usuario WHERE id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            ps.executeUpdate();
            
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar usuário: " + e.getMessage());
        }
    }
    
    @Override
    public boolean isPrimeiroUsuario(int id){
        String sql = "SELECT MIN(id) as id_inicial FROM usuario";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            if (rs.next()) {
                int idPrimeiro = rs.getInt("id_inicial");
                return id == idPrimeiro; 
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    @Override
    public void restaurarSistemaCompleto() throws Exception {
        String sqlNotificacoes = "DELETE FROM notificacao";
        String sqlUsuarios = "DELETE FROM usuario";
        String sqlResetSeq = "DELETE FROM sqlite_sequence WHERE name='usuario' OR name='notificacao'";
        
        Connection conn = DatabaseConnection.getInstance().getConnection();
        boolean autoCommitOriginal = conn.getAutoCommit();
        
        try (Statement stmt = conn.createStatement()) {
            conn.setAutoCommit(false);
            
            stmt.executeUpdate(sqlNotificacoes);
            stmt.executeUpdate(sqlUsuarios);
            stmt.executeUpdate(sqlResetSeq);
            
            conn.commit();
            
        } catch (Exception e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(autoCommitOriginal);
        }
    }

}
