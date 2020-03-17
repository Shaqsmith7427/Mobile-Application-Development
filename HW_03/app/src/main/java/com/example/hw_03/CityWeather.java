package com.example.hw_03;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class CityWeather extends AppCompatActivity {
    private static final String TAG = "demo";

    TextView location, text, forecast, temp, clickLink;
    ImageView day, night;
    TextView dayStatus, nightStatus;

    String key;
    String baseUrl;
    String cit;

    String locate, heading;
    RecyclerView recyclerView;
    RecyclerView.Adapter rv_adapter;
    RecyclerView.LayoutManager rv_layout;
    Daily dayList;
    Button save, setCurrent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_weather);



        Intent intent = getIntent();
        key = intent.getStringExtra("key");
        baseUrl = intent.getStringExtra("base");
        cit = intent.getStringExtra("city");


        location = findViewById(R.id.tv_location);
        text = findViewById(R.id.tv_status);
        forecast = findViewById(R.id.tv_forecast);
        temp = findViewById(R.id.tv_temp);
        dayStatus = findViewById(R.id.dayStatus);
        nightStatus = findViewById(R.id.nightStatus);
        clickLink = findViewById(R.id.click);
        save = findViewById(R.id.bt_saveCity);
        setCurrent = findViewById(R.id.bt_setCurrent);

        day = findViewById(R.id.iv_day);
        night = findViewById(R.id.iv_night);

        new get5Day().execute();

    }

    class get5Day extends AsyncTask<ArrayList<String>, Void, ArrayList<Daily>> {


        @Override
        protected ArrayList<Daily> doInBackground(ArrayList<String>... arrayLists) {
            HttpURLConnection connection = null;

            ArrayList<Daily> days = new ArrayList<>();

            try {
                String url = baseUrl + "/forecasts/v1/daily/5day/" + key + "?apikey=" + getResources().getString(R.string.api_key);
                Log.d(TAG, "URL1 " + url);
                URL urlB = new URL(url);


                connection = (HttpURLConnection) urlB.openConnection();
                connection.connect();

                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    String json = IOUtils.toString(connection.getInputStream(), "UTF8");

                    JSONObject root = new JSONObject(json);
                    JSONObject headline = root.getJSONObject("Headline");
                    //
                    heading = headline.getString("Text");

                    JSONArray day = root.getJSONArray("DailyForecasts");

                    for (int i = 0; i < day.length(); i++) {

                        JSONObject dayJson = day.getJSONObject(i);

                        if (!dayJson.equals(null)) {

                            Daily daily = new Daily();

                            daily.date = dayJson.getString("Date");
                            daily.lowTemp = dayJson.getJSONObject("Temperature").getJSONObject("Minimum").getString("Value");
                            daily.highTemp = dayJson.getJSONObject("Temperature").getJSONObject("Maximum").getString("Value");
                            daily.dayIcon = dayJson.getJSONObject("Day").getString("Icon");
                            daily.nightIcon = dayJson.getJSONObject("Night").getString("Icon");
                            daily.dayStatus = dayJson.getJSONObject("Day").getString("IconPhrase");
                            daily.nightStatus = dayJson.getJSONObject("Night").getString("IconPhrase");
                            daily.link = dayJson.getString("MobileLink");
                            days.add(daily);

                        } else {
                            // Toast.makeText(this, "Check Internet Connection", Toast.LENGTH_SHORT).show();
                        }
                    }

                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return days;
        }

        @Override
        protected void onPostExecute(final ArrayList<Daily> dailies) {
            super.onPostExecute(dailies);

            location.setText(cit);
            text.setText(heading);
            forecast.setText("Forecast for " + dailies.get(0).getDate());
            temp.setText("Temperature: " + dailies.get(0).getHighTemp()+"/"+dailies.get(0).getLowTemp()+" F");
            Log.d(TAG, "DayIcon" + dailies.get(0).getDayIcon());
            String imDay = dailies.get(0).getDayIcon();
            String imNite = dailies.get(0).getNightIcon();

            String imageDay =  "http://developer.accuweather.com/sites/default/files/"+imDay+ "-s.png";
            String imageNight = "http://developer.accuweather.com/sites/default/files/"+imNite+ "-s.png";
            Picasso.get().load(imageDay).into(day);
            Picasso.get().load(imageNight).into(night);
            dayStatus.setText(dailies.get(0).getDayStatus());
            nightStatus.setText(dailies.get(0).getNightStatus());
            clickLink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent linkIntent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse(dailies.get(0).getLink()));

                    startActivity(linkIntent);
                }
            });
            for (int i=1; i<dailies.size(); i++)
            {
              Daily dayList = dailies.get(i);
                Log.d(TAG, "Date: " + dayList.date);
                Log.d(TAG, "Temp: " + dayList.highTemp + "/" + dayList.lowTemp + " F");
                Log.d(TAG, "DayIcon: " + dayList.dayIcon);
                Log.d(TAG, "NightIcon: " + dayList.nightIcon);
                Log.d(TAG, "DayStatus: " + dayList.dayStatus);
                Log.d(TAG, "nightStatus " + dayList.nightStatus);
                Log.d(TAG, "Link: " + dayList.link);
            }

            setCurrent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent currentIntent = new Intent(CityWeather.this,MainActivity.class);
                    String[] cityName = key.split(",");
                    currentIntent.putExtra("City", cityName[0]);
                    startActivity(currentIntent);
                }

            });



            recyclerView = findViewById(R.id.rv_5day);

            // recyclerView.setHasFixedSize(true);

            rv_layout = new LinearLayoutManager(CityWeather.this);
            recyclerView.setLayoutManager(rv_layout);

            rv_adapter = new RecyclerAdapter(dayList, CityWeather.this);
            recyclerView.setAdapter(rv_adapter);





        }
    }
}



// forecast.setText(today.getJSONObject(0).getString("Date"));
//
//         dayStatus.setText(today.getJSONObject(0).getJSONObject("Day").getString("IconPhrase"));
//         nightStatus.setText(today.getJSONObject(0).getJSONObject("Night").getString("IconPhrase"));
//
//         temp.setText("Temperature" + high+"/"+low+" F");
//         clickLink.setOnClickListener(new View.OnClickListener() {
//@Override
//public void onClick(View v) {
//        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
//        startActivity(intent);
//        }
//        });
//
//        String imageDay =  "http://developer.accuweather.com/sites/default/files/"+String.format("%02d", dayIcon)+ "-s.png";
//        String imageNight = "http://developer.accuweather.com/sites/default/files/"+String.format("%02d", nightIcon)+ "-s.png";
//        Picasso.get().load(imageDay).into(day);
//        Picasso.get().load(imageDay).into(night);