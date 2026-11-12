package kyj.poo.api;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ChatBotApi {

    private static final String CHATBOT_URL =
            System.getenv().getOrDefault(
                    "CHATBOT_URL",
                    "http://127.0.0.1:5000/chat"
            );

    private static final Duration TIMEOUT =
            Duration.ofSeconds(30);

    private final HttpClient client;

    public ChatBotApi() {
        this.client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .connectTimeout(TIMEOUT)
                .build();
    }

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

        HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(CHATBOT_URL))
                    .timeout(TIMEOUT)
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
