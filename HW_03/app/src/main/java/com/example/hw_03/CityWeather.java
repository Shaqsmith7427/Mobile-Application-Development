package com.example.hw_03;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class CityWeather extends AppCompatActivity {

    Intent intent = getIntent();
    String key = intent.getStringExtra("key");
    String baseUrl = intent.getStringExtra("base");

    TextView location, text, forecast, temp;
    ImageView day, night;
    TextView dayStatus, nightStatus;

    String high, low, dayIcon, nightIcon, link;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_weather);

        location.findViewById(R.id.tv_location);
        text.findViewById(R.id.tv_status);
        forecast.findViewById(R.id.tv_forecast);
        temp.findViewById(R.id.tv_temp);
        dayStatus.findViewById(R.id.dayStatus);
        nightStatus.findViewById(R.id.nightStatus);

        day.findViewById(R.id.iv_day);
        night.findViewById(R.id.iv_night);


    }

    class get5Day extends AsyncTask<ArrayList<String>, Void, Void> {

        private static final String TAG = "demo";

        @Override
        protected Void doInBackground(ArrayList<String>... arrayLists) {
            HttpURLConnection connection = null;

            ArrayList<City> cityList = new ArrayList<>();

            try {
                String url = baseUrl + "forecasts/v1/daily/5day/" + key + "?apikey=" + getResources().getString(R.string.api_key) + "&q=";
                Log.d(TAG, "URL1 " + url);
                URL urlB = new URL(url);


                connection = (HttpURLConnection) urlB.openConnection();
                connection.connect();

                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    String json = IOUtils.toString(connection.getInputStream(), "UTF8");

                    JSONObject root = new JSONObject(json);
                    JSONObject headline = root.getJSONObject("Headline");

                    headline.getString("Text");
                    headline.getString("");

                    JSONArray today = root.getJSONArray("DailyForecasts");

                    today.getJSONObject(0).getString("Date");
                    low =  today.getJSONObject(0).getJSONObject("Temperature").getJSONObject("Minimum").getString("Value");
                    high = today.getJSONObject(0).getJSONObject("Temperature").getJSONObject("Maximum").getString("Value");
                    dayIcon = today.getJSONObject(0).getJSONObject("Day").getString("Icon");
                    nightIcon = today.getJSONObject(0).getJSONObject("Night").getString("Icon");
                    link = today.getJSONObject(0).getString("MobileLink");


                            temp;
                    ImageView day, night;
                    TextView dayStatus, nightStatus;

                    String high, low, dayIcon, nightIcon, link;



//                    for (int i = 0; i < root.length(); i++) {
//                        JSONObject cityJson = root.getJSONObject(i);
//
//                        if (!cityJson.equals(null)) {
//
//                            City city1 = new City();
//                            city1.citykey = cityJson.getString("Key");
//                            city1.cityname = cityJson.getString("LocalizedName");
//                            city1.state = cityJson.getJSONObject("AdministrativeArea").getString("ID");
//                            //Log.d(TAG, "City Key: " + city1.citykey);
//                            cityList.add(city1);
//
//                        } else {
//                            //       Toast.makeText(this, "Check Internet Connection", Toast.LENGTH_SHORT).show();
//                        }
//                    }

                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}
