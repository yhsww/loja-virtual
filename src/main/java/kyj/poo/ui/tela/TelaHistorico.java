package kyj.poo.ui.tela;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import kyj.poo.regras.compra.Compra;
import kyj.poo.regras.compra.ItemDaCompra;
import kyj.poo.ui.Aplicacao;
import kyj.poo.ui.Cartao;
import kyj.poo.ui.Sessao;
import kyj.poo.ui.Tela;
import kyj.poo.ui.Tema;

public class TelaHistorico extends Tela {

    private static final DateTimeFormatter DATA_HORA =
        DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final DateTimeFormatter DATA = DateTimeFormatter.ofPattern(
        "dd/MM/yyyy"
    );

    private final DefaultTableModel modelo = new DefaultTableModel(
        new String[] { "DATA", "PEDIDO", "VALOR", "STATUS" },
        0
    ) {
        @Override
        public boolean isCellEditable(int r, int c) {
            return false;
        }
    };
    private final JTable tabela = new JTable(modelo);
    private final JLabel vazio = new JLabel(
        "Você ainda não realizou nenhuma compra.",
        SwingConstants.CENTER
    );

    private final JLabel detId = texto();
    private final JLabel detData = texto();
    private final JLabel detPagamento = texto();
    private final JLabel detStatus = texto();
    private final JLabel detSubtotal = texto();
    private final JLabel detDesconto = texto();
    private final JLabel detTotal = texto();

    private final JPanel detItens = Tema.coluna();

    private List<Compra> compras = new ArrayList<>();

    public TelaHistorico(Aplicacao app) {
        super(app);
        setBorder(BorderFactory.createEmptyBorder(24, 32, 24, 32));

        Tema.estilizarTabela(tabela);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabela.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) mostrarDetalhes();
        });

        int[] larguras = { 110, 110, 100, 110 };
        for (int i = 0; i < larguras.length; i++) {
            tabela.getColumnModel().getColumn(i).setPreferredWidth(larguras[i]);
        }

        tabela.getColumnModel().getColumn(3).setCellRenderer(
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
                        sel,
                        false,
                        r,
                        c
                    );
                    setBorder(BorderFactory.createEmptyBorder(0, 14, 0, 14));
                    setHorizontalAlignment(SwingConstants.LEFT);
                    if (!sel) setForeground(
                        "Cancelado".equals(v) ? Tema.PERIGO : Tema.SUCESSO
                    );
                    return this;
                }
            }
        );

        vazio.setFont(Tema.fonte(Font.PLAIN, 15f));
        vazio.setForeground(Tema.TEXTO_SUAVE);
        vazio.setBorder(BorderFactory.createEmptyBorder(16, 0, 16, 0));

        JPanel header = Tema.coluna();
        header.add(Tema.esq(Tema.titulo("Meu histórico")));
        header.add(Box.createVerticalStrut(4));
        header.add(Tema.esq(Tema.subtitulo("Acompanhe seus pedidos")));

        JPanel tabelaPainel = Tema.painel(new BorderLayout());
        tabelaPainel.add(
            Tema.rolagem(tabela, Color.WHITE),
            BorderLayout.CENTER
        );
        tabelaPainel.add(vazio, BorderLayout.SOUTH);

        Cartao esquerda = new Cartao(new BorderLayout(0, 12), 16, 8);
        esquerda.add(tabelaPainel, BorderLayout.CENTER);
        esquerda.setBorder(
            BorderFactory.createCompoundBorder(
                esquerda.getBorder(),
                BorderFactory.createEmptyBorder(0, 6, 8, 6)
            )
        );

        JPanel campos = Tema.coluna();
        campos.add(Tema.esq(linhaCampo("PEDIDO", detId)));
        campos.add(Box.createVerticalStrut(8));
        campos.add(Tema.esq(linhaCampo("DATA", detData)));
        campos.add(Box.createVerticalStrut(8));
        campos.add(Tema.esq(linhaCampo("PAGAMENTO", detPagamento)));
        campos.add(Box.createVerticalStrut(8));
        campos.add(Tema.esq(linhaCampo("STATUS", detStatus)));

        detItens.setAlignmentX(Component.LEFT_ALIGNMENT);
        Tema.Rolavel rolavel = new Tema.Rolavel(new BorderLayout());
        rolavel.setBackground(Color.WHITE);
        rolavel.add(detItens, BorderLayout.CENTER);

        JPanel lista = Tema.painel(new BorderLayout(0, 6));
        lista.add(Tema.rotulo("ITENS"), BorderLayout.NORTH);
        lista.add(Tema.rolagem(rolavel, Color.WHITE), BorderLayout.CENTER);

        detTotal.setFont(Tema.fonte(Font.BOLD, 20f));
        detTotal.setForeground(Tema.PRIMARIA);

        JPanel totais = Tema.coluna();
        totais.add(
            Tema.esq(linhaValor(Tema.subtitulo("Subtotal"), detSubtotal))
        );
        totais.add(Box.createVerticalStrut(4));
        totais.add(
            Tema.esq(linhaValor(Tema.subtitulo("Desconto"), detDesconto))
        );
        totais.add(Box.createVerticalStrut(4));
        totais.add(Tema.esq(linhaValor(Tema.rotulo("TOTAL"), detTotal)));
        totais.add(Box.createVerticalStrut(16));

        Cartao direita = new Cartao(new BorderLayout(0, 14), 16, 14);
        direita.add(campos, BorderLayout.NORTH);
        direita.add(lista, BorderLayout.CENTER);
        direita.add(totais, BorderLayout.SOUTH);
        direita.setPreferredSize(new Dimension(340, 0));

        JPanel corpo = Tema.painel(new BorderLayout(20, 0));
        corpo.add(esquerda, BorderLayout.CENTER);
        corpo.add(direita, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);
        add(corpo, BorderLayout.CENTER);
    }

    private static JLabel texto() {
        JLabel l = new JLabel("—");
        l.setFont(Tema.fonte(Font.BOLD, 14f));
        l.setForeground(Tema.TEXTO);
        return l;
    }

    private static JPanel linhaCampo(String titulo, JLabel valor) {
        JPanel p = Tema.painel(new BorderLayout());
        p.add(Tema.rotulo(titulo), BorderLayout.WEST);
        p.add(valor, BorderLayout.EAST);
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 22));
        return p;
    }

    private static JPanel linhaValor(JComponent titulo, JLabel valor) {
        JPanel p = Tema.painel(new BorderLayout());
        p.add(titulo, BorderLayout.WEST);
        p.add(valor, BorderLayout.EAST);
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 26));
        return p;
    }

    @Override
    public void aoMostrar() {
        if (Sessao.logado == null) return;

        compras = new ArrayList<>(
            Sessao.compraService.historico(Sessao.logado.getNome())
        );
        Collections.reverse(compras);

        modelo.setRowCount(0);
        for (Compra c : compras) {
            modelo.addRow(new Object[] {
                c.getDataHora().format(DATA),
                c.getId().substring(0, 8).toUpperCase(),
                Sessao.moeda(c.getValorFinal()),
                c.isCancelada() ? "Cancelado" : "Concluído",
            });
        }
        vazio.setVisible(modelo.getRowCount() == 0);

        tabela.clearSelection();
        if (modelo.getRowCount() > 0) tabela.setRowSelectionInterval(0, 0);
        mostrarDetalhes();
    }

    private void mostrarDetalhes() {
        Compra c = selecionada();
        detItens.removeAll();

        if (c == null) {
            detId.setText("—");
            detId.setToolTipText(null);
            detData.setText("—");
            detPagamento.setText("—");
            detPagamento.setToolTipText(null);
            detStatus.setText("—");
            detStatus.setForeground(Tema.TEXTO);
            detSubtotal.setText("—");
            detDesconto.setText("—");
            detTotal.setText("—");
        } else {
            detId.setText(c.getId().substring(0, 8).toUpperCase());
            detId.setToolTipText(c.getId());
            detData.setText(c.getDataHora().format(DATA_HORA));
            detPagamento.setText(formaCurta(c.getPagamento()));
            detPagamento.setToolTipText(c.getPagamento());
            detStatus.setText(c.isCancelada() ? "Cancelado" : "Concluído");
            detStatus.setForeground(
                c.isCancelada() ? Tema.PERIGO : Tema.SUCESSO
            );
            detSubtotal.setText(Sessao.moeda(c.getSubtotal()));
            detDesconto.setText(
                c.getDesconto() > 0
                    ? "- " + Sessao.moeda(c.getDesconto())
                    : Sessao.moeda(0)
            );
            detTotal.setText(Sessao.moeda(c.getValorFinal()));

            for (ItemDaCompra it : c.getItens()) {
                JLabel esq = new JLabel(it.qtd() + "x  " + it.nome());
                esq.setFont(Tema.fonte(Font.PLAIN, 14f));
                esq.setForeground(Tema.TEXTO);

                JLabel dir = new JLabel(Sessao.moeda(it.calcularSubtotal()));
                dir.setFont(Tema.fonte(Font.BOLD, 14f));
                dir.setForeground(Tema.TEXTO);

                JPanel l = linha(esq, dir);
                l.setBorder(
                    BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 1, 0, Tema.BORDA),
                        BorderFactory.createEmptyBorder(10, 0, 10, 0)
                    )
                );
                l.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
                detItens.add(l);
            }
        }

        detItens.revalidate();
        detItens.repaint();
    }

    private Compra selecionada() {
        int i = tabela.getSelectedRow();
        if (i < 0 || i >= compras.size()) return null;
        return compras.get(i);
    }

    private static String formaCurta(String descricao) {
        if (descricao == null) return "—";
        String d = descricao.toLowerCase();
        if (d.startsWith("pix")) return "Pix";
        if (d.contains("crédito")) return "Crédito";
        if (d.contains("débito")) return "Débito";
        int i = descricao.indexOf(" (");
        return i > 0 ? descricao.substring(0, i) : descricao;
    }
}
