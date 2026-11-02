package kyj.poo.ui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.BorderFactory;
import javax.swing.ButtonModel;
import javax.swing.JButton;

public class Botao extends JButton {

    public enum Variante {
        PRIMARIO,
        SECUNDARIO,
        PERIGO,
        TEXTO,
        NAV,
    }

    private Variante variante;
    private boolean ativo;

    public Botao(String texto, Variante variante) {
        super(texto);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setOpaque(false);
        setRolloverEnabled(true);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setFont(Tema.fonte(Font.BOLD, 14f));
        setBorder(BorderFactory.createEmptyBorder(11, 20, 11, 20));
        setVariante(variante);
    }

    public static Botao primario(String t) {
        return new Botao(t, Variante.PRIMARIO);
    }

    public static Botao secundario(String t) {
        return new Botao(t, Variante.SECUNDARIO);
    }

    public static Botao perigo(String t) {
        return new Botao(t, Variante.PERIGO);
    }

    public static Botao texto(String t) {
        return new Botao(t, Variante.TEXTO);
    }

    public static Botao nav(String t) {
        return new Botao(t, Variante.NAV);
    }

    public void setVariante(Variante v) {
        this.variante = v;
        atualizarCor();
        repaint();
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
        atualizarCor();
        repaint();
    }

    private void atualizarCor() {
        switch (variante) {
            case PRIMARIO, PERIGO -> setForeground(Color.WHITE);
            case SECUNDARIO -> setForeground(Tema.TEXTO);
            case TEXTO -> setForeground(Tema.PRIMARIA);
            case NAV -> setForeground(
                ativo ? Color.WHITE : new Color(209, 213, 219)
            );
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON
        );

        ButtonModel m = getModel();
        boolean hover = m.isRollover();
        boolean press = m.isPressed() && m.isArmed();
        boolean on = isEnabled();
        int w = getWidth(),
            h = getHeight();

        switch (variante) {
            case PRIMARIO -> {
                g2.setColor(
                    !on
                        ? new Color(165, 180, 252)
                        : press
                          ? Tema.PRIMARIA_ESCURA.darker()
                          : hover
                            ? Tema.PRIMARIA_ESCURA
                            : Tema.PRIMARIA
                );
                g2.fillRoundRect(0, 0, w, h, 12, 12);
            }
            case PERIGO -> {
                g2.setColor(
                    !on
                        ? new Color(252, 165, 165)
                        : hover
                          ? Tema.PERIGO.darker()
                          : Tema.PERIGO
                );
                g2.fillRoundRect(0, 0, w, h, 12, 12);
            }
            case SECUNDARIO -> {
                g2.setColor(hover ? Tema.FUNDO : Color.WHITE);
                g2.fillRoundRect(0, 0, w - 1, h - 1, 12, 12);
                g2.setColor(hover ? Tema.PRIMARIA : Tema.BORDA);
                g2.drawRoundRect(0, 0, w - 1, h - 1, 12, 12);
            }
            case TEXTO -> {
                if (hover) {
                    g2.setColor(Tema.PRIMARIA_CLARA);
                    g2.fillRoundRect(0, 0, w, h, 12, 12);
                }
            }
            case NAV -> {
                if (hover) {
                    g2.setColor(new Color(255, 255, 255, 28));
                    g2.fillRoundRect(0, 0, w, h, 10, 10);
                }
                if (ativo) {
                    g2.setColor(new Color(129, 140, 248));
                    g2.fillRoundRect(16, h - 5, w - 32, 3, 3, 3);
                }
            }
        }
        g2.dispose();
        super.paintComponent(g);
    }
}
