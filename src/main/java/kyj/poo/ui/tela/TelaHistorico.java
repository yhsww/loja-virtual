package kyj.poo.ui.tela;

import java.time.format.DateTimeFormatter;
import java.util.List;
import kyj.poo.regras.Comprador;
import kyj.poo.regras.compra.Compra;
import kyj.poo.regras.compra.ItemDaCompra;
import kyj.poo.servico.CompraService;
import kyj.poo.ui.Formatador;

public class TelaHistorico {

    private static final DateTimeFormatter formatoData =
        DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final CompraService compraService;

    public TelaHistorico(CompraService compraService) {
        this.compraService = compraService;
    }

    public void executar(Comprador comprador) {
        Formatador.limpar();
        System.out.println(Formatador.titulo("Histórico de compras"));

        List<Compra> compras = compraService.historico(comprador.getNome());

        if (compras.isEmpty()) {
            System.out.println("Nenhuma compra registrada.");
            return;
        }

        for (Compra compra : compras) {
            System.out.println(Formatador.separador());
            System.out.println("Compra: " + compra.getId()
                + (compra.isCancelada() ? " (CANCELADA)" : ""));
            System.out.println("Data: " + compra.getDataHora().format(formatoData));
            System.out.println("Pagamento: " + compra.getPagamento());

            for (ItemDaCompra item : compra.getItens()) {
                System.out.println("  " + item.qtd() + "x "
                    + Formatador.ajustar(item.nome(), 28)
                    + Formatador.ajustar(Formatador.dinheiro(item.precoUnitario()), 14)
                    + Formatador.dinheiro(item.calcularSubtotal()));
            }

            if (compra.getDesconto() > 0) {
                System.out.println("   Subtotal: " + Formatador.dinheiro(compra.getSubtotal()));
                System.out.println("   Desconto: " + Formatador.dinheiro(compra.getDesconto()));
            }

            System.out.println("   Total: " + Formatador.dinheiro(compra.getValorFinal()));
        }

        System.out.println(Formatador.separador());
        System.out.println("Total gasto: "
            + Formatador.dinheiro(compraService.totalGasto(comprador.getNome())));
    }
}
