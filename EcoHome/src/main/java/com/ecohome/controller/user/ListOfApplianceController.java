package com.ecohome.controller.user;

//> javaFX imports
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

//> java imports
import javafx.application.Platform;
import java.net.URL;
import java.util.*;
import java.util.function.Predicate;

//> service imports
import com.ecohome.service.filterappliance.FilterAppliance;
import com.ecohome.service.sceneloader.SceneLoader;
import com.ecohome.service.validation.FiltrationValidation;

//> other misc
import com.ecohome.data.appliance.Appliance;
import static com.ecohome.Main.getScenePath;
import static com.ecohome.data.user.User.getListOfAppliances;

public class ListOfApplianceController implements Initializable {
    @FXML
    private GridPane gridPane;
    @FXML
    private Pane mainPane;


    // vbox to respond to clicking to sort
    @FXML
    private VBox vBoxModel;
    @FXML
    private VBox vBoxWattHour;
    @FXML
    private VBox vBoxConsumedEnergy;
    @FXML
    private VBox vBoxPowerClass;
    @FXML
    private VBox vBoxMark;
    @FXML
    private VBox vBoxName;


    //* image caret indicates by which parameter the sorting will be performed
    @FXML
    private ImageView imgCaretMark;
    @FXML
    private ImageView imgCaretModel;
    @FXML
    private ImageView imgCaretWattHour;
    @FXML
    private ImageView imgCaretConsumedEnergy;
    @FXML
    private ImageView imgCaretPowerClass;
    @FXML
    private ImageView imgCaretName;


    // filter check boxes
    @FXML
    private CheckBox checkBoxConsumedEnergyFilter;
    @FXML
    private CheckBox checkBoxPowerConsumptionFilter;
    @FXML
    private CheckBox checkBoxWattHourFilter;


    // filter fields
    @FXML
    private TextField fieldMinPowerConsumption;
    @FXML
    private TextField fieldMaxPowerConsumption;
    @FXML
    private TextField fieldMinConsumedEnergy;
    @FXML
    private TextField fieldMaxConsumedEnergy;
    @FXML
    private TextField fieldMaxWattHour;
    @FXML
    private TextField fieldMinWattHour;

    // apply filters
    @FXML
    private Button btnApplyFilters;


    // search
    @FXML
    private TextField fieldSearch;
    @FXML
    private ImageView btnSearch;

    // filter
    @FXML
    private ImageView btnFilter;
    @FXML
    private AnchorPane filterMenu;


    // sorting
    private ImageView[] arrayOfCaret;
    private boolean sortByGrowth = false;


    // data
    private List <Appliance> curListOfAppliances;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        arrayOfCaret = new ImageView[]{imgCaretName,imgCaretMark, imgCaretModel, imgCaretWattHour, imgCaretConsumedEnergy, imgCaretPowerClass};
        curListOfAppliances = getListOfAppliances();

        new Thread(()-> {
            Platform.runLater(this::showAppliances);
        }).start();
    }

    private void showAppliances() {
        if(curListOfAppliances == null) return;

        gridPane.getChildren().clear();
        for(int i = 0; i < curListOfAppliances.size(); i++){
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getScenePath(SceneLoader.ItemScene));
                AnchorPane anchorPane = loader.load();

                ItemController itemController = loader.getController();
                itemController.setData(curListOfAppliances.get(i));
                gridPane.add(anchorPane, 0,i);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    // sortings functions
    @FXML
    private void vBoxClicked(MouseEvent event){
        if(curListOfAppliances == null) return;

        String nameBtnClicked = ((VBox) event.getSource()).getId().substring(4);

        setCaret(nameBtnClicked);
        new Thread(()-> {
            int indexOfParameter = 0;
            for (int i = 0; i < arrayOfCaret.length; i++) {
                if (arrayOfCaret[i].getId().equals("imgCaret" + nameBtnClicked)) {
                    indexOfParameter = i;
                    break;
                }
            }

            sortAppliances(indexOfParameter);
            Platform.runLater(this::showAppliances);
        }).start();
    }

    private void setCaret(String nameOfCaret){
        for (ImageView caret : arrayOfCaret) {
            caret.setVisible(caret.getId().contains(nameOfCaret));
            if(caret.getId().contains(nameOfCaret)){
                caret.setImage(new Image("file:src/main/resources/image/caret/caret_" + (sortByGrowth ? "down" : "up") + ".png"));
                sortByGrowth = !sortByGrowth;
            }
        }

    }

    private void sortAppliances(int index){
        Comparator<Appliance> comparator = null;
        switch (index) {
            case 0 -> comparator = Comparator.comparing(Appliance::getName);
            case 1 -> comparator = Comparator.comparing(Appliance::getMark);
            case 2 -> comparator = Comparator.comparing(Appliance::getModel);
            case 3 -> comparator = Comparator.comparing(Appliance::getWattHour);
            case 4 -> comparator = Comparator.comparing(Appliance::getConsumedEnergy);
            case 5 -> comparator = Comparator.comparing(appliance -> appliance.getPowerConsumptionClass().chars().sum());
        }

        Appliance[] arrayOfAppliances = curListOfAppliances.toArray(new Appliance[0]);


        Arrays.sort(arrayOfAppliances, sortByGrowth ? Objects.requireNonNull(comparator).reversed() : comparator);
        curListOfAppliances = Arrays.asList(arrayOfAppliances);
    }


    // search functions
    @FXML
    private void btnSearchClicked(MouseEvent event) {
        if(curListOfAppliances == null) return;

        String text = fieldSearch.getText();

        if(text.isEmpty()) {
            setCurrentListOfAppliances(getListOfAppliances());

            Platform.runLater(()->{
                setImgSearchDisabled();
                setImgFilterDisabled();
                showAppliances();
            });
            return;
        }

        new Thread(()-> {
            findAppliances(appliance -> appliance.getName().contains(text) ||
                    appliance.getMark().contains(text) ||
                    appliance.getModel().contains(text) ||
                    appliance.getPowerConsumptionClass().contains(text) ||
                    String.valueOf(appliance.getWattHour()).contains(text) ||
                    String.valueOf(appliance.getConsumedEnergy()).contains(text));
            Platform.runLater(()->{
                showAppliances();

                setImgSearchActive();
                setImgFilterDisabled();
            });

        }).start();
    }

    private void findAppliances(Predicate<Appliance> predicate) {
        List<Appliance> listOfFoundAppliances  = new ArrayList<>();

        for (Appliance appliance : getListOfAppliances()) {
            if (predicate.test(appliance)) {
                listOfFoundAppliances.add(appliance);
            }
        }

        setCurrentListOfAppliances(listOfFoundAppliances);
    }


    // filter functions
    @FXML
    private void btnFilterClicked(MouseEvent event) {
        if (filterMenu.isVisible()) {
            hideFilterMenu();
        } else {
            showFilterMenu();
        }
    }

    @FXML
    private void btnApplyFiltersClicked(MouseEvent event) {
        curListOfAppliances = getListOfAppliances();

        if(curListOfAppliances.isEmpty()) return;

        showFilterMenu();

        FiltrationValidation filtrationValidation = new FiltrationValidation();
        FilterAppliance filterAppliance = new FilterAppliance(curListOfAppliances);


        if(checkBoxConsumedEnergyFilter.isSelected()
                && filtrationValidation.checkConsumedEnergyFields(fieldMinConsumedEnergy,fieldMaxConsumedEnergy)){
            filterAppliance.filterByConsumedEnergy(Double.parseDouble(fieldMinConsumedEnergy.getText()), Double.parseDouble(fieldMaxConsumedEnergy.getText()));
        }

        if(checkBoxWattHourFilter.isSelected()
                && filtrationValidation.checkWattHourFields(fieldMinWattHour,fieldMaxWattHour)){

            filterAppliance.filterByWattHour(Double.parseDouble(fieldMinWattHour.getText()), Double.parseDouble(fieldMaxWattHour.getText()));
        }

        if(checkBoxPowerConsumptionFilter.isSelected()
                && filtrationValidation.checkPowerConsumptionClassField(fieldMinPowerConsumption,fieldMaxPowerConsumption)){

            filterAppliance.filterByPowerConsumptionClass(fieldMinPowerConsumption.getText(), fieldMaxPowerConsumption.getText());
        }

        if(!filtersUnchecked()) {
            setImgSearchDisabled();
            setImgFilterActive();
        }

        if(curListOfAppliances != filterAppliance.getFilteredAppliances()) {
            curListOfAppliances = filterAppliance.getFilteredAppliances();
            showAppliances();
            hideFilterMenu();
        }
    }

    private void showFilterMenu(){
        filterMenu.setVisible(true);
        filterMenu.setDisable(false);
        mainPane.setDisable(true);
    }

    private void hideFilterMenu(){
        filterMenu.setVisible(false);
        filterMenu.setDisable(true);
        mainPane.setDisable(false);
    }



    private boolean filtersUnchecked(){
        return !checkBoxConsumedEnergyFilter.isSelected() && !checkBoxWattHourFilter.isSelected() && !checkBoxPowerConsumptionFilter.isSelected();
    }

    private void setCurrentListOfAppliances(List<Appliance> curListOfAppliances){
        this.curListOfAppliances = curListOfAppliances;
    }
    private void setImgSearchActive(){ btnSearch.setImage(new Image("file:src/main/resources/image/search/search_active.png")); }

    private void setImgSearchDisabled(){ btnSearch.setImage(new Image("file:src/main/resources/image/search/search_disabled.png")); }

    private void setImgFilterActive(){ btnFilter.setImage(new Image("file:src/main/resources/image/filter/filter_active.png")); }
    private void setImgFilterDisabled(){ btnFilter.setImage(new Image("file:src/main/resources/image/filter/filter_disabled.png")); }

}
