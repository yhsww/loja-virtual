package kyj.poo.ui.tela;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import kyj.poo.regras.carrinho.Carrinho;
import kyj.poo.regras.carrinho.ItemDoCarrinho;
import kyj.poo.regras.compra.Compra;
import kyj.poo.regras.excecao.EstoqueInsuficienteException;
import kyj.poo.regras.excecao.PagamentoRecusadoException;
import kyj.poo.regras.excecao.PersistenciaException;
import kyj.poo.regras.excecao.QuantidadeInvalidaException;
import kyj.poo.regras.pagamento.FormaPagamento;
import kyj.poo.regras.pagamento.PagamentoComCartaoDeCredito;
import kyj.poo.regras.pagamento.PagamentoComCartaoDeDebito;
import kyj.poo.regras.pagamento.PagamentoComPix;
import kyj.poo.ui.Aplicacao;
import kyj.poo.ui.Botao;
import kyj.poo.ui.Cartao;
import kyj.poo.ui.Sessao;
import kyj.poo.ui.Tela;
import kyj.poo.ui.Tema;

public class TelaCheckout extends Tela {

    private static final String[] NOMES = {
        "Pix",
        "Cartão de crédito",
        "Cartão de débito",
    };
    private static final String[] CURTOS = { "Pix", "Crédito", "Débito" };
    private static final int[] DESCONTOS = { 5, 0, 10 };

    private final Botao[] opcoes = new Botao[3];
    private int escolhida = 0;

    private final JPanel itens = Tema.coluna();
    private final JLabel total = new JLabel("R$ 0,00");
    private final JLabel desconto = Tema.subtitulo("");
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
        rodape.add(
            linha(Tema.subtitulo("Total a pagar"), total),
            BorderLayout.NORTH
        );
        JPanel aviso = Tema.painel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        aviso.add(desconto);
        rodape.add(aviso, BorderLayout.SOUTH);

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
        Botao cancelar = Botao.perigo("Cancelar pedido");
        Botao voltar = Botao.texto("Voltar ao carrinho");
        confirmar.addActionListener(e -> confirmar());
        cancelar.addActionListener(e -> cancelarPedido());
        voltar.addActionListener(e -> app.ir("carrinho"));
        for (Botao b : new Botao[] { confirmar, cancelar, voltar }) {
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
        pag.add(cancelar);
        pag.add(Box.createVerticalStrut(6));
        pag.add(voltar);

        Cartao pagamento = new Cartao(new BorderLayout(), 16, 16);
        pagamento.add(pag, BorderLayout.NORTH);

        JPanel titulo = Tema.coluna();
        titulo.add(Tema.esq(Tema.titulo("Finalizar compra")));
        titulo.add(Box.createVerticalStrut(4));
        titulo.add(
            Tema.esq(Tema.subtitulo("Confira o pedido e escolha como pagar"))
        );
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
            opcoes[k].setVariante(
                k == i ? Botao.Variante.PRIMARIO : Botao.Variante.SECUNDARIO
            );
        }
        rotuloDado.setText(i == 0 ? "CHAVE PIX" : "NÚMERO DO CARTÃO");
        dado.setText("");
        atualizarTotal();
    }

    private FormaPagamento forma() {
        if (escolhida == 0) return new PagamentoComPix();
        if (escolhida == 1) return new PagamentoComCartaoDeCredito();
        return new PagamentoComCartaoDeDebito();
    }

    private void atualizarTotal() {
        if (Sessao.logado == null) return;
        long subtotal = Sessao.logado.getCarrinho().calcularTotal();
        total.setText(Sessao.moeda(forma().calcularTotal(subtotal)));
        desconto.setText(
            DESCONTOS[escolhida] > 0
                ? "Desconto de " + DESCONTOS[escolhida] + "% aplicado"
                : ""
        );
    }

    @Override
    public void aoMostrar() {
        if (Sessao.logado == null) return;
        Carrinho car = Sessao.logado.getCarrinho();

        itens.removeAll();
        for (ItemDoCarrinho it : car.getItens()) {
            JLabel esq = new JLabel(
                it.getQtd() + "x  " + it.getProduto().getNome()
            );
            esq.setFont(Tema.fonte(Font.PLAIN, 14f));
            esq.setForeground(Tema.TEXTO);
            JLabel dir = new JLabel(Sessao.moeda(it.calcularSubtotal()));
            dir.setFont(Tema.fonte(Font.BOLD, 14f));
            dir.setForeground(Tema.TEXTO);

            JPanel l = linha(esq, dir);
            l.setBorder(
                BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 1, 0, Tema.BORDA),
                    BorderFactory.createEmptyBorder(12, 0, 12, 0)
                )
            );
            itens.add(l);
        }
        itens.revalidate();
        itens.repaint();

        selecionar(0);
    }

    private void confirmar() {
        Carrinho car = Sessao.logado.getCarrinho();
        if (car.getItens().isEmpty()) {
            erro("Carrinho vazio.");
            return;
        }
        if (dado.getText().trim().isEmpty()) {
            erro("Preencha os dados do pagamento.");
            return;
        }

        try {
            Compra compra = Sessao.compraService.finalizarCompra(
                Sessao.logado,
                forma()
            );
            info(
                "Pagamento confirmado via " +
                    NOMES[escolhida] +
                    "! Total de " +
                    Sessao.moeda(compra.getValorFinal()) +
                    ". Obrigado pela compra."
            );
            app.ir("loja");
        } catch (EstoqueInsuficienteException ex) {
            erro(ex.getMessage());
        } catch (PagamentoRecusadoException ex) {
            erro("Pagamento recusado.");
        } catch (QuantidadeInvalidaException ex) {
            erro("Carrinho vazio.");
        } catch (PersistenciaException ex) {
            erro("Não foi possível salvar o pedido.");
        }
    }

    private void cancelarPedido() {
        Carrinho car = Sessao.logado.getCarrinho();
        if (car.getItens().isEmpty()) {
            erro("Carrinho vazio.");
            return;
        }

        int r = JOptionPane.showConfirmDialog(
            this,
            "Cancelar este pedido? Ele ficará registrado como cancelado no histórico.",
            "Confirmar cancelamento",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );
        if (r != JOptionPane.YES_OPTION) return;

        try {
            Sessao.compraService.cancelarCompra(Sessao.logado, forma());
            info("Pedido cancelado. Ele foi registrado no seu histórico.");
            app.ir("historico");
        } catch (QuantidadeInvalidaException ex) {
            erro("Carrinho vazio.");
        } catch (PersistenciaException ex) {
            erro("Não foi possível registrar o cancelamento.");
        }
    }
}
