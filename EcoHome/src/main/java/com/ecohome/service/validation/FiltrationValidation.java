package com.ecohome.service.validation;

import javafx.scene.control.TextField;

public class FiltrationValidation extends BaseValidation{

    public boolean checkWattHourFields(TextField fieldMinWattHour,TextField fieldMaxWattHour){
        checkValue(fieldMinWattHour.getText(),s -> s.matches(getRegexForWattHour()), fieldMinWattHour);
        checkValue(fieldMaxWattHour.getText(),s -> s.matches(getRegexForWattHour()), fieldMaxWattHour);

        return isValuesAreValid();
    }

    public boolean checkConsumedEnergyFields(TextField fieldMinConsumedEnergy, TextField fieldMaxConsumedEnergy){
        checkValue(fieldMinConsumedEnergy.getText(),s -> s.matches(getRegexForConsumedEnergy()), fieldMinConsumedEnergy);
        checkValue(fieldMaxConsumedEnergy.getText(),s -> s.matches(getRegexForConsumedEnergy()), fieldMaxConsumedEnergy);

        return isValuesAreValid();
    }

    public boolean checkPowerConsumptionClassField(TextField fieldMinPowerConsumptionClass,TextField fieldMaxPowerConsumptionClass){
        checkValue(fieldMinPowerConsumptionClass.getText(),s -> s.matches(getRegexForPowerConsumptionClass()), fieldMinPowerConsumptionClass);
        checkValue(fieldMaxPowerConsumptionClass.getText(),s -> s.matches(getRegexForPowerConsumptionClass()), fieldMaxPowerConsumptionClass);

        return isValuesAreValid();
    }

    private String getRegexForConsumedEnergy(){
        return "[0-9]{2,10}"; //!
    }
}
