package kyj.poo.ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import kyj.poo.regras.carrinho.ItemDoCarrinho;
import kyj.poo.ui.tela.TelaChatBot;
import kyj.poo.ui.tela.TelaAdmin;
import kyj.poo.ui.tela.TelaCarrinho;
import kyj.poo.ui.tela.TelaCheckout;
import kyj.poo.ui.tela.TelaComprador;
import kyj.poo.ui.tela.TelaHistorico;
import kyj.poo.ui.tela.TelaLogin;
import kyj.poo.ui.tela.TelaLoja;

public class Aplicacao extends JFrame {

    private final CardLayout cards = new CardLayout();
    private final JPanel centro = new JPanel(cards);
    private final Map<String, Tela> telas = new HashMap<>();
    private final JPanel nav = Tema.painel(
        new FlowLayout(FlowLayout.RIGHT, 6, 14)
    );
    private String atualNome = "loja";

    public Aplicacao() {
        super("Loja Virtual");
        try {
            Sessao.inicializar();
        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(
                null,
                e.getMessage(),
                "Erro ao carregar os dados",
                JOptionPane.ERROR_MESSAGE
            );
            throw e;
        }
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1120, 780);
        setMinimumSize(new Dimension(980, 640));
        setLocationRelativeTo(null);

        centro.setBackground(Tema.FUNDO);
        add(criarBarra(), BorderLayout.NORTH);
        add(centro, BorderLayout.CENTER);

        registrar("loja", new TelaLoja(this));
        registrar("chatbot", new TelaChatBot(this));
        registrar("cadastro", new TelaComprador(this));
        registrar("login", new TelaLogin(this));
        registrar("admin", new TelaAdmin(this));
        registrar("carrinho", new TelaCarrinho(this));
        registrar("checkout", new TelaCheckout(this));
        registrar("historico", new TelaHistorico(this));

        ir("loja");
    }

    private void registrar(String nome, Tela tela) {
        telas.put(nome, tela);
        centro.add(tela, nome);
    }

    public void ir(String nome) {
        if (
            Set.of("carrinho", "checkout", "historico").contains(nome) &&
            Sessao.logado == null
        ) {
            JOptionPane.showMessageDialog(this, "Faça login para continuar.");
            nome = "login";
        }
        if (nome.equals("admin") && !Sessao.admin) {
            JOptionPane.showMessageDialog(
                this,
                "Acesso restrito ao administrador."
            );
            nome = "login";
        }
        atualNome = nome;
        Tela tela = telas.get(nome);
        tela.aoMostrar();
        cards.show(centro, nome);
        atualizarBarra();
    }

    private JPanel criarBarra() {
        JPanel barra = new JPanel(new BorderLayout());
        barra.setBackground(Tema.BARRA);
        barra.setBorder(BorderFactory.createEmptyBorder(0, 28, 0, 18));
        barra.setPreferredSize(new Dimension(0, 66));

        JLabel logo = new JLabel(
            "<html><span style='color:white'>Loja</span>" +
                "<span style='color:#818cf8'>Virtual</span></html>"
        );
        logo.setFont(Tema.fonte(Font.BOLD, 24f));
        logo.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        logo.addMouseListener(
            new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    ir("loja");
                }
            }
        );

        barra.add(logo, BorderLayout.WEST);
        barra.add(nav, BorderLayout.EAST);
        return barra;
    }

    private void atualizarBarra() {
        nav.removeAll();
        nav.add(item("Início", "loja"));

        if (Sessao.admin) {
            nav.add(item("Painel Admin", "admin"));
            nav.add(botaoSair());
        } else if (Sessao.logado != null) {
            int qtd = 0;
            for (ItemDoCarrinho it : Sessao.logado.getCarrinho().getItens())
                qtd += it.getQtd();
            nav.add(item("Carrinho (" + qtd + ")", "carrinho"));
            nav.add(item("Histórico", "historico"));
            nav.add(item("Chatbot", "chatbot"));

            JLabel ola = new JLabel(
                "Olá, " + Sessao.logado.getNome().trim().split(" ")[0]
            );
            ola.setFont(Tema.fonte(Font.PLAIN, 14f));
            ola.setForeground(new Color(209, 213, 219));
            ola.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 6));
            nav.add(ola);
            nav.add(botaoSair());
        } else {
            nav.add(item("Carrinho", "carrinho"));
            nav.add(item("Entrar", "login"));
            Botao cadastrar = Botao.primario("Cadastrar-se");
            cadastrar.addActionListener(e -> ir("cadastro"));
            nav.add(cadastrar);
        }
        nav.revalidate();
        nav.repaint();
    }

    private Botao item(String texto, String destino) {
        Botao b = Botao.nav(texto);
        b.setAtivo(destino.equals(atualNome));
        b.addActionListener(e -> ir(destino));
        return b;
    }

    private Botao botaoSair() {
        Botao b = Botao.nav("Sair");
        b.addActionListener(e -> {
            Sessao.sair();
            ir("loja");
        });
        return b;
    }
}
