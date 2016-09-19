package com.example.alan_flores.sunshine;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by alan_flores on 19/09/16.
 */
public class WeatherDataParser {

    private final String LOG_TAG = WeatherDataParser.class.getSimpleName();

    public double getMaxTemperatureForDay(String weatherJsonStr, int dayIndex){
        try{
            JSONObject weather = new JSONObject(weatherJsonStr);
            JSONArray days = weather.getJSONArray("list");
            JSONObject dayInfo = days.getJSONObject(dayIndex);
            JSONObject temperatureInfo = dayInfo.getJSONObject("temp");
            return temperatureInfo.getDouble("max");
        }catch (JSONException e){
            Log.e(LOG_TAG,"");
        }
            return -1;
    }
}
