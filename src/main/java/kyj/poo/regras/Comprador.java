package kyj.poo.regras;

import java.util.ArrayList;
import java.util.List;

import kyj.poo.regras.carrinho.Carrinho;
import kyj.poo.regras.compra.Compra;

public class Comprador {

    private String nome;
    private String cpf;
    private String email;
    private Carrinho carrinho;
    private List<Compra> compras;

    public Comprador(String nome, String cpf, String email) {
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        this.carrinho = new Carrinho();
        this.compras = new ArrayList<>();
    }

    public void addCompra(Compra compra) {
        this.compras.add(compra);
    }

    public long calcularTotalGasto() {
        long total = 0;

        for (Compra compra : this.compras) {
            if (!compra.isCancelada()) {
                total += compra.getValorFinal();
            }
        }

        return total;
    }

    public List<Compra> consultarHistorico() {
        return compras;
    }

    public void setNome(String nome) {
        if (nome == null || nome.isBlank()) throw new IllegalArgumentException(
            "O nome não pode ser vazio."
        );

        this.nome = nome;
    }

    public String getNome() {
        return this.nome;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getCpf() {
        return this.cpf;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return this.email;
    }

    public void setCarrinho(Carrinho carrinho) {
        this.carrinho = carrinho;
    }

    public Carrinho getCarrinho() {
        return this.carrinho;
    }
}
