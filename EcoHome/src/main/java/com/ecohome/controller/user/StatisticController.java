package com.ecohome.controller.user;

//> javafx imports
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.text.SimpleDateFormat;

import java.util.List;
import java.util.ResourceBundle;

// service imports
import com.ecohome.service.config.ConfigManipulation;
import com.ecohome.service.database.DatabaseManipulation;

//> data imports
import com.ecohome.data.config.Config;
import com.ecohome.data.workinfo.WorkInfo;
import com.ecohome.data.appliance.Appliance;
import static com.ecohome.data.user.User.getListOfAppliances;


public class StatisticController implements Initializable {

    public ComboBox<String> comboBoxPeriod;
    public ComboBox<Appliance> comboBoxAppliance;
    public Label labelSpent;
    public Label labelConsumedEnergy;
    public Button btnApply;

    private double pricePerKwh;
    private String currencySymbol;

    private final String[] daysOfWeek = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
    private final String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    public AreaChart<String, Double> areaChart;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initChoiceBoxAppliance();
        initChoiceBoxPeriod();
        areaChart.legendVisibleProperty().setValue(false);

        Config config = new ConfigManipulation().loadConfiguration();
        pricePerKwh = config.getPricePerKwh();
        currencySymbol = String.valueOf(config.getCurrencySymbol());
        labelConsumedEnergy.setText("0.00 kWh");
        labelSpent.setText(currencySymbol + " 0.00");
    }


    private void setStatisticForDay(Appliance appliance) {
        areaChart.getData().clear();
        XYChart.Series<String, Double> series = new XYChart.Series<>();

        long curTime = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd");

        String minDateTime = sdf.format(curTime) + " 00:00:00";

        DatabaseManipulation databaseManipulation = new DatabaseManipulation();
        List<WorkInfo> workInfo = databaseManipulation.getApplianceActivity(appliance.getId(), minDateTime);

        int curHour = Integer.parseInt(new SimpleDateFormat("HH").format(curTime));

        boolean isTurnedOn = false;
        for (int hour = 0; hour < 24; hour++) {

            series.getData().add(new XYChart.Data<>((String.valueOf(hour).length() > 1 ? "" : "0") + hour + ":00", isTurnedOn ? appliance.getWattHour() : 0));

            for (WorkInfo time : workInfo) {

                if (time.getHourTurnedOn() == hour) {
                    series.getData().add(new XYChart.Data<>(time.getTimeTurnedOn(), appliance.getWattHour()));
                    isTurnedOn = true;
                }

                if (time.getHourTurnedOff() == -1) continue;


                if (time.getHourTurnedOff() == hour) {
                    series.getData().add(new XYChart.Data<>(time.getTimeTurnedOff(), (double) 0));
                    isTurnedOn = false;
                }
            }

            if (hour == curHour) {
                String curTimeStr = new SimpleDateFormat("HH:mm").format(curTime);
                series.getData().add(new XYChart.Data<>(curTimeStr, isTurnedOn ? appliance.getWattHour() : 0));
                break;
            }
        }
            areaChart.getData().add(series);

            double consumedEnergy = 0;
            for (WorkInfo el : workInfo) {
                consumedEnergy += el.getConsumedEnergy(appliance.getWattHour());
            }
            consumedEnergy /= 1000;

            labelConsumedEnergy.setText(String.format("%.2f Kwh", consumedEnergy));
            labelSpent.setText(String.format(currencySymbol + "%.2f", consumedEnergy * pricePerKwh));
    }

    private void setStatisticForWeek(Appliance appliance) {
//        areaChart.getData().clear();
//
//        XYChart.Series<String, Double> series = new XYChart.Series<>();
//
//        SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd");
//
//        String minDateTime = sdf.format(System.currentTimeMillis() - 604800000);
//
//        DatabaseManipulation databaseManipulation = new DatabaseManipulation();
//        List<WorkInfo> workInfo = databaseManipulation.getApplianceActivity(appliance.getId(), minDateTime);
//
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(System.currentTimeMillis());
//        int curDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

    }

    private void initChoiceBoxPeriod(){
        if(getListOfAppliances().isEmpty()){ return; }

        comboBoxPeriod.getItems().addAll("All time","Day", "Week", "Month", "Year" );
    }
    private void initChoiceBoxAppliance() {
        if(getListOfAppliances().isEmpty()){ return; }

        for (int i = 0; i < getListOfAppliances().size(); i++) {
            comboBoxAppliance.getItems().add(getListOfAppliances().get(i));
        }
    }

    @FXML
    public void btnApplyClicked(MouseEvent event) {

        Appliance appliance = comboBoxAppliance.getValue();

        switch (comboBoxPeriod.getValue()) {
//            case "All time":
//                break;
            case "Day":
                setStatisticForDay(appliance);
                break;
            case "Week":
                setStatisticForWeek(appliance);
                break;
//            case "Month":
//                break;
//            case "Year":
//                break;
        }

    }
}
