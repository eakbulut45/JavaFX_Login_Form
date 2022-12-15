package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import model.WeatherModel;
import org.json.JSONException;

public class weather {
    WeatherModel wm;
    @FXML
    private TextField cityNameTxt;
    @FXML
    private Label cityNameLbl;
    @FXML
    private Label descriptionLbl;
    @FXML
    private Label temperatureLbl;
    @FXML
    private Label pressureLbl;
    @FXML
    private Label humidityLbl;
    @FXML
    private Label windLbl;
    @FXML
    private Label iconLbl;
    @FXML
    private Button refreshBtn;
    @FXML
    private Label timeLbl;
    @FXML
    private void selectText(){
        cityNameTxt.selectAll();
    }
    @FXML
    private void initialize() throws JSONException {
        wm = new WeatherModel();
        wm.getWeather(cityNameTxt.getText());

        cityNameLbl.textProperty().bindBidirectional(wm.cityNameProperty());
        descriptionLbl.textProperty().bindBidirectional(wm.descriptionProperty());
        temperatureLbl.textProperty().bindBidirectional(wm.temperatureProperty());
        pressureLbl.textProperty().bindBidirectional(wm.pressureProperty());
        humidityLbl.textProperty().bindBidirectional(wm.humidityProperty());
        windLbl.textProperty().bindBidirectional(wm.windProperty());

        iconLbl.setGraphic(new ImageView("http://openweathermap.org/img/w/" + wm.getIconID() + ".png"));
        iconLbl.setScaleX(1.5);
        iconLbl.setScaleY(1.5);
        refreshBtn.setGraphic(new ImageView("image/refresh1.png"));
        refreshBtn.setScaleX(0.8);
        refreshBtn.setScaleY(0.8);
        timeLbl.setText(new java.util.Date().toString());
    }

}
