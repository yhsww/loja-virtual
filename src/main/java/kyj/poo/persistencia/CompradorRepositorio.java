package kyj.poo.persistencia;

import java.util.List;
import kyj.poo.regras.Comprador;
import kyj.poo.regras.excecao.PersistenciaException;

public interface CompradorRepositorio {
    List<Comprador> carregar() throws PersistenciaException;

    void salvar(List<Comprador> compradores) throws PersistenciaException;
}
