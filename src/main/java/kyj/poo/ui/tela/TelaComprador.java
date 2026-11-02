package kyj.poo.ui.tela;

import javax.swing.JComponent;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import kyj.poo.regras.excecao.PersistenciaException;
import kyj.poo.ui.Aplicacao;
import kyj.poo.ui.Botao;
import kyj.poo.ui.Sessao;
import kyj.poo.ui.Tela;
import kyj.poo.ui.Tema;

public class TelaComprador extends Tela {

    private final JTextField nome = Tema.campo(new JTextField());
    private final JTextField cpf = Tema.campo(new JTextField());
    private final JTextField email = Tema.campo(new JTextField());
    private final JPasswordField senha = Tema.campo(new JPasswordField());

    public TelaComprador(Aplicacao app) {
        super(app);
        Botao cadastrar = Botao.primario("Criar minha conta");
        Botao jaTenho = Botao.texto("Já tenho conta");
        cadastrar.addActionListener(e -> cadastrar());
        jaTenho.addActionListener(e -> app.ir("login"));
        senha.addActionListener(e -> cadastrar());

        add(
            layoutAuth(
                "Crie sua conta em minutos",
                "Cadastre-se para montar seu carrinho, pagar com Pix ou cartão e acompanhar seus pedidos.",
                formulario(
                    "Cadastro",
                    "Preencha seus dados abaixo",
                    new String[] {
                        "Nome completo",
                        "CPF (só números)",
                        "E-mail",
                        "Senha",
                    },
                    new JComponent[] { nome, cpf, email, senha },
                    cadastrar,
                    jaTenho
                )
            )
        );
    }

    private void cadastrar() {
        String n = nome.getText().trim();
        String c = cpf.getText().trim();
        String em = email.getText().trim();
        String s = new String(senha.getPassword());

        if (n.isEmpty()) {
            erro("Informe o nome.");
            return;
        }
        if (!c.matches("\\d{11}")) {
            erro("CPF deve ter 11 dígitos.");
            return;
        }
        if (!em.contains("@")) {
            erro("E-mail inválido.");
            return;
        }
        if (s.length() < 4) {
            erro("A senha precisa ter ao menos 4 caracteres.");
            return;
        }

        try {
            Sessao.compradorService.cadastrar(n, c, em, s);
        } catch (IllegalArgumentException ex) {
            erro(ex.getMessage());
            return;
        } catch (PersistenciaException ex) {
            erro("Não foi possível salvar o cadastro.");
            return;
        }

        nome.setText("");
        cpf.setText("");
        email.setText("");
        senha.setText("");
        info("Cadastro realizado! Agora faça login.");
        app.ir("login");
    }
}
