package kyj.poo.ui;

import java.io.File;
import kyj.poo.persistencia.CompraRepositorioJson;
import kyj.poo.persistencia.ProdutoRepositorioJson;
import kyj.poo.regras.excecao.PersistenciaException;
import kyj.poo.regras.servico.CompraService;
import kyj.poo.regras.servico.ProdutoService;
import kyj.poo.ui.tela.TelaAdmin;
import kyj.poo.ui.tela.TelaComprador;

public class Aplicacao {

    public static void rodar() {
        ProdutoService produtoService;
        CompraService compraService;

        try {
            produtoService = new ProdutoService(
                new ProdutoRepositorioJson(new File("produtos.json"))
            );
            compraService = new CompraService(
                produtoService,
                new CompraRepositorioJson(new File("compras.json"))
            );
        } catch (PersistenciaException e) {
            System.out.println(Formatador.aviso("Falha ao iniciar a loja: " + e.getMessage()));
            return;
        }

        while (true) {
            Formatador.limpar();
            System.out.println(Formatador.titulo("Loja Virtual"));
            System.out.println();
            System.out.println("1) Modo comprador");
            System.out.println("2) Modo administrador");
            System.out.println("0) Sair");

            String opcao = Entrada.lerTexto("Opção: ");

            switch (opcao) {
                case "1" -> new TelaComprador(produtoService, compraService).executar();
                case "2" -> new TelaAdmin(produtoService).executar();
                case "0" -> {
                    System.out.println("Até logo!");
                    return;
                }
                default -> System.out.println(Formatador.aviso("Opção inválida."));
            }
        }
    }
}
