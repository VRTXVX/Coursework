package com.ecohome.controller.authorization;

//> javaFX imports
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

//> validation values
import com.ecohome.service.validation.AuthorizationValidation;

//> other misc imports
import com.ecohome.data.user.User;
import com.ecohome.service.sceneloader.SceneLoader;

public class LogInController {

    @FXML
    private Label labelLinkCreateAccount;

    @FXML
    private Button btnLogIn;

    @FXML
    private PasswordField fieldPassword;

    @FXML
    private TextField fieldUsername;

    @FXML
    private void labelLinkClicked(MouseEvent mouseEvent) {
        new SceneLoader().changeScene(SceneLoader.RegisterScene);
    }

    @FXML
    private void btnLogInClicked(ActionEvent event) {
        if(!new AuthorizationValidation().checkFields(fieldUsername, fieldPassword)) return;

        User.setData(fieldUsername.getText());

        new SceneLoader().changeScene(SceneLoader.MainScene);

    }

}
