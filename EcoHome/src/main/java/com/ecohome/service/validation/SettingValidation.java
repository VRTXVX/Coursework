package com.ecohome.service.validation;

import javafx.scene.control.TextField;

public class SettingValidation extends BaseValidation{

    public boolean checkPriceField(TextField fieldPrice) {
        checkValue(fieldPrice.getText(),s -> s.matches(getRegexForPrice()) ,fieldPrice);

        return isValuesAreValid();
    }

    public boolean checkSymbolField(TextField fieldSymbol) {
        checkValue(fieldSymbol.getText(),s -> s.length() == 1,fieldSymbol);

        return isValuesAreValid();
    }


    private String getRegexForPrice() { return "^\\d*\\.?\\d+$";}
}
