package com.example.hw_03;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static String TAG = "demo";
    String baseUrl = "http://dataservice.accuweather.com";
    EditText et_city;
    EditText et_country;
    AlertDialog alertDialog;
    EditText et_city_curr;
    EditText et_country_curr;
    Button curr_city;
    Button search_city;
    Button Search;
    Button Cancel;
    String code;
    TextView text;

    LayoutInflater layoutInflater;
    String city;
    String country;
    ArrayList<String> CityState = new ArrayList<>();
    String keyword;
    ArrayList<String> location = new ArrayList<>();

    String cityCurrent = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final RelativeLayout relativeLayout;
        relativeLayout = findViewById(R.id.conditions);
        relativeLayout.setVisibility(View.GONE);


        curr_city = findViewById(R.id.bt_currCity);
        search_city = findViewById(R.id.bt_search);
        text = findViewById(R.id.textView);

        if (!(cityCurrent.isEmpty()))
        {   Intent intent = getIntent();
            cityCurrent = intent.getStringExtra("City");
            location.add(cityCurrent);
            location.add("US");
            new GetLocation().execute(location).toString();
        }

        curr_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                layoutInflater = getLayoutInflater();
                View view = layoutInflater.inflate(R.layout.current_conditions,null);

                builder.setCancelable(false);

                builder.setView(view);

                et_city_curr = view.findViewById(R.id.et_city_curr);
                et_country_curr = view.findViewById(R.id.et_country_curr);



                Search = view.findViewById(R.id.bt_ok);
                Cancel = view.findViewById(R.id.bt_cancel);

                alertDialog = builder.create();

               // relativeLayout.setVisibility(View.INVISIBLE);

                Search.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        city = et_city_curr.getText().toString();
                        country = et_country_curr.getText().toString();

                        ArrayList<String> location = new ArrayList<>();
                        location.add(city);
                        location.add(country);

                       // Log.d(TAG, "Send me Your location" + location);
                        code = new GetLocation().execute(location).toString();
                        code = new GetLocation().execute(location).toString();
                        alertDialog.cancel();

                        text.setVisibility(View.GONE);
                        curr_city.setVisibility(View.GONE);

                        relativeLayout.setVisibility(View.VISIBLE);

                    }
                });

               Cancel.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       alertDialog.cancel();
                   }
               });
                alertDialog.show();
            }
        });

        search_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_city = findViewById(R.id.et_city);
                et_country = findViewById(R.id.et_country);

                city = et_city.getText().toString();
                country = et_country.getText().toString();

                Log.d(TAG, "City: " + city);
                Log.d(TAG, "Country: " + country);



                location.add(city);
                location.add(country);

                new GetCities().execute(location).toString();
            }
        });

    }

    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo == null || !networkInfo.isConnected() ||
                (networkInfo.getType() != ConnectivityManager.TYPE_WIFI
                        && networkInfo.getType() != ConnectivityManager.TYPE_MOBILE)) {
            return false;
        }
        return true;
    }


    class GetLocation extends AsyncTask<ArrayList<String>, Void, ArrayList<String>[]> {

        @Override
        protected ArrayList<String>[] doInBackground(ArrayList<String>... arrayLists) {
            HttpURLConnection connection = null;
            String keycode = "";
            String cit = arrayLists[0].get(0);;
            String coun = arrayLists[0].get(1);


            try {
                String url = baseUrl + "/locations/v1/cities/"+coun+"/search?apikey=" + getResources().getString(R.string.api_key) + "&q=" + cit;
                Log.d(TAG, "URL1 " + url);
                URL urlB = new URL(url);



                connection = (HttpURLConnection) urlB.openConnection();
                connection.connect();

                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    String json = IOUtils.toString(connection.getInputStream(), "UTF8");

                    JSONArray root = new JSONArray(json);
                    String key = root.getJSONObject(0).getString("Key");
                    Log.d(TAG, "Key is: " + key);

                    if (!key.equals(null)) {

                       keycode = key;

                    }
                    else
                    {
             //           Toast.makeText(this, "Check Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.d(TAG, "Key is" + keycode);

            arrayLists[0].add(keycode);

            return arrayLists;

        }

        @Override
        protected void onPostExecute(ArrayList<String>[] array)
        {

            Log.d(TAG, "onPostExecute: " + array);
            new GetCurrConditions().execute(array);

        }

    }


    class GetCurrConditions extends AsyncTask<ArrayList<String>,Void, ArrayList<Condtions>>
    {

        @Override
        protected ArrayList<Condtions> doInBackground(ArrayList<String>... array) {
            HttpURLConnection connection = null;
            ArrayList<Condtions> condit = new ArrayList<>();
            String city = array[0].get(0);
            String country = array[0].get(1);
            String keycode = array[0].get(2);


            try {
                String url = baseUrl + "/currentconditions/v1/"+keycode+"?apikey=" + getResources().getString(R.string.api_key);
                Log.d(TAG, "doInBackground: " + url);
                URL urlB = new URL(url);


                connection = (HttpURLConnection) urlB.openConnection();
                connection.connect();

                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    String json = IOUtils.toString(connection.getInputStream(), "UTF8");

                    JSONArray root = new JSONArray(json);

                    Condtions conditions = new Condtions();

                    conditions.City = city;
                    conditions.Country = country;
                    int icon = root.getJSONObject(0).getInt("WeatherIcon");
                    conditions.Icon  = String.format("%02d", icon);
                    conditions.Text  = root.getJSONObject(0).getString("WeatherText");
                    conditions.LODTime = root.getJSONObject(0).getString("LocalObservationDateTime");

                    Log.d(TAG, "doInBackground: " + root.getJSONObject(0).getJSONObject("Temperature").getJSONObject("Imperial").getString("Value"));
                    conditions.Value = root.getJSONObject(0).getJSONObject("Temperature").getJSONObject("Imperial").getString("Value");
                    conditions.Unit = root.getJSONObject(0).getJSONObject("Temperature").getJSONObject("Imperial").getString("Unit");
                    conditions.UnitType = root.getJSONObject(0).getJSONObject("Temperature").getJSONObject("Imperial").getString("UnitType");

                    condit.add(conditions);
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return condit;


        }

        @Override
        protected void onPostExecute(ArrayList<Condtions> condtions) {

            Log.d(TAG, "Conditions:  " + condtions);

            TextView location;
            TextView condition;
            ImageView image;
            TextView temp;
            TextView updated;

            location = findViewById(R.id.location);
            condition = findViewById(R.id.tv_dayday);
            temp = findViewById(R.id.temperature);
            updated = findViewById(R.id.updated);
            image = findViewById(R.id.icon);

            String imageURL =  "http://developer.accuweather.com/sites/default/files/"+ condtions.get(0).getIcon() + "-s.png";

            try {
                URL urlImage = new URL(imageURL);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            Log.d(TAG, "City: " + et_country_curr);

            location.setText(condtions.get(0).getCity()+"," + condtions.get(0).getCountry());

            condition.setText(condtions.get(0).getText());
            temp.setText("Temperature: " + condtions.get(0).getValue() +" "+ condtions.get(0).getUnit());
            updated.setText(condtions.get(0).getLODTime());
            Log.d(TAG, "imageurl: " + imageURL);
            Picasso.get().load(imageURL).into(image);

        }
    }




    class GetCities extends AsyncTask<ArrayList<String>, Void, ArrayList<City>> {

        @Override
        protected ArrayList<City> doInBackground(ArrayList<String>... arrayLists) {
            HttpURLConnection connection = null;
            String cit = arrayLists[0].get(0);
            String coun = arrayLists[0].get(1);

            ArrayList<City> cityList = new ArrayList<>();

            try {
                String url = baseUrl + "/locations/v1/cities/" + coun + "/search?apikey=" + getResources().getString(R.string.api_key) + "&q=" + cit;
                Log.d(TAG, "URL1 " + url);
                URL urlB = new URL(url);


                connection = (HttpURLConnection) urlB.openConnection();
                connection.connect();

                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    String json = IOUtils.toString(connection.getInputStream(), "UTF8");

                    JSONArray root = new JSONArray(json);

                    for (int i = 0; i < root.length(); i++) {
                        JSONObject cityJson = root.getJSONObject(i);

                        if (!cityJson.equals(null)) {

                            City city1 = new City();
                            city1.citykey = cityJson.getString("Key");
                            city1.cityname = cityJson.getString("LocalizedName");
                            city1.state = cityJson.getJSONObject("AdministrativeArea").getString("ID");
                            //Log.d(TAG, "City Key: " + city1.citykey);
                            cityList.add(city1);

                        } else {
                            //       Toast.makeText(this, "Check Internet Connection", Toast.LENGTH_SHORT).show();
                        }
                    }

                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return cityList;
        }

        @Override
        protected void onPostExecute(ArrayList<City> cities) {
            super.onPostExecute(cities);
            ArrayList<String> CityState = new ArrayList<>();
             final String str[] = new String[cities.size()];
             final String key[] = new String[cities.size()];
             int i = 0;

            for (City cityList : cities) {

                Log.d(TAG, "City: " + cityList.getCityname());
                Log.d(TAG, "State: " + cityList.getState());
               // CityState.add(0,cityList.getCityname() + ", " + cityList.getState() + "/" + cityList.getCitykey());
               // Log.d(TAG, "onPostExecute: " + CityState);
                str[i] =  cityList.getCityname() + ", " + cityList.getState();
                key[i] =  cityList.getCitykey();

                i++;
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

            builder.setTitle("Select City").setItems(str, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Log.d(TAG, "onClick: "+ str[which]);
                     keyword = str[which];
                    String ke = key[which];

                    Log.d(TAG, "onClick: " + keyword);
                    Log.d(TAG, "onClick: " + ke);

                    Intent intent = new Intent(MainActivity.this,CityWeather.class);
                    intent.putExtra("city", keyword);
                    intent.putExtra("key", key[which]);
                    intent.putExtra("base", baseUrl);
                    Log.d(TAG, "Click ");
                    startActivity(intent);
                }
            });
            builder.create().show();


        }
    }



}