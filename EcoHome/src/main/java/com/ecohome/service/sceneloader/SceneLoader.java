package com.ecohome.service.sceneloader;


import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import static com.ecohome.Main.getStage;
import static com.ecohome.Main.getScenePath;


public class SceneLoader {
    public final static String ApplianceScene = "ListOfAppliances";
    public final static String EditApplianceScene = "EditAppliance";
    public final static String StatisticScene = "Statistic";
    public final static String SettingScene = "Settings";
    public final static String LogInScene = "Login";
    public final static String RegisterScene = "CreateAccount";
    public final static String WaitConnectionScene = "WaitConnection";
    public final static String MainScene = "MainScene";
    public final static String ChangeListScene = "AddAppliance";
    public final static String ItemScene = "Item";
    private static Scene prevScene;

    public void changeScene(String nameScene) {

        Parent root = (Parent) getScene(nameScene);
        Scene scene = new Scene(root);
        getStage().setScene(scene);
        if(nameScene.equals(MainScene) || nameScene.equals(RegisterScene) || nameScene.equals(LogInScene)) setPrevScene(scene);
    }

    public Object getScene(String nameScene){
        Object scene = null;

        try {
            scene = FXMLLoader.load(getScenePath(nameScene));
        }
        catch (Exception e){
            System.out.println("Failed to load " + nameScene);
        }

        if(scene == null) System.exit(1);

        return scene;
    }

    public void loadPreviousScene() {
        getStage().setScene(getPrevScene());
    }
    public static Scene getPrevScene() { return SceneLoader.prevScene; }
    public static void setPrevScene(Scene scene) { SceneLoader.prevScene = scene; }
}
