package kyj.poo.regras.pagamento;

public class PagamentoComPix implements FormaPagamento {
    @Override
    public double calcularTotal(double subtotal) {
        return subtotal * 0.95;
    }

    @Override
    public String getDescricao() {
        return "Pix (desconto de 5%)";
    }
}
