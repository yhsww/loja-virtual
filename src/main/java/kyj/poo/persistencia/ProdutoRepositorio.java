package kyj.poo.persistencia;

import java.util.List;
import kyj.poo.regras.Produto;
import kyj.poo.regras.excecao.PersistenciaException;

public interface ProdutoRepositorio {
    List<Produto> carregar() throws PersistenciaException;

    void salvar(List<Produto> produtos) throws PersistenciaException;
}
