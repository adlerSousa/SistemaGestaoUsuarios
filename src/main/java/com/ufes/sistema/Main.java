/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ufes.sistema;

import com.ufes.sistema.presenter.CadastroUsuarioPresenter;
import com.ufes.sistema.repository.IUsuarioRepository;
import com.ufes.sistema.repository.sqlite.UsuarioRepositorySQLite;
import com.ufes.sistema.view.CadastroUsuarioView;
import javax.swing.JOptionPane;

/**
 *
 * @author Adler
 */
public class Main {
    public static void main(String[] args) {
        try {
            IUsuarioRepository repository = new UsuarioRepositorySQLite();
            CadastroUsuarioView view = new CadastroUsuarioView();
            new CadastroUsuarioPresenter(view, repository);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro fatal ao iniciar o sistema:\n" + e.getMessage());
        }
    }
}
