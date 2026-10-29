package kyj.poo.ui.tela;

import kyj.poo.regras.Comprador;
import kyj.poo.regras.servico.CompraService;
import kyj.poo.regras.servico.ProdutoService;
import kyj.poo.ui.Entrada;
import kyj.poo.ui.Formatador;

public class TelaComprador {

    private final ProdutoService produtoService;
    private final CompraService compraService;

    public TelaComprador(ProdutoService produtoService, CompraService compraService) {
        this.produtoService = produtoService;
        this.compraService = compraService;
    }

    public void executar() {
        Formatador.limpar();
        System.out.println(Formatador.titulo("Modo comprador"));
        Comprador comprador = fazerLogin();

        while (true) {
            Formatador.limpar();
            System.out.println(Formatador.titulo("Modo comprador"));
            System.out.println("Comprador: " + comprador.getNome());
            System.out.println("1) Loja");
            System.out.println("2) Carrinho");
            System.out.println("3) Histórico de compras");
            System.out.println("0) Voltar");

            String opcao = Entrada.lerTexto("Opção: ");

            switch (opcao) {
                case "1" -> new TelaLoja(produtoService).executar(comprador);
                case "2" -> new TelaCarrinho(compraService).executar(comprador);
                case "3" -> new TelaHistorico(compraService).executar(comprador);
                case "0" -> {
                    return;
                }
                default -> System.out.println(Formatador.aviso("Opção inválida."));
            }
        }
    }

    private Comprador fazerLogin() {
        System.out.println("Informe seus dados:");
        String nome = Entrada.lerTexto("Nome: ");
        String cpf = Entrada.lerTexto("CPF: ");
        String email = Entrada.lerTexto("E-mail: ");

        return new Comprador(nome, cpf, email);
    }
}
