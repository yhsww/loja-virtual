package kyj.poo.regras.excecao;

public class QuantidadeInvalidaException extends Exception {

    public QuantidadeInvalidaException() {
        super("A quantidade solicitada não é válida.");
    }

    public QuantidadeInvalidaException(String mensagem) {
        super(mensagem);
    }
}
