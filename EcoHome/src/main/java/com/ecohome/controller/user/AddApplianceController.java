package com.ecohome.controller.user;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

//> service imports
import com.ecohome.service.database.DatabaseManipulation;
import com.ecohome.service.validation.ApplianceValidation;

//> misc
import static com.ecohome.data.user.User.getCutUser;
import com.ecohome.data.appliance.Appliance;
import com.ecohome.data.user.User;


public class AddApplianceController {

    @FXML
    private Button btnApplyChanges;
    @FXML
    private Button btnOk;
    @FXML
    private AnchorPane descriptionPane;
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
    @FXML
    private AnchorPane mainPane;

    @FXML
    private void btnAddClicked(MouseEvent event) {
        System.out.println("AddApplianceController: btnAddClicked");
        ApplianceValidation applianceValidation = new ApplianceValidation();

        if(!applianceValidation.checkFields(fieldName, fieldMark, fieldModel, fieldWattHour, fieldPowerConsumptionClass)){
            return;
        }

        Appliance appliance = new Appliance(fieldName.getText(), fieldMark.getText(),
                fieldModel.getText(),Double.parseDouble(fieldWattHour.getText()),false,fieldPowerConsumptionClass.getText(),0,0);

        DatabaseManipulation databaseManipulation = new DatabaseManipulation();

        databaseManipulation.insertAppliance(appliance, getCutUser());

        new Thread(User::updateListOfAppliances).start();

        turnDescriptionPane();
    }

    private void turnDescriptionPane(){
        descriptionPane.setVisible(!descriptionPane.isVisible());
        descriptionPane.setDisable(!descriptionPane.isDisable());
        mainPane.setDisable(!mainPane.isDisable());
    }

    @FXML
    private void btnOkClicked(MouseEvent event) {
        turnDescriptionPane();
    }
}
