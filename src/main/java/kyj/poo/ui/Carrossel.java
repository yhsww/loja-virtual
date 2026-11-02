package kyj.poo.ui;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JComponent;
import javax.swing.Timer;

public class Carrossel extends JComponent {

    public record Slide(
        String titulo,
        String subtitulo,
        Color cor1,
        Color cor2
    ) {}

    private final Slide[] slides;
    private final Rectangle[] pontos;
    private final Rectangle setaEsq = new Rectangle();
    private final Rectangle setaDir = new Rectangle();
    private final Timer auto;
    private final Timer fade;
    private int atual = 0;
    private int anterior = 0;
    private float alpha = 1f;

    public Carrossel(Slide... slides) {
        this.slides = slides;
        this.pontos = new Rectangle[slides.length];
        for (int i = 0; i < pontos.length; i++) pontos[i] = new Rectangle();

        setPreferredSize(new Dimension(0, 250));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        fade = new Timer(16, e -> {
            alpha += 0.07f;
            if (alpha >= 1f) {
                alpha = 1f;
                ((Timer) e.getSource()).stop();
            }
            repaint();
        });

        auto = new Timer(4500, e -> mover(1));
        auto.start();

        addMouseListener(
            new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    Point p = e.getPoint();
                    if (setaEsq.contains(p)) mover(-1);
                    else if (setaDir.contains(p)) mover(1);
                    else {
                        for (int i = 0; i < pontos.length; i++) {
                            if (pontos[i].contains(p)) ir(i);
                        }
                    }
                    auto.restart();
                }
            }
        );
    }

    private void mover(int passo) {
        ir((atual + passo + slides.length) % slides.length);
    }

    private void ir(int i) {
        if (i == atual) return;
        anterior = atual;
        atual = i;
        alpha = 0f;
        fade.restart();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON
        );
        g2.setRenderingHint(
            RenderingHints.KEY_TEXT_ANTIALIASING,
            RenderingHints.VALUE_TEXT_ANTIALIAS_ON
        );
        int w = getWidth(),
            h = getHeight();

        g2.setClip(new RoundRectangle2D.Float(0, 0, w, h, 26, 26));
        desenhar(g2, slides[anterior], 1f, w, h);
        desenhar(g2, slides[atual], alpha, w, h);

        int d = 40;
        setaEsq.setBounds(16, h / 2 - d / 2, d, d);
        setaDir.setBounds(w - 16 - d, h / 2 - d / 2, d, d);
        seta(g2, setaEsq, "‹");
        seta(g2, setaDir, "›");

        // bolinhas
        int larguraTotal = 0;
        for (int i = 0; i < slides.length; i++) larguraTotal +=
            (i == atual ? 28 : 10) + 8;
        int x = (w - larguraTotal + 8) / 2;
        int y = h - 28;
        for (int i = 0; i < slides.length; i++) {
            int largura = i == atual ? 28 : 10;
            g2.setColor(
                i == atual ? Color.WHITE : new Color(255, 255, 255, 120)
            );
            g2.fillRoundRect(x, y, largura, 10, 10, 10);
            pontos[i].setBounds(x - 4, y - 6, largura + 8, 22);
            x += largura + 8;
        }
        g2.dispose();
    }

    private void desenhar(Graphics2D g2, Slide s, float a, int w, int h) {
        Composite antigo = g2.getComposite();
        g2.setComposite(
            AlphaComposite.SrcOver.derive(Math.max(0f, Math.min(1f, a)))
        );

        g2.setPaint(new GradientPaint(0, 0, s.cor1(), w, h, s.cor2()));
        g2.fillRect(0, 0, w, h);
        g2.setColor(new Color(255, 255, 255, 28));
        g2.fillOval(w - 260, -90, 400, 400);
        g2.fillOval(w - 420, h - 120, 260, 260);

        g2.setColor(Color.WHITE);
        g2.setFont(Tema.fonte(Font.BOLD, 38f));
        g2.drawString(s.titulo(), 90, h / 2 - 6);
        g2.setColor(new Color(255, 255, 255, 215));
        g2.setFont(Tema.fonte(Font.PLAIN, 18f));
        g2.drawString(s.subtitulo(), 92, h / 2 + 30);

        g2.setComposite(antigo);
    }

    private void seta(Graphics2D g2, Rectangle r, String s) {
        g2.setColor(new Color(255, 255, 255, 60));
        g2.fillOval(r.x, r.y, r.width, r.height);
        g2.setColor(Color.WHITE);
        g2.setFont(Tema.fonte(Font.BOLD, 26f));
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(
            s,
            r.x + (r.width - fm.stringWidth(s)) / 2,
            r.y + (r.height + fm.getAscent() - fm.getDescent()) / 2
        );
    }
}
