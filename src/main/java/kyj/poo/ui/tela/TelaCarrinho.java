package kyj.poo.ui.tela;

import java.util.List;
import kyj.poo.regras.Comprador;
import kyj.poo.regras.Produto;
import kyj.poo.regras.carrinho.Carrinho;
import kyj.poo.regras.carrinho.ItemDoCarrinho;
import kyj.poo.regras.excecao.EstoqueInsuficienteException;
import kyj.poo.regras.excecao.ProdutoNaoEncontradoException;
import kyj.poo.regras.excecao.QuantidadeInvalidaException;
import kyj.poo.regras.servico.CompraService;
import kyj.poo.ui.Entrada;
import kyj.poo.ui.Formatador;

public class TelaCarrinho {

    private final CompraService compraService;

    public TelaCarrinho(CompraService compraService) {
        this.compraService = compraService;
    }

    public void executar(Comprador comprador) {
        Carrinho carrinho = comprador.getCarrinho();

        while (true) {
            Formatador.limpar();
            System.out.println(Formatador.titulo("Carrinho de " + comprador.getNome()));

            List<ItemDoCarrinho> itens = carrinho.getItens();

            if (itens.isEmpty()) {
                System.out.println("O carrinho está vazio.");
            } else {
                for (ItemDoCarrinho item : itens) {
                    Produto produto = item.getProduto();

                    System.out.println("  " + item.getQtd() + "x "
                        + Formatador.ajustar(produto.getNome(), 28)
                        + Formatador.ajustar(Formatador.dinheiro(produto.getPreco()), 14)
                        + Formatador.dinheiro(item.calcularSubtotal()));
                }

                System.out.println("   Total: " + Formatador.dinheiro(carrinho.calcularTotal()));
            }

            System.out.println("1) Alterar quantidade de um item");
            System.out.println("2) Remover um item");
            System.out.println("3) Esvaziar carrinho");
            System.out.println("4) Finalizar compra");
            System.out.println("0) Voltar");

            String opcao = Entrada.lerTexto("Opção: ");

            try {
                switch (opcao) {
                    case "1" -> alterarQuantidade(carrinho);
                    case "2" -> removerItem(carrinho);
                    case "3" -> esvaziar(carrinho);
                    case "4" -> new TelaCheckout(compraService).executar(comprador);
                    case "0" -> {
                        return;
                    }
                    default -> System.out.println(Formatador.aviso("Opção inválida."));
                }
            } catch (
                ProdutoNaoEncontradoException
                | EstoqueInsuficienteException
                | QuantidadeInvalidaException e
            ) {
                System.out.println(Formatador.aviso(e.getMessage()));
            }
        }
    }

    private void alterarQuantidade(Carrinho carrinho)
        throws ProdutoNaoEncontradoException, EstoqueInsuficienteException, QuantidadeInvalidaException {
        String id = Entrada.lerTexto("Id do produto: ");
        Produto produto = produtoDoCarrinho(carrinho, id);

        int qtd = Entrada.lerInteiro("Nova quantidade: ");
        carrinho.alterarQuantidade(produto, qtd);

        System.out.println("Quantidade atualizada.");
    }

    private void removerItem(Carrinho carrinho) throws ProdutoNaoEncontradoException {
        String id = Entrada.lerTexto("Id do produto: ");
        Produto produto = produtoDoCarrinho(carrinho, id);
        carrinho.removeProdutos(produto);

        System.out.println("Produto removido do carrinho.");
    }

    private void esvaziar(Carrinho carrinho) {
        carrinho.esvaziarCarrinho();
        System.out.println("Carrinho esvaziado.");
    }

    private Produto produtoDoCarrinho(Carrinho carrinho, String id)
        throws ProdutoNaoEncontradoException {
        for (ItemDoCarrinho item : carrinho.getItens()) {
            if (item.getProduto().getId().equals(id)) {
                return item.getProduto();
            }
        }

        throw new ProdutoNaoEncontradoException(id);
    }
}
