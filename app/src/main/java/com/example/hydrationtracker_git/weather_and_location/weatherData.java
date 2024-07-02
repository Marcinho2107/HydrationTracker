/**
 * Stellt Wetterdaten dar, die aus einer JSON-Antwort stammen.
 * Enthält Temperatur, Wettersymbol, Stadtname und Wettertyp.
 */
package com.example.hydrationtracker_git.weather_and_location;

import org.json.JSONException;
import org.json.JSONObject;

public class weatherData {

    private String wdTemperature, wdIcon, wdCity, wdType;
    private int wdCondition;

    /**
     * Konstruiert ein `weatherData` Objekt aus einem JSONObject.
     *
     * @param jsonObject Das JSONObject mit den Wetterdaten.
     * @return Ein `weatherData` Objekt, initialisiert mit den Daten des JSONObjects,
     * oder null, wenn das JSON-Parsing fehlschlägt.
     */
    public static weatherData fromJson(JSONObject jsonObject) {
        try {
            weatherData wd = new weatherData();
            wd.wdCity = jsonObject.getString("name");
            wd.wdCondition = jsonObject.getJSONArray("weather").getJSONObject(0).getInt("id");
            wd.wdType = jsonObject.getJSONArray("weather").getJSONObject(0).getString("main");
            wd.wdIcon = updateWeatherIcon(wd.wdCondition);
            double tempResult = jsonObject.getJSONObject("main").getDouble("temp") - 273.15;
            int roundedValue = (int)Math.rint(tempResult);
            wd.wdTemperature = Integer.toString(roundedValue);
            return wd;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Gibt die Temperatur in Celsius als formatierte Zeichenkette zurück.
     * @return Temperatur gefolgt vom °C-Symbol.
     */
    public String getWdTemperature() {
        return wdTemperature + "°C";
    }

    /**
     * Gibt das Wettersymbol zurück, das mit der Wetterlage verbunden ist.
     * @return Wettersymbol-String.
     */
    public String getWdIcon() {
        return wdIcon;
    }

    /**
     * Gibt den Städtenamen zurück, der mit den Wetterdaten verknüpft ist.
     * @return Stadtname-String.
     */
    public String getWdCity() {
        return wdCity;
    }

    /**
     * Gibt den Wettertyp (z.B. "Wolken", "Regen") zurück, der mit den Wetterdaten verbunden ist.
     * @return Wettertyp-String.
     */
    public String getWdType() {
        return wdType;
    }

    /**
     * Aktualisiert das Wettersymbol auf der Grundlage der angegebenen Wetterlage-ID.
     *
     * @param condition Die Wetterbedingungs-ID.
     * @return Eine Zeichenkette, die das Wettersymbol darstellt. Mögliche Werte sind:
     * "bewölkt", "sonnig", "bewölkt_sonnig", "verschneit", "regnerisch", "windig", "Sturm" und "Fragezeichen".
     */
    private static String updateWeatherIcon(int condition) {
        if (condition >= 801 && condition <= 804) {
            return "cloudy";
        } else if (condition == 800) {
            return "sunny";
        } else if (condition >= 701 && condition <= 781) {
            return "cloudy_sunny";
        } else if (condition >= 600 && condition <= 622) {
            return "snowy";
        } else if (condition >= 500 && condition <= 531) {
            return "rainy";
        } else if (condition >= 300 && condition <= 321) {
            return "windy";
        } else if (condition >= 200 && condition <= 232) {
            return "storm";
        }
        return "questionmark";
    }
}