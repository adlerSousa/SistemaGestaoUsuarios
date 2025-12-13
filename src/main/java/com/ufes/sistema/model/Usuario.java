package com.ufes.sistema.model;

import java.time.LocalDate;

public class Usuario {
    private int id;
    private String nome;
    private String nomeUsuario;
    private String senha;
    private Boolean admin;
    private Boolean autorizado;
    private LocalDate dataCadastro;
    private int notificacoesEnviadas;
    private int notificacoesLidas;

    public Usuario(String nome, String nomeUsuario, String senha, Boolean admin, Boolean autorizado, LocalDate dataCadastro) {
        this.nome = nome;
        this.nomeUsuario = nomeUsuario;
        this.senha = senha;
        this.admin = admin;
        this.autorizado = autorizado;
        this.dataCadastro = dataCadastro;
        this.notificacoesEnviadas = 0;
        this.notificacoesLidas = 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Boolean isAdmin() {
        return admin;
    }

    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }

    public Boolean isAutorizado() {
        return autorizado;
    }

    public void setAutorizado(Boolean autorizado) {
        this.autorizado = autorizado;
    }

    public LocalDate getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDate dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public int getNotificacoesEnviadas() {
        return notificacoesEnviadas;
    }

    public void setNotificacoesEnviadas(int notificacoesEnviadas) {
        this.notificacoesEnviadas = notificacoesEnviadas;
    }

    public int getNotificacoesLidas() {
        return notificacoesLidas;
    }

    public void setNotificacoesLidas(int notificacoesLidas) {
        this.notificacoesLidas = notificacoesLidas;
    }
    
    @Override
    public String toString(){
        return "Usuario{" + "id=" + id + ", nome=" + nome + ", admin=" + admin + ", autorizado=" + autorizado + '}';
    }
    
}
