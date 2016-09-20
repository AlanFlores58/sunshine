package com.example.alan_flores.sunshine;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.renderscript.ScriptGroup;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Adapter itemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final ArrayList<Item> items = new ArrayList<Item>();
        items.add(new Item("Today - Sunny - 88 / 63"));
        items.add(new Item("Tommorow - Foggy - 70 / 46"));
        items.add(new Item("Today - Sunny - 88 / 63"));
        items.add(new Item("Today - Sunny - 88 / 63"));
        items.add(new Item("Today - Sunny - 88 / 63"));
        items.add(new Item("Today - Sunny - 88 / 63"));
        items.add(new Item("Today - Sunny - 88 / 63"));
        itemsAdapter = new Adapter(this, items);

        ListView listView = (ListView) findViewById(R.id.listviewforecast);
        listView.setAdapter(itemsAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String forecast = itemsAdapter.getItem(position).getText();
                Toast.makeText(MainActivity.this,forecast,Toast.LENGTH_SHORT).show();
                Intent i = new Intent(MainActivity.this, DetailActivity.class)
                        .putExtra(Intent.EXTRA_TEXT,forecast);
                startActivity(i);
            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.forecastfragment,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                new FetchWeatherTask().execute("44820");
                Toast.makeText(this,R.string.action_refresh,Toast.LENGTH_SHORT).show();
                return true;
            case R.id.setting:
                Toast.makeText(this,R.string.action_setting,Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public class FetchWeatherTask extends AsyncTask<String ,Void,ArrayList<Item>>{

        private final String LOG_TAG = FetchWeatherTask.class.getSimpleName();

        @Override
        protected ArrayList<Item> doInBackground(String... params) {

            if (params.length == 0){
                Log.w(LOG_TAG,"Error empty params");
                return null;
            }
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String forecastJsonStr = null;

            try{
                //URL url = new URL("http://api.openweathermap.org/data/2.5/forecast/daily?q=44820&mode=json&units=metric&cnt=7&appid=0e48143b55aa4f28695abf671260a180");

                String FORECAST_BASE_URL = "http://api.openweathermap.org/data/2.5/forecast/daily?";

                String QUERY_PARAM = "q";
                String FORMAT_PARAM = "mode";
                String UNITS_PARAM = "units";
                String DAYS_PARAM = "cnt";
                String APPID_PARAM = "appid";

                Uri builtUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
                        .appendQueryParameter(QUERY_PARAM,params[0])
                        .appendQueryParameter(FORMAT_PARAM,"json")
                        .appendQueryParameter(UNITS_PARAM,"metric")
                        .appendQueryParameter(DAYS_PARAM,"7")
                        .appendQueryParameter(APPID_PARAM,"0e48143b55aa4f28695abf671260a180").build();

                URL url = new URL(builtUri.toString());
                //Log.v(LOG_TAG,"URL: " + builtUri.toString());




                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    Log.w(LOG_TAG,"Error inputStream null");
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null){
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0){
                    Log.w(LOG_TAG,"Error buffer is empty");
                    return null;
                }else {
                    forecastJsonStr = buffer.toString();

                }
            }catch (IOException e){
                Log.v("Check",e.getMessage());
                Log.e(LOG_TAG,"Error",e);
                return null;
            }finally {
                if (urlConnection != null)
                    urlConnection.disconnect();
                if (reader != null)
                    try {
                        reader.close();
                    }catch (final IOException e){
                        Log.e(LOG_TAG,"Error closing stream",e);
                    }
            }
            return (new WeatherDataParser().getWeatherFromJSON(forecastJsonStr,7));
        }

        @Override
        protected void onPostExecute(ArrayList<Item> items) {

            if(items != null){
                itemsAdapter.clear();
                itemsAdapter = new Adapter(MainActivity.this, items);
                ListView listView = (ListView) findViewById(R.id.listviewforecast);
                listView.setAdapter(itemsAdapter);

            }
            super.onPostExecute(items);
        }
    }
}
