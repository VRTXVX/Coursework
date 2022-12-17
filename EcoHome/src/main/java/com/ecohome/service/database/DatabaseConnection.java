package com.ecohome.service.database;

//> sql imports
import java.sql.*;

//> use environment variables from .env file
import io.github.cdimascio.dotenv.Dotenv;

//> other misc imports
import com.ecohome.service.sceneloader.SceneLoader;
import com.ecohome.controller.general.WaitConnectionController;

public class DatabaseConnection {

    public Connection getConnection() {
        Connection conn = getConnectionWithoutWaiting();
        if(conn == null && !WaitConnectionController.isActive()) {
            new SceneLoader().changeScene(SceneLoader.WaitConnectionScene);
        }

        return conn;
    }

    public Connection getConnectionWithoutWaiting() {
        Connection conn = null;

        Dotenv dotenv = Dotenv.configure().load();

        try {
            conn = DriverManager.getConnection(
                    dotenv.get("ECOHOME_DB_URL"),
                    dotenv.get("ECOHOME_DB_USER"),
                    dotenv.get("ECOHOME_DB_PASSWORD"));
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return conn;
    }

    public void closeConnection(Connection conn) {
        if (conn == null) return;

        try {
            if (!conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
