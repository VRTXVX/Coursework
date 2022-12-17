package com.ecohome.data.appliance;

import com.ecohome.service.database.DatabaseManipulation;
import java.text.SimpleDateFormat;

public class Appliance {

    private int id;
    private String name;
    private String mark;
    private String model;
    private double wattHour;
    private boolean status;
    private double workingHours;
    private String powerConsumptionClass;
    private double timeTurnedOn;

    public Appliance(){}

    public Appliance(String name, String mark, String model,
                     double wattHour, boolean status,
                     String powerConsumptionClass, double timeTurnedOn, double workingHours) {
        this.name = name;
        this.mark = mark;
        this.model = model;
        this.wattHour = wattHour;
        this.status = status;
        this.powerConsumptionClass = powerConsumptionClass;
      }


    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getMark() { return mark; }
    public void setMark(String mark) { this.mark = mark; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public double getWattHour() { return wattHour; }
    public void setWattHour(double wattHour) { this.wattHour = wattHour; }

    public boolean getStatus() { return status; }

    public void turnWithSaving() {
        DatabaseManipulation databaseManipulation = new DatabaseManipulation();
        String curTime =  new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(System.currentTimeMillis());


        if(!getStatus()){
            setStatus(!getStatus());
            databaseManipulation.updateApplianceActivity(getId(), curTime, null);
            timeTurnedOn = System.currentTimeMillis();

        }
        else {
            setStatus(!getStatus());
            databaseManipulation.updateApplianceActivity(getId(),null,curTime);
        }

        System.out.println("\n" + Thread.currentThread().getName() + "\t-\t" + Thread.currentThread().getId());
    }

    public void setStatus(boolean status) {
        if(status && !getStatus())
            setTimeTurnedOn(System.currentTimeMillis()); // save time when appliance was turned on
        else if(!status && getStatus())
            setWorkingHours(getWorkingHours() + (System.currentTimeMillis() - getTimeTurnedOn()) / 3600000.0); // add time when appliance was turned on to working hours

        this.status = status;
    }

    public double getWorkingHours() {
        if (getStatus()) // if appliance is turned on then add time from last time turned on to now
            return workingHours + (System.currentTimeMillis() - getTimeTurnedOn()) / 3600000.0;

        return workingHours;
    }
    public void setWorkingHours(double workingHours) { this.workingHours = workingHours; }
    public String getPowerConsumptionClass() { return powerConsumptionClass; }
    public void setPowerConsumptionClass(String powerConsumptionClass) { this.powerConsumptionClass = powerConsumptionClass; }
    public double getConsumedEnergy() { return getWorkingHours() * getWattHour() / 1000.0; }

    public void setTimeTurnedOn(double timeTurnedOn) { this.timeTurnedOn = timeTurnedOn; }
    public double getTimeTurnedOn() { return timeTurnedOn; }

    @Override
    public String toString() {
        return getName() + " " + getMark() + " id: " + getId();
    }
}
