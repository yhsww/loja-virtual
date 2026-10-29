package kyj.poo.regras.excecao;

public class PagamentoRecusadoException extends Exception {

    public PagamentoRecusadoException() {
        super("Pagamento recusado. A compra não foi concluída.");
    }
}
