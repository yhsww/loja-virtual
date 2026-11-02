package kyj.poo.persistencia;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import kyj.poo.regras.excecao.PersistenciaException;

final class ArquivoJson {

    private static final Gson GSON = new GsonBuilder()
        .setPrettyPrinting()
        .create();

    private ArquivoJson() {}

    static <T> List<T> ler(File arquivo, Type tipoLista)
        throws PersistenciaException {
        if (!arquivo.exists()) return new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(arquivo))) {
            List<T> lista = GSON.fromJson(br, tipoLista);

            if (lista == null) return new ArrayList<>();

            return lista;
        } catch (IOException | JsonParseException e) {
            throw new PersistenciaException("Falha ao ler arquivo.", e);
        }
    }

    static void gravar(File arquivo, Object dados)
        throws PersistenciaException {
        File pai = arquivo.getParentFile();
        if (pai != null) pai.mkdirs();

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(arquivo))) {
            String objeto = GSON.toJson(dados);
            bw.write(objeto);
        } catch (IOException e) {
            throw new PersistenciaException("Falha ao gravar arquivo.", e);
        }
    }
}
