package kyj.poo.ui;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import kyj.poo.persistencia.CompraRepositorioJson;
import kyj.poo.persistencia.CompradorRepositorioJson;
import kyj.poo.persistencia.ProdutoRepositorioJson;
import kyj.poo.regras.Comprador;
import kyj.poo.regras.Produto;
import kyj.poo.regras.excecao.PersistenciaException;
import kyj.poo.servico.CompraService;
import kyj.poo.servico.CompradorService;
import kyj.poo.servico.ProdutoService;

public final class Sessao {

    public static List<Produto> produtos = new ArrayList<>();
    public static List<Comprador> compradores = new ArrayList<>();
    public static Comprador logado;
    public static boolean admin;

    public static ProdutoService produtoService;
    public static CompradorService compradorService;
    public static CompraService compraService;

    public static final String ADMIN_EMAIL = "admin";
    public static final String ADMIN_SENHA = "admin";

    private static final File DIRETORIO_DADOS = new File("dados");

    private Sessao() {}

    public static void inicializar() {
        if (produtoService != null) return;

        try {
            produtoService = new ProdutoService(
                new ProdutoRepositorioJson(
                    new File(DIRETORIO_DADOS, "produtos.json")
                )
            );
            compradorService = new CompradorService(
                new CompradorRepositorioJson(
                    new File(DIRETORIO_DADOS, "compradores.json")
                )
            );
            compraService = new CompraService(
                produtoService,
                new CompraRepositorioJson(
                    new File(DIRETORIO_DADOS, "compras.json")
                )
            );

            produtos = produtoService.listarProdutos();
            if (produtos.isEmpty()) {
                produtos.add(new Produto("1", "Camiseta", 5990, 10));
                produtos.add(new Produto("2", "Caneca", 2990, 20));
                produtos.add(new Produto("3", "Mochila", 15990, 5));
                produtoService.salvar();
            }

            compradores = compradorService.listar();
        } catch (PersistenciaException e) {
            throw new IllegalStateException(
                "Não foi possível carregar os dados da loja.",
                e
            );
        }
    }

    public static String moeda(long centavos) {
        return String.format("R$ %d,%02d", centavos / 100, centavos % 100);
    }

    public static long parseMoeda(String texto) {
        return new BigDecimal(texto.trim().replace(",", "."))
            .movePointRight(2)
            .setScale(0, RoundingMode.HALF_UP)
            .longValue();
    }

    public static void sair() {
        logado = null;
        admin = false;
    }
}
