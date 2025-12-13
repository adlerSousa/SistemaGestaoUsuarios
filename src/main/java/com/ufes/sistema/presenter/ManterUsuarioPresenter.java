package com.ufes.sistema.presenter;

import com.github.adlersousa.logger.lib.LoggerLib;
import com.ufes.sistema.model.Usuario;
import com.ufes.sistema.repository.IUsuarioRepository;
import com.ufes.sistema.view.CadastroUsuarioView;
import com.ufes.sistema.view.ManterUsuarioView;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;

public class ManterUsuarioPresenter {

    private ManterUsuarioView view;
    private IUsuarioRepository repository;
    private Usuario usuarioLogado;
    private List<Usuario> listaUsuarios;

    public ManterUsuarioPresenter(ManterUsuarioView view, IUsuarioRepository repository, Usuario adminLogado) {
        this.view = Objects.requireNonNull(view, "A view é obrigatória");
        this.repository = Objects.requireNonNull(repository, "O Repositório é obrigatório");
        this.usuarioLogado = Objects.requireNonNull(adminLogado, "O Usuário Logado é obrigatório");
        
        if (!usuarioLogado.isAdmin()) {
         throw new SecurityException("Acesso negado: Usuário não é administrador.");
        }

        carregarTabela();

        view.getBtnAutorizar().addActionListener(this::autorizar);
        view.getBtnTornarAdmin().addActionListener(this::tornarAdmin);
        view.getBtnExcluir().addActionListener(this::excluir);
        view.getBtnCriarNovoUsuario().addActionListener(e -> abrirTelaCadastro());
        view.setVisible(true);
    }
    
    private Usuario getUsuarioSelecionado() {
        int linha = view.getLinhaSelecionada();
        if (linha == -1) {
            view.mostrarMensagem("Selecione um usuário na lista.");
            return null;
        }
        return listaUsuarios.get(linha);
    }

    private void carregarTabela() {
        
        try{
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("ID");
        modelo.addColumn("Nome");
        modelo.addColumn("Nome de Usuário");
        modelo.addColumn("Data de Cadastro");
        modelo.addColumn("Total de notificações enviadas");
        modelo.addColumn("Total de notificações lidas");
        modelo.addColumn("Perfil");
        modelo.addColumn("Status");

        this.listaUsuarios = repository.buscarTodos();

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        
        for (Usuario u : listaUsuarios) {
            modelo.addRow(new Object[]{
                u.getId(),
                u.getNome(),
                u.getNomeUsuario(),
                u.getDataCadastro().format(fmt),
                u.getNotificacoesEnviadas(),
                u.getNotificacoesLidas(),
                u.isAdmin() ? "Administrador" : "Padrão",
                u.isAutorizado() ? "Ativo" : "PENDENTE"
            });
        }
        
        view.getTabela().setModel(modelo);
        view.getTabela().getColumnModel().getColumn(0).setPreferredWidth(30);
        view.getTabela().getColumnModel().getColumn(4).setPreferredWidth(300); 
        view.getTabela().getColumnModel().getColumn(5).setPreferredWidth(300);
        } catch(Exception e){
            view.mostrarMensagem("Erro ao carregar lista de usuários: " + e.getMessage());
            LoggerLib.getInstance().escrever("LISTAGEM_USUARIOS", "SISTEMA", usuarioLogado.getNome(), false, e.getMessage());
        }
    }

    private void autorizar(ActionEvent e) {
        Usuario u = getUsuarioSelecionado();
        if (u == null) return;

        if (u.isAutorizado()) {
            view.mostrarMensagem("Usuário já está autorizado!");
            return;
        }
        
        u.setAutorizado(true);
        
        repository.atualizar(u);
        
        LoggerLib.getInstance().escrever(
            "AUTORIZACAO_USUARIO", 
            u.getNome(),
            usuarioLogado.getNome(),
            true, 
            null
        );
        
        view.mostrarMensagem("Usuário autorizado com sucesso.");
        carregarTabela();
    }

    private void tornarAdmin(ActionEvent e) {
        Usuario usuarioAlvo = getUsuarioSelecionado();
        if (usuarioAlvo == null) return;
        
        if (!repository.isPrimeiroUsuario(usuarioLogado.getId())) {
            view.mostrarMensagem("Permissão negada: Apenas o Administrador Inicial pode alterar perfis (promover/rebaixar).");
            return;
        }

        if (repository.isPrimeiroUsuario(usuarioAlvo.getId())) {
            view.mostrarMensagem("Operação bloqueada: O Administrador Inicial não pode ser rebaixado!");
            return;
        }

        boolean novoStatus = !usuarioAlvo.isAdmin();
        usuarioAlvo.setAdmin(novoStatus);
        
        repository.atualizar(usuarioAlvo);
        
        LoggerLib.getInstance().escrever(
            "ALTERACAO_USUARIO", 
            usuarioAlvo.getNome(), 
            usuarioLogado.getNome(), 
            true, 
            "Alteração de perfil para: " + (novoStatus ? "Administrador" : "Padrão")
        );
        
        String msg = novoStatus ? "promovido a Administrador." : "rebaixado a Usuário Padrão.";
        view.mostrarMensagem("Sucesso: Usuário " + usuarioAlvo.getNome() + " foi " + msg);
        
        carregarTabela();
    }
    
    private void excluir(ActionEvent e) {
        Usuario usuarioAlvo = getUsuarioSelecionado();
        if (usuarioAlvo == null) return;
        
        boolean fundador = repository.isPrimeiroUsuario(usuarioLogado.getId());

        if (usuarioAlvo.getId() == usuarioLogado.getId()) {
            view.mostrarMensagem("Você não pode excluir a si mesmo!");
            return;
        }

        if (repository.isPrimeiroUsuario(usuarioAlvo.getId())) {
            view.mostrarMensagem("Segurança: O Administrador Inicial não pode ser excluído do sistema!");
            return;
        }
        
        if (!fundador && usuarioAlvo.isAdmin()) {
            view.mostrarMensagem("Permissão negada: Apenas o Administrador Inicial pode excluir outros administradores.");
            return;
        }
        
        int confirmacao = JOptionPane.showConfirmDialog(
            view, 
            "Tem certeza que deseja excluir o usuário " + usuarioAlvo.getNome() + "?", 
            "Confirmar Exclusão", 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );

        if (confirmacao == JOptionPane.YES_OPTION) {
            try {
                String nomeAlvo = usuarioAlvo.getNome();
                repository.deletar(usuarioAlvo.getId());
                view.mostrarMensagem("Usuário excluído com sucesso!");
                
                LoggerLib.getInstance().escrever(
                    "EXCLUSAO_USUARIO", 
                    nomeAlvo, 
                    usuarioLogado.getNome(), 
                    true, 
                    null
                );
                
                carregarTabela();
                
            } catch (Exception ex) {
                view.mostrarMensagem("Erro ao excluir: " + ex.getMessage());
                LoggerLib.getInstance().escrever("EXCLUSAO_USUARIO", usuarioAlvo.getNome(), usuarioLogado.getNome(), false, ex.getMessage());
            }
        }
    }
    
    public ManterUsuarioView getView() {
        return view;
    }
    
    private void abrirTelaCadastro() {
        CadastroUsuarioView cadastroView = new CadastroUsuarioView();
        
        cadastroView.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        
        CadastroUsuarioPresenter cadastroPresenter = new CadastroUsuarioPresenter(
            cadastroView, 
            repository, 
            true, 
            this.usuarioLogado
        );
        
        cadastroView.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                carregarTabela();
            }
        });
    }
}