package com.ecohome.service.filterappliance;

import com.ecohome.data.appliance.Appliance;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class FilterAppliance {

    private List<Appliance> listOfAppliance = null;
    public FilterAppliance(List<Appliance> listOfAppliance) {
        this.listOfAppliance = listOfAppliance;
    }

    public void filterByWattHour(double minWattHour, double maxWattHour) {
        filter(x -> x.getWattHour() >= minWattHour && x.getWattHour() <= maxWattHour);
    }

    public void filterByConsumedEnergy(double minConsumedEnergy, double maxConsumedEnergy) {
        filter(x -> x.getConsumedEnergy() >= minConsumedEnergy && x.getConsumedEnergy() <= maxConsumedEnergy);
    }

    public void filterByPowerConsumptionClass(String minPowerConsumption, String maxPowerConsumption) {
        filter(x ->  x.getPowerConsumptionClass().chars().sum() >= minPowerConsumption.chars().sum()
                && x.getPowerConsumptionClass().chars().sum() <= maxPowerConsumption.chars().sum());
    }

    private void filter(Predicate<Appliance> predicate) {
        if(listOfAppliance.isEmpty()) return;

        List<Appliance> result = new ArrayList<>();

        for (Appliance appliance : listOfAppliance) {
            if(predicate.test(appliance)) {
                result.add(appliance);
            }
        }

        setListOfAppliance(result);
    }

    public List<Appliance> getFilteredAppliances() {return listOfAppliance;}
    private void setListOfAppliance(List<Appliance> listOfAppliance) {
        this.listOfAppliance = listOfAppliance; }
}


