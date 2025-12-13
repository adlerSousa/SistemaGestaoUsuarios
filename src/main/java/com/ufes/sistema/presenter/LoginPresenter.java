package com.ufes.sistema.presenter;

import com.github.adlersousa.logger.lib.LoggerLib;
import com.ufes.sistema.model.Usuario;
import com.ufes.sistema.repository.IUsuarioRepository;
import com.ufes.sistema.repository.INotificacaoRepository;
import com.ufes.sistema.repository.IConfiguracaoRepository;
import com.ufes.sistema.view.LoginView;
import com.ufes.sistema.presenter.PrincipalPresenter;
import com.ufes.sistema.view.CadastroUsuarioView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class LoginPresenter {

    private LoginView view;
    private IUsuarioRepository repository;
    private INotificacaoRepository notificacaoRepository;
    private IConfiguracaoRepository configuracaoRepository;
    
    public LoginPresenter(LoginView view, IUsuarioRepository repository,INotificacaoRepository notificacaoRepository, IConfiguracaoRepository configuracaoRepository) {
        this.view = Objects.requireNonNull(view, "A view é obrigatória");
        this.repository = Objects.requireNonNull(repository, "A Repository é obrigatória");
        this.notificacaoRepository = Objects.requireNonNull(notificacaoRepository, "A Repository é obrigatória");
        this.configuracaoRepository = Objects.requireNonNull(configuracaoRepository, "A Repository é obrigatória");

       
        this.view.getBtnEntrar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                autenticar();
            }
        });
        
        this.view.getBtnCadastrar().addActionListener(new ActionListener(){
           @Override
           public void actionPerformed(ActionEvent e){
               abrirTelaCadastro();
           }
        });

        this.view.setVisible(true);
    }

    private void autenticar() {
        String nomeUsuario = view.getNomeUsuario();
        String senha = view.getSenha();

        if (nomeUsuario.isEmpty() || senha.isEmpty()) {
            view.mostrarMensagem("Preencha login e senha!");
            return;
        }

        try {
            Usuario usuario = repository.buscarPorNomeUsuario(nomeUsuario);

            if (usuario == null) {
                view.mostrarMensagem("Usuário não encontrado!");
                LoggerLib.getInstance().escrever("LOGIN", nomeUsuario, "SISTEMA", false, "Usuário não encontrado");
                return;
            }

            if (!usuario.getSenha().equals(senha)) {
                view.mostrarMensagem("Senha incorreta!");
                LoggerLib.getInstance().escrever("LOGIN", usuario.getNome(), "SISTEMA", false, "Senha incorreta");
                return;
            }

            if (!usuario.isAutorizado()) {
                view.mostrarMensagem("Seu cadastro ainda não foi autorizado pelo administrador.");
                LoggerLib.getInstance().escrever("LOGIN", usuario.getNome(), "SISTEMA", false, "Tentativa de acesso de usuário não autorizado");
                return;
            }
            view.mostrarMensagem("Login realizado com sucesso! Bem-vindo(a) " + usuario.getNome());
            LoggerLib.getInstance().escrever("LOGIN", usuario.getNome(), "SISTEMA", true, null);

            new PrincipalPresenter(usuario, repository, notificacaoRepository,configuracaoRepository);
            view.fechar();

        } catch (Exception e) {

            LoggerLib.getInstance().escrever("LOGIN", nomeUsuario, "SISTEMA", false, "Erro técnico: " + e.getMessage());
            
            view.mostrarMensagem("Erro ao tentar autenticar: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void abrirTelaCadastro() {
        CadastroUsuarioView cadastroView = new CadastroUsuarioView();
        new CadastroUsuarioPresenter(cadastroView, repository);
        view.fechar();
    }
}
