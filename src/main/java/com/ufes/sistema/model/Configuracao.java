package com.ufes.sistema.model;

public class Configuracao {
    
    private String formatoLog; 

    public Configuracao(String formatoLog) {
        this.formatoLog = formatoLog;
    }

    public String getFormatoLog() {
        return formatoLog;
    }

    public void setFormatoLog(String formatoLog) {
        this.formatoLog = formatoLog;
    }
}
