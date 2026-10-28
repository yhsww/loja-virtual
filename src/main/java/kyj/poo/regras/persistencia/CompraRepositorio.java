package kyj.poo.regras.persistencia;

import java.util.List;

import kyj.poo.regras.excecao.PersistenciaException;
import kyj.poo.regras.modelo.Compra;

public interface CompraRepositorio {

    List<Compra> carregar() throws PersistenciaException;

    void salvar (List<Compra> compras) throws PersistenciaException;
    
}
