/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ufes.sistema.presenter;

import com.github.adlersousa.logger.lib.LoggerLib;
import com.ufes.sistema.model.Configuracao;
import com.ufes.sistema.model.Usuario;
import com.ufes.sistema.repository.IConfiguracaoRepository;
import com.ufes.sistema.view.ConfiguracaoView;
import java.sql.SQLException;

/**
 *
 * @author Adler
 */
public class ConfiguracaoPresenter {

    private final ConfiguracaoView view;
    private final IConfiguracaoRepository repository;
    private final Usuario administradorLogado; 

    
    private Configuracao configuracaoAtual;

    public ConfiguracaoPresenter(ConfiguracaoView view, IConfiguracaoRepository repository, Usuario admin) {
        this.view = view;
        this.repository = repository;
        this.administradorLogado = admin;

        carregarConfiguracao();

        this.view.getBtnSalvar().addActionListener(e -> salvarConfiguracao());
        this.view.getBtnFechar().addActionListener(e -> view.fechar());

        this.view.setVisible(true);
    }

    private void carregarConfiguracao() {
        try {
            
            this.configuracaoAtual = repository.buscarConfiguracao();

            view.setFormatoSelecionado(configuracaoAtual.getFormatoLog());

        } catch (SQLException e) {
            view.mostrarMensagem("Erro ao carregar configuração: " + e.getMessage());
            this.configuracaoAtual = new Configuracao("CSV");
        }
    }

    private void salvarConfiguracao() {
        String novoFormato = view.getFormatoSelecionado();

        if (novoFormato == null || novoFormato.isEmpty()) {
            view.mostrarMensagem("Selecione um formato de log válido.");
            return;
        }
        
        configuracaoAtual.setFormatoLog(novoFormato);

        try {
          
            repository.salvar(configuracaoAtual);

            LoggerLib.setTipoLog(novoFormato);

            LoggerLib.getInstance().escrever(
                    "ALTERACAO_CONFIGURACAO",
                    "Formato Log", 
                    administradorLogado.getNome(), 
                    true,
                    null
            );

            view.mostrarMensagem("Configuração salva. Novo formato: " + novoFormato);

        } catch (SQLException e) {

            LoggerLib.getInstance().escrever(
                    "ALTERACAO_CONFIGURACAO",
                    "Formato Log",
                    administradorLogado.getNome(),
                    false,
                    "Erro de persistência: " + e.getMessage()
            );
            view.mostrarMensagem("Erro ao salvar configuração: " + e.getMessage());
        }
    }

}
