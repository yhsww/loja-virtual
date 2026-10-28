package kyj.poo.regras.excecao;

public class ProdutoDuplicadoException extends Exception {

    public ProdutoDuplicadoException(String id) {
        super("Já existe um produto cadastrado com o id: " + id + ".");
    }
}
