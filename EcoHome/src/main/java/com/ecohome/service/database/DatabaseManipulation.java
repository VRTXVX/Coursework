package com.ecohome.service.database;

//> sql imports
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

//> other misc imports
import com.ecohome.data.appliance.Appliance;

import java.util.ArrayList;
import java.util.List;

import com.ecohome.data.workinfo.WorkInfo;

//> compare hash and input password
import com.ecohome.service.hash.Hash;

public class DatabaseManipulation {

    private final DatabaseConnection databaseConnection = new DatabaseConnection();

    public void insertIntoUser(String username, String email, String password, String firstName) {
        Connection conn = databaseConnection.getConnection();
        if (conn == null) return;

        String query = "INSERT INTO \"user\" (username,email,password,first_name) VALUES ('" + username + "','" + email + "','" + password + "','" + firstName + "')";
        Statement statement;

        try {
            statement = conn.createStatement();
            statement.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            databaseConnection.closeConnection(conn);
        }
    }

    public boolean isUniqueInTable(String table, String columnName, String value) {

        Connection conn = databaseConnection.getConnection();
        if (conn == null) return false;

        String query = "SELECT * FROM " + "\"" + table + "\"" + " WHERE " + columnName + " = '" + value + "'";

        try {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            databaseConnection.closeConnection(conn);
        }

        return true;
    }

    public boolean verifyUser(String username, String password) {

        Connection conn = databaseConnection.getConnection();
        if (conn == null) return false;

        String query = "SELECT password FROM \"user\" WHERE username = '" + username + "' AND disabled = false";

        try {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            if (resultSet.next()) {
                return new Hash().validatePassword(password, resultSet.getString("password"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            databaseConnection.closeConnection(conn);
        }

        return false;
    }

    public List<WorkInfo> getApplianceActivity(int idAppliance, String minDateTime) {
        Connection conn = databaseConnection.getConnection();
        if (conn == null) return null;

        String query = "SELECT time_turn_on,time_turn_off FROM \"appliance_activity\" WHERE id_appliance = " + idAppliance + " AND time_turn_on >= '" + minDateTime + "'";
        List<WorkInfo> workInfo = new ArrayList<>();

        try {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                workInfo.add(new WorkInfo(resultSet.getString("time_turn_on"), resultSet.getString("time_turn_off")));
            }

            return workInfo;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            databaseConnection.closeConnection(conn);
        }
    }

    public void updateApplianceActivity(int idAppliance,String timeTurnOn,String timeTurnOff){
        Connection conn = databaseConnection.getConnection();
        if(conn == null) return;

        String query;

        if(timeTurnOn == null)  query = "UPDATE appliance_activity SET time_turn_off = " + "'" + timeTurnOff + "'" + " WHERE id_appliance = " + idAppliance + " AND time_turn_off IS NULL";
        else query = "INSERT INTO appliance_activity (id_appliance,time_turn_on) VALUES (" + idAppliance + "," + "'" + timeTurnOn + "')";

        System.out.println(query);

        try {
            Statement statement = conn.createStatement();
            statement.executeUpdate(query);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            databaseConnection.closeConnection(conn);
        }
    }


    public void insertAppliance(Appliance appliance, String username) {
        Connection conn = databaseConnection.getConnection();
        if(conn == null) return;

        // insert value id by username and insert value of appliance
        String query = "INSERT INTO appliance (id_user,name,mark,model,watt_hour,status,power_consumption_class,time_turn_on,working_hours) " +
                "VALUES ((SELECT id FROM \"user\" WHERE username = '" + username + "'),'" + appliance.getName() + "','" +
                appliance.getMark() + "','" + appliance.getModel() + "'," + appliance.getWattHour() + ",'" + appliance.getStatus() + "',"
                + "'" + appliance.getPowerConsumptionClass() + "'," + appliance.getTimeTurnedOn() + "," + appliance.getWorkingHours() + ")";

        System.out.println(query);

        try {
            Statement statement = conn.createStatement();
            statement.executeUpdate(query);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateAppliance(Appliance appliance) {
        Connection conn = databaseConnection.getConnection();
        if(conn == null) return;

        String query = "UPDATE appliance SET name = '" + appliance.getName() + "', " +
                "mark = '" + appliance.getMark() + "', model = '" + appliance.getModel() + "'," +
                " watt_hour = " + appliance.getWattHour() + ", status = '" + appliance.getStatus() + "'," +
                " power_consumption_class = " + "'" + appliance.getPowerConsumptionClass() + "'" +
                ", time_turn_on = " + "'" + appliance.getTimeTurnedOn() + "'" + ", working_hours = '" + appliance.getWorkingHours() +
                "' WHERE id = " + appliance.getId();

        System.out.println(query);
        try {
            Statement statement = conn.createStatement();
            statement.executeUpdate(query);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteAppliance(int id) {
        Connection conn = databaseConnection.getConnection();
        if(conn == null) return;

        String query = "DELETE FROM appliance_activity WHERE id_appliance = " + id + "; DELETE FROM appliance WHERE id = " + id;

        try {
            Statement statement = conn.createStatement();
            statement.executeUpdate(query);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Appliance> selectAppliances(String username){
        Connection conn = databaseConnection.getConnection();
        if(conn == null) return null;

        String query = "SELECT * FROM appliance WHERE id_user = (SELECT id FROM \"user\" WHERE username = '" + username + "')";

        List<Appliance> appliances = new ArrayList<>();

        try {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                Appliance appliance = new Appliance();
                appliance.setId(resultSet.getInt("id"));
                appliance.setName(resultSet.getString("name"));
                appliance.setMark(resultSet.getString("mark"));
                appliance.setModel(resultSet.getString("model"));
                appliance.setWattHour(resultSet.getDouble("watt_hour"));
                appliance.setWorkingHours(resultSet.getDouble("working_hours"));
                appliance.setTimeTurnedOn(resultSet.getDouble("time_turn_on"));
                appliance.setStatus(resultSet.getBoolean("status"));
                appliance.setPowerConsumptionClass(resultSet.getString("power_consumption_class"));
                appliances.add(appliance);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return appliances;
    }

    public void deleteAccount(String username) {
        Connection conn = databaseConnection.getConnection();
        if(conn == null) return;
        String query = "UPDATE \"user\" SET disabled = 'true' WHERE username = '" + username + "'";

        try {
            Statement statement = conn.createStatement();
            statement.executeUpdate(query);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }


}

