package kyj.poo.servico;

import java.util.ArrayList;
import java.util.List;
import kyj.poo.persistencia.ProdutoRepositorio;
import kyj.poo.regras.Produto;
import kyj.poo.regras.excecao.PersistenciaException;
import kyj.poo.regras.excecao.ProdutoDuplicadoException;
import kyj.poo.regras.excecao.ProdutoNaoEncontradoException;
import kyj.poo.regras.excecao.QuantidadeInvalidaException;

public class ProdutoService {

    private final ProdutoRepositorio repositorio;
    private final List<Produto> produtos;

    public ProdutoService(ProdutoRepositorio repositorio)
        throws PersistenciaException {
        this.repositorio = repositorio;
        this.produtos = new ArrayList<>(repositorio.carregar());
    }

    public void addProduto(Produto produto)
        throws ProdutoDuplicadoException, PersistenciaException {
        if (produtos.contains(produto)) {
            throw new ProdutoDuplicadoException(produto.getId());
        }

        produtos.add(produto);

        try {
            repositorio.salvar(produtos);
        } catch (PersistenciaException e) {
            produtos.remove(produto);
            throw e;
        }
    }

    public void removerProduto(String id)
        throws ProdutoNaoEncontradoException, PersistenciaException {
        Produto busca = buscarProduto(id);
        int pos = produtos.indexOf(busca);
        produtos.remove(busca);

        try {
            repositorio.salvar(produtos);
        } catch (PersistenciaException e) {
            produtos.set(pos, busca);
            throw e;
        }
    }

    public void editarProduto(String id, String novoNome, long novoPreco)
        throws ProdutoNaoEncontradoException, PersistenciaException {
        editarProduto(id, novoNome, novoPreco, buscarProduto(id).getEstoque());
    }

    public void editarProduto(
        String id,
        String novoNome,
        long novoPreco,
        int novoEstoque
    ) throws ProdutoNaoEncontradoException, PersistenciaException {
        Produto busca = buscarProduto(id);
        String nomeAntes = busca.getNome();
        long precoAntes = busca.getPreco();
        int estoqueAntes = busca.getEstoque();

        busca.setNome(novoNome);
        busca.setPreco(novoPreco);
        busca.setEstoque(novoEstoque);

        try {
            repositorio.salvar(produtos);
        } catch (PersistenciaException e) {
            busca.setNome(nomeAntes);
            busca.setPreco(precoAntes);
            busca.setEstoque(estoqueAntes);
            throw e;
        }
    }

    public void salvar() throws PersistenciaException {
        this.repositorio.salvar(this.produtos);
    }

    public void aumentarEstoque(String id, int qtd)
        throws QuantidadeInvalidaException, ProdutoNaoEncontradoException, PersistenciaException {
        if (qtd <= 0) {
            throw new QuantidadeInvalidaException();
        }

        Produto produto = buscarProduto(id);
        int qtdAntes = produto.getEstoque();
        produto.aumentarEstoque(qtd);

        try {
            repositorio.salvar(produtos);
        } catch (PersistenciaException e) {
            produto.setEstoque(qtdAntes - qtd);
            throw e;
        }
    }

    public List<Produto> listarProdutos() {
        return produtos;
    }

    public Produto buscarProduto(String id)
        throws ProdutoNaoEncontradoException {
        for (Produto produto : produtos) {
            if (produto.getId().equals(id)) {
                return produto;
            }
        }

        throw new ProdutoNaoEncontradoException(id);
    }
}
