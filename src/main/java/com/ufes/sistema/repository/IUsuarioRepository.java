package com.ufes.sistema.repository;

import com.ufes.sistema.model.Usuario;
import java.util.List;
import java.sql.SQLException;

public interface IUsuarioRepository {
    void cadastrarUsuario(Usuario usuario);
    void atualizarSenha(int idUsuario, String novaSenha) throws SQLException;
    boolean existeUsuarioComNomeUsuario(String nomeUsuario);
    int contarUsuarios();
    Usuario buscarPorNomeUsuario(String nomeUsuario);
    List<Usuario> buscarTodos();
    void atualizar(Usuario usuario);
    void deletar(int idUsuario);
    boolean isPrimeiroUsuario(int id);
    void restaurarSistemaCompleto() throws Exception;
}