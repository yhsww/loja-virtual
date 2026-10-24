package kyj.poo.regras.modelo;

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

    public void aumentarEstoque(int qtd) {
        this.estoque += qtd;
    }

    public void diminuirEstoque(int qtd) {
        this.estoque -= qtd;
    }

    public boolean verificarEstoque(int qtd) {
        return this.estoque >= qtd;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return this.nome;
    }

    public void setPreco(long preco) {
        this.preco = preco;
    }

    public long getPreco() {
        return this.preco;
    }

    public void setEstoque(int estoque) {
        this.estoque = estoque;
    }

    public int getEstoque() {
        return this.estoque;
    }
}
