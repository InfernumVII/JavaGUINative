import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class JavaFXTest extends Application {
    @Override
    public void start(Stage primaryStage) {
        Button btn = new Button("Нажми меня");
        btn.setOnAction(event -> System.out.println("Кнопка нажата!"));
        
        StackPane root = new StackPane();
        root.getChildren().add(btn);
        
        Scene scene = new Scene(root, 300, 200);
        primaryStage.setTitle("Тест JavaFX");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
