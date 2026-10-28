package kyj.poo.regras.persistencia;

import java.util.List;

import kyj.poo.regras.excecao.PersistenciaException;
import kyj.poo.regras.modelo.Produto;

public interface ProdutoRepositorio {

    List<Produto> carregar() throws PersistenciaException;

    void salvar (List<Produto> produtos) throws PersistenciaException;
    
    
}
