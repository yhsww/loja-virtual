package kyj.poo.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;

import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public abstract class Tela extends JPanel {

    protected final Aplicacao app;

    protected Tela(Aplicacao app) {
        super(new BorderLayout());
        this.app = app;
        setBackground(Tema.FUNDO);
    }

    public void aoMostrar() {}

    protected void erro(String msg) {
        JOptionPane.showMessageDialog(
            this,
            msg,
            "Atenção",
            JOptionPane.WARNING_MESSAGE
        );
    }

    protected void info(String msg) {
        JOptionPane.showMessageDialog(
            this,
            msg,
            "Loja Virtual",
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    protected static JPanel linha(JComponent esquerda, JComponent direita) {
        JPanel l = Tema.painel(new BorderLayout());
        l.add(esquerda, BorderLayout.WEST);
        l.add(direita, BorderLayout.EAST);
        return l;
    }

    protected JPanel formulario(
        String titulo,
        String subtitulo,
        String[] rotulos,
        JComponent[] campos,
        Botao principal,
        Botao... secundarios
    ) {
        JPanel col = Tema.coluna();
        col.add(Tema.esq(Tema.titulo(titulo)));
        col.add(Box.createVerticalStrut(6));
        col.add(Tema.esq(Tema.subtitulo(subtitulo)));
        col.add(Box.createVerticalStrut(26));

        for (int i = 0; i < rotulos.length; i++) {
            col.add(Tema.campoRotulado(rotulos[i], campos[i]));
            col.add(Box.createVerticalStrut(14));
        }

        col.add(Box.createVerticalStrut(10));
        principal.setAlignmentX(Component.LEFT_ALIGNMENT);
        principal.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        col.add(principal);
        for (Botao b : secundarios) {
            col.add(Box.createVerticalStrut(8));
            b.setAlignmentX(Component.LEFT_ALIGNMENT);
            b.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
            col.add(b);
        }
        col.setPreferredSize(new Dimension(380, col.getPreferredSize().height));

        JPanel fundo = new JPanel(new GridBagLayout());
        fundo.setBackground(Color.WHITE);
        fundo.add(col);
        return fundo;
    }

    protected JPanel layoutAuth(String titulo, String frase, JPanel form) {
        PainelGradiente lado = new PainelGradiente(
            Tema.PRIMARIA,
            new Color(124, 58, 237)
        );
        lado.setLayout(new GridBagLayout());

        JLabel marca = new JLabel("Loja Virtual");
        marca.setFont(Tema.fonte(Font.BOLD, 20f));
        marca.setForeground(new Color(199, 210, 254));

        JLabel t = new JLabel(
            "<html><div style='width:300px'>" + titulo + "</div></html>"
        );
        t.setFont(Tema.fonte(Font.BOLD, 34f));
        t.setForeground(Color.WHITE);

        JLabel f = new JLabel(
            "<html><div style='width:300px'>" + frase + "</div></html>"
        );
        f.setFont(Tema.fonte(Font.PLAIN, 16f));
        f.setForeground(new Color(224, 231, 255));

        JPanel col = Tema.coluna();
        col.add(Tema.esq(marca));
        col.add(Box.createVerticalStrut(30));
        col.add(Tema.esq(t));
        col.add(Box.createVerticalStrut(14));
        col.add(Tema.esq(f));
        lado.add(col);
        lado.setPreferredSize(new Dimension(440, 0));

        JPanel raiz = new JPanel(new BorderLayout());
        raiz.add(lado, BorderLayout.WEST);
        raiz.add(form, BorderLayout.CENTER);
        return raiz;
    }
}
