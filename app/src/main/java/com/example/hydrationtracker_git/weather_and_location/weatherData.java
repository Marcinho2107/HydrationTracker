package com.example.hydrationtracker_git.weather_and_location;

import org.json.JSONException;
import org.json.JSONObject;

public class weatherData {

    private String wdTemperature,wdIcon,wdCity,wdType;
    private int wdCondition;
    public static weatherData fromJson(JSONObject jsonObject){
        try{
            weatherData wd=new weatherData();
            wd.wdCity=jsonObject.getString("name");
            wd.wdCondition=jsonObject.getJSONArray("weather").getJSONObject(0).getInt("id");
            wd.wdType=jsonObject.getJSONArray("weather").getJSONObject(0).getString("main");
            wd.wdIcon=updateWeatherIcon(wd.wdCondition);
            double tempResult=jsonObject.getJSONObject("main").getDouble("temp")-273.15;
            int roundedValue=(int)Math.rint(tempResult);
            wd.wdTemperature=Integer.toString(roundedValue);
            return wd;
        }
        catch(JSONException e){
            e.printStackTrace();
            return null;
        }
    }

    private static String updateWeatherIcon(int condition){
        if(condition>=801 && condition<=804){
            return "cloudy";
        }
        else if(condition==800){
            return "sunny";
        }
        else if(condition>=701 && condition<=781){
            return "cloudy_sunny";
        }
        else if(condition>=600 && condition<=622){
            return "snowy";
        }
        else if(condition>=500 && condition<=531){
            return "rainy";
        }
        else if(condition>=300 && condition<=321){
            return "windy";
        }
        else if(condition>=200 && condition<=232){
            return "storm";
        }
        return "questionmark";
    }

    public String getWdTemperature() {
        return wdTemperature+"°C";
    }

    public String getWdIcon() {
        return wdIcon;
    }

    public String getWdCity() {
        return wdCity;
    }

    public String getWdType() {
        return wdType;
    }
}