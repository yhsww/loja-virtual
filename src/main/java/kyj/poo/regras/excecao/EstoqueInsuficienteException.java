package kyj.poo.regras.excecao;

public class EstoqueInsuficienteException extends Exception {

    public EstoqueInsuficienteException() {
        super(
            "A quantidade de unidades disponíveis no estoque é insuficiente para realizar a operação."
        );
    }

    public EstoqueInsuficienteException(String nomeProduto) {
        super(
            "Estoque insuficiente para o produto: " + nomeProduto + "."
        );
    }
}
