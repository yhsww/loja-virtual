package kyj.poo.regras.pagamento;

public interface FormaPagamento {
    double calcularTotal(double subtotal);
    String getDescricao();
}
