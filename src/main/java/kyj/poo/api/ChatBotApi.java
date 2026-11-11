package kyj.poo.api;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ChatBotApi {
    
    public String enviarPergunta(
            String nome, 
            String cpf, 
            String email, 
            String pergunta
    ) throws Exception {

        String json = """
        {
            "nome": "%s",
            "cpf": "%s",
            "email": "%s",
            "pergunta": "%s"
        }
        """.formatted(nome, cpf, email, pergunta);

        System.out.println(json);

        HttpClient client = HttpClient.newBuilder()
                        .version(HttpClient.Version.HTTP_1_1)
                        .build();

        HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://127.0.0.1:5000/chat"))
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

        HttpResponse<String> response =
        client.send(
            request,
            HttpResponse.BodyHandlers.ofString()
        );

        JsonObject jsonResposta =
                JsonParser.parseString(response.body())
                        .getAsJsonObject();

        return jsonResposta
                .get("resposta")
                .getAsString();

    }   
}
