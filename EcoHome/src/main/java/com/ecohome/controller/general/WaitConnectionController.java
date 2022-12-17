package com.ecohome.controller.general;

//> javaFX imports
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

//> java imports
import javafx.application.Platform;
import java.sql.Connection;

//> other misc imports
import com.ecohome.service.sceneloader.SceneLoader;
import com.ecohome.service.database.DatabaseConnection;

public class WaitConnectionController {

    @FXML
    private Button btnCancel;

    @FXML
    private Label labelLinkTryNow;

    @FXML
    private Label labelMessage;

    private static boolean isActive = false;
    private boolean block;
    private boolean stopFlag = false;

    @FXML
    public void initialize() {
        setActive(true);
        Thread secondThread = new Thread(this::waitConnection);
        secondThread.start();
    }


    @FXML
    private void btnCancelClick(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    private void labelLinkTryNowClicked(MouseEvent mouseEvent) {
        DatabaseConnection databaseConnection = new DatabaseConnection();
        Connection conn = databaseConnection.getConnectionWithoutWaiting();
        if(conn != null){
            returnToPreviousScene();
            return;
        }

        labelLinkTryNow.setText("Connection failed");
        block = true;
        labelMessage.setText("");

        new Thread(()-> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
               e.printStackTrace();
            }
            Platform.runLater(() -> labelLinkTryNow.setText("Try now"));
            block = false;
        }).start();
    }

    private void waitConnection(){
        DatabaseConnection databaseConnection = new DatabaseConnection();
        Connection conn = null;
        int seconds = 1;

        for(int n = 0;conn == null;){

            try {
                Thread.sleep(1000);
                System.out.println(seconds);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if(block) continue;
            if(stopFlag) return;

            int finalSeconds = seconds;
            Platform.runLater(() -> labelMessage.setText("Reconnecting in " + finalSeconds + " seconds"));

            if(seconds-- > 0) continue;

            seconds = 5 * ++n;
            if(seconds > 60) seconds = 60;

            Platform.runLater(() -> labelMessage.setText("Trying to connect..."));

            conn = databaseConnection.getConnectionWithoutWaiting();
        }
        returnToPreviousScene();

    }


    private void returnToPreviousScene(){
        Platform.runLater(() ->{
            stopFlag = true;
            new SceneLoader().loadPreviousScene();
            setActive(false);
        });
    }


    private static void setActive(boolean active) { isActive = active; }
    public static boolean isActive() { return isActive; }

}
