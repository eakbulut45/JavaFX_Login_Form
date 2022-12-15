
package model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import net.aksingh.owmjapis.CurrentWeather;
import net.aksingh.owmjapis.OpenWeatherMap;
import org.json.JSONException;

import java.io.IOException;

public class WeatherModel {

    private final StringProperty cityName = new SimpleStringProperty(this, "cityName");
    private final StringProperty description = new SimpleStringProperty(this, "description");
    private final StringProperty temperature = new SimpleStringProperty(this, "temperature");
    private final StringProperty pressure = new SimpleStringProperty(this, "pressure");
    private final StringProperty humidity = new SimpleStringProperty(this, "humidity");
    private final StringProperty wind = new SimpleStringProperty(this, "wind");
    private final StringProperty iconID = new SimpleStringProperty(this, "iconID");

    public WeatherModel() {
    }

    public StringProperty cityNameProperty() {
        return cityName;
    }
    public StringProperty descriptionProperty() {
        return description;
    }
    public StringProperty temperatureProperty() {
        return temperature;
    }
    public StringProperty pressureProperty() {
        return pressure;
    }
    public StringProperty humidityProperty() {
        return humidity;
    }
    public StringProperty windProperty() {
        return wind;
    }
    public String getIconID() {
        return iconID.get();
    }

    public void getWeather(String cityName) throws JSONException {
        try {
            OpenWeatherMap wm = new OpenWeatherMap("53b2f8cb3a8e502f9dc9ee08b6c09fb0"); // personal OWM API KEY
            CurrentWeather cw = wm.currentWeatherByCityName(cityName, "USA,FR,DE,TUR");

            this.cityName.set(cw.getCityName());
            this.description.set(cw.getWeatherInstance(0).getWeatherDescription());
            float tempC = (((cw.getMainInstance().getTemperature()) - 32) * 5) / 9;
            this.temperature.set(String.format("%.1f", tempC));
            this.pressure.set(String.valueOf(cw.getMainInstance().getPressure()));
            this.humidity.set(String.valueOf(cw.getMainInstance().getHumidity()));
            float kmph = (cw.getWindInstance().getWindSpeed()) * 1.609F;
            this.wind.set(String.format("%.1f", kmph));
            this.iconID.set(cw.getWeatherInstance(0).getWeatherIconName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}