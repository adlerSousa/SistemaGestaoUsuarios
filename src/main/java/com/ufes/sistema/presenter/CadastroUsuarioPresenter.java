package com.ufes.sistema.presenter;

import com.ufes.sistema.model.Usuario;
import com.ufes.sistema.repository.IUsuarioRepository;
import com.ufes.sistema.view.CadastroUsuarioView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;

public class CadastroUsuarioPresenter {

    private CadastroUsuarioView view;
    private IUsuarioRepository repository;

    public CadastroUsuarioPresenter(CadastroUsuarioView view, IUsuarioRepository repository) {
        this.view = view;
        this.repository = repository;
        
        this.view.getBtnSalvar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                salvarUsuario();
            }
        });
        
        this.view.setVisible(true);
    }

    private void salvarUsuario() {
        String nome = view.getNome();
        String login = view.getEmail();
        String senha = view.getSenha();

        if(nome.isEmpty() || login.isEmpty() || senha.isEmpty()) {
            view.mostrarMensagem("Preencha todos os campos!");
            return;
        }

        try {
            int qtdUsuarios = repository.contarUsuarios();
            
            boolean ehPrimeiro = (qtdUsuarios == 0);
            boolean admin = ehPrimeiro;      
            boolean autorizado = ehPrimeiro; 

            Usuario novoUsuario = new Usuario(
                nome, 
                login, 
                senha, 
                admin, 
                autorizado, 
                LocalDate.now()
            );

            repository.cadastrarUsuario(novoUsuario);

            String msg = "Usuário cadastrado com sucesso!";
            if(ehPrimeiro) {
                msg += "\nVocê é o ADMINISTRADOR do sistema.";
            } else {
                msg += "\nAguarde autorização do administrador.";
            }
            
            view.mostrarMensagem(msg);
            view.fechar();

        } catch (Exception e) {
            view.mostrarMensagem("Erro ao salvar: " + e.getMessage());
            e.printStackTrace();
        }
    }
}