package com.ufes.sistema.presenter;

import com.github.adlersousa.logger.lib.LoggerLib;
import com.ufes.sistema.model.Usuario;
import com.ufes.sistema.repository.IUsuarioRepository;
import com.ufes.sistema.view.CadastroUsuarioView;
import com.ufes.sistema.view.RestaurarSistemaView;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.Cursor;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class RestaurarSistemaPresenter {
    
    private RestaurarSistemaView view;
    private IUsuarioRepository repository;
    private Usuario usuarioLogado;

    public RestaurarSistemaPresenter(RestaurarSistemaView view, IUsuarioRepository repository, Usuario admin) {
        this.view = view;
        this.repository = repository;
        this.usuarioLogado = admin;
        
        this.view.getBtnRestaurarSistema().addActionListener(this::confirmarRestauracao);
        
        this.view.setVisible(true);
    }
    
    private void confirmarRestauracao(ActionEvent e) {
        String s1 = view.getSenhaAtual();
        String s2 = view.getConfirmarSenhaAtual();
        
        if(s1.isEmpty() || s2.isEmpty()) {
            view.mostrarMensagem("Informe sua senha nos dois campos para confirmar.");
            return;
        }
        
        if(!s1.equals(s2)) {
            view.mostrarMensagem("As senhas informadas não conferem.");
            return;
        }
        
        if(!usuarioLogado.getSenha().equals(s1)) {
            view.mostrarMensagem("Senha incorreta! Operação cancelada.");
            
            LoggerLib.getInstance().escrever("RESTAURACAO_SISTEMA", "SISTEMA", usuarioLogado.getNome(), false, "Senha incorreta na tentativa de restauração");
            return;
        }
        
        int opt = JOptionPane.showConfirmDialog(
            view, 
            "ATENÇÃO: Isso apagará TODOS os usuários, notificações e configurações.\n" +
            "O sistema voltará ao estado de 'primeira instalação'.\n\n" +
            "Tem certeza absoluta?", 
            "CONFIRMAÇÃO DE PERIGO", 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );
        
        if (opt == JOptionPane.YES_OPTION) {
            executarRestauracao();
        }
    }
    
    private void executarRestauracao() {
        try {
            view.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            
            repository.restaurarSistemaCompleto();
            
            LoggerLib.getInstance().escrever(
                "RESTAURACAO_SISTEMA", 
                "TODOS OS DADOS", 
                usuarioLogado.getNome(), 
                true, 
                "Sistema resetado para o padrão de fábrica"
            );
            
            view.setCursor(Cursor.getDefaultCursor());
            
            JOptionPane.showMessageDialog(view, 
                "Restauração concluída com sucesso!\n" +
                "O sistema será reinicializado para o cadastro do novo administrador."
            );
            
            CadastroUsuarioView cadastroView = new CadastroUsuarioView();
            new CadastroUsuarioPresenter(cadastroView, repository); 
            
            Window janelaPrincipal = SwingUtilities.getWindowAncestor(view);
            
            if (janelaPrincipal != null) {
                janelaPrincipal.dispose();
            }
            
        } catch (Exception ex) {
            view.setCursor(Cursor.getDefaultCursor());
            view.mostrarMensagem("Falha fatal ao restaurar: " + ex.getMessage());
            
            LoggerLib.getInstance().escrever(
                "RESTAURACAO_SISTEMA", 
                "SISTEMA", 
                usuarioLogado.getNome(), 
                false, 
                ex.getMessage()
            );
            ex.printStackTrace();
        }
    }
}