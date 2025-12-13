package com.ufes.sistema.presenter;

import com.github.adlersousa.logger.lib.LoggerLib;
import com.pss.senha.validacao.ValidadorSenha;
import com.ufes.sistema.model.Usuario;
import com.ufes.sistema.repository.IConfiguracaoRepository;
import com.ufes.sistema.repository.INotificacaoRepository;
import com.ufes.sistema.repository.IUsuarioRepository;
import com.ufes.sistema.repository.sqlite.ConfiguracaoRepositorySQLite;
import com.ufes.sistema.repository.sqlite.NotificacaoRepositorySQLite;
import com.ufes.sistema.view.CadastroUsuarioView;
import com.ufes.sistema.view.LoginView;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class CadastroUsuarioPresenter {

    private CadastroUsuarioView view;
    private IUsuarioRepository repository;
    private boolean modoAdmin;
    private Usuario usuarioLogado;

    public CadastroUsuarioPresenter(CadastroUsuarioView view, IUsuarioRepository repository) {
        this(view, repository, false, null);
    }

    public CadastroUsuarioPresenter(CadastroUsuarioView view, IUsuarioRepository repository, boolean isModoAdmin, Usuario usuarioLogado) {
        this.view = Objects.requireNonNull(view, "A view é obrigatória");
        this.repository = Objects.requireNonNull(repository, "O Repositório é obrigatório");
        this.modoAdmin = isModoAdmin;
        
        if (this.modoAdmin) {
            this.usuarioLogado = Objects.requireNonNull(usuarioLogado, "No modo administrativo, o usuário logado é obrigatório!");
            this.view.setTitle("Novo Usuário (Modo Administrativo)");
        } else {
            this.usuarioLogado = null;
        }
        
        this.view.getBtnSalvar().addActionListener(e -> salvarUsuario());
        
        this.view.setVisible(true);
    }

    private void salvarUsuario() {
        String nome = view.getNome();
        String login = view.getNomeUsuario();
        String senha = view.getSenha();
        String confirmarSenha = view.getConfirmarSenha();

        if(nome.isEmpty() || login.isEmpty() || senha.isEmpty()) {
            view.mostrarMensagem("Preencha todos os campos!");
            return;
        }

        if (!senha.equals(confirmarSenha)) {
            String msg = "As senhas não conferem!";
            view.mostrarMensagem(msg);
            registrarLogFalha(nome,msg);
            return;
        }

         ValidadorSenha validador = new ValidadorSenha();
         List<String> errosSenha = validador.validar(senha);
         
         if(!errosSenha.isEmpty()) {
            String msgErro = "Senha inválida:\n" + String.join("\n", errosSenha);
            view.mostrarMensagem(msgErro);
            registrarLogFalha(nome, "Senha inválida de acordo com regras de segurança"); 
            return;
        }
        
        try {
            if (repository.existeUsuarioComNomeUsuario(login)) {
                String msg = "Este Nome de Usuário já está cadastrado!";
                view.mostrarMensagem(msg);
                registrarLogFalha(nome,msg);
                return;
            }

            int qtdUsuarios = repository.contarUsuarios();
            
            boolean isPrimeiro = (qtdUsuarios == 0);   
            
            boolean autorizado = isPrimeiro || this.modoAdmin; 
            
            boolean admin = isPrimeiro;    

            Usuario novoUsuario = new Usuario(
                nome, 
                login, 
                senha, 
                admin, 
                autorizado, 
                LocalDate.now()
            );

            repository.cadastrarUsuario(novoUsuario);
            
            Usuario usuarioSalvo = repository.buscarPorNomeUsuario(login); 
            
            String autor = (modoAdmin && usuarioLogado != null) ? usuarioLogado.getNome() : "SISTEMA";
            LoggerLib.getInstance().escrever("INCLUSAO_USUARIO", novoUsuario.getNome(), autor, true, null);


            if (modoAdmin) {
                view.mostrarMensagem("Usuário cadastrado com sucesso e JÁ AUTORIZADO!");
                view.fechar();
                
            } else {
                
                if (isPrimeiro) {
                    view.mostrarMensagem("Cadastro realizado! Você é o Administrador Inicial.\nO sistema será iniciado automaticamente.");
                    
                    INotificacaoRepository nRepo = new NotificacaoRepositorySQLite();
                    IConfiguracaoRepository cRepo = new ConfiguracaoRepositorySQLite();
                    
                    new PrincipalPresenter(usuarioSalvo, repository, nRepo, cRepo);
                    
                } else {
                    view.mostrarMensagem("Usuário cadastrado com sucesso!\nAguarde autorização do administrador.");
                    
                    INotificacaoRepository nRepo = new NotificacaoRepositorySQLite();
                    IConfiguracaoRepository cRepo = new ConfiguracaoRepositorySQLite();
                    
                    LoginView loginView = new LoginView();
                    new LoginPresenter(loginView, repository, nRepo, cRepo);
                }
                
                view.fechar();
            }

        } catch (Exception e) {
            registrarLogFalha(nome, e.getMessage());
            view.mostrarMensagem("Erro ao salvar: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void registrarLogFalha(String nome,String msg){
        LoggerLib.getInstance().escrever("INCLUSAO_USUARIO", nome, "SISTEMA", false, msg);
    }
}