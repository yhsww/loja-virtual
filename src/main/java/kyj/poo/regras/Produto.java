package kyj.poo.regras;

import kyj.poo.regras.excecao.EstoqueInsuficienteException;
import kyj.poo.regras.excecao.QuantidadeInvalidaException;

public class Produto {

    private String id;
    private String nome;
    private long preco;
    private int estoque;

    public Produto(String id, String nome, long preco, int estoque) {
        this.id = id;
        this.nome = nome;
        this.preco = preco;
        this.estoque = estoque;
    }

    public void aumentarEstoque(int qtd) throws QuantidadeInvalidaException {
        if (qtd <= 0) throw new QuantidadeInvalidaException();
        this.estoque += qtd;
    }

    public void diminuirEstoque(int qtd)
        throws QuantidadeInvalidaException, EstoqueInsuficienteException {
        if (qtd <= 0) throw new QuantidadeInvalidaException();

        if (!verificarEstoque(qtd)) throw new EstoqueInsuficienteException();

        this.estoque -= qtd;
    }

    public boolean verificarEstoque(int qtd) {
        return this.estoque >= qtd;
    }

    public void setId(String id) {
        if (id == null || id.isBlank()) throw new IllegalArgumentException(
            "O id não pode ser vazio."
        );

        this.id = id.trim();
    }

    public String getId() {
        return this.id;
    }

    public void setNome(String nome) {
        if (nome == null || nome.isBlank()) throw new IllegalArgumentException(
            "O nome não pode ser vazio."
        );

        this.nome = nome.trim();
    }

    public String getNome() {
        return this.nome;
    }

    public void setPreco(long preco) {
        if (preco < 0) throw new IllegalArgumentException(
            "O preço do produto deve ser positivo."
        );

        this.preco = preco;
    }

    public long getPreco() {
        return this.preco;
    }

    public void setEstoque(int estoque) {
        if (estoque < 0) throw new IllegalArgumentException(
            "A quantidade de unidades no estoque deve ser positiva."
        );

        this.estoque = estoque;
    }

    public int getEstoque() {
        return this.estoque;
    }
}
