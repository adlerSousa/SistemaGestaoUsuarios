/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ufes.sistema.presenter;

import com.ufes.sistema.model.Usuario;
import com.ufes.sistema.repository.IUsuarioRepository;
import com.ufes.sistema.view.LoginView;
import com.ufes.sistema.presenter.PrincipalPresenter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author Adler
 */
public class LoginPresenter {

    private LoginView view;
    private IUsuarioRepository repository;

    public LoginPresenter(LoginView view, IUsuarioRepository repository) {
        this.view = view;
        this.repository = repository;

       
        this.view.getBtnEntrar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                autenticar();
            }
        });

       
        this.view.setVisible(true);
    }

    private void autenticar() {
        String login = view.getLogin();
        String senha = view.getSenha();

        if (login.isEmpty() || senha.isEmpty()) {
            view.mostrarMensagem("Preencha login e senha!");
            return;
        }

        Usuario usuario = repository.buscarPorLogin(login);

        if (usuario == null) {
            view.mostrarMensagem("Usuário não encontrado!");
            return;
        }

        
        if (!usuario.getSenha().equals(senha)) {
            view.mostrarMensagem("Senha incorreta!");
            return;
        }

        
        if (!usuario.isAutorizado()) {
            view.mostrarMensagem("Seu cadastro ainda não foi autorizado pelo administrador.");
            return;
        }

       
        view.mostrarMensagem("Login realizado com sucesso! Bem-vindo(a) " + usuario.getNome());

        
        new PrincipalPresenter(usuario,repository); 
        
        view.fechar(); 
    }

}
