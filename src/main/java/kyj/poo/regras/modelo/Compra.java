package kyj.poo.regras.modelo;

import java.util.ArrayList;
import java.util.List;

import kyj.poo.regras.pagamento.FormaPagamento;

public class Compra {
    private String id;
    private LocalDateTime dataHora;
    private Comprador comprador;
    private List<ItemDoCarrinho> itens;
    private FormaPagamento formaPagamento;
    private double subtotal;
    private double descontoAplicado;
    private double valorFinal;

    // Construtor, getters...
}
