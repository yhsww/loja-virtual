package kyj.poo.ui.tela;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * Avatar animado da Íris.
 *
 * A animação é feita apenas com Swing/Java2D:
 * o Timer atualiza uma pequena posição vertical e chama repaint().
 * Não depende de GIF, JavaFX ou bibliotecas externas.
 */
public class AvatarIris extends JPanel {

    private enum Estado {
        NORMAL,
        PENSANDO
    }

    private final Timer timer;
    private Estado estado = Estado.NORMAL;

    private double fase = 0;
    private double velocidade = 0.09;

    public AvatarIris() {
        setOpaque(false);
        setPreferredSize(new Dimension(82, 82));

        timer = new Timer(40, e -> {
            fase += velocidade;
            repaint();
        });

        timer.start();
    }

    public void setPensando(boolean pensando) {
        estado = pensando ? Estado.PENSANDO : Estado.NORMAL;
        velocidade = pensando ? 0.16 : 0.09;
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

        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON
        );

        double onda = Math.sin(fase);
        int flutuacao = (int) Math.round(onda * (estado == Estado.PENSANDO ? 4 : 2));

        int largura = getWidth();
        int altura = getHeight();

        int tamanho = Math.min(largura, altura) - 10;
        int x = (largura - tamanho) / 2;
        int y = (altura - tamanho) / 2 + flutuacao;

        // Sombra/glow suave abaixo do robô.
        int sombraX = x + tamanho / 4;
        int sombraY = y + tamanho - 2;

        g2.setColor(new Color(90, 110, 220, 35));
        g2.fillOval(
            sombraX,
            sombraY,
            tamanho / 2,
            8
        );

        // Corpo branco arredondado.
        int corpoX = x + 15;
        int corpoY = y + 38;
        int corpoW = tamanho - 30;
        int corpoH = tamanho - 24;

        g2.setColor(new Color(235, 242, 255));
        g2.fillRoundRect(corpoX, corpoY, corpoW, corpoH, 24, 24);

        g2.setColor(new Color(115, 145, 230));
        g2.setStroke(new BasicStroke(1.8f));
        g2.drawRoundRect(corpoX, corpoY, corpoW, corpoH, 24, 24);

        // Cabeça/tela do robô.
        int telaX = x + 7;
        int telaY = y + 10;
        int telaW = tamanho - 14;
        int telaH = tamanho - 30;

        g2.setColor(new Color(35, 43, 78));
        g2.fillRoundRect(telaX, telaY, telaW, telaH, 25, 25);

        g2.setColor(new Color(95, 130, 230));
        g2.drawRoundRect(telaX, telaY, telaW, telaH, 25, 25);

        // Antena.
        int centro = x + tamanho / 2;
        g2.setColor(new Color(100, 185, 255));
        g2.setStroke(new BasicStroke(2f));
        g2.drawLine(centro, telaY, centro, y + 3);
        g2.fillOval(centro - 4, y, 8, 8);

        // Olhos: ficam mais "atentos" quando a Íris está pensando.
        g2.setColor(new Color(125, 225, 255));

        if (estado == Estado.PENSANDO) {
            int olhoY = telaY + telaH / 2 - 2;
            g2.fillRoundRect(telaX + 18, olhoY, 16, 5, 5, 5);
            g2.fillRoundRect(telaX + telaW - 34, olhoY, 16, 5, 5, 5);
        } else {
            int olhoY = telaY + telaH / 2 - 7;
            g2.fillOval(telaX + 19, olhoY, 13, 15);
            g2.fillOval(telaX + telaW - 32, olhoY, 13, 15);

            // Pequeno brilho nos olhos.
            g2.setColor(Color.WHITE);
            g2.fillOval(telaX + 22, olhoY + 3, 4, 4);
            g2.fillOval(telaX + telaW - 29, olhoY + 3, 4, 4);
        }

        // Sorriso.
        g2.setColor(new Color(125, 225, 255));
        g2.setStroke(new BasicStroke(2f));

        int sorrisoY = telaY + telaH - 18;
        g2.drawArc(
            centro - 12,
            sorrisoY - 5,
            24,
            14,
            200,
            140
        );

        // Ícone de carrinho no peito.
        int carrinhoX = centro - 11;
        int carrinhoY = corpoY + 12;

        g2.setColor(new Color(80, 145, 240));
        g2.setStroke(new BasicStroke(1.8f));
        g2.drawLine(carrinhoX - 5, carrinhoY, carrinhoX, carrinhoY);
        g2.drawRect(carrinhoX, carrinhoY + 3, 18, 11);
        g2.drawLine(
            carrinhoX + 18,
            carrinhoY + 3,
            carrinhoX + 21,
            carrinhoY + 3
        );
        g2.fillOval(carrinhoX + 3, carrinhoY + 15, 4, 4);
        g2.fillOval(carrinhoX + 13, carrinhoY + 15, 4, 4);

        // Pequenos brilhos quando está pensando.
        if (estado == Estado.PENSANDO) {
            g2.setColor(new Color(120, 200, 255, 180));
            g2.fillOval(x + 1, y + 22, 4, 4);
            g2.fillOval(x + tamanho - 5, y + 31, 4, 4);
        }

        g2.dispose();
    }
}
