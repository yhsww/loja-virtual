package kyj.poo.persistencia;

import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.util.List;
import kyj.poo.regras.Produto;
import kyj.poo.regras.excecao.PersistenciaException;

public class ProdutoRepositorioJson implements ProdutoRepositorio {

    private final File arquivo;

    public ProdutoRepositorioJson(File arquivo) {
        this.arquivo = arquivo;
    }

    public List<Produto> carregar() throws PersistenciaException {
        return ArquivoJson.ler(
            this.arquivo,
            new TypeToken<List<Produto>>() {}.getType()
        );
    }

    public void salvar(List<Produto> produtos) throws PersistenciaException {
        ArquivoJson.gravar(this.arquivo, produtos);
    }
}
