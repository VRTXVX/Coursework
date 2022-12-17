package com.ecohome.data.config;

import java.io.Serializable;

public class Config implements Serializable {

    private double pricePerKwh;
    private char currencySymbol;

    public Config(double pricePerKwh, char currencySymbol) {
        this.pricePerKwh = pricePerKwh;
        this.currencySymbol = currencySymbol;
    }
    public double getPricePerKwh() {return pricePerKwh;}
    public void setPricePerKwh(double pricePerKwh) {this.pricePerKwh = pricePerKwh;}
    public char getCurrencySymbol() {return currencySymbol;}
    public void setCurrencySymbol(char currencySymbol) {this.currencySymbol = currencySymbol;}


}
