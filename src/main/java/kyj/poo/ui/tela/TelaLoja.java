package kyj.poo.ui.tela;

import java.util.List;
import kyj.poo.regras.Comprador;
import kyj.poo.regras.Produto;
import kyj.poo.regras.carrinho.Carrinho;
import kyj.poo.regras.excecao.EstoqueInsuficienteException;
import kyj.poo.regras.excecao.ProdutoNaoEncontradoException;
import kyj.poo.regras.excecao.QuantidadeInvalidaException;
import kyj.poo.regras.servico.ProdutoService;
import kyj.poo.ui.Entrada;
import kyj.poo.ui.Formatador;

public class TelaLoja {

    private final ProdutoService produtoService;

    public TelaLoja(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    public void executar(Comprador comprador) {
        Carrinho carrinho = comprador.getCarrinho();

        while (true) {
            Formatador.limpar();
            System.out.println(Formatador.titulo("Loja"));

            List<Produto> produtos = produtoService.listarProdutos();

            if (produtos.isEmpty()) {
                System.out.println("Nenhum produto cadastrado.");
            } else {
                System.out.println(Formatador.tabelaProdutos(produtos));
            }

            System.out.println("1) Adicionar produto ao carrinho");
            System.out.println("0) Voltar");

            String opcao = Entrada.lerTexto("Opção: ");

            try {
                switch (opcao) {
                    case "1" -> adicionarAoCarrinho(carrinho);
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

    private void adicionarAoCarrinho(Carrinho carrinho)
        throws ProdutoNaoEncontradoException, EstoqueInsuficienteException, QuantidadeInvalidaException {
        String id = Entrada.lerTexto("Id do produto: ");
        Produto produto = produtoService.buscarProduto(id);

        System.out.println("Produto: " + produto.getNome()
            + " | " + Formatador.dinheiro(produto.getPreco())
            + " | estoque: " + produto.getEstoque());

        int qtd = Entrada.lerInteiro("Quantidade: ");
        carrinho.addProdutos(produto, qtd);

        System.out.println("Produto adicionado ao carrinho.");
    }
}
