package kyj.poo.ui;

import java.awt.*;
import javax.swing.*;

public class Cartao extends JPanel {

    private static final int SOMBRA = 5;
    private final int raio;

    public Cartao(LayoutManager layout) {
        this(layout, 16, 20);
    }

    public Cartao(LayoutManager layout, int raio, int padding) {
        super(layout);
        this.raio = raio;
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(
                padding + SOMBRA, padding + SOMBRA, padding + SOMBRA + 2, padding + SOMBRA));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int w = getWidth(), h = getHeight();

        for (int i = SOMBRA; i > 0; i--) {
            int d = SOMBRA - i;
            g2.setColor(new Color(15, 23, 42, 7));
            g2.fillRoundRect(d, d + 2, w - 2 * d, h - 2 * d - 2, raio + i, raio + i);
        }
        g2.setColor(Color.WHITE);
        g2.fillRoundRect(SOMBRA, SOMBRA, w - 2 * SOMBRA, h - 2 * SOMBRA - 2, raio, raio);
        g2.setColor(Tema.BORDA);
        g2.drawRoundRect(SOMBRA, SOMBRA, w - 2 * SOMBRA - 1, h - 2 * SOMBRA - 3, raio, raio);
        g2.dispose();
        super.paintComponent(g);
    }
}