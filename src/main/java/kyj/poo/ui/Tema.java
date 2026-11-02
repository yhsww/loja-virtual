package kyj.poo.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.Collections;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.Scrollable;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.text.JTextComponent;

public final class Tema {

    public static final Color PRIMARIA = new Color(79, 70, 229);
    public static final Color PRIMARIA_ESCURA = new Color(55, 48, 163);
    public static final Color PRIMARIA_CLARA = new Color(238, 242, 255);
    public static final Color FUNDO = new Color(243, 244, 248);
    public static final Color TEXTO = new Color(17, 24, 39);
    public static final Color TEXTO_SUAVE = new Color(107, 114, 128);
    public static final Color BORDA = new Color(229, 231, 235);
    public static final Color SUCESSO = new Color(22, 163, 74);
    public static final Color PERIGO = new Color(220, 38, 38);
    public static final Color BARRA = new Color(17, 24, 39);

    private Tema() {}

    public static Font fonte(int estilo, float tamanho) {
        return new Font("Segoe UI", estilo, 1).deriveFont(estilo, tamanho);
    }

    public static void aplicar() {
        try {
            UIManager.setLookAndFeel(
                UIManager.getCrossPlatformLookAndFeelClassName()
            );
        } catch (Exception ignorado) {}

        for (Object chave : Collections.list(UIManager.getDefaults().keys())) {
            if (UIManager.get(chave) instanceof FontUIResource) {
                UIManager.put(
                    chave,
                    new FontUIResource(fonte(Font.PLAIN, 14f))
                );
            }
        }
    }

    public static JPanel painel(LayoutManager layout) {
        JPanel p = new JPanel(layout);
        p.setOpaque(false);
        return p;
    }

    public static JPanel coluna() {
        JPanel p = painel(null);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        return p;
    }

    public static <T extends JComponent> T esq(T c) {
        c.setAlignmentX(Component.LEFT_ALIGNMENT);
        return c;
    }

    public static JLabel titulo(String texto) {
        JLabel l = new JLabel(texto);
        l.setFont(fonte(Font.BOLD, 26f));
        l.setForeground(TEXTO);
        return l;
    }

    public static JLabel secao(String texto) {
        JLabel l = new JLabel(texto);
        l.setFont(fonte(Font.BOLD, 18f));
        l.setForeground(TEXTO);
        return l;
    }

    public static JLabel subtitulo(String texto) {
        JLabel l = new JLabel(texto);
        l.setFont(fonte(Font.PLAIN, 14f));
        l.setForeground(TEXTO_SUAVE);
        return l;
    }

    public static JLabel rotulo(String texto) {
        JLabel l = new JLabel(texto);
        l.setFont(fonte(Font.BOLD, 12f));
        l.setForeground(TEXTO_SUAVE);
        return l;
    }

    public static <T extends JTextComponent> T campo(T c) {
        c.setFont(fonte(Font.PLAIN, 14f));
        c.setForeground(TEXTO);
        c.setBackground(Color.WHITE);
        c.setCaretColor(PRIMARIA);
        c.setBorder(bordaCampo(false));
        c.addFocusListener(
            new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    c.setBorder(bordaCampo(true));
                }

                @Override
                public void focusLost(FocusEvent e) {
                    c.setBorder(bordaCampo(false));
                }
            }
        );
        return c;
    }

    private static Border bordaCampo(boolean foco) {
        int pad = foco ? 8 : 9;
        return BorderFactory.createCompoundBorder(
            new LineBorder(foco ? PRIMARIA : BORDA, foco ? 2 : 1, true),
            BorderFactory.createEmptyBorder(pad, 12, pad, 12)
        );
    }

    public static JPanel campoRotulado(String texto, JComponent campo) {
        JPanel p = coluna();
        p.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel l = esq(rotulo(texto));
        campo.setAlignmentX(Component.LEFT_ALIGNMENT);
        campo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        p.add(l);
        p.add(Box.createVerticalStrut(6));
        p.add(campo);
        return p;
    }

    public static void estilizarTabela(JTable t) {
        t.setRowHeight(46);
        t.setShowGrid(false);
        t.setIntercellSpacing(new Dimension(0, 0));
        t.setFont(fonte(Font.PLAIN, 14f));
        t.setForeground(TEXTO);
        t.setBackground(Color.WHITE);
        t.setSelectionBackground(PRIMARIA_CLARA);
        t.setSelectionForeground(PRIMARIA_ESCURA);
        t.setFillsViewportHeight(true);

        DefaultTableCellRenderer celula = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                JTable tb,
                Object v,
                boolean sel,
                boolean foco,
                int r,
                int c
            ) {
                super.getTableCellRendererComponent(tb, v, sel, false, r, c);
                setBorder(BorderFactory.createEmptyBorder(0, 14, 0, 14));
                setHorizontalAlignment(SwingConstants.LEFT);
                return this;
            }
        };
        t.setDefaultRenderer(Object.class, celula);
        t.setDefaultRenderer(Number.class, celula);

        JTableHeader h = t.getTableHeader();
        h.setReorderingAllowed(false);
        h.setPreferredSize(new Dimension(0, 40));
        h.setDefaultRenderer(
            new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(
                    JTable tb,
                    Object v,
                    boolean sel,
                    boolean foco,
                    int r,
                    int c
                ) {
                    super.getTableCellRendererComponent(
                        tb,
                        v,
                        false,
                        false,
                        r,
                        c
                    );
                    setBackground(FUNDO);
                    setForeground(TEXTO_SUAVE);
                    setFont(fonte(Font.BOLD, 12f));
                    setHorizontalAlignment(SwingConstants.LEFT);
                    setBorder(
                        BorderFactory.createCompoundBorder(
                            BorderFactory.createMatteBorder(0, 0, 1, 0, BORDA),
                            BorderFactory.createEmptyBorder(0, 14, 0, 14)
                        )
                    );
                    return this;
                }
            }
        );
    }

    public static JScrollPane rolagem(Component c, Color fundo) {
        JScrollPane sp = new JScrollPane(
            c,
            ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER
        );
        sp.setBorder(BorderFactory.createEmptyBorder());
        sp.setBackground(fundo);
        sp.getViewport().setBackground(fundo);
        JScrollBar sb = sp.getVerticalScrollBar();
        sb.setUI(new BarraFina());
        sb.setPreferredSize(new Dimension(10, 0));
        sb.setUnitIncrement(16);
        return sp;
    }

    public static class Rolavel extends JPanel implements Scrollable {

        public Rolavel(LayoutManager layout) {
            super(layout);
            setBackground(FUNDO);
        }

        @Override
        public Dimension getPreferredScrollableViewportSize() {
            return getPreferredSize();
        }

        @Override
        public int getScrollableUnitIncrement(Rectangle v, int o, int d) {
            return 16;
        }

        @Override
        public int getScrollableBlockIncrement(Rectangle v, int o, int d) {
            return v.height;
        }

        @Override
        public boolean getScrollableTracksViewportWidth() {
            return true;
        }

        @Override
        public boolean getScrollableTracksViewportHeight() {
            return false;
        }
    }

    private static class BarraFina extends BasicScrollBarUI {

        @Override
        protected void configureScrollBarColors() {
            super.configureScrollBarColors();
            thumbColor = new Color(203, 213, 225);
            trackColor = new Color(0, 0, 0, 0);
        }

        @Override
        protected JButton createDecreaseButton(int o) {
            return vazio();
        }

        @Override
        protected JButton createIncreaseButton(int o) {
            return vazio();
        }

        private JButton vazio() {
            JButton b = new JButton();
            b.setPreferredSize(new Dimension(0, 0));
            return b;
        }

        @Override
        protected void paintTrack(Graphics g, JComponent c, Rectangle r) {}

        @Override
        protected void paintThumb(Graphics g, JComponent c, Rectangle r) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
            );
            g2.setColor(thumbColor);
            g2.fillRoundRect(r.x + 2, r.y + 2, r.width - 4, r.height - 4, 8, 8);
            g2.dispose();
        }
    }
}
