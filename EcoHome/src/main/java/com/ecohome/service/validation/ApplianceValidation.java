package com.ecohome.service.validation;

import javafx.scene.control.TextField;

public class ApplianceValidation extends BaseValidation {

    public boolean checkFields(TextField fieldName, TextField fieldMark, TextField fieldModel,
                               TextField fieldWattHour, TextField fieldPowerConsumptionClass) {

        checkValue(fieldName.getText(), s -> s.matches(getRegexForName()), fieldMark);
        checkValue(fieldMark.getText(), s -> s.matches(getRegexForMark()), fieldMark);
        checkValue(fieldModel.getText(), s -> s.matches(getRegexForModel()), fieldModel);
        checkValue(fieldWattHour.getText(), s -> s.matches(getRegexForWattHour()), fieldWattHour);
        checkValue(fieldPowerConsumptionClass.getText(), s -> s.matches(getRegexForPowerConsumptionClass()), fieldPowerConsumptionClass);

        System.out.println("ApplianceValidation.checkFields: " + isValuesAreValid());
        return isValuesAreValid();
    }

    private String getRegexForName() { return "[a-zA-Z]{3,20}"; }
    private String getRegexForModel() { return "[a-zA-Z0-9]{2,20}"; }
    private String getRegexForMark() { return "[a-zA-Z0-9]{2,20}"; }


}
