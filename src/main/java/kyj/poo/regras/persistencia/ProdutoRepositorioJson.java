package kyj.poo.regras.persistencia;

import java.io.File;
import java.util.List;

import com.google.gson.reflect.TypeToken;

import kyj.poo.regras.excecao.PersistenciaException;
import kyj.poo.regras.modelo.Produto;

public class ProdutoRepositorioJson implements ProdutoRepositorio {

    private final File arquivo;

    public ProdutoRepositorioJson (File arquivo) {
        this.arquivo = arquivo;
    }

    public List<Produto> carregar () throws PersistenciaException {

        return ArquivoJson.ler(this.arquivo, new TypeToken<List<Produto>>() {}.getType());

    }

    public void salvar (List<Produto> produtos) throws PersistenciaException {

        ArquivoJson.gravar(this.arquivo, produtos);

    }

    
}
