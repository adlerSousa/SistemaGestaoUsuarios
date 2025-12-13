package com.ufes.sistema.presenter;

import com.github.adlersousa.logger.lib.LoggerLib;
import com.pss.senha.validacao.ValidadorSenha;
import com.ufes.sistema.model.Usuario;
import com.ufes.sistema.repository.IUsuarioRepository;
import com.ufes.sistema.view.AlterarSenhaView;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

public class AlterarSenhaPresenter {

    private final AlterarSenhaView view;
    private final IUsuarioRepository repository;
    private final Usuario usuarioLogado;
    private final ValidadorSenha validador;

    public AlterarSenhaPresenter(AlterarSenhaView view, IUsuarioRepository repository, Usuario usuario) {
        this.view = Objects.requireNonNull(view, "A view é obrigatória.");
        this.repository = Objects.requireNonNull(repository, "O Repositório é obrigatório");
        this.usuarioLogado = Objects.requireNonNull(usuario, "O Usuário Logado é obrigatório");
        this.validador = new ValidadorSenha();
        
        this.view.getBtnSalvar().addActionListener(e -> alterarSenha());
        this.view.getBtnFechar().addActionListener(e -> view.fechar());

        this.view.setVisible(true);
    }

    private void alterarSenha() {
        String senhaAtual = view.getSenhaAtual();
        String novaSenha = view.getNovaSenha();
        String confirmacao = view.getConfirmacaoSenha();

        if (senhaAtual.isEmpty() || novaSenha.isEmpty() || confirmacao.isEmpty()) {
            view.mostrarMensagem("Preencha todos os campos.");
            return;
        }

        if (!novaSenha.equals(confirmacao)) {
            view.mostrarMensagem("A nova senha e a confirmação não coincidem.");
            view.limparECorrigirFoco();
            return;
        }

        if (!usuarioLogado.getSenha().equals(senhaAtual)) {
            view.mostrarMensagem("Senha atual incorreta.");
            view.limparCampos();

            LoggerLib.getInstance().escrever(
                    "ALTERACAO_SENHA",
                    usuarioLogado.getNome(),
                    usuarioLogado.getNome(),
                    false,
                    "Senha atual incorreta."
            );
            return;
        }

        if (novaSenha.equals(senhaAtual)) {
            view.mostrarMensagem("A nova senha deve ser diferente da senha atual.");
            view.limparECorrigirFoco();
            return;
        }

        List<String> errosValidacao = validador.validar(novaSenha);

        if (!errosValidacao.isEmpty()) {
            view.mostrarMensagem("Nova senha inválida: " + String.join("\n- ", errosValidacao));
            view.limparECorrigirFoco();
            LoggerLib.getInstance().escrever(
                    "ALTERACAO_SENHA",
                    usuarioLogado.getNome(),
                    usuarioLogado.getNome(),
                    false,
                    "Falha na validação: " + String.join(", ", errosValidacao)
            );
            return;
        }

        try {
            repository.atualizarSenha(usuarioLogado.getId(), novaSenha);

            usuarioLogado.setSenha(novaSenha);

            view.mostrarMensagem("Senha alterada com sucesso!");
            view.fechar();

            LoggerLib.getInstance().escrever(
                    "ALTERACAO_SENHA",
                    usuarioLogado.getNome(),
                    usuarioLogado.getNome(),
                    true,
                    null
            );

        } catch (SQLException e) {
            view.mostrarMensagem("Erro ao alterar senha no banco: " + e.getMessage());

            LoggerLib.getInstance().escrever(
                    "ALTERACAO_SENHA",
                    usuarioLogado.getNome(),
                    usuarioLogado.getNome(),
                    false,
                    "Erro de persistência: " + e.getMessage()
            );
        }
    }

}
