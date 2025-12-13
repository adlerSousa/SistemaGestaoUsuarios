package com.ufes.sistema.repository;

import com.ufes.sistema.model.Usuario;
import java.util.List;
import java.sql.SQLException;
public interface IUsuarioRepository {
    void cadastrarUsuario(Usuario usuario);
    void atualizarSenha(int idUsuario, String novaSenha) throws SQLException;
    boolean existeUsuarioComLogin(String login);
    int contarUsuarios();
    Usuario buscarPorLogin(String login);
    List<Usuario> buscarTodos();
    void atualizar(Usuario usuario);
    void deletar(int idUsuario);
    boolean isPrimeiroUsuario(int id);
}