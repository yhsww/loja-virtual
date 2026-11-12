package kyj.poo.ui.tela;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.concurrent.ExecutionException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;

import kyj.poo.api.ChatBotApi;
import kyj.poo.ui.Aplicacao;
import kyj.poo.ui.Sessao;
import kyj.poo.ui.Tela;
import kyj.poo.ui.Tema;

public class TelaChatBot extends Tela {

    private final JPanel conversa = new JPanel();
    private final JTextField pergunta = new JTextField();
    private final JButton enviar = new JButton("Enviar");
    private final JLabel status = Tema.subtitulo("");

    public TelaChatBot(Aplicacao app) {
        super(app);

        JPanel principal = new JPanel(new BorderLayout(0, 14));
        principal.setBackground(Tema.FUNDO);
        principal.setBorder(
            BorderFactory.createEmptyBorder(18, 18, 18, 18)
        );

        JPanel cabecalho = Tema.painel(new BorderLayout());

        JPanel textos = Tema.coluna();
        textos.add(Tema.titulo("Íris - Assistente Virtual"));
        textos.add(Box.createVerticalStrut(4));
        textos.add(
            Tema.subtitulo(
                "Tire suas dúvidas sobre produtos, pedidos e compras"
            )
        );

        JPanel esquerda = new JPanel(new BorderLayout());
        esquerda.setBackground(Tema.FUNDO);
        esquerda.add(textos, BorderLayout.CENTER);

        cabecalho.add(esquerda, BorderLayout.WEST);
        principal.add(cabecalho, BorderLayout.NORTH);

        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Tema.BORDA),
                BorderFactory.createEmptyBorder(18, 18, 18, 18)
            )
        );

        conversa.setLayout(new BoxLayout(conversa, BoxLayout.Y_AXIS));
        conversa.setBackground(Color.WHITE);
        conversa.setBorder(new EmptyBorder(4, 4, 4, 4));

        JScrollPane scroll = Tema.rolagem(conversa, Color.WHITE);
        card.add(scroll, BorderLayout.CENTER);
        principal.add(card, BorderLayout.CENTER);

        JPanel entrada = Tema.painel(new BorderLayout(10, 5));

        Tema.campo(pergunta);
        pergunta.setPreferredSize(new Dimension(0, 44));

        enviar.setPreferredSize(new Dimension(105, 44));
        enviar.setFont(Tema.fonte(Font.BOLD, 14f));
        enviar.setForeground(Color.WHITE);
        enviar.setBackground(Tema.PRIMARIA);
        enviar.setFocusPainted(false);
        enviar.setBorder(
            BorderFactory.createEmptyBorder(0, 18, 0, 18)
        );

        JPanel rodape = Tema.painel(new BorderLayout(0, 4));
        rodape.add(entrada, BorderLayout.CENTER);
        rodape.add(status, BorderLayout.SOUTH);

        entrada.add(pergunta, BorderLayout.CENTER);
        entrada.add(enviar, BorderLayout.EAST);

        principal.add(rodape, BorderLayout.SOUTH);
        add(principal, BorderLayout.CENTER);

        String nome = Sessao.logado != null
            ? Sessao.logado.getNome()
            : "Cliente";

        adicionarMensagem(
            "Íris",
            "Olá, " + nome + "! Como posso ajudar?",
            false
        );

        enviar.addActionListener(e -> enviarPergunta());
        pergunta.addActionListener(e -> enviarPergunta());
    }

    private void enviarPergunta() {
        String texto = pergunta.getText().trim();

        if (texto.isEmpty()) {
            return;
        }

        if (Sessao.logado == null) {
            erro("É necessário estar logado para conversar com a Íris.");
            return;
        }

        String nome = Sessao.logado.getNome();
        String cpf = Sessao.logado.getCpf();
        String email = Sessao.logado.getEmail();

        adicionarMensagem("Você", texto, true);
        pergunta.setText("");
        alterarEstadoEnvio(false, "Íris está respondendo...");

        SwingWorker<String, Void> worker = new SwingWorker<>() {
            @Override
            protected String doInBackground() throws Exception {
                ChatBotApi api = new ChatBotApi();

                return api.enviarPergunta(
                    nome,
                    cpf,
                    email,
                    texto
                );
            }

            @Override
            protected void done() {
                try {
                    adicionarMensagem("Íris", get(), false);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    mostrarErroConexao();
                } catch (ExecutionException e) {
                    mostrarErroConexao();
                } finally {
                    alterarEstadoEnvio(true, "");
                    pergunta.requestFocusInWindow();
                }
            }
        };

        worker.execute();
    }

    private void alterarEstadoEnvio(boolean habilitado, String mensagem) {
        enviar.setEnabled(habilitado);
        pergunta.setEnabled(habilitado);
        status.setText(mensagem);

        if (habilitado) {
            enviar.setBackground(Tema.PRIMARIA);
        } else {
            enviar.setBackground(Tema.PRIMARIA_ESCURA);
        }
    }

    private void mostrarErroConexao() {
        adicionarMensagem(
            "Íris",
            "Não foi possível conectar ao servidor no momento. "
                + "Verifique se o chatbot está em execução e tente novamente.",
            false
        );
    }

    private void adicionarMensagem(
            String remetente,
            String mensagem,
            boolean usuario
    ) {
        JPanel linha = new JPanel(
            new FlowLayout(
                usuario ? FlowLayout.RIGHT : FlowLayout.LEFT,
                0,
                0
            )
        );

        linha.setBackground(Color.WHITE);
        linha.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel bolha = criarBolha(remetente, mensagem, usuario);
        linha.add(bolha);

        conversa.add(linha);
        conversa.add(Box.createVerticalStrut(12));

        conversa.revalidate();
        conversa.repaint();

        SwingUtilities.invokeLater(this::rolarParaFim);
    }

    private void rolarParaFim() {
        if (conversa.getParent() == null
                || conversa.getParent().getParent() == null) {
            return;
        }

        JScrollPane scroll =
            (JScrollPane) conversa.getParent().getParent();

        JScrollBar barra = scroll.getVerticalScrollBar();
        barra.setValue(barra.getMaximum());
    }

    private JPanel criarBolha(
            String remetente,
            String mensagem,
            boolean usuario
    ) {
        JPanel bolha = new JPanel(new BorderLayout());
        bolha.setBorder(
            BorderFactory.createEmptyBorder(12, 16, 12, 16)
        );

        Color fundo = usuario
            ? Tema.PRIMARIA
            : Tema.PRIMARIA_CLARA;

        bolha.setBackground(fundo);

        JLabel nome = new JLabel(remetente);
        nome.setFont(Tema.fonte(Font.BOLD, 12f));
        nome.setForeground(
            usuario ? Color.WHITE : Tema.PRIMARIA_ESCURA
        );

        JTextArea texto = new JTextArea(mensagem);
        texto.setEditable(false);
        texto.setFocusable(false);
        texto.setLineWrap(true);
        texto.setWrapStyleWord(true);
        texto.setOpaque(false);
        texto.setBorder(null);
        texto.setFont(Tema.fonte(Font.PLAIN, 14f));
        texto.setForeground(usuario ? Color.WHITE : Tema.TEXTO);

        // Mantém as mensagens legíveis sem criar uma bolha excessivamente larga.
        texto.setColumns(42);
        texto.setRows(1);
        texto.setMaximumSize(new Dimension(520, Integer.MAX_VALUE));

        JPanel conteudo = new JPanel();
        conteudo.setLayout(new BoxLayout(conteudo, BoxLayout.Y_AXIS));
        conteudo.setBackground(fundo);

        conteudo.add(nome);
        conteudo.add(Box.createVerticalStrut(5));
        conteudo.add(texto);

        bolha.add(conteudo, BorderLayout.CENTER);

        return bolha;
    }
}
