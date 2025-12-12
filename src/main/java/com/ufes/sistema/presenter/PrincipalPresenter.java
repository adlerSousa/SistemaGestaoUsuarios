/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ufes.sistema.presenter;

import com.ufes.sistema.model.Usuario;
import com.ufes.sistema.repository.IUsuarioRepository;
import com.ufes.sistema.repository.INotificacaoRepository;
import com.ufes.sistema.view.PrincipalView;
import com.ufes.sistema.view.EnviarNotificacaoView;
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
    private final INotificacaoRepository notificacaoRepository;

    public PrincipalPresenter(Usuario usuario, IUsuarioRepository uRepository, INotificacaoRepository nRepository) {
        this.usuarioLogado = usuario;
        this.repository = uRepository;
        this.notificacaoRepository = nRepository;
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
        int naoLidas = notificacaoRepository.contarNaoLidas(usuarioLogado.getId());
        this.view.getBtnNotificacoes().setText(naoLidas + " Notificações pendentes");
    }

    private void configurarPermissoes() {
        if (!usuarioLogado.isAdmin()) {
            this.view.getMnuAdministracao().setVisible(false);
        }
    }

    private void inicializarMenus() {
        
        this.view.getMitEnviarNotificacao().addActionListener((ActionEvent e) -> {
            if (usuarioLogado.isAdmin()) {
                EnviarNotificacaoView notifView = new EnviarNotificacaoView();
     
                new EnviarNotificacaoPresenter(notifView, notificacaoRepository,repository, usuarioLogado);
                this.view.getDesktopPane().add(notifView);
                notifView.toFront();
            } else {
                JOptionPane.showMessageDialog(view, "Você não tem permissão para enviar notificações.");
            }
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
