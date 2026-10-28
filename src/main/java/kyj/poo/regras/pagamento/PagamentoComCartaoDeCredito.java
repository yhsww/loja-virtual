package kyj.poo.regras.pagamento;

public class PagamentoComCartaoDeCredito implements FormaPagamento {

    @Override 
    public boolean processar(long valor) {
        return valor > 0;
    }

    @Override
    public long calcularTotal(long subtotal) {
        return subtotal;
    }

    @Override
    public String getDescricao() {
        return "Cartão de crédito (pagamento do valor integral)";
    }
}
