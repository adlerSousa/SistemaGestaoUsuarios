package com.ufes.sistema.repository;

import com.ufes.sistema.model.Usuario;
import java.util.List;
public interface IUsuarioRepository {
    void cadastrarUsuario(Usuario usuario);
    boolean existeUsuarioComLogin(String login);
    int contarUsuarios();
    Usuario buscarPorLogin(String login);
    List<Usuario> buscarTodos();
}