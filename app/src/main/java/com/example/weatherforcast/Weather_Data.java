package com.example.weatherforcast;

import org.json.JSONException;
import org.json.JSONObject;

public class Weather_Data {
    private String Temp,Icon,City,Weather_Type;
    private int condition;

    public  static Weather_Data fromJson(JSONObject jsonObject){
        try{
            Weather_Data wd =new Weather_Data();
            wd.City=jsonObject.getString("name");
            wd.condition=jsonObject.getJSONArray("weather").getJSONObject(0).getInt("id");
            wd.Weather_Type=jsonObject.getJSONArray("weather").getJSONObject(0).getString("main");
            wd.Icon=updateWeatherIcon(wd.condition);
            double tempResult=jsonObject.getJSONObject("main").getDouble("temp")-273.15;
            int roundedValue=(int)Math.rint(tempResult);
            wd.Temp=Integer.toString(roundedValue);
            return wd;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

    }

    private static String updateWeatherIcon(int condition){
        if(condition>=0 && condition<=300){
            return "thunderstrom";
        }
        else if(condition>=300 && condition<=500){
            return "lightrain";
        }
        else if(condition>=500 && condition<=600){
            return "shower";
        }
        else if(condition>=600 && condition<=700){
            return "snowing";
        }
        else if(condition>=700 && condition<=771){
            return "fog";
        }
        else if(condition>=772 && condition<=800){
            return "overcast";
        }
        else if(condition==800){
            return "sunny";
        }
        else if(condition>=801 && condition<=804){
            return "cloudy";
        }
        else if(condition==900 && condition<=902){
            return "thunderstrom";
        }
        else if(condition>=903){
            return "snowing";
        }
        else if(condition>=904){
            return "sunny";
        }
        else if(condition>=905 && condition<=1000){
            return "thunderstrom1";
        }
        return "dunno";


    }

    public String getTemp() {
        return Temp+"°C";
    }

    public String getIcon() {
        return Icon;
    }

    public String getCity() {
        return City;
    }

    public String getWeather_Type() {
        return Weather_Type;
    }
}
