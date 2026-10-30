package kyj.poo.ui.tela;

import java.awt.*;
import javax.swing.*;
import kyj.poo.regras.carrinho.Carrinho;
import kyj.poo.regras.carrinho.ItemDoCarrinho;
import kyj.poo.ui.*;

public class TelaCheckout extends Tela {

    private static final String[] NOMES = { "Pix", "Cartão de crédito", "Cartão de débito" };
    private static final String[] CURTOS = { "Pix", "Crédito", "Débito" };

    private final Botao[] opcoes = new Botao[3];
    private int escolhida = 0;

    private final JPanel itens = Tema.coluna();
    private final JLabel total = new JLabel("R$ 0,00");
    private final JLabel rotuloDado = Tema.rotulo("CHAVE PIX");
    private final JTextField dado = Tema.campo(new JTextField());

    public TelaCheckout(Aplicacao app) {
        super(app);
        setBorder(BorderFactory.createEmptyBorder(24, 32, 24, 32));

        total.setFont(Tema.fonte(Font.BOLD, 26f));
        total.setForeground(Tema.PRIMARIA);

        // ----- resumo (esquerda) -----
        JPanel envolve = Tema.painel(new BorderLayout());
        envolve.add(itens, BorderLayout.NORTH);

        JPanel rodape = Tema.painel(new BorderLayout());
        rodape.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        rodape.add(linha(Tema.subtitulo("Total a pagar"), total), BorderLayout.CENTER);

        Cartao resumo = new Cartao(new BorderLayout(0, 12), 16, 16);
        resumo.add(Tema.secao("Resumo do pedido"), BorderLayout.NORTH);
        resumo.add(Tema.rolagem(envolve, Color.WHITE), BorderLayout.CENTER);
        resumo.add(rodape, BorderLayout.SOUTH);

        JPanel botoesForma = Tema.painel(new GridLayout(1, 3, 8, 0));
        for (int i = 0; i < 3; i++) {
            final int idx = i;
            opcoes[i] = Botao.secundario(CURTOS[i]);
            opcoes[i].addActionListener(e -> selecionar(idx));
            botoesForma.add(opcoes[i]);
        }

        dado.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));

        Botao confirmar = Botao.primario("Confirmar pagamento");
        Botao voltar = Botao.texto("Voltar ao carrinho");
        confirmar.addActionListener(e -> confirmar());
        voltar.addActionListener(e -> app.ir("carrinho"));
        for (Botao b : new Botao[] { confirmar, voltar }) {
            b.setAlignmentX(Component.LEFT_ALIGNMENT);
            b.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        }

        JPanel pag = Tema.coluna();
        pag.add(Tema.esq(Tema.secao("Pagamento")));
        pag.add(Box.createVerticalStrut(6));
        pag.add(Tema.esq(Tema.subtitulo("Escolha a forma de pagamento")));
        pag.add(Box.createVerticalStrut(18));
        pag.add(Tema.esq(botoesForma));
        pag.add(Box.createVerticalStrut(22));
        pag.add(Tema.esq(rotuloDado));
        pag.add(Box.createVerticalStrut(6));
        pag.add(Tema.esq(dado));
        pag.add(Box.createVerticalStrut(26));
        pag.add(confirmar);
        pag.add(Box.createVerticalStrut(6));
        pag.add(voltar);

        Cartao pagamento = new Cartao(new BorderLayout(), 16, 16);
        pagamento.add(pag, BorderLayout.NORTH);

        JPanel titulo = Tema.coluna();
        titulo.add(Tema.esq(Tema.titulo("Finalizar compra")));
        titulo.add(Box.createVerticalStrut(4));
        titulo.add(Tema.esq(Tema.subtitulo("Confira o pedido e escolha como pagar")));
        titulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 16, 0));

        JPanel corpo = Tema.painel(new GridLayout(1, 2, 20, 0));
        corpo.add(resumo);
        corpo.add(pagamento);

        add(titulo, BorderLayout.NORTH);
        add(corpo, BorderLayout.CENTER);

        selecionar(0);
    }

    private void selecionar(int i) {
        escolhida = i;
        for (int k = 0; k < opcoes.length; k++) {
            opcoes[k].setVariante(k == i ? Botao.Variante.PRIMARIO : Botao.Variante.SECUNDARIO);
        }
        rotuloDado.setText(i == 0 ? "CHAVE PIX" : "NÚMERO DO CARTÃO");
        dado.setText("");
    }

    @Override
    public void aoMostrar() {
        if (Sessao.logado == null) return;
        Carrinho car = Sessao.logado.getCarrinho();

        itens.removeAll();
        for (ItemDoCarrinho it : car.getItens()) {
            JLabel esq = new JLabel(it.getQtd() + "x  " + it.getProduto().getNome());
            esq.setFont(Tema.fonte(Font.PLAIN, 14f));
            esq.setForeground(Tema.TEXTO);
            JLabel dir = new JLabel(Sessao.moeda(it.calcularSubtotal()));
            dir.setFont(Tema.fonte(Font.BOLD, 14f));
            dir.setForeground(Tema.TEXTO);

            JPanel l = linha(esq, dir);
            l.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 1, 0, Tema.BORDA),
                    BorderFactory.createEmptyBorder(12, 0, 12, 0)));
            itens.add(l);
        }
        itens.revalidate();
        itens.repaint();

        total.setText(Sessao.moeda(car.calcularTotal()));
        selecionar(0);
    }

    private void confirmar() {
        Carrinho car = Sessao.logado.getCarrinho();
        if (car.getItens().isEmpty()) { erro("Carrinho vazio."); return; }
        if (dado.getText().trim().isEmpty()) { erro("Preencha os dados do pagamento."); return; }

        for (ItemDoCarrinho it : car.getItens()) {
            if (!it.getProduto().verificarEstoque(it.getQtd())) {
                erro("Estoque insuficiente para " + it.getProduto().getNome());
                return;
            }
        }
        try {
            for (ItemDoCarrinho it : car.getItens()) {
                it.getProduto().diminuirEstoque(it.getQtd());
            }
        } catch (Exception ex) {
            erro("Falha ao processar a compra.");
            return;
        }

        car.esvaziarCarrinho();
        info("Pagamento confirmado via " + NOMES[escolhida] + "! Obrigado pela compra.");
        app.ir("loja");
    }
}