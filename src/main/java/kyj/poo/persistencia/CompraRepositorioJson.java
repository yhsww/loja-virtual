package kyj.poo.persistencia;

import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.util.List;
import kyj.poo.regras.compra.Compra;
import kyj.poo.regras.excecao.PersistenciaException;

public class CompraRepositorioJson implements CompraRepositorio {

    private final File arquivo;

    public CompraRepositorioJson(File arquivo) {
        this.arquivo = arquivo;
    }

    public List<Compra> carregar() throws PersistenciaException {
        return ArquivoJson.ler(
            this.arquivo,
            new TypeToken<List<Compra>>() {}.getType()
        );
    }

    public void salvar(List<Compra> compras) throws PersistenciaException {
        ArquivoJson.gravar(this.arquivo, compras);
    }
}
