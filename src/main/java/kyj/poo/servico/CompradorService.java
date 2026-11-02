package kyj.poo.servico;

import java.util.ArrayList;
import java.util.List;
import kyj.poo.persistencia.CompradorRepositorio;
import kyj.poo.regras.Comprador;
import kyj.poo.regras.excecao.PersistenciaException;

public class CompradorService {

    private final CompradorRepositorio repositorio;
    private final List<Comprador> compradores;

    public CompradorService(CompradorRepositorio repositorio)
        throws PersistenciaException {
        this.repositorio = repositorio;
        this.compradores = new ArrayList<>(repositorio.carregar());
    }

    public Comprador cadastrar(
        String nome,
        String cpf,
        String email,
        String senha
    ) throws PersistenciaException {
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

        Comprador novo = new Comprador(nome, cpf, email, senha);
        compradores.add(novo);

        try {
            repositorio.salvar(compradores);
        } catch (PersistenciaException e) {
            compradores.remove(novo);
            throw e;
        }

        return novo;
    }

    public Comprador login(String email, String senha) {
        for (Comprador comprador : compradores) {
            if (
                comprador.getEmail().equalsIgnoreCase(email) &&
                comprador.getSenha().equals(senha)
            ) {
                return comprador;
            }
        }

        return null;
    }

    public List<Comprador> listar() {
        return compradores;
    }
}
