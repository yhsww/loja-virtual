package kyj.poo.ui.tela;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import kyj.poo.regras.carrinho.ItemDoCarrinho;
import kyj.poo.ui.Aplicacao;
import kyj.poo.ui.Botao;
import kyj.poo.ui.Cartao;
import kyj.poo.ui.Sessao;
import kyj.poo.ui.Tela;
import kyj.poo.ui.Tema;

public class TelaCarrinho extends Tela {

    private final DefaultTableModel modelo = new DefaultTableModel(
        new String[] { "PRODUTO", "PREÇO", "QTD", "SUBTOTAL" },
        0
    ) {
        @Override
        public boolean isCellEditable(int r, int c) {
            return false;
        }
    };
    private final JTable tabela = new JTable(modelo);
    private final JLabel vazio = new JLabel(
        "Seu carrinho está vazio.",
        SwingConstants.CENTER
    );
    private final JLabel qtdItens = new JLabel("0");
    private final JLabel total = new JLabel("R$ 0,00");

    public TelaCarrinho(Aplicacao app) {
        super(app);
        setBorder(BorderFactory.createEmptyBorder(24, 32, 24, 32));

        Tema.estilizarTabela(tabela);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        vazio.setFont(Tema.fonte(Font.PLAIN, 15f));
        vazio.setForeground(Tema.TEXTO_SUAVE);
        vazio.setBorder(BorderFactory.createEmptyBorder(16, 0, 16, 0));

        Botao remover = Botao.perigo("Remover item");
        Botao alterar = Botao.secundario("Alterar quantidade");
        remover.addActionListener(e -> remover());
        alterar.addActionListener(e -> alterar());

        JPanel acoes = Tema.painel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        acoes.add(alterar);
        acoes.add(remover);

        JPanel tabelaPainel = Tema.painel(new BorderLayout());
        tabelaPainel.add(
            Tema.rolagem(tabela, Color.WHITE),
            BorderLayout.CENTER
        );
        tabelaPainel.add(vazio, BorderLayout.SOUTH);

        Cartao esquerda = new Cartao(new BorderLayout(0, 12), 16, 8);
        esquerda.add(tabelaPainel, BorderLayout.CENTER);
        esquerda.add(acoes, BorderLayout.SOUTH);
        esquerda.setBorder(
            BorderFactory.createCompoundBorder(
                esquerda.getBorder(),
                BorderFactory.createEmptyBorder(0, 6, 8, 6)
            )
        );

        // resumo
        qtdItens.setFont(Tema.fonte(Font.BOLD, 15f));
        qtdItens.setForeground(Tema.TEXTO);
        total.setFont(Tema.fonte(Font.BOLD, 28f));
        total.setForeground(Tema.PRIMARIA);

        Botao finalizar = Botao.primario("Ir para pagamento");
        Botao continuar = Botao.texto("Continuar comprando");
        finalizar.addActionListener(e -> {
            if (Sessao.logado.getCarrinho().getItens().isEmpty()) erro(
                "Seu carrinho está vazio."
            );
            else app.ir("checkout");
        });
        continuar.addActionListener(e -> app.ir("loja"));
        for (Botao b : new Botao[] { finalizar, continuar }) {
            b.setAlignmentX(Component.LEFT_ALIGNMENT);
            b.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        }

        JPanel resumo = Tema.coluna();
        resumo.add(Tema.esq(Tema.secao("Resumo do pedido")));
        resumo.add(Box.createVerticalStrut(18));
        resumo.add(Tema.esq(linha(Tema.subtitulo("Itens"), qtdItens)));
        resumo.add(Box.createVerticalStrut(14));
        resumo.add(Tema.esq(Tema.rotulo("TOTAL")));
        resumo.add(Box.createVerticalStrut(2));
        resumo.add(Tema.esq(total));
        resumo.add(Box.createVerticalStrut(22));
        resumo.add(finalizar);
        resumo.add(Box.createVerticalStrut(6));
        resumo.add(continuar);

        Cartao direita = new Cartao(new BorderLayout(), 16, 14);
        direita.add(resumo, BorderLayout.NORTH);
        direita.setPreferredSize(new Dimension(340, 0));

        JPanel titulo = Tema.coluna();
        titulo.add(Tema.esq(Tema.titulo("Meu carrinho")));
        titulo.add(Box.createVerticalStrut(4));
        titulo.add(
            Tema.esq(Tema.subtitulo("Revise seus itens antes de pagar"))
        );
        titulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 16, 0));

        JPanel corpo = Tema.painel(new BorderLayout(20, 0));
        corpo.add(esquerda, BorderLayout.CENTER);
        corpo.add(direita, BorderLayout.EAST);

        add(titulo, BorderLayout.NORTH);
        add(corpo, BorderLayout.CENTER);
    }

    @Override
    public void aoMostrar() {
        if (Sessao.logado == null) return;
        modelo.setRowCount(0);
        int qtd = 0;
        for (ItemDoCarrinho it : Sessao.logado.getCarrinho().getItens()) {
            modelo.addRow(new Object[] {
                it.getProduto().getNome(),
                Sessao.moeda(it.getProduto().getPreco()),
                it.getQtd(),
                Sessao.moeda(it.calcularSubtotal()),
            });
            qtd += it.getQtd();
        }
        vazio.setVisible(modelo.getRowCount() == 0);
        qtdItens.setText(String.valueOf(qtd));
        total.setText(
            Sessao.moeda(Sessao.logado.getCarrinho().calcularTotal())
        );
    }

    private ItemDoCarrinho selecionado() {
        int i = tabela.getSelectedRow();
        if (i < 0) {
            erro("Selecione um item.");
            return null;
        }
        List<ItemDoCarrinho> itens = Sessao.logado.getCarrinho().getItens();
        return itens.get(i);
    }

    private void remover() {
        ItemDoCarrinho it = selecionado();
        if (it == null) return;
        try {
            Sessao.logado.getCarrinho().removeProdutos(it.getProduto());
            app.ir("carrinho");
        } catch (Exception ex) {
            erro("Não foi possível remover.");
        }
    }

    private void alterar() {
        ItemDoCarrinho it = selecionado();
        if (it == null) return;
        String r = JOptionPane.showInputDialog(
            this,
            "Nova quantidade:",
            it.getQtd()
        );
        if (r == null) return;
        try {
            Sessao.logado
                .getCarrinho()
                .alterarQuantidade(it.getProduto(), Integer.parseInt(r.trim()));
            app.ir("carrinho");
        } catch (Exception ex) {
            erro("Quantidade inválida ou estoque insuficiente.");
        }
    }
}
