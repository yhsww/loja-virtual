package kyj.poo.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Aplicacao extends Application {

    @Override
    public void start(Stage stage) {
        stage.setTitle("Loja Virtual");

        VBox raiz = new VBox(new Label("Loja Virtual"));
        stage.setScene(new Scene(raiz, 420, 360));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
