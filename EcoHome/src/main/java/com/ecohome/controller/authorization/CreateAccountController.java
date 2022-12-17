package com.ecohome.controller.authorization;

//> javaFX imports
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

//> change scene
import com.ecohome.service.sceneloader.SceneLoader;

//> database import
import com.ecohome.service.database.DatabaseManipulation;

//> validation values
import com.ecohome.service.validation.AuthorizationValidation;

//> save user data for static variables
import static com.ecohome.data.user.User.setData;

//> generate hash from password
import com.ecohome.service.hash.Hash;


public class CreateAccountController {
    @FXML
    private Button btnCreateAccount;
    @FXML
    private TextField fieldEmail;
    @FXML
    private TextField fieldFirstName;
    @FXML
    private PasswordField fieldPassword;
    @FXML
    private TextField fieldUsername;
    @FXML
    private Label labelLinkLogIn;

    @FXML
    private void labelLinkClicked() {
        new SceneLoader().changeScene(SceneLoader.LogInScene);
    }

    @FXML
    private void btnCreateAccountClicked() {
        if(!new AuthorizationValidation().checkFields(fieldUsername,fieldPassword,fieldFirstName,fieldEmail)) return; //* check values in fields if values are not valid return from this method

        String username = fieldUsername.getText();
        setData(username);

        new SceneLoader().changeScene(SceneLoader.MainScene);

        String hashedPassword = new Hash().getHashedPassword(fieldPassword.getText());

        new DatabaseManipulation().insertIntoUser(username,fieldEmail.getText(),hashedPassword,fieldFirstName.getText()); //* insert data in database table
       }
}
