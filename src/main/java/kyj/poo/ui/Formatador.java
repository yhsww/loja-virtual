package kyj.poo.ui;

import java.util.List;
import kyj.poo.regras.Produto;

public class Formatador {

    public static String ajustar(String texto, int tamanho) {
        if (texto.length() > tamanho) {
            return texto.substring(0, tamanho - 3) + "...";
        }

        StringBuilder sb = new StringBuilder(texto);
        while (sb.length() < tamanho) sb.append(' ');

        return sb.toString();
    }

    public static String aviso(String mensagem) {
        return "[!] " + mensagem;
    }

    public static void limpar() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static String dinheiro(long centavos) {
        boolean negativo = centavos < 0;
        long valor = Math.abs(centavos);
        long reais = valor / 100;
        long centavosResto = valor % 100;

        String numero =
            agruparMilhar(reais) + "," + (centavosResto < 10 ? "0" : "") + centavosResto;

        return (negativo ? "-R$ " : "R$ ") + numero;
    }

    public static String separador() {
        return "-".repeat(64);
    }

    public static String tabelaProdutos(List<Produto> produtos) {
        StringBuilder sb = new StringBuilder();
        sb.append(ajustar("ID", 12))
            .append(ajustar("NOME", 28))
            .append(ajustar("PREÇO", 14))
            .append("ESTOQUE")
            .append('\n');
        sb.append(separador()).append('\n');

        for (Produto produto : produtos) {
            sb.append(ajustar(produto.getId(), 12))
                .append(ajustar(produto.getNome(), 28))
                .append(ajustar(dinheiro(produto.getPreco()), 14))
                .append(produto.getEstoque())
                .append('\n');
        }

        return sb.toString();
    }

    public static String titulo(String tela) {
        return "=== " + tela + " ===";
    }

    private static String agruparMilhar(long valor) {
        String texto = Long.toString(valor);
        StringBuilder resultado = new StringBuilder();

        for (int i = 0; i < texto.length(); i++) {
            int resta = texto.length() - i;
            resultado.append(texto.charAt(i));

            if (resta > 1 && (resta - 1) % 3 == 0) {
                resultado.append('.');
            }
        }

        return resultado.toString();
    }
}
