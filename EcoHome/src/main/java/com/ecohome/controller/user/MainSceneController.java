package com.ecohome.controller.user;

//> javaFX imports
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

//> other misc
import com.ecohome.service.sceneloader.SceneLoader;

public class MainSceneController {

    @FXML
    private Label btnAppliance;
    @FXML
    private Label btnChangeList;
    @FXML
    private Label btnStatistic;
    @FXML
    private Label btnSetting;
    @FXML
    private Label btnExit;
    @FXML
    private AnchorPane mainSceneSection;
    private final SceneLoader sceneLoader = new SceneLoader();
    @FXML
    private void btnApplianceClicked(MouseEvent event) {
        setMainSceneSection((Node) sceneLoader.getScene(SceneLoader.ApplianceScene));
    }

    @FXML
    private void btnChangeListClicked(MouseEvent event) {
        setMainSceneSection((Node) sceneLoader.getScene(SceneLoader.ChangeListScene));
    }

    @FXML
    private void btnStatisticClicked(MouseEvent event) {
        setMainSceneSection((Node) sceneLoader.getScene(SceneLoader.StatisticScene));
    }

    @FXML
    private void btnSettingClicked(MouseEvent event) {
        setMainSceneSection((Node) sceneLoader.getScene(SceneLoader.SettingScene));
    }

    @FXML
    private void btnExitClicked(MouseEvent event) {
        System.exit(0);
    }
    private void setMainSceneSection(Node mainScene) {
        mainSceneSection.getChildren().setAll(mainScene);
    }

}
