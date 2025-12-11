package com.ufes.sistema.presenter;


import com.github.adlersousa.logger.lib.LoggerLib;
import com.pss.senha.validacao.ValidadorSenha;
import com.ufes.sistema.model.Usuario;
import com.ufes.sistema.repository.IUsuarioRepository;
import com.ufes.sistema.view.CadastroUsuarioView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.List;

public class CadastroUsuarioPresenter {

    private CadastroUsuarioView view;
    private IUsuarioRepository repository;

    public CadastroUsuarioPresenter(CadastroUsuarioView view, IUsuarioRepository repository) {
        this.view = view;
        this.repository = repository;
        
        this.view.getBtnSalvar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                salvarUsuario();
            }
        });
        
        this.view.setVisible(true);
    }

    private void salvarUsuario() {
        String nome = view.getNome();
        String login = view.getEmail();
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
            if (repository.existeUsuarioComLogin(login)) {
                String msg = "Este E-mail/Login já está cadastrado!";
                view.mostrarMensagem(msg);
                registrarLogFalha(nome,msg);
                return;
            }

            int qtdUsuarios = repository.contarUsuarios();
            
            boolean ehPrimeiro = (qtdUsuarios == 0);
            boolean admin = ehPrimeiro;      
            boolean autorizado = ehPrimeiro; 

            Usuario novoUsuario = new Usuario(
                nome, 
                login, 
                senha, 
                admin, 
                autorizado, 
                LocalDate.now()
            );

            repository.cadastrarUsuario(novoUsuario);
            
            LoggerLib.getInstance().escrever("INCLUSAO_USUARIO", novoUsuario.getNome(), "SISTEMA", true, null);

            String msg = "Usuário cadastrado com sucesso!";
            if(ehPrimeiro) {
                msg += "\nVocê é o ADMINISTRADOR do sistema.";
            } else {
                msg += "\nAguarde autorização do administrador.";
            }
            
            view.mostrarMensagem(msg);
            view.limparCampos(); 
            view.fechar();

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