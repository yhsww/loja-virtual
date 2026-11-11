package kyj.poo.api;

public class TesteChatBot {

    public static void main(String[] args) throws Exception {

        ChatBotApi api = new ChatBotApi();

        String resposta = api.enviarPergunta(
              "y",
    "12345678912",
    "yhasmins80@gmail.com",
                "O que eu já comprei?"
        );

        System.out.println("Resposta do chatbot:");
        System.out.println(resposta);
    }
}
    
