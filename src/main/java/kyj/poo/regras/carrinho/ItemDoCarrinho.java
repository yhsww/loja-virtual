package kyj.poo.regras.carrinho;

import kyj.poo.regras.Produto;

public class ItemDoCarrinho {

    private Produto produto;
    private int qtd;

    public ItemDoCarrinho(Produto produto, int qtd) {
        this.produto = produto;
        this.qtd = qtd;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public long calcularSubtotal() {
        return qtd * produto.getPreco();
    }

    public Produto getProduto() {
        return this.produto;
    }

    public void setQtd(int qtd) {
        this.qtd = qtd;
    }

    public int getQtd() {
        return this.qtd;
    }
}
