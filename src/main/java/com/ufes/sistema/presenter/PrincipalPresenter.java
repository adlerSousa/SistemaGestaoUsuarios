package com.ufes.sistema.presenter;

import com.ufes.sistema.model.Usuario;
import com.ufes.sistema.repository.IUsuarioRepository;
import com.ufes.sistema.repository.INotificacaoRepository;
import com.ufes.sistema.repository.IConfiguracaoRepository;
import com.ufes.sistema.view.ConfiguracaoView;
import com.ufes.sistema.view.PrincipalView;
import com.ufes.sistema.view.AlterarSenhaView;
import com.ufes.sistema.view.EnviarNotificacaoView;
import com.ufes.sistema.view.ManterUsuarioView;
import com.ufes.sistema.view.MinhasNotificacoesView;
import com.ufes.sistema.view.RestaurarSistemaView;
import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;

public class PrincipalPresenter {

    private final PrincipalView view;
    private final Usuario usuarioLogado;
    private final IUsuarioRepository repository;
    private final INotificacaoRepository notificacaoRepository;
    private final IConfiguracaoRepository configuracaoRepository;

    public PrincipalPresenter(Usuario usuario, IUsuarioRepository uRepository, INotificacaoRepository nRepository, IConfiguracaoRepository cRepository) {
        this.usuarioLogado = usuario;
        this.repository = uRepository;
        this.notificacaoRepository = nRepository;
        this.configuracaoRepository = cRepository;
        this.view = new PrincipalView();

        inicializarSistema();
    }

    private void inicializarSistema() {
        configurarPermissoes();
        atualizarRodape();
        inicializarMenus();

        this.view.setVisible(true);
    }

    public void atualizarRodape() {
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
        
        this.view.getMitManterUsuarios().addActionListener((ActionEvent e) -> {
            ManterUsuarioView manterView = new ManterUsuarioView();

            view.getDesktopPane().add(manterView);

            new ManterUsuarioPresenter(manterView, repository, usuarioLogado);

            manterView.toFront();
            try { manterView.setSelected(true); } catch (Exception ex) {}
        });

        this.view.getMitEnviarNotificacao().addActionListener((ActionEvent e) -> {
            if (usuarioLogado.isAdmin()) {
                EnviarNotificacaoView notifView = new EnviarNotificacaoView();

                new EnviarNotificacaoPresenter(notifView, notificacaoRepository, repository, usuarioLogado);
                this.view.getDesktopPane().add(notifView);
                notifView.toFront();
            } else {
                JOptionPane.showMessageDialog(view, "Você não tem permissão para enviar notificações.");
            }
        });

        this.view.getMitConfigurarLog().addActionListener((ActionEvent e) -> {

            if (usuarioLogado.isAdmin()) {
                ConfiguracaoView configView = new ConfiguracaoView();

                new ConfiguracaoPresenter(configView, configuracaoRepository, usuarioLogado);

                this.view.getDesktopPane().add(configView);
                configView.toFront();
            } else {
                JOptionPane.showMessageDialog(view, "Você não tem permissão para configurar o sistema.");
            }
        });

        this.view.getMitAlterarSenha().addActionListener((ActionEvent e) -> {
            AlterarSenhaView alterarSenhaView = new AlterarSenhaView();

            new AlterarSenhaPresenter(alterarSenhaView, repository, usuarioLogado);

            this.view.getDesktopPane().add(alterarSenhaView);
            alterarSenhaView.toFront();
        });

        this.view.getMitSair().addActionListener((ActionEvent e) -> {
            fecharSistema();
        });

        this.view.getBtnNotificacoes().addActionListener((ActionEvent e) -> {
            MinhasNotificacoesView minhasNotifView = new MinhasNotificacoesView();
            new MinhasNotificacoesPresenter(minhasNotifView, notificacaoRepository, usuarioLogado, this);
            this.view.getDesktopPane().add(minhasNotifView);
            minhasNotifView.toFront();
        });
        
        this.view.getMitRestaurarSistema().addActionListener(e -> {
            if (!repository.isPrimeiroUsuario(usuarioLogado.getId())) {
                 JOptionPane.showMessageDialog(view, "Apenas o Administrador Inicial pode realizar esta operação.");
                 return;
            }

            RestaurarSistemaView resetView = new RestaurarSistemaView();
            view.getDesktopPane().add(resetView);

            new RestaurarSistemaPresenter(resetView, repository, usuarioLogado);

            resetView.toFront();
            try { resetView.setSelected(true); } catch (Exception ex) {}
            });

            if (!repository.isPrimeiroUsuario(usuarioLogado.getId())) {
                this.view.getMitRestaurarSistema().setVisible(false);
            }

        }

    private void fecharSistema() {
        System.exit(0);
    }

}
