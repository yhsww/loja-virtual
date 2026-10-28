package kyj.poo.regras.excecao;

public class PersistenciaException extends Exception {

    public PersistenciaException(String mensagem, Throwable problema) {
        super(mensagem, problema);
    }
}
