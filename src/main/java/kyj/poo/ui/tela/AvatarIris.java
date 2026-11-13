package kyj.poo.ui.tela;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JPanel;
import javax.swing.Timer;


public class AvatarIris extends JPanel {

    private static final int GRADE = 32;

    private static final Color CONTORNO = new Color(25, 30, 65);
    private static final Color BRANCO = new Color(242, 246, 255);
    private static final Color SOMBRA = new Color(183, 198, 235);
    private static final Color AZUL = new Color(63, 102, 230);
    private static final Color AZUL_CLARO = new Color(78, 196, 255);
    private static final Color CYAN = new Color(123, 240, 255);
    private static final Color TELA = new Color(35, 43, 82);
    private static final Color ROXO = new Color(125, 91, 220);

    private enum Estado {
        NORMAL,
        PENSANDO
    }

    private final Timer timer;
    private Estado estado = Estado.NORMAL;
    private int frame;

    public AvatarIris() {
        setOpaque(false);
        setPreferredSize(new Dimension(88, 88));

        timer = new Timer(90, e -> {
            frame = (frame + 1) % 12;
            repaint();
        });

        timer.start();
    }

    public void setPensando(boolean pensando) {
        estado = pensando ? Estado.PENSANDO : Estado.NORMAL;
        repaint();
    }

    public void pararAnimacao() {
        timer.stop();
    }

    public void iniciarAnimacao() {
        if (!timer.isRunning()) {
            timer.start();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int escala = Math.min(
            getWidth() / GRADE,
            getHeight() / GRADE
        );

        if (escala < 1) {
            return;
        }

        int tamanho = GRADE * escala;
        int origemX = (getWidth() - tamanho) / 2;
        int origemY = (getHeight() - tamanho) / 2;

        // Movimento vertical sutil para dar vida ao mascote.
        int flutuacao = (frame % 6 < 3) ? 0 : -1;

        Graphics2D g2 = (Graphics2D) g.create();
        g2.translate(origemX, origemY + flutuacao * escala);
        g2.setRenderingHint(
            RenderingHints.KEY_INTERPOLATION,
            RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR
        );
        g2.setRenderingHint(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_OFF
        );

        desenharPixelArt(g2, escala);
        g2.dispose();
    }

    private void desenharPixelArt(Graphics2D g, int s) {
        // Plataforma de luz.
        pixel(g, 7, 27, 18, 2, AZUL_CLARO, s);
        pixel(g, 9, 29, 14, 1, new Color(63, 102, 230, 90), s);

        // Antena.
        pixel(g, 15, 1, 2, 4, AZUL, s);
        pixel(g, 14, 0, 4, 2, AZUL_CLARO, s);
        pixel(g, 15, 0, 2, 1, CYAN, s);

        // Corpo.
        pixel(g, 9, 17, 14, 10, CONTORNO, s);
        pixel(g, 10, 18, 12, 8, BRANCO, s);
        pixel(g, 11, 23, 10, 3, SOMBRA, s);

        // Braços.
        pixel(g, 7, 19, 3, 5, CONTORNO, s);
        pixel(g, 6, 21, 3, 2, AZUL, s);
        pixel(g, 22, 19, 3, 5, CONTORNO, s);
        pixel(g, 23, 21, 3, 2, AZUL, s);

        // Cabeça/tela.
        pixel(g, 5, 6, 22, 14, CONTORNO, s);
        pixel(g, 7, 7, 18, 11, BRANCO, s);
        pixel(g, 8, 9, 16, 7, TELA, s);

        // Laterais.
        pixel(g, 4, 10, 3, 5, CONTORNO, s);
        pixel(g, 4, 11, 2, 3, AZUL, s);
        pixel(g, 25, 10, 3, 5, CONTORNO, s);
        pixel(g, 26, 11, 2, 3, AZUL, s);

        // Expressão.
        if (estado == Estado.PENSANDO) {
            pixel(g, 11, 11, 3, 1, CYAN, s);
            pixel(g, 18, 11, 3, 1, CYAN, s);
            pixel(g, 15, 13, 2, 1, AZUL_CLARO, s);

            // Pequenos pixels indicam processamento.
            pixel(g, 26, 5, 1, 1, CYAN, s);
            pixel(g, 28, 3, 1, 1, ROXO, s);
        } else {
            boolean piscando = frame == 0 || frame == 1;

            if (piscando) {
                pixel(g, 11, 12, 3, 1, CYAN, s);
                pixel(g, 18, 12, 3, 1, CYAN, s);
            } else {
                pixel(g, 11, 11, 3, 3, CYAN, s);
                pixel(g, 18, 11, 3, 3, CYAN, s);
                pixel(g, 11, 11, 1, 1, Color.WHITE, s);
                pixel(g, 18, 11, 1, 1, Color.WHITE, s);
            }

            // Sorriso.
            pixel(g, 14, 14, 4, 1, CYAN, s);
            pixel(g, 13, 13, 1, 1, CYAN, s);
            pixel(g, 17, 13, 1, 1, CYAN, s);
        }

        // Ícone de carrinho no peito.
        pixel(g, 14, 20, 4, 1, AZUL, s);
        pixel(g, 15, 21, 4, 2, AZUL, s);
        pixel(g, 15, 23, 1, 1, ROXO, s);
        pixel(g, 18, 23, 1, 1, ROXO, s);
    }

    private void pixel(
            Graphics2D g,
            int x,
            int y,
            int largura,
            int altura,
            Color cor,
            int escala
    ) {
        g.setColor(cor);
        g.fillRect(
            x * escala,
            y * escala,
            largura * escala,
            altura * escala
        );
    }
}
