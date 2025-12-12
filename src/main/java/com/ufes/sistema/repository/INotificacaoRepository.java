/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.ufes.sistema.repository;

import com.ufes.sistema.model.Notificacao;
import java.util.List;
/**
 *
 * @author Adler
 */
public interface INotificacaoRepository {
    
    public void enviar(Notificacao notificacao);
    public List<Notificacao> buscarPorDestinatario(int idDestinatario);
    public void marcarComoLida(int idNotificacao);
    public int contarNaoLidas(int idDestinatario);
    
}
