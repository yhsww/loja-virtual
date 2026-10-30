
package kyj.poo.servico;

import java.util.ArrayList;
import java.util.List;

import kyj.poo.regras.Comprador;
import kyj.poo.persistencia.CompradorRepositorio;
import kyj.poo.regras.excecao.PersistenciaException;

public class CompradorService {

    private final CompradorRepositorio repositorio;

    public CompradorService(CompradorRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    public void cadastrar(String nome, String cpf, String email, String senha)
            throws PersistenciaException {

        List<Comprador> compradores = repositorio.carregar();

        if (compradores == null) {
            compradores = new ArrayList<>();
        }

        for (Comprador comprador : compradores) {
            if (comprador.getEmail().equalsIgnoreCase(email)) {
                throw new IllegalArgumentException(
                        "Este email já está cadastrado."
                );
            }

            if (comprador.getCpf().equals(cpf)) {
                throw new IllegalArgumentException(
                        "Este CPF já está cadastrado."
                );
            }
        }

        Comprador novoComprador = new Comprador(
                nome, cpf, email, senha
        );

        compradores.add(novoComprador);

        repositorio.salvar(compradores);
    }

    public Comprador login(String email, String senha)
            throws PersistenciaException {

        List<Comprador> compradores = repositorio.carregar();

        if (compradores == null) {
            return null;
        }

        for (Comprador comprador : compradores) {
            if (comprador.getEmail().equalsIgnoreCase(email)
                    && comprador.getSenha().equals(senha)) {

                return comprador;
            }
        }

        return null;
    }
}
