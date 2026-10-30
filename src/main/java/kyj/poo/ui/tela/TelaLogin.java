package kyj.poo.ui.tela;

import javax.swing.*;
import kyj.poo.regras.Comprador;
import kyj.poo.ui.*;

public class TelaLogin extends Tela {

    private final JTextField email = Tema.campo(new JTextField());
    private final JPasswordField senha = Tema.campo(new JPasswordField());

    public TelaLogin(Aplicacao app) {
        super(app);
        Botao entrar = Botao.primario("Entrar");
        Botao criar = Botao.texto("Não tem conta? Cadastre-se");
        entrar.addActionListener(e -> entrar());
        criar.addActionListener(e -> app.ir("cadastro"));
        email.addActionListener(e -> entrar());
        senha.addActionListener(e -> entrar());

        add(layoutAuth("Bom te ver de novo",
                "Entre para acompanhar seu carrinho e finalizar suas compras com segurança.",
                formulario("Entrar", "Use seu e-mail e senha cadastrados",
                        new String[] { "E-mail", "Senha" },
                        new JComponent[] { email, senha },
                        entrar, criar)));
    }

    private void entrar() {
        String em = email.getText().trim();
        String s = new String(senha.getPassword());

        if (em.equals(Sessao.ADMIN_EMAIL) && s.equals(Sessao.ADMIN_SENHA)) {
            Sessao.admin = true;
            Sessao.logado = null;
            limpar();
            app.ir("admin");
            return;
        }

        for (Comprador c : Sessao.compradores) {
            if (c.getEmail().equalsIgnoreCase(em) && c.getSenha().equals(s)) {
                Sessao.logado = c;
                Sessao.admin = false;
                limpar();
                app.ir("loja");
                return;
            }
        }
        erro("E-mail ou senha incorretos.");
    }

    private void limpar() {
        email.setText("");
        senha.setText("");
    }
}