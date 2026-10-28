package kyj.poo.regras.modelo;

import java.util.ArrayList;
import java.util.List;

import kyj.poo.regras.excecao.EstoqueInsuficienteException;
import kyj.poo.regras.excecao.ProdutoNaoEncontradoException;
import kyj.poo.regras.excecao.QuantidadeInvalidaException;

public class Carrinho {

    private final List<ItemDoCarrinho> itens;

    public Carrinho() {
        this.itens = new ArrayList<>();
    }

    public void addProdutos(Produto produto, int qtd)
            throws QuantidadeInvalidaException, EstoqueInsuficienteException {

        if (qtd <= 0) {
            throw new QuantidadeInvalidaException();
        }

        ItemDoCarrinho busca = buscarItem(produto);

        int novaQtd = qtd + (busca == null ? 0 : busca.getQtd());

        if (!produto.verificarEstoque(novaQtd)) {
            throw new EstoqueInsuficienteException();
        }

        if(busca == null){
            itens.add(new ItemDoCarrinho(produto, qtd));
        }else{
            busca.setQtd(novaQtd);
        }
    }

    public void removeProdutos(Produto produto) throws ProdutoNaoEncontradoException {
        ItemDoCarrinho item = buscarItem(produto);
        if(item == null){
            throw new ProdutoNaoEncontradoException();
        }
        itens.remove(item);
    }

    public void alterarQuantidade(Produto produto, int qtd)
            throws QuantidadeInvalidaException, 
            EstoqueInsuficienteException,
            ProdutoNaoEncontradoException {
        
        ItemDoCarrinho item = buscarItem(produto);

        if(item == null){
            throw new ProdutoNaoEncontradoException();
        }

        if(qtd <= 0){
            throw new QuantidadeInvalidaException();
        }

        if(!produto.verificarEstoque(qtd)){
            throw new EstoqueInsuficienteException();
        }

        item.setQtd(qtd);
    }

    public long calcularTotal() {
        long total = 0;
        for (ItemDoCarrinho item : itens) {
            total += item.calcularSubtotal();
        }
        return total;
    }

    public List<ItemDoCarrinho> getItens() {
        return this.itens;
    }

    public void esvaziarCarrinho() {
        this.itens.clear();
    }

    private ItemDoCarrinho buscarItem(Produto produto) {

        for (ItemDoCarrinho item : this.itens) {
            if(item.getProduto().getId().equals(produto.getId())) {
                return item;
            }
        }
        return null;
    }
}

