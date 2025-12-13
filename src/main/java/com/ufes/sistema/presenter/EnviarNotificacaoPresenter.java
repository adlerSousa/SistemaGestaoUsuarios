package com.ufes.sistema.presenter;

import com.github.adlersousa.logger.lib.LoggerLib;
import com.ufes.sistema.model.Notificacao;
import com.ufes.sistema.model.Usuario;
import com.ufes.sistema.repository.INotificacaoRepository;
import com.ufes.sistema.repository.IUsuarioRepository;
import com.ufes.sistema.view.EnviarNotificacaoView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Objects;

public class EnviarNotificacaoPresenter {
    
    private final EnviarNotificacaoView view;
    private final INotificacaoRepository notificacaoRepository;
    private final IUsuarioRepository usuarioRepository;
    private final Usuario administradorLogado;
    
    public EnviarNotificacaoPresenter(EnviarNotificacaoView view, INotificacaoRepository notificacaoRepository,IUsuarioRepository usuarioRepository, Usuario admin) {
        this.view = Objects.requireNonNull(view, "A view é obrigatória");
        this.notificacaoRepository = Objects.requireNonNull(notificacaoRepository, "A Repository de Notificação é obrigatória");
        this.usuarioRepository = Objects.requireNonNull(usuarioRepository,"A Repository de Usuário é obrigatória");
        this.administradorLogado = Objects.requireNonNull(admin, "O Usuário Logado é obrigatório");
        
        if(!administradorLogado.isAdmin()){
            throw new SecurityException("Acesso negado: Usuário não é administrador.");
        }
        
        carregarUsuariosNaView();

        this.view.getBtnEnviar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enviar();
            }
        });
        
        this.view.getBtnFechar().addActionListener(e -> view.fechar());
        
        this.view.setVisible(true);
    }
    
    private void carregarUsuariosNaView() {
        try {
            List<Usuario> todosUsuarios = usuarioRepository.buscarTodos();
            view.carregarUsuarios(todosUsuarios);
        } catch (Exception e) {
            view.mostrarMensagem("Erro ao carregar lista de usuários: " + e.getMessage());
            LoggerLib.getInstance().escrever(
                "LISTAGEM_USUARIOS", 
                "Lista", 
                administradorLogado.getNome(), 
                false, 
                "Erro de leitura: " + e.getMessage()
            );
        }
    }

    private void enviar() {
        try {
            
            String conteudo = view.getMensagem();
            List<Usuario> destinatarios = view.getUsuariosSelecionados();

            if (conteudo.isEmpty()) {
                view.mostrarMensagem("A mensagem não pode estar vazia.");
                return;
            }

            if (destinatarios.isEmpty()) {
                view.mostrarMensagem("Selecione pelo menos um destinatário.");
                return;
            }

            for (Usuario destinatario : destinatarios) {
                
                if (!destinatario.isAutorizado()) {
                    LoggerLib.getInstance().escrever(
                        "ENVIO_NOTIFICACAO", 
                        destinatario.getNome(), 
                        administradorLogado.getNome(), 
                        false, 
                        "Tentativa de envio para usuário não autorizado"
                    );
                    continue; 
                }

                Notificacao novaNotificacao = new Notificacao(
                    conteudo, 
                    administradorLogado.getId(), 
                    destinatario.getId()
                );

                notificacaoRepository.enviar(novaNotificacao);

                LoggerLib.getInstance().escrever(
                    "ENVIO_NOTIFICACAO", 
                    destinatario.getNome(), 
                    administradorLogado.getNome(),
                    true, 
                    null
                );
            }
            view.mostrarMensagem("Processo de envio finalizado!");
            view.limparCampos();

        } catch (Exception e) {
            LoggerLib.getInstance().escrever(
                "ENVIO_NOTIFICACAO", 
                "Vários Destinatários", 
                administradorLogado.getNome(), 
                false, 
                "Erro de persistência ou sistema: " + e.getMessage()
            );
            view.mostrarMensagem("Erro ao enviar notificações: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
}
