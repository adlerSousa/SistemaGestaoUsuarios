/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ufes.sistema;

import com.ufes.sistema.presenter.CadastroUsuarioPresenter;
import com.ufes.sistema.presenter.LoginPresenter;
import com.ufes.sistema.repository.IUsuarioRepository;
import com.ufes.sistema.repository.INotificacaoRepository;
import com.ufes.sistema.repository.IConfiguracaoRepository;
import com.ufes.sistema.repository.sqlite.UsuarioRepositorySQLite;
import com.ufes.sistema.repository.sqlite.NotificacaoRepositorySQLite;
import com.ufes.sistema.repository.sqlite.ConfiguracaoRepositorySQLite;
import com.ufes.sistema.view.CadastroUsuarioView;
import com.ufes.sistema.view.LoginView;
import javax.swing.JOptionPane;

/**
 *
 * @author Adler
 */
public class Main {
    public static void main(String[] args) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
            // Se der erro, usa o padrão do Java mesmo, sem problemas
        }
        
        try{
            IUsuarioRepository repository = new UsuarioRepositorySQLite();
            INotificacaoRepository nRepository = new NotificacaoRepositorySQLite();
            IConfiguracaoRepository cRepository = new ConfiguracaoRepositorySQLite();
            
            int qtdUsuarios = repository.contarUsuarios();
            
            if(qtdUsuarios == 0){
                CadastroUsuarioView view = new CadastroUsuarioView();
                new CadastroUsuarioPresenter(view, repository);
            }else{
                LoginView view = new LoginView();
                new LoginPresenter(view,repository,nRepository, cRepository);
            }
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Erro fatal ao iniciar sistema" + e.getMessage());
            e.printStackTrace();
        }
        
    }
}
