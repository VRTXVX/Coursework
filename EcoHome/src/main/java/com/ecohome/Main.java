package com.ecohome;

//> javaFX imports
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

//> misc imports
import com.ecohome.service.sceneloader.SceneLoader;
import java.net.URL;

public class Main extends Application {

    private static Stage stage;
    @Override
    public void start(Stage stage) {
        setStage(stage);

        try {
            Parent root = (Parent) new SceneLoader().getScene(SceneLoader.LogInScene);
            Scene scene = new Scene(root);

            stage.setResizable(false);
            stage.getIcons().add(new Image("file:src/main/resources/image/misc/icon_stage.png"));
            stage.setTitle("EcoHome");

            stage.setScene(scene);
            stage.show();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
    public static Stage getStage() { return stage; }

    public static void setStage(Stage stage) {
        Main.stage = stage;
    }

    public static URL getScenePath(String sceneName) {
        return Main.class.getResource(sceneName + ".fxml");
    }


}