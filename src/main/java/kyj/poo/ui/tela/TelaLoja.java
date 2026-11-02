package kyj.poo.ui.tela;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import kyj.poo.regras.Produto;
import kyj.poo.ui.Aplicacao;
import kyj.poo.ui.Botao;
import kyj.poo.ui.Carrossel;
import kyj.poo.ui.Cartao;
import kyj.poo.ui.Sessao;
import kyj.poo.ui.Tela;
import kyj.poo.ui.Tema;

public class TelaLoja extends Tela {

    private final JPanel grade = Tema.painel(new GridLayout(0, 3, 18, 18));

    public TelaLoja(Aplicacao app) {
        super(app);

        Carrossel carrossel = new Carrossel(
            new Carrossel.Slide(
                "Bem-vindo à Loja Virtual",
                "Os melhores produtos com entrega rápida",
                new Color(79, 70, 229),
                new Color(147, 51, 234)
            ),
            new Carrossel.Slide(
                "Ofertas da semana",
                "Aproveite descontos em produtos selecionados",
                new Color(225, 29, 72),
                new Color(249, 115, 22)
            ),
            new Carrossel.Slide(
                "Frete grátis",
                "Em compras acima de R$ 200,00",
                new Color(5, 150, 105),
                new Color(13, 148, 136)
            )
        );

        JPanel topoGrade = Tema.painel(new BorderLayout());
        topoGrade.add(grade, BorderLayout.NORTH);

        JPanel secao = Tema.painel(new BorderLayout(0, 14));
        secao.add(Tema.secao("Produtos em destaque"), BorderLayout.NORTH);
        secao.add(topoGrade, BorderLayout.CENTER);

        Tema.Rolavel conteudo = new Tema.Rolavel(new BorderLayout(0, 26));
        conteudo.setBorder(BorderFactory.createEmptyBorder(24, 32, 32, 32));
        conteudo.add(carrossel, BorderLayout.NORTH);
        conteudo.add(secao, BorderLayout.CENTER);

        add(Tema.rolagem(conteudo, Tema.FUNDO), BorderLayout.CENTER);
    }

    @Override
    public void aoMostrar() {
        grade.removeAll();
        for (Produto p : Sessao.produtos) grade.add(card(p));
        grade.revalidate();
        grade.repaint();
    }

    private Cartao card(Produto p) {
        Cartao c = new Cartao(new BorderLayout(0, 12), 16, 14);
        c.add(new ImagemProduto(p.getNome()), BorderLayout.NORTH);

        JLabel nome = new JLabel(p.getNome());
        nome.setFont(Tema.fonte(Font.BOLD, 16f));
        nome.setForeground(Tema.TEXTO);

        JLabel preco = new JLabel(Sessao.moeda(p.getPreco()));
        preco.setFont(Tema.fonte(Font.BOLD, 22f));
        preco.setForeground(Tema.PRIMARIA);

        JLabel estoque = new JLabel();
        estoque.setFont(Tema.fonte(Font.PLAIN, 13f));
        if (p.getEstoque() == 0) {
            estoque.setText("Esgotado");
            estoque.setForeground(Tema.PERIGO);
        } else if (p.getEstoque() <= 3) {
            estoque.setText("Últimas " + p.getEstoque() + " unidades");
            estoque.setForeground(new Color(217, 119, 6));
        } else {
            estoque.setText(p.getEstoque() + " em estoque");
            estoque.setForeground(Tema.SUCESSO);
        }

        JPanel info = Tema.coluna();
        info.add(Tema.esq(nome));
        info.add(Box.createVerticalStrut(4));
        info.add(Tema.esq(preco));
        info.add(Box.createVerticalStrut(2));
        info.add(Tema.esq(estoque));
        c.add(info, BorderLayout.CENTER);

        Botao add = Botao.primario(
            p.getEstoque() == 0 ? "Esgotado" : "Adicionar ao carrinho"
        );
        add.setEnabled(p.getEstoque() > 0);
        add.addActionListener(e -> {
            if (Sessao.logado == null) {
                erro("Faça login para comprar.");
                app.ir("login");
                return;
            }
            try {
                Sessao.logado.getCarrinho().addProdutos(p, 1);
                app.ir("loja"); // atualiza o contador do carrinho
                info(p.getNome() + " adicionado ao carrinho.");
            } catch (Exception ex) {
                erro("Não foi possível adicionar (estoque insuficiente?).");
            }
        });
        c.add(add, BorderLayout.SOUTH);
        return c;
    }

    private static class ImagemProduto extends JComponent {

        private final String nome;

        ImagemProduto(String nome) {
            this.nome = nome;
            setPreferredSize(new Dimension(0, 130));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
            );
            int w = getWidth(),
                h = getHeight();

            float matiz = (Math.abs(nome.hashCode()) % 360) / 360f;
            Color a = Color.getHSBColor(matiz, 0.45f, 0.95f);
            Color b = Color.getHSBColor((matiz + 0.08f) % 1f, 0.65f, 0.80f);
            g2.setPaint(new GradientPaint(0, 0, a, w, h, b));
            g2.fillRoundRect(0, 0, w, h, 14, 14);

            int d = 64;
            g2.setColor(new Color(255, 255, 255, 80));
            g2.fillOval((w - d) / 2, (h - d) / 2, d, d);

            String inicial = nome.isEmpty()
                ? "?"
                : nome.substring(0, 1).toUpperCase();
            g2.setColor(Color.WHITE);
            g2.setFont(Tema.fonte(Font.BOLD, 30f));
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(
                inicial,
                (w - fm.stringWidth(inicial)) / 2,
                (h + fm.getAscent() - fm.getDescent()) / 2
            );
            g2.dispose();
        }
    }
}
