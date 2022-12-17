module com.ecohome {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires io.github.cdimascio.dotenv.java;


    opens com.ecohome to javafx.fxml;
    exports com.ecohome;
    exports com.ecohome.controller.authorization;
    opens com.ecohome.controller.authorization to javafx.fxml;
    exports com.ecohome.controller.user;
    opens com.ecohome.controller.user to javafx.fxml;
    exports com.ecohome.service.sceneloader;
    opens com.ecohome.service.sceneloader to javafx.fxml;
    exports com.ecohome.service.hash;
    opens com.ecohome.service.hash to javafx.fxml;
    exports com.ecohome.service.validation;
    opens com.ecohome.service.validation to javafx.fxml;
    exports com.ecohome.controller.general;
    opens com.ecohome.controller.general to javafx.fxml;
    exports com.ecohome.service.filterappliance;
    opens com.ecohome.service.filterappliance to javafx.fxml;
    exports com.ecohome.data.workinfo;
    opens com.ecohome.data.workinfo to javafx.fxml;
}