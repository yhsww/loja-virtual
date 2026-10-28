package kyj.poo.regras.pagamento;

public class PagamentoComCartaoDeDebito implements FormaPagamento {

    @Override 
    public boolean processar(long valor) {
        return valor > 0;
    }
    @Override
    public long calcularTotal(long subtotal) {
        return subtotal * (90 / 100);
    }

    @Override
    public String getDescricao() {
        return "Cartão de débito (desconto de 10%)";
    }
}
