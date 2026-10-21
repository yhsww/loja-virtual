package kyj.poo.regras.pagamento;

public class PagamentoComCartaoDeCredito implements FormaPagamento {
    @Override
    public double calcularTotal(double subtotal) {
        return subtotal;
    }

    @Override
    public String getDescricao() {
        return "Cartão de crédito (pagamento do valor integral)";
    }
}
