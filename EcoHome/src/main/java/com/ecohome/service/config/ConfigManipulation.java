package com.ecohome.service.config;

import com.ecohome.data.config.Config;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.nio.file.Path;

public class ConfigManipulation {


    String pathToProject = System.getProperty("user.dir");
    String nameConfigurationFile = "appconfig.bin";

    public Config loadConfiguration() {
        Path pathToFile = Path.of(pathToProject + "\\" + nameConfigurationFile);

        if(!pathToFile.toFile().exists()){
            createDefaultConfiguration();
        }

        Config config = null;
        try {
            File file = new File(pathToFile.toString());
            FileInputStream fileIn = new FileInputStream(file);
            config = (Config) new ObjectInputStream(fileIn).readObject();
            fileIn.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return config;
    }

    public void saveConfiguration(Config config) {
        Path pathToFile = Path.of(pathToProject + "\\" + nameConfigurationFile);
        try {
            File file = new File(pathToFile.toString());
            java.io.ObjectOutputStream out = new java.io.ObjectOutputStream(new java.io.FileOutputStream(file));
            out.writeObject(config);
            out.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createDefaultConfiguration() {
        Config config = new Config(0.25, '€');
        saveConfiguration(config);
    }
}
