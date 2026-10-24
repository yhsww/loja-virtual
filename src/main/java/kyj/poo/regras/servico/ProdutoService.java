package kyj.poo.regras.servico;

import java.util.ArrayList;
import java.util.List;

import kyj.poo.regras.excecao.ProdutoDuplicadoException;
import kyj.poo.regras.excecao.ProdutoNaoEncontradoException;
import kyj.poo.regras.excecao.QuantidadeInvalidaException;
import kyj.poo.regras.modelo.Produto;

public class ProdutoService {

    private List<Produto> produtos;

    public ProdutoService(){
        produtos = new ArrayList<>();
    }

    public void addProduto(Produto produto) throws ProdutoDuplicadoException{

            if(produtos.contains(produto)){
                throw new ProdutoDuplicadoException();
            }

        produtos.add(produto);
    }

    public void removerProduto(String id) throws ProdutoNaoEncontradoException{
        
        Produto busca = buscarProduto(id);
        produtos.remove(busca);

    }

    public void aumentarEstoque(String id, int qtd) throws QuantidadeInvalidaException, ProdutoNaoEncontradoException{

        if(qtd <= 0){
            throw new QuantidadeInvalidaException();
        }

        Produto produto = buscarProduto(id);
        produto.aumentarEstoque(qtd);
    }

    public List<Produto> listarProdutos(){
        return produtos;
    }

    public Produto buscarProduto(String id) throws ProdutoNaoEncontradoException{

        for(Produto produto: produtos){
            if(produto.getId().equals(id)){
                return produto;
            }
        }

        throw new ProdutoNaoEncontradoException();
    }
    
}
