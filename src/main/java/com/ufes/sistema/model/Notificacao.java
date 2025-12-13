package com.ufes.sistema.model;

import java.time.LocalDateTime;

public class Notificacao {
    private int id;
    private String conteudo;
    private int idRemetente;
    private int idDestinatario;
    private boolean lida;
    private LocalDateTime dataEnvio;
    
    public Notificacao(String conteudo, int idRemetente, int idDestinatario) {
        this.conteudo = conteudo;
        this.idRemetente = idRemetente;
        this.idDestinatario = idDestinatario;
        this.lida = false;
        this.dataEnvio = LocalDateTime.now();
    }
    
     public Notificacao(int id, String conteudo, int idRemetente, int idDestinatario, boolean lida, LocalDateTime dataEnvio) {
        this.id = id;
        this.conteudo = conteudo;
        this.idRemetente = idRemetente;
        this.idDestinatario = idDestinatario;
        this.lida = lida;
        this.dataEnvio = dataEnvio;
    }
     
    public int getId() { 
        return id;
    }
    
    public String getConteudo() { 
        return conteudo; 
    }
    
    public int getIdRemetente() { 
        return idRemetente;
    }
    
    public int getIdDestinatario() { 
        return idDestinatario; 
    }
    
    public boolean isLida() { 
        return lida; 
    }
    
    public LocalDateTime getDataEnvio() { 
        return dataEnvio; 
    }
    
    public void setLida(boolean lida) { 
        this.lida = lida; 
    }
}
