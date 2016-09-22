package com.example.alan_flores.sunshine;

import android.content.Context;
import android.text.format.Time;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by alan_flores on 19/09/16.
 */
public class WeatherDataParser{

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

    private String getDateString(long time){



        SimpleDateFormat shortenedDateFormat = new SimpleDateFormat("EEE, MMM dd");
        return shortenedDateFormat.format(time);
    }

    private String formanMaxMin(double max, double min, String units,Context cont){

        if (units.equals(cont.getString(R.string.pref_units_imperial))){
            max = (max * 1.8) + 32;
            min = (min * 1.8) + 32;
        }

        long roundMax = Math.round(max);
        long roundMin = Math.round(min);
        String stringMaxMin = roundMax + " / " + roundMin;

        return stringMaxMin;
    }

    public ArrayList<Item> getWeatherFromJSON(String weatherJsonStr, int numDays, String units, Context cont){
        ArrayList<Item> items = new ArrayList<Item>();
        try{
            JSONObject forecastJson = new JSONObject(weatherJsonStr);
            JSONArray weatherArray = forecastJson.getJSONArray("list");

            Time dayTime = new Time();
            dayTime.setToNow();

            int julianStartDay = Time.getJulianDay(System.currentTimeMillis(),dayTime.gmtoff);
            dayTime = new Time();

            for (int i = 0;i < numDays;i++){

                JSONObject dayForecast = weatherArray.getJSONObject(i);

                String day;
                String description;
                String highAndLow;

                JSONObject weatherObject = dayForecast.getJSONArray("weather").getJSONObject(0);
                description = weatherObject.getString("main");

                JSONObject temperatureInfo = dayForecast.getJSONObject("temp");
                double max = temperatureInfo.getDouble("max");
                double min = temperatureInfo.getDouble("min");
                highAndLow = formanMaxMin(max,min,units,cont);

                long dateTime;
                dateTime = dayTime.setJulianDay(julianStartDay + i);
                day = getDateString(dateTime);


                items.add(new Item((day + " - " + description + " - " + highAndLow)));
                //Log.v(LOG_TAG,items.get(i).getText());
            }
        }catch (JSONException e){
            Log.e(LOG_TAG,"error",e);
            return null;
        }

        return items;
    }
}
