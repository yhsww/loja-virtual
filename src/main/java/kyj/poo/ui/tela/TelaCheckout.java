package kyj.poo.ui.tela;

import kyj.poo.regras.Comprador;
import kyj.poo.regras.carrinho.Carrinho;
import kyj.poo.regras.compra.Compra;
import kyj.poo.regras.excecao.EstoqueInsuficienteException;
import kyj.poo.regras.excecao.PagamentoRecusadoException;
import kyj.poo.regras.excecao.PersistenciaException;
import kyj.poo.regras.excecao.QuantidadeInvalidaException;
import kyj.poo.regras.pagamento.FormaPagamento;
import kyj.poo.regras.pagamento.PagamentoComCartaoDeCredito;
import kyj.poo.regras.pagamento.PagamentoComCartaoDeDebito;
import kyj.poo.regras.pagamento.PagamentoComPix;
import kyj.poo.regras.servico.CompraService;
import kyj.poo.ui.Entrada;
import kyj.poo.ui.Formatador;

public class TelaCheckout {

    private final CompraService compraService;

    public TelaCheckout(CompraService compraService) {
        this.compraService = compraService;
    }

    public void executar(Comprador comprador) {
        Carrinho carrinho = comprador.getCarrinho();

        if (carrinho.getItens().isEmpty()) {
            System.out.println(Formatador.aviso("O carrinho está vazio."));
            return;
        }

        Formatador.limpar();
        System.out.println(Formatador.titulo("Checkout"));
        System.out.println();

        long subtotal = carrinho.calcularTotal();
        FormaPagamento formaPagamento = escolherPagamento();
        long total = formaPagamento.calcularTotal(subtotal);

        System.out.println("Subtotal: " + Formatador.dinheiro(subtotal));
        System.out.println("Pagamento: " + formaPagamento.getDescricao());
        System.out.println("Total: " + Formatador.dinheiro(total));

        String confirmar = Entrada.lerTexto("Confirmar compra (s/n): ");

        if (!"s".equalsIgnoreCase(confirmar)) {
            System.out.println("Compra cancelada, o carrinho continua guardado.");
            return;
        }

        try {
            Compra compra = compraService.finalizarCompra(comprador, formaPagamento);
            System.out.println("Compra efetuada! Id: " + compra.getId());
            System.out.println("Total pago: " + Formatador.dinheiro(compra.getValorFinal()));
        } catch (
            QuantidadeInvalidaException
            | PagamentoRecusadoException
            | EstoqueInsuficienteException
            | PersistenciaException e
        ) {
            System.out.println(Formatador.aviso(e.getMessage()));
        }
    }

    private FormaPagamento escolherPagamento() {
        while (true) {
            System.out.println("1) " + new PagamentoComPix().getDescricao());
            System.out.println("2) " + new PagamentoComCartaoDeDebito().getDescricao());
            System.out.println("3) " + new PagamentoComCartaoDeCredito().getDescricao());

            String opcao = Entrada.lerTexto("Forma de pagamento: ");

            switch (opcao) {
                case "1" -> {
                    return new PagamentoComPix();
                }
                case "2" -> {
                    return new PagamentoComCartaoDeDebito();
                }
                case "3" -> {
                    return new PagamentoComCartaoDeCredito();
                }
                default -> System.out.println(Formatador.aviso("Opção inválida."));
            }
        }
    }
}
