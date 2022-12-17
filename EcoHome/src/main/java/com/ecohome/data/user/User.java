package com.ecohome.data.user;

import com.ecohome.data.appliance.Appliance;
import com.ecohome.service.database.DatabaseManipulation;
import java.util.List;

public class User {
    private static String username;
    private static List<Appliance> listOfAppliances;
    private static Appliance selectedAppliance;

    public static void setData(String username){
        User.username = username;
        User.listOfAppliances = new DatabaseManipulation().selectAppliances(username);
    }
    public static String getCutUser() { return username; }
    public static void setCutUser(String curUser) { User.username = curUser; }
    public static List<Appliance> getListOfAppliances() { return listOfAppliances; }
    public static void setListOfAppliances(List<Appliance> listOfAppliances) { User.listOfAppliances = listOfAppliances; }

    public static void updateListOfAppliances(){
        listOfAppliances = new DatabaseManipulation().selectAppliances(username);
    }
    public static Appliance getSelectedAppliance() { return selectedAppliance; }
    public static void setSelectedAppliance(Appliance selectedAppliance) { User.selectedAppliance = selectedAppliance;}
}
