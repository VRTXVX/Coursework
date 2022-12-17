package com.ecohome.service.validation;

//> javaFX imports
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

//> database imports
import com.ecohome.service.database.DatabaseManipulation;

public class AuthorizationValidation extends BaseValidation {

    public boolean checkFields(TextField fieldUsername, PasswordField fieldPassword, TextField fieldFirstName,TextField fieldEmail){
        DatabaseManipulation databaseManipulation = new DatabaseManipulation();

        if(fieldFirstName != null && fieldEmail != null) {
            checkValue(fieldFirstName.getText(), s -> s.matches(getRegexForFirstName()), fieldFirstName);

            checkValue(fieldEmail.getText()
                    ,s -> s.matches(getRegexForEmail()) && databaseManipulation.isUniqueInTable("user","email",s)
                    ,fieldEmail);
        }

        checkValue(fieldUsername.getText(),
                s -> s.matches(getRegexForUsername()) && (fieldEmail == null || databaseManipulation.isUniqueInTable("user", "username", s)),
                fieldUsername);

        checkValue(fieldPassword.getText(),
                s-> s.matches(getRegexForPassword()),
                fieldPassword);

        return isValuesAreValid();
    }

    public boolean checkFields(TextField fieldUsername, PasswordField fieldPassword){
        if(!checkFields(fieldUsername, fieldPassword, null, null)) return false;

        if(new DatabaseManipulation().verifyUser(fieldUsername.getText(), fieldPassword.getText())) {
            return true;
        }

        setBorderColor(fieldUsername,"red");
        setBorderColor(fieldPassword,"red");
        return false;
    }

    //* regex for validation fields                                                                           *//
    private String getRegexForPassword(){
        return "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,50}$";
    }

    private String getRegexForUsername(){
        return "[a-z]{5,20}";
    }

    private String getRegexForFirstName(){
        return "[a-zA-Z]{3,20}";
    }

    private String getRegexForEmail(){
        return "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}";
    }
    //*                                                                                                        *//

}
