package kyj.poo.regras.excecao;

public class ProdutoNaoEncontradoException extends Exception {

    public ProdutoNaoEncontradoException() {
        super("Produto não encontrado.");
    }

    public ProdutoNaoEncontradoException(String id) {
        super("Produto com id: " + id + " não encontrado.");
    }
}
