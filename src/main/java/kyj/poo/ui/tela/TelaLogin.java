package kyj.poo.ui.tela;

import javax.swing.JComponent;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import kyj.poo.regras.Comprador;
import kyj.poo.ui.Aplicacao;
import kyj.poo.ui.Botao;
import kyj.poo.ui.Sessao;
import kyj.poo.ui.Tela;
import kyj.poo.ui.Tema;

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

        add(
            layoutAuth(
                "Bom te ver de novo",
                "Entre para acompanhar seu carrinho e finalizar suas compras com segurança.",
                formulario(
                    "Entrar",
                    "Use seu e-mail e senha cadastrados",
                    new String[] { "E-mail", "Senha" },
                    new JComponent[] { email, senha },
                    entrar,
                    criar
                )
            )
        );
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

        Comprador c = Sessao.compradorService.login(em, s);
        if (c != null) {
            Sessao.logado = c;
            Sessao.admin = false;
            limpar();
            app.ir("loja");
            return;
        }
        erro("E-mail ou senha incorretos.");
    }

    private void limpar() {
        email.setText("");
        senha.setText("");
    }
}
