package kyj.poo.ui.tela;

import java.util.List;
import kyj.poo.regras.Produto;
import kyj.poo.regras.excecao.PersistenciaException;
import kyj.poo.regras.excecao.ProdutoDuplicadoException;
import kyj.poo.regras.excecao.ProdutoNaoEncontradoException;
import kyj.poo.regras.excecao.QuantidadeInvalidaException;
import kyj.poo.servico.ProdutoService;
import kyj.poo.ui.Entrada;
import kyj.poo.ui.Formatador;

public class TelaAdmin {

    private final ProdutoService produtoService;

    public TelaAdmin(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    public void executar() {
        while (true) {
            Formatador.limpar();
            System.out.println(Formatador.titulo("Modo administrador"));
            System.out.println();
            System.out.println("1) Cadastrar produto");
            System.out.println("2) Editar produto");
            System.out.println("3) Remover produto");
            System.out.println("4) Repor estoque");
            System.out.println("5) Listar produtos");
            System.out.println("0) Voltar");

            String opcao = Entrada.lerTexto("Opção: ");

            try {
                switch (opcao) {
                    case "1" -> cadastrar();
                    case "2" -> editar();
                    case "3" -> remover();
                    case "4" -> reporEstoque();
                    case "5" -> listar();
                    case "0" -> {
                        return;
                    }
                    default -> System.out.println(Formatador.aviso("Opção inválida."));
                }
            } catch (
                ProdutoDuplicadoException
                | ProdutoNaoEncontradoException
                | QuantidadeInvalidaException
                | PersistenciaException
                | IllegalArgumentException e
            ) {
                System.out.println(Formatador.aviso(e.getMessage()));
            }
        }
    }

    private void cadastrar() throws ProdutoDuplicadoException, PersistenciaException {
        System.out.println(Formatador.titulo("Cadastrar produto"));

        String id = Entrada.lerTexto("Id: ");
        String nome = Entrada.lerTexto("Nome: ");
        long preco = Entrada.lerPreco("Preço: ");
        int estoque = Entrada.lerInteiro("Estoque: ");

        produtoService.addProduto(new Produto(id, nome, preco, estoque));
        System.out.println("Produto cadastrado.");
    }

    private void editar() throws ProdutoNaoEncontradoException, PersistenciaException {
        System.out.println(Formatador.titulo("Editar produto"));

        String id = Entrada.lerTexto("Id do produto: ");
        String novoNome = Entrada.lerTexto("Novo nome: ");
        long novoPreco = Entrada.lerPreco("Novo preço: ");

        produtoService.editarProduto(id, novoNome, novoPreco);
        System.out.println("Produto editado.");
    }

    private void remover() throws ProdutoNaoEncontradoException, PersistenciaException {
        String id = Entrada.lerTexto("Id do produto a remover: ");

        produtoService.removerProduto(id);
        System.out.println("Produto removido.");
    }

    private void reporEstoque() throws QuantidadeInvalidaException, ProdutoNaoEncontradoException, PersistenciaException {
        String id = Entrada.lerTexto("Id do produto: ");
        int qtd = Entrada.lerInteiro("Quantidade a adicionar: ");

        produtoService.aumentarEstoque(id, qtd);
        System.out.println("Estoque atualizado.");
    }

    private void listar() {
        System.out.println(Formatador.titulo("Produtos cadastrados"));
        List<Produto> produtos = produtoService.listarProdutos();

        if (produtos.isEmpty()) {
            System.out.println("Nenhum produto cadastrado.");
        } else {
            System.out.println(Formatador.tabelaProdutos(produtos));
        }
    }
}
