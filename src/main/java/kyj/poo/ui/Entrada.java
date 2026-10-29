package kyj.poo.ui;

import java.util.Scanner;

public class Entrada {

    private static final Scanner sc = new Scanner(System.in);

    public static String lerTexto(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String valor = sc.nextLine().trim();

            if (!valor.isEmpty()) return valor;

            System.out.println(Formatador.aviso("Não pode ficar em branco, tente novamente."));
        }
    }

    public static int lerInteiro(String mensagem) {
        while (true) {
            String valor = lerTexto(mensagem);

            try {
                return Integer.parseInt(valor);
            } catch (NumberFormatException e) {
                System.out.println(Formatador.aviso("Isso não é um número inteiro."));
            }
        }
    }

    public static long lerPreco(String mensagem) {
        while (true) {
            String valor = lerTexto(mensagem)
                .replace("R$", "")
                .replace(" ", "")
                .replace(',', '.');

            try {
                long centavos = Math.round(Double.parseDouble(valor) * 100);

                if (centavos >= 0) return centavos;
            } catch (NumberFormatException e) {
                System.out.println(Formatador.aviso("Preço inválido, exemplo: 12,90"));
            }
        }
    }
}
