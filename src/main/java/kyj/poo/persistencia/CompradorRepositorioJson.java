package kyj.poo.persistencia;

import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.util.List;
import kyj.poo.regras.Comprador;
import kyj.poo.regras.excecao.PersistenciaException;

public class CompradorRepositorioJson implements CompradorRepositorio {

    private final File arquivo;

    public CompradorRepositorioJson(File arquivo) {
        this.arquivo = arquivo;
    }

    @Override
    public List<Comprador> carregar() throws PersistenciaException {
        return ArquivoJson.ler(
            this.arquivo,
            new TypeToken<List<Comprador>>() {}.getType()
        );
    }

    @Override
    public void salvar(List<Comprador> compradores)
        throws PersistenciaException {
        ArquivoJson.gravar(this.arquivo, compradores);
    }
}
