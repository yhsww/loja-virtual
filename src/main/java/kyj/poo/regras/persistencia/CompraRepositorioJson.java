package kyj.poo.regras.persistencia;

import java.io.File;
import java.util.List;

import com.google.gson.reflect.TypeToken;

import kyj.poo.regras.excecao.PersistenciaException;
import kyj.poo.regras.modelo.Compra;

public class CompraRepositorioJson implements CompraRepositorio {

    private final File arquivo;

    public CompraRepositorioJson (File arquivo) {
        this.arquivo = arquivo;
    }

    public List<Compra> carregar() throws PersistenciaException {
        return ArquivoJson.ler(this.arquivo, new TypeToken<List<Compra>>() {}.getType());
    }
    
    public void salvar(List<Compra> compras) throws PersistenciaException {

        ArquivoJson.gravar(this.arquivo, compras);

    }
}
