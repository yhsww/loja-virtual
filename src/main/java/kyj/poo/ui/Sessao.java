package kyj.poo.ui;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import kyj.poo.regras.Comprador;
import kyj.poo.regras.Produto;

public final class Sessao {

    public static final List<Produto> produtos = new ArrayList<>();
    public static final List<Comprador> compradores = new ArrayList<>();
    public static Comprador logado;
    public static boolean admin;

    public static final String ADMIN_EMAIL = "admin";
    public static final String ADMIN_SENHA = "admin";

    static {
        produtos.add(new Produto("1", "Camiseta", 5990, 10));
        produtos.add(new Produto("2", "Caneca", 2990, 20));
        produtos.add(new Produto("3", "Mochila", 15990, 5));
    }

    private Sessao() {}


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