package kyj.poo.regras.pagamento;

public class PagamentoComPix implements FormaPagamento {

    @Override
    public boolean processar(long valor) {
        return valor > 0;
    }

    @Override
    public long calcularTotal(long subtotal) {
        return subtotal * 95 / 100;
    }

    @Override
    public String getDescricao() {
        return "Pix (desconto de 5%)";
    }
}
