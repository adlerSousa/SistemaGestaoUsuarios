/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ufes.sistema;

import com.ufes.sistema.presenter.CadastroUsuarioPresenter;
import com.ufes.sistema.repository.IUsuarioRepository;
import com.ufes.sistema.repository.sqlite.UsuarioRepositorySQLite;
import com.ufes.sistema.view.CadastroUsuarioView;

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

 
        IUsuarioRepository repository = new UsuarioRepositorySQLite();
        CadastroUsuarioView view = new CadastroUsuarioView();
        new CadastroUsuarioPresenter(view, repository);
        
    }
}
