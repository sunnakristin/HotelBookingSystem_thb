package assignment4_thb;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("customer-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 500, 400);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        DatabaseManager.initializeDatabase();
        launch();
    }
}