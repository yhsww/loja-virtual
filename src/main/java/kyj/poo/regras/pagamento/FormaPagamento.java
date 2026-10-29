package kyj.poo.regras.pagamento;

public interface FormaPagamento {
    long calcularTotal(long subtotal);

    String getDescricao();

    boolean processar(long valor);
}
