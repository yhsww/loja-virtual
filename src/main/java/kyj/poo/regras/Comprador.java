package kyj.poo.regras;

import kyj.poo.regras.carrinho.Carrinho;

public class Comprador {

    private String nome;
    private String cpf;
    private String email;
    private String senha;
    private transient Carrinho carrinho;

    public Comprador(String nome, String cpf, String email, String senha) {
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        this.senha = senha;
        this.carrinho = new Carrinho();
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
        if (this.carrinho == null) this.carrinho = new Carrinho();
        return this.carrinho;
    }

    public String getSenha() {
        return this.senha;
    }
}
