package kyj.poo.regras.compra;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import kyj.poo.regras.pagamento.FormaPagamento;

public class Compra {

    private String compradorNome;
    private String compradorCpf;
    private String compradorEmail;
    private String id;
    private String dataHora;
    private List<ItemDaCompra> itens;
    private String pagamento;
    private boolean cancelada;
    private long subtotal;
    private long desconto;
    private long valorFinal;

    public Compra(String compradorNome, String compradorCpf, String compradorEmail) {
        this.id = UUID.randomUUID().toString();
        this.dataHora = LocalDateTime.now().toString();
        this.compradorNome = compradorNome;
        this.compradorCpf = compradorCpf;
        this.compradorEmail = compradorEmail;
        this.itens = new ArrayList<>();
        this.cancelada = false;
    }

    public Compra() {}

    public void cancelar() {
        this.cancelada = true;
    }

    public boolean isCancelada() {
        return this.cancelada;
    }

    public void addItem(ItemDaCompra item) {
        this.itens.add(item);
    }

    public long calcularSubtotal() {
        long subtotal = 0;

        for (ItemDaCompra item : itens) {
            subtotal += item.calcularSubtotal();
        }

        return subtotal;
    }

    public void fechar(FormaPagamento formaPagamento) {
        this.subtotal = calcularSubtotal();
        this.valorFinal = formaPagamento.calcularTotal(this.subtotal);
        this.desconto = this.subtotal - this.valorFinal;
        this.pagamento = formaPagamento.getDescricao();
    }

    public long calcularTotal() {
        return this.valorFinal;
    }

    public void setPagamento(String pagamento) {
        this.pagamento = pagamento;
    }

    public String getId() {
        return this.id;
    }

    public String getCompradorNome() {
        return this.compradorNome;
    }

    public String getCompradorCpf() {
    return this.compradorCpf;
    }

    public String getCompradorEmail() {
        return this.compradorEmail;
    }

    public LocalDateTime getDataHora() {
        return LocalDateTime.parse(dataHora);
    }

    public String getPagamento() {
        return this.pagamento;
    }

    public List<ItemDaCompra> getItens() {
        return this.itens;
    }

    public long getSubtotal() {
        return this.subtotal;
    }

    public long getDesconto() {
        return this.desconto;
    }

    public long getValorFinal() {
        return this.valorFinal;
    }
}
