/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ufes.sistema.presenter;

import com.github.adlersousa.logger.lib.LoggerLib;
import com.ufes.sistema.model.Notificacao;
import com.ufes.sistema.model.Usuario;
import com.ufes.sistema.repository.INotificacaoRepository;
import com.ufes.sistema.view.MinhasNotificacoesView;
import java.util.List;

/**
 *
 * @author Adler
 */
public class MinhasNotificacoesPresenter {
    private final MinhasNotificacoesView view;
    private final INotificacaoRepository repository;
    private final Usuario usuarioLogado;
    private final PrincipalPresenter principalPresenter; 
    private List<Notificacao> notificacoesAtuais;

    public MinhasNotificacoesPresenter(MinhasNotificacoesView view, INotificacaoRepository repo, Usuario user, PrincipalPresenter principal) {
        this.view = view;
        this.repository = repo;
        this.usuarioLogado = user;
        this.principalPresenter = principal;

        this.view.getBtnMarcarLida().addActionListener(e -> marcarComoLida());
        this.view.getBtnFechar().addActionListener(e -> view.fechar());

        carregarNotificacoes();
        this.view.setVisible(true);
    }

    private void carregarNotificacoes() {
        this.notificacoesAtuais = repository.buscarPorDestinatario(usuarioLogado.getId());
        view.carregarTabela(notificacoesAtuais);
    }

    private void marcarComoLida() {
        int linha = view.getTblNotificacoes().getSelectedRow();
        if (linha < 0) {
            view.mostrarMensagem("Selecione uma notificação!");
            return;
        }

        try {
            Notificacao n = notificacoesAtuais.get(linha);
            repository.marcarComoLida(n.getId());

            LoggerLib.getInstance().escrever(
                "LEITURA_NOTIFICACAO", 
                usuarioLogado.getNome(), 
                usuarioLogado.getNome(), 
                true, 
                null
            );

            view.mostrarMensagem("Notificação marcada como lida!");
            
            
            carregarNotificacoes();
            principalPresenter.atualizarRodape(); 

        } catch (Exception e) {
            view.mostrarMensagem("Erro: " + e.getMessage());
        }
    }
}
