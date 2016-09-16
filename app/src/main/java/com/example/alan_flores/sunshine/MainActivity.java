package com.example.alan_flores.sunshine;

import android.os.AsyncTask;
import android.renderscript.ScriptGroup;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

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
        Adapter itemsAdapter = new Adapter(this, items);

        ListView listView = (ListView) findViewById(R.id.listviewforecast);
        listView.setAdapter(itemsAdapter);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.help:
                Toast.makeText(this,"help",Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public class FetchWeatherTask extends AsyncTask<Void,Void,Void>{

        private final String LOG_TAG = FetchWeatherTask.class.getSimpleName();

        @Override
        protected Void doInBackground(Void... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String forecastJsonStr = null;

            try{
                URL url = new URL("http://api.openweathermap.org/data/2.5/forecast/daily?q=44820&modejson&units=metric&cnt=7&appid=0e48143b55aa4f28695abf671260a180");

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

            return null;
        }
    }
}
