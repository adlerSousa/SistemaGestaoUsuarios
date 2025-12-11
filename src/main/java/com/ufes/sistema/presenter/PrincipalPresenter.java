/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ufes.sistema.presenter;

import com.ufes.sistema.model.Usuario;
import com.ufes.sistema.repository.IUsuarioRepository;
import com.ufes.sistema.view.PrincipalView;
import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;

/**
 *
 * @author Adler
 */
public class PrincipalPresenter {

    private final PrincipalView view;
    private final Usuario usuarioLogado;
    private final IUsuarioRepository repository;

    public PrincipalPresenter(Usuario usuario, IUsuarioRepository repository) {
        this.usuarioLogado = usuario;
        this.repository = repository;
        this.view = new PrincipalView();

        inicializarSistema();
    }

    private void inicializarSistema() {
        configurarPermissoes();
        atualizarRodape();
        inicializarMenus();

        this.view.setVisible(true);
    }

    private void atualizarRodape() {
        String perfil = usuarioLogado.isAdmin() ? "Administrador" : "Usuário";
        this.view.getLblUsuario().setText("Usuário: " + usuarioLogado.getNome() + " | Perfil: " + perfil);
        this.view.getBtnNotificacoes().setText("0 Notificações pendentes");
    }

    private void configurarPermissoes() {
        if (!usuarioLogado.isAdmin()) {
            this.view.getMnuAdministracao().setVisible(false);
        }
    }

    private void inicializarMenus() {

       // this.view.getMitManterUsuarios().addActionListener((ActionEvent e) -> {
          //  ManterUsuariosPresenter presenter = new ManterUsuariosPresenter(repository);
           // view.getDesktopPane().add(presenter.getView());
          //  presenter.getView().toFront();
      //  });

        this.view.getMitEnviarNotificacao().addActionListener((ActionEvent e) -> {
            JOptionPane.showMessageDialog(view, "Tela de Enviar Notificação");
        });

        this.view.getMitConfigurarLog().addActionListener((ActionEvent e) -> {
            JOptionPane.showMessageDialog(view, "Tela de Configuração de Log");
        });

        this.view.getMitAlterarSenha().addActionListener((ActionEvent e) -> {
            JOptionPane.showMessageDialog(view, "Tela de Alterar Senha");
        });

        this.view.getMitSair().addActionListener((ActionEvent e) -> {
            fecharSistema();
        });

        this.view.getBtnNotificacoes().addActionListener((ActionEvent e) -> {
            JOptionPane.showMessageDialog(view, "Tela de Minhas Notificações");
        });
    }

    private void fecharSistema() {
        System.exit(0);
    }

}
