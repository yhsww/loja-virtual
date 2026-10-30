package kyj.poo.ui;

import java.awt.*;
import javax.swing.*;

public class PainelGradiente extends JPanel {

    private final Color c1, c2;

    public PainelGradiente(Color c1, Color c2) {
        this.c1 = c1;
        this.c2 = c2;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int w = getWidth(), h = getHeight();
        g2.setPaint(new GradientPaint(0, 0, c1, w, h, c2));
        g2.fillRect(0, 0, w, h);
        g2.setColor(new Color(255, 255, 255, 26));
        g2.fillOval(w - 200, -80, 320, 320);
        g2.fillOval(-100, h - 180, 280, 280);
        g2.dispose();
    }
}