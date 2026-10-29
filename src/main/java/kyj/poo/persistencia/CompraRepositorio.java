package kyj.poo.persistencia;

import java.util.List;
import kyj.poo.regras.compra.Compra;
import kyj.poo.regras.excecao.PersistenciaException;

public interface CompraRepositorio {
    List<Compra> carregar() throws PersistenciaException;

    void salvar(List<Compra> compras) throws PersistenciaException;
}
