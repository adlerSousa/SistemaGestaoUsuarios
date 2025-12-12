/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.ufes.sistema.repository;

import com.ufes.sistema.model.Configuracao;
import java.sql.SQLException;

/**
 *
 * @author Adler
 */
public interface IConfiguracaoRepository {
    Configuracao buscarConfiguracao() throws SQLException;
    void salvar(Configuracao config) throws SQLException;
}
