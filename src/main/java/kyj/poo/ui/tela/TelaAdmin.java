package kyj.poo.ui.tela;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import kyj.poo.regras.Produto;
import kyj.poo.ui.*;

public class TelaAdmin extends Tela {

    private final DefaultTableModel modelo = new DefaultTableModel(
            new String[] { "ID", "PRODUTO", "PREÇO", "ESTOQUE" }, 0) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable tabela = new JTable(modelo);

    private final JTextField id = Tema.campo(new JTextField());
    private final JTextField nome = Tema.campo(new JTextField());
    private final JTextField preco = Tema.campo(new JTextField());
    private final JTextField estoque = Tema.campo(new JTextField());

    private final JLabel statProdutos = valor();
    private final JLabel statUnidades = valor();
    private final JLabel statValor = valor();

    public TelaAdmin(Aplicacao app) {
        super(app);
        setBorder(BorderFactory.createEmptyBorder(24, 32, 24, 32));

        Tema.estilizarTabela(tabela);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabela.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) preencherForm();
        });

        // cabeçalho + estatísticas
        JPanel header = Tema.coluna();
        header.add(Tema.esq(Tema.titulo("Painel do administrador")));
        header.add(Box.createVerticalStrut(4));
        header.add(Tema.esq(Tema.subtitulo("Gerencie os produtos da loja")));

        JPanel stats = Tema.painel(new GridLayout(1, 3, 16, 0));
        stats.add(stat("Produtos cadastrados", statProdutos));
        stats.add(stat("Unidades em estoque", statUnidades));
        stats.add(stat("Valor em estoque", statValor));

        JPanel topo = Tema.painel(new BorderLayout(0, 16));
        topo.add(header, BorderLayout.NORTH);
        topo.add(stats, BorderLayout.CENTER);

        // tabela
        Cartao cartaoTabela = new Cartao(new BorderLayout(), 16, 6);
        cartaoTabela.add(Tema.rolagem(tabela, Color.WHITE), BorderLayout.CENTER);

        // formulário
        Botao add = Botao.primario("Adicionar");
        Botao salvar = Botao.secundario("Salvar alterações");
        Botao remover = Botao.perigo("Remover");
        Botao limpar = Botao.texto("Limpar");
        add.addActionListener(e -> adicionar());
        salvar.addActionListener(e -> salvar());
        remover.addActionListener(e -> remover());
        limpar.addActionListener(e -> limparForm());

        JPanel campos = Tema.painel(new GridLayout(1, 4, 12, 0));
        campos.add(Tema.campoRotulado("ID", id));
        campos.add(Tema.campoRotulado("Nome", nome));
        campos.add(Tema.campoRotulado("Preço (R$)", preco));
        campos.add(Tema.campoRotulado("Estoque", estoque));

        JPanel botoes = Tema.painel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        botoes.add(add); botoes.add(salvar); botoes.add(remover); botoes.add(limpar);

        Cartao cartaoForm = new Cartao(new BorderLayout(0, 14), 16, 14);
        cartaoForm.add(campos, BorderLayout.CENTER);
        cartaoForm.add(botoes, BorderLayout.SOUTH);

        JPanel centro = Tema.painel(new BorderLayout(0, 14));
        centro.setBorder(BorderFactory.createEmptyBorder(14, 0, 0, 0));
        centro.add(cartaoTabela, BorderLayout.CENTER);
        centro.add(cartaoForm, BorderLayout.SOUTH);

        add(topo, BorderLayout.NORTH);
        add(centro, BorderLayout.CENTER);
    }

    private static JLabel valor() {
        JLabel l = new JLabel("0");
        l.setFont(Tema.fonte(Font.BOLD, 26f));
        l.setForeground(Tema.PRIMARIA);
        return l;
    }

    private Cartao stat(String titulo, JLabel valor) {
        Cartao c = new Cartao(new BorderLayout(0, 4), 16, 12);
        c.add(Tema.rotulo(titulo.toUpperCase()), BorderLayout.NORTH);
        c.add(valor, BorderLayout.CENTER);
        return c;
    }

    @Override
    public void aoMostrar() { recarregar(); }

    private void recarregar() {
        modelo.setRowCount(0);
        long unidades = 0, valorTotal = 0;
        for (Produto p : Sessao.produtos) {
            modelo.addRow(new Object[] {
                    p.getId(), p.getNome(), Sessao.moeda(p.getPreco()), p.getEstoque() });
            unidades += p.getEstoque();
            valorTotal += p.getPreco() * p.getEstoque();
        }
        statProdutos.setText(String.valueOf(Sessao.produtos.size()));
        statUnidades.setText(String.valueOf(unidades));
        statValor.setText(Sessao.moeda(valorTotal));
    }

    private void preencherForm() {
        int i = tabela.getSelectedRow();
        if (i < 0) return;
        Produto p = Sessao.produtos.get(i);
        id.setText(p.getId());
        id.setEnabled(false); // o ID não muda
        nome.setText(p.getNome());
        preco.setText(String.format("%d,%02d", p.getPreco() / 100, p.getPreco() % 100));
        estoque.setText(String.valueOf(p.getEstoque()));
    }

    private void limparForm() {
        tabela.clearSelection();
        id.setEnabled(true);
        id.setText(""); nome.setText(""); preco.setText(""); estoque.setText("");
    }

    private void adicionar() {
        try {
            String i = id.getText().trim();
            if (i.isEmpty()) { erro("Informe o ID."); return; }
            for (Produto p : Sessao.produtos) {
                if (p.getId().equals(i)) { erro("Já existe produto com esse ID."); return; }
            }
            Produto novo = new Produto(i, nome.getText().trim(),
                    Sessao.parseMoeda(preco.getText()), Integer.parseInt(estoque.getText().trim()));
            novo.setNome(novo.getNome());
            novo.setPreco(novo.getPreco());
            novo.setEstoque(novo.getEstoque());
            Sessao.produtos.add(novo);
            recarregar();
            limparForm();
        } catch (Exception ex) {
            erro("Dados inválidos: " + ex.getMessage());
        }
    }

    private void salvar() {
        int i = tabela.getSelectedRow();
        if (i < 0) { erro("Selecione um produto na tabela."); return; }
        try {
            Produto p = Sessao.produtos.get(i);
            p.setNome(nome.getText());
            p.setPreco(Sessao.parseMoeda(preco.getText()));
            p.setEstoque(Integer.parseInt(estoque.getText().trim()));
            recarregar();
            limparForm();
        } catch (Exception ex) {
            erro("Dados inválidos: " + ex.getMessage());
        }
    }

    private void remover() {
        int i = tabela.getSelectedRow();
        if (i < 0) { erro("Selecione um produto na tabela."); return; }
        int r = JOptionPane.showConfirmDialog(this, "Remover este produto?", "Confirmar",
                JOptionPane.YES_NO_OPTION);
        if (r != JOptionPane.YES_OPTION) return;
        Sessao.produtos.remove(i);
        recarregar();
        limparForm();
    }
}