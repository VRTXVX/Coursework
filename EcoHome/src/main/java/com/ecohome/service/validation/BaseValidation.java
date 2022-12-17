package com.ecohome.service.validation;

//> javaFX imports
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.util.function.Predicate;

public abstract class BaseValidation {
    protected boolean valuesAreValid = true;

    protected void checkValue(String string, Predicate<String> predicate, TextField textField, PasswordField passwordField){
        if(predicate.test(string)){
            setBorderColor(textField,"#D2D2D2");
            setBorderColor(passwordField,"#D2D2D2");
            return;
        }

        setBorderColor(textField,"red");
        setBorderColor(passwordField,"red");
        setValuesAreValid(false);
    }

    protected void checkValue(String string, Predicate<String> predicate, TextField textField){
        checkValue(string,predicate,textField,null);
    }

    protected void checkValue(String string, Predicate<String> predicate,PasswordField passwordField){
        checkValue(string,predicate,null,passwordField);
    }

    protected static void setBorderColor(TextField textField, String color){
        if(textField != null) textField.setStyle("-fx-border-color: " + color);
    }

    protected static void setBorderColor(PasswordField passwordField, String color){
        if(passwordField != null) passwordField.setStyle("-fx-border-color: " + color);
    }

    private void setValuesAreValid(boolean value){ valuesAreValid = value;}
    protected boolean isValuesAreValid(){
        boolean temp = valuesAreValid;
        setValuesAreValid(true);
        return temp;
    }

    protected String getRegexForWattHour() {return "^\\d*\\.?\\d+$";}
    protected String getRegexForPowerConsumptionClass() { return "[A-G+]{1,4}"; }

}
