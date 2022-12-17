package com.ecohome.controller.user;

//> JavaFX imports
import com.ecohome.data.user.User;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

//> initialization
import java.net.URL;
import java.util.ResourceBundle;

//> other misc imports
import com.ecohome.service.database.DatabaseManipulation;
import com.ecohome.service.sceneloader.SceneLoader;
import com.ecohome.service.validation.ApplianceValidation;

//> db
import com.ecohome.data.appliance.Appliance;

//> other misc
import static com.ecohome.data.user.User.*;

public class EditApplianceController implements Initializable {

    @FXML
    private AnchorPane descriptionPane;
    @FXML
    private Label labelDescription;
    @FXML
    private Button btnOk;
    @FXML
    private Button btnYes;
    @FXML
    private Label btnDelete;
    @FXML
    private AnchorPane mainPane;
    @FXML
    private Button btnApplyChanges;
    @FXML
    private Button btnNo;
    @FXML
    private ImageView btnReturnBack;
    @FXML
    private AnchorPane confirmationPane;
    @FXML
    private TextField fieldMark;
    @FXML
    private TextField fieldModel;
    @FXML
    private TextField fieldName;
    @FXML
    private TextField fieldPowerConsumptionClass;
    @FXML
    private TextField fieldWattHour;
    private Appliance appliance;

    private final DatabaseManipulation databaseManipulation = new DatabaseManipulation();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        appliance = getSelectedAppliance();

        fieldName.setText(appliance.getName());
        fieldMark.setText(appliance.getMark());
        fieldModel.setText(appliance.getModel());
        fieldWattHour.setText(String.valueOf(appliance.getWattHour()));
        fieldPowerConsumptionClass.setText(appliance.getPowerConsumptionClass());
    }

    @FXML
    private void btnReturnBackClicked(MouseEvent event) {
        new SceneLoader().changeScene(SceneLoader.MainScene);
    }

    @FXML
    private void btnDeleteClicked(MouseEvent event) {
        turnConfirmMenu();
    }

    @FXML
    private void btnYesClicked(MouseEvent event) {
        databaseManipulation.deleteAppliance(appliance.getId());
        updateListOfAppliances();
        turnDescriptionPane();
        labelDescription.setText("Appliance deleted");
        turnConfirmMenu();
    }

    @FXML
    private void btnNoClicked(MouseEvent event) {
        turnConfirmMenu();
    }

    @FXML
    private void btnApplyChangesClicked(MouseEvent event) {
        if(!checkChanges()) return;

        if(!new ApplianceValidation().checkFields(fieldName, fieldMark, fieldModel, fieldWattHour, fieldPowerConsumptionClass)) return;

        Appliance changedAppliance = new Appliance(fieldName.getText(), fieldMark.getText(),
                fieldModel.getText(),Double.parseDouble(fieldWattHour.getText()),false,fieldPowerConsumptionClass.getText(),appliance.getTimeTurnedOn(),appliance.getWorkingHours());
        changedAppliance.setId(appliance.getId());

        databaseManipulation.updateAppliance(changedAppliance);

        turnDescriptionPane();
        labelDescription.setText("Appliance updated");
        updateListOfAppliances();

        new Thread(User::updateListOfAppliances).start();
    }

    @FXML
    private void btnOkClicked(MouseEvent event) {
        turnDescriptionPane();
        new SceneLoader().changeScene(SceneLoader.MainScene);
    }

    private boolean checkChanges() {
        return !(fieldName.getText().equals(appliance.getName())
                && fieldMark.getText().equals(appliance.getMark())
                && fieldModel.getText().equals(appliance.getModel())
                && fieldWattHour.getText().equals(String.valueOf(appliance.getWattHour()))
                && fieldPowerConsumptionClass.getText().equals(appliance.getPowerConsumptionClass()));

    }

    private void turnConfirmMenu() {
        confirmationPane.setVisible(!confirmationPane.isVisible());
        confirmationPane.setDisable(!confirmationPane.isDisable());
        mainPane.setDisable(!mainPane.isDisable());
    }
    private void turnDescriptionPane(){
        descriptionPane.setVisible(!descriptionPane.isVisible());
        descriptionPane.setDisable(!descriptionPane.isDisable());
        mainPane.setDisable(!mainPane.isDisable());
    }



}
