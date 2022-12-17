package com.ecohome.controller.user;

//> JavaFX imports
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

//> database import
import com.ecohome.data.appliance.Appliance;

//> service imports
import com.ecohome.service.database.DatabaseManipulation;
import com.ecohome.service.sceneloader.SceneLoader;

//> other misc
import static com.ecohome.data.user.User.setSelectedAppliance;

public class ItemController {

    @FXML
    private ImageView btnPlug;
    @FXML
    private HBox characteristicsBox;
    @FXML
    private Label labelName;
    @FXML
    private Label labelMark;
    @FXML
    private Label labelModel;
    @FXML
    private Label labelWattHour;
    @FXML
    private Label labelConsumedEnergy;
    @FXML
    private Label labelPowerConsumptionClass;

    //> data
    private Appliance curAppliance;

    public void setData(Appliance appliance) {
        curAppliance = appliance;
        labelName.setText(appliance.getName());
        labelMark.setText(appliance.getMark());
        labelModel.setText(appliance.getModel());
        labelWattHour.setText(String.valueOf(appliance.getWattHour()));
        labelConsumedEnergy.setText(String.format("%.2f", appliance.getConsumedEnergy()));
        labelPowerConsumptionClass.setText(String.valueOf(appliance.getPowerConsumptionClass()));

        if(appliance.getStatus()) {
            btnPlug.setImage(new Image("file:src/main/resources/image/plug/plug_on.png"));
            return;
        }

        btnPlug.setImage(new Image("file:src/main/resources/image/plug/plug_off.png"));
    }


    @FXML
    private void btnPlugClicked(MouseEvent actionEvent){

        btnPlug.setImage(new Image("file:src/main/resources/image/plug/plug_" + (curAppliance.getStatus() ? "off" : "on") + ".png"));

        new Thread(() -> {
            curAppliance.turnWithSaving();
            new DatabaseManipulation().updateAppliance(curAppliance);
        }).start();


    }

    @FXML
    private void characteristicsBoxClicked(MouseEvent event) {
        setSelectedAppliance(curAppliance);
        SceneLoader sceneLoader = new SceneLoader();
        sceneLoader.changeScene(SceneLoader.EditApplianceScene);
    }
}
