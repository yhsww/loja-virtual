package kyj.poo.regras.pagamento;

public class PagamentoComCartaoDeDebito implements FormaPagamento {
    @Override
    public double calcularTotal(double subtotal) {
        return subtotal * 0.90;
    }

    @Override
    public String getDescricao() {
        return "Cartão de dédito (desconto de 10%)";
    }
}
