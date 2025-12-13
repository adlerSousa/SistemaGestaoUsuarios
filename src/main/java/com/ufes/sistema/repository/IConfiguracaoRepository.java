package com.ufes.sistema.repository;

import com.ufes.sistema.model.Configuracao;
import java.sql.SQLException;

public interface IConfiguracaoRepository {
    Configuracao buscarConfiguracao() throws SQLException;
    void salvar(Configuracao config) throws SQLException;
}
