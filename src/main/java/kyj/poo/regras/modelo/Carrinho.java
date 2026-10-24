package kyj.poo.regras.modelo;

import java.util.ArrayList;
import java.util.List;

public class Carrinho {

    private List<ItemDoCarrinho> itens;

    public Carrinho() {
        this.itens = new ArrayList<>();
    }

    public void addProdutos(Produto produto) {
        ItemDoCarrinho item = new ItemDoCarrinho(produto, 1);
        itens.add(item);
    }

    public void removeProdutos(Produto produto) {
        itens.removeIf(item -> item.getProduto() == produto);
    }

    public long calcularTotal() {
        long total = 0;

        for (ItemDoCarrinho item : itens) {
            total += item.calcularSubtotal();
        }

        return total;
    }

    public void esvaziarCarrinho() {
        this.itens.clear();
    }
}
