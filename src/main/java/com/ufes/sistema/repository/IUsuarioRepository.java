package com.ufes.sistema.repository;

import com.ufes.sistema.model.Usuario;

public interface IUsuarioRepository {
    void cadastrarUsuario(Usuario usuario);
    boolean existeUsuarioComLogin(String login);
    int contarUsuarios();
    Usuario buscarPorLogin(String login);
}