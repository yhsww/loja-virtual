package kyj.poo.regras.compra;

import kyj.poo.regras.Produto;
import kyj.poo.regras.carrinho.ItemDoCarrinho;

public record ItemDaCompra(
    String id,
    String nome,
    int qtd,
    long precoUnitario
) {
    public ItemDaCompra {
        if (qtd <= 0) throw new IllegalArgumentException(
            "A quantidade de itens deve ser maior que zero."
        );

        if (precoUnitario < 0) throw new IllegalArgumentException(
            "O preço do item deve ser positivo."
        );
    }

    public static ItemDaCompra de(ItemDoCarrinho item) {
        Produto produto = item.getProduto();
        return new ItemDaCompra(
            produto.getId(),
            produto.getNome(),
            produto.getEstoque(),
            produto.getPreco()
        );
    }

    public long calcularSubtotal() {
        return this.qtd * this.precoUnitario;
    }
}
