package com.ufes.sistema.repository;

import com.ufes.sistema.model.Notificacao;
import java.util.List;

public interface INotificacaoRepository {
    
    public void enviar(Notificacao notificacao);
    public List<Notificacao> buscarPorDestinatario(int idDestinatario);
    public void marcarComoLida(int idNotificacao);
    public int contarNaoLidas(int idDestinatario);
    
}
