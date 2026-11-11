package kyj.poo.ui.tela;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.io.File;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JScrollBar;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import java.io.File;
import kyj.poo.api.ChatBotApi;
import kyj.poo.ui.Aplicacao;
import kyj.poo.ui.Sessao;
import kyj.poo.ui.Tela;
import kyj.poo.ui.Tema;

public class TelaChatBot extends Tela {

    private final JPanel conversa = new JPanel();
    private final JTextField pergunta = new JTextField();

    public TelaChatBot(Aplicacao app) {
        super(app);

        JPanel principal = new JPanel(new BorderLayout(0, 14));
        principal.setBackground(Tema.FUNDO);
        principal.setBorder(
            BorderFactory.createEmptyBorder(18, 18, 18, 18)
        );

        //cabecalho

        JPanel cabecalho = Tema.painel(new BorderLayout());

        JPanel textos = Tema.coluna();

        JLabel titulo = Tema.titulo(
            "Íris - Assistente Virtual"
        );

        JLabel subtitulo = Tema.subtitulo(
            "Tire suas dúvidas sobre produtos, pedidos e compras"
        );

        textos.add(titulo);
        textos.add(Box.createVerticalStrut(4));
        textos.add(subtitulo);

        JPanel esquerda = new JPanel(new BorderLayout());
        esquerda.setBackground(Tema.FUNDO);

        esquerda.add(
            textos,
            BorderLayout.CENTER
        );

        cabecalho.add(
            esquerda,
            BorderLayout.WEST
        );

        principal.add(
            cabecalho,
            BorderLayout.NORTH
        );

        //card conversa

        JPanel card = new JPanel(new BorderLayout());

        card.setBackground(Color.WHITE);

        card.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                    Tema.BORDA
                ),
                BorderFactory.createEmptyBorder(
                    18, 18, 18, 18
                )
            )
        );

        conversa.setLayout(
            new BoxLayout(
                conversa,
                BoxLayout.Y_AXIS
            )
        );

        conversa.setBackground(Color.WHITE);

        conversa.setBorder(
            new EmptyBorder(
                4, 4, 4, 4
            )
        );

        JScrollPane scroll = Tema.rolagem(
            conversa,
            Color.WHITE
        );

        card.add(
            scroll,
            BorderLayout.CENTER
        );

        principal.add(
            card,
            BorderLayout.CENTER
        );

        //campo conversa

        JPanel entrada = Tema.painel(
            new BorderLayout(10, 0)
        );

        Tema.campo(pergunta);

        pergunta.setPreferredSize(
            new Dimension(0, 44)
        );

        JButton enviar = new JButton("Enviar");

        enviar.setPreferredSize(
            new Dimension(105, 44)
        );

        enviar.setFont(
            Tema.fonte(
                Font.BOLD,
                14f
            )
        );

        enviar.setForeground(
            Color.WHITE
        );

        enviar.setBackground(
            Tema.PRIMARIA
        );

        enviar.setFocusPainted(false);

        enviar.setBorder(
            BorderFactory.createEmptyBorder(
                0, 18, 0, 18
            )
        );

        entrada.add(
            pergunta,
            BorderLayout.CENTER
        );

        entrada.add(
            enviar,
            BorderLayout.EAST
        );

        principal.add(
            entrada,
            BorderLayout.SOUTH
        );

        add(
            principal,
            BorderLayout.CENTER
        );

        //mensagem inicio
        String nome = Sessao.logado != null
                ? Sessao.logado.getNome()
                : "Cliente";

        adicionarMensagem(
            "Íris",
            "Olá, " + nome + "! Como posso ajudar?",
            false
        );


        //eventos botoes
        enviar.addActionListener(
            e -> enviarPergunta()
        );

        pergunta.addActionListener(
            e -> enviarPergunta()
        );
    }

   //enviar pergunta
    private void enviarPergunta() {

        String texto =
            pergunta.getText().trim();

        if (texto.isEmpty()) {
            return;
        }

        String nome =
            Sessao.logado.getNome();

        String cpf =
            Sessao.logado.getCpf();

        String email =
            Sessao.logado.getEmail();

        //mensagem cliente
        adicionarMensagem(
            "Você",
            texto,
            true
        );

        pergunta.setText("");

        try {

            ChatBotApi api =
                new ChatBotApi();

            String resposta =
                api.enviarPergunta(
                    nome,
                    cpf,
                    email,
                    texto
                );

            // Resposta da Íris

            adicionarMensagem(
                "Íris",
                resposta,
                false
            );

        } catch (Exception e) {

            adicionarMensagem(
                "Íris",
                "Não foi possível conectar ao servidor no momento. "
                + "Tente novamente mais tarde.",
                false
            );

            e.printStackTrace();
        }
    }

    private void adicionarMensagem(
            String remetente,
            String mensagem,
            boolean usuario
    ) {

        JPanel linha =
            new JPanel(
                new FlowLayout(
                    usuario
                        ? FlowLayout.RIGHT
                        : FlowLayout.LEFT
                )
            );

        linha.setBackground(
            Color.WHITE
        );

        linha.setAlignmentX(
            Component.LEFT_ALIGNMENT
        );

        JPanel bolha =
            criarBolha(
                remetente,
                mensagem,
                usuario
            );

        linha.add(bolha);

        conversa.add(linha);

        conversa.add(
            Box.createVerticalStrut(12)
        );

        conversa.revalidate();
        conversa.repaint();

        SwingUtilities.invokeLater(() -> {

            JScrollPane scroll =
                (JScrollPane) conversa
                    .getParent()
                    .getParent();

            JScrollBar barra =
                scroll.getVerticalScrollBar();

            barra.setValue(
                barra.getMaximum()
            );
        });
    }

    //balao de mensagem
    private JPanel criarBolha(
            String remetente,
            String mensagem,
            boolean usuario
    ) {

        JPanel bolha = new JPanel(
            new BorderLayout()
        );

        bolha.setBorder(
            BorderFactory.createEmptyBorder(
                12, 16, 12, 16
            )
        );

        if (usuario) {
            bolha.setBackground(
                Tema.PRIMARIA
            );
        } else {
            bolha.setBackground(
                Tema.PRIMARIA_CLARA
            );
        }

        // Nome de quem enviou
        JLabel nome = new JLabel(
            remetente
        );

        nome.setFont(
            Tema.fonte(
                Font.BOLD,
                12f
            )
        );

        if (usuario) {
            nome.setForeground(
                Color.WHITE
            );
        } else {
            nome.setForeground(
                Tema.PRIMARIA_ESCURA
            );
        }

        // Texto da mensagem
        JTextArea texto = new JTextArea(
            mensagem
        );

        texto.setEditable(false);
        texto.setFocusable(false);
        texto.setLineWrap(true);
        texto.setWrapStyleWord(true);
        texto.setOpaque(false);
        texto.setBorder(null);

        texto.setFont(
            Tema.fonte(
                Font.PLAIN,
                14f
            )
        );

        if (usuario) {
            texto.setForeground(
                Color.WHITE
            );
        } else {
            texto.setForeground(
                Tema.TEXTO
            );
        }

        // Painel interno
        JPanel conteudo = new JPanel();

        conteudo.setLayout(
            new BoxLayout(
                conteudo,
                BoxLayout.Y_AXIS
            )
        );

        conteudo.setBackground(
            usuario
                ? Tema.PRIMARIA
                : Tema.PRIMARIA_CLARA
        );

        conteudo.add(nome);

        conteudo.add(
            Box.createVerticalStrut(5)
        );

        conteudo.add(texto);

        conteudo.setPreferredSize(
            new Dimension(
                480,
                conteudo.getPreferredSize().height
            )
        );

        bolha.add(
            conteudo,
            BorderLayout.CENTER
        );

        return bolha;
    }
}