/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ufes.sistema.model;

/**
 *
 * @author Adler
 */
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
