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
import java.util.Objects;
import javax.swing.JOptionPane;

public class PrincipalPresenter {

    private final PrincipalView view;
    private final Usuario usuarioLogado;
    private final IUsuarioRepository repository;
    private final INotificacaoRepository notificacaoRepository;
    private final IConfiguracaoRepository configuracaoRepository;

    public PrincipalPresenter(Usuario usuario, IUsuarioRepository uRepository, INotificacaoRepository nRepository, IConfiguracaoRepository cRepository) {
        this.usuarioLogado = Objects.requireNonNull(usuario, "O Usuário logado é obrigatório");
        this.repository = Objects.requireNonNull(uRepository, "O Repositório de Usuários é obrigatório");
        this.notificacaoRepository = Objects.requireNonNull(nRepository, "O Repositório de Notificações é obrigatório");
        this.configuracaoRepository = Objects.requireNonNull(cRepository, "O Repositório de Configurações é obrigatório");

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
        
        try {
            int naoLidas = notificacaoRepository.contarNaoLidas(usuarioLogado.getId());
            this.view.getBtnNotificacoes().setText(naoLidas + " Notificações pendentes");
        } catch (Exception e) {
            this.view.getBtnNotificacoes().setText("Erro ao carregar");
        }
    }

    private void configurarPermissoes() {
        if (!usuarioLogado.isAdmin()) {
            this.view.getMnuAdministracao().setVisible(false);
        }
    }

    private void inicializarMenus() {
        
        this.view.getMitManterUsuarios().addActionListener((ActionEvent e) -> {
            try {
                ManterUsuarioView manterView = new ManterUsuarioView();
                view.getDesktopPane().add(manterView);
                
                new ManterUsuarioPresenter(manterView, repository, usuarioLogado); 

                manterView.toFront();
                manterView.setSelected(true);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(view, "Erro ao abrir Manter Usuários: " + ex.getMessage());
                ex.printStackTrace(); // Imprime no log para detalhe técnico
            }
        });

        this.view.getMitEnviarNotificacao().addActionListener((ActionEvent e) -> {
            if (usuarioLogado.isAdmin()) {
                try {
                    EnviarNotificacaoView notifView = new EnviarNotificacaoView();
                    view.getDesktopPane().add(notifView);
                    
                    new EnviarNotificacaoPresenter(notifView, notificacaoRepository, repository, usuarioLogado);
                    
                    notifView.toFront();
                    notifView.setSelected(true);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(view, "Erro ao abrir Notificações: " + ex.getMessage());
                    ex.printStackTrace();
                }
            } else {
                JOptionPane.showMessageDialog(view, "Você não tem permissão.");
            }
        });

        this.view.getMitConfigurarLog().addActionListener((ActionEvent e) -> {
            if (usuarioLogado.isAdmin()) {
                try {
                    ConfiguracaoView configView = new ConfiguracaoView();
                    view.getDesktopPane().add(configView);
                    
                    new ConfiguracaoPresenter(configView, configuracaoRepository, usuarioLogado);
                    
                    configView.toFront();
                    configView.setSelected(true);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(view, "Erro ao abrir Configuração: " + ex.getMessage());
                    ex.printStackTrace();
                }
            } else {
                JOptionPane.showMessageDialog(view, "Sem permissão.");
            }
        });

        this.view.getMitAlterarSenha().addActionListener((ActionEvent e) -> {
            try {
                AlterarSenhaView alterarSenhaView = new AlterarSenhaView();
                view.getDesktopPane().add(alterarSenhaView);
                
                new AlterarSenhaPresenter(alterarSenhaView, repository, usuarioLogado);
                
                alterarSenhaView.toFront();
                alterarSenhaView.setSelected(true);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(view, "Erro ao abrir Alterar Senha: " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        this.view.getMitSair().addActionListener((ActionEvent e) -> {
            fecharSistema();
        });

        this.view.getBtnNotificacoes().addActionListener((ActionEvent e) -> {
            try {
                MinhasNotificacoesView minhasNotifView = new MinhasNotificacoesView();
                view.getDesktopPane().add(minhasNotifView);
                
                new MinhasNotificacoesPresenter(minhasNotifView, notificacaoRepository, usuarioLogado, this);
                
                minhasNotifView.toFront();
                minhasNotifView.setSelected(true);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(view, "Erro ao abrir Minhas Notificações: " + ex.getMessage());
                ex.printStackTrace();
            }
        });
        
        this.view.getMitRestaurarSistema().addActionListener(e -> {
            if (!repository.isPrimeiroUsuario(usuarioLogado.getId())) {
                 JOptionPane.showMessageDialog(view, "Apenas o Administrador Inicial pode realizar esta operação.");
                 return;
            }
            try {
                RestaurarSistemaView resetView = new RestaurarSistemaView();
                view.getDesktopPane().add(resetView);

                new RestaurarSistemaPresenter(resetView, repository, usuarioLogado);

                resetView.toFront();
                resetView.setSelected(true);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(view, "Erro ao abrir Restauração: " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        if (!repository.isPrimeiroUsuario(usuarioLogado.getId())) {
            this.view.getMitRestaurarSistema().setVisible(false);
        }
    }

    private void fecharSistema() {
        System.exit(0);
    }

}
