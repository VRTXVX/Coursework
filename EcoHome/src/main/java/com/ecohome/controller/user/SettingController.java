package com.ecohome.controller.user;

//> javafx imports
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

//> java imports
import java.net.URL;
import java.util.ResourceBundle;

//> service imports
import com.ecohome.service.config.ConfigManipulation;
import com.ecohome.service.database.DatabaseManipulation;
import com.ecohome.service.sceneloader.SceneLoader;
import com.ecohome.service.validation.SettingValidation;

//> data imports
import static com.ecohome.data.user.User.getCutUser;
import com.ecohome.data.config.Config;

public class SettingController implements Initializable {

    public Label labelDescription;
    public Button btnOk;
    public AnchorPane descriptionPane;
    public AnchorPane confirmationPane;
    public Button btnYes;
    public Button btnNo;
    public AnchorPane mainPane;
    @FXML
    private Button btnApplyPrice;

    @FXML
    private Button btnApplySymbol;

    @FXML
    private Button btnDeleteAccount;

    @FXML
    private Button btnLogOut;

    @FXML
    private TextField fieldPrice;

    @FXML
    private TextField fieldSymbol;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Config config = new ConfigManipulation().loadConfiguration();
        fieldSymbol.setText(String.valueOf(config.getCurrencySymbol()));
        fieldPrice.setText(String.valueOf(config.getPricePerKwh()));
    }

    @FXML
    private void btnApplyPriceClicked(MouseEvent event) {
        SettingValidation settingValidation = new SettingValidation();

        if(!settingValidation.checkPriceField(fieldPrice)) return;

        Config config = new ConfigManipulation().loadConfiguration();
        config.setPricePerKwh(Double.parseDouble(fieldPrice.getText()));
        new ConfigManipulation().saveConfiguration(config);

        turnDescriptionPane("Price applied successfully!");

    }

    @FXML
    private void btnApplySymbolClicked(MouseEvent event) {
        SettingValidation settingValidation = new SettingValidation();

        if(!settingValidation.checkSymbolField(fieldSymbol)) return;

        Config config = new ConfigManipulation().loadConfiguration();
        config.setCurrencySymbol(fieldSymbol.getText().charAt(0));
        new ConfigManipulation().saveConfiguration(config);

        turnDescriptionPane("Symbol applied successfully!");
    }

    @FXML
    private void btnDeleteAccountClicked(MouseEvent event) {
        labelDescription.setText("Are you sure you want to delete your account?");
        turnConfirmMenu();
    }

    private void deleteAccountAndExit() {
        new DatabaseManipulation().deleteAccount(getCutUser());
        new SceneLoader().changeScene(SceneLoader.LogInScene);
    }

    @FXML
    private void btnLogOutClicked(MouseEvent event) {
        new SceneLoader().changeScene(SceneLoader.LogInScene);
    }

    public void btnOkClicked(MouseEvent event) {
        turnDescriptionPane("");
    }

    public void btnYesClicked(MouseEvent event) {
        deleteAccountAndExit();
    }

    public void btnNoClicked(MouseEvent event) {
        turnConfirmMenu();
    }

    private void turnConfirmMenu() {
        confirmationPane.setVisible(!confirmationPane.isVisible());
        confirmationPane.setDisable(!confirmationPane.isDisable());
        mainPane.setDisable(!mainPane.isVisible());
    }
    private void turnDescriptionPane(String description){
        labelDescription.setText(description);
        descriptionPane.setVisible(!descriptionPane.isVisible());
        descriptionPane.setDisable(!descriptionPane.isDisable());
        mainPane.setDisable(!mainPane.isVisible());
    }

}
