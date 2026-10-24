package kyj.poo.regras.modelo;

import java.util.ArrayList;
import java.util.List;

import kyj.poo.regras.pagamento.FormaPagamento;

public class Compra{

    private List<ItemDaCompra> itens;
    private FormaPagamento pagamento;
    private boolean cancelada;

    public Compra(){
        this.itens = new ArrayList<>();
        this.cancelada = false;
    }

    public void cancelar(){
        this.cancelada = true;
    }

    public boolean isCancelada(){
        return this.cancelada;
    }

    public void addItem(ItemDaCompra item) {
        this.itens.add(item);
    }

    public double calcularSubtotal() {

        double subtotal = 0;

        for(ItemDaCompra item : itens){
            subtotal += item.calcularSubtotal();
        }

        return subtotal;
    }

    public double calcularTotal() {

        double subtotal = calcularSubtotal();

        if(pagamento == null){
            return subtotal;
        }

        return pagamento.calcularTotal(subtotal);
    }

    public void setPagamento(FormaPagamento pagamento) {
        this.pagamento = pagamento;
    }

    public FormaPagamento getPagamento(){
        return this.pagamento;
    }

    public List<ItemDaCompra> getItens(){
        return this.itens;
    }
}