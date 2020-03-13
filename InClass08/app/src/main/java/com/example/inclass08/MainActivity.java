package com.example.inclass08;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class MainActivity extends AppCompatActivity {

    EditText email;
    EditText password;
    Button login;
    Button signup;
    String name = "", token = "";

    public static final String MyPREFERENCES = "MyPrefs" ;
    LayoutInflater layoutInflater;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        email = findViewById(R.id.et_email);
        password = findViewById(R.id.et_password);

        login = findViewById(R.id.bt_login);


       login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             String emailString = email.getText().toString();
             String passString = password.getText().toString();

        final OkHttpClient client = new OkHttpClient();
                RequestBody formBody = new FormBody.Builder()
                        .add("email", emailString)
                        .add("password", passString)
                        .build();

                Request request = new Request.Builder()
                        .url("http://ec2-18-234-222-229.compute-1.amazonaws.com/api/login")
                        .post(formBody)
                        .build();

                client.newCall(request).enqueue(new Callback() {
                    @Override public void onFailure(Call call, IOException e) {

                    }

                    @Override public void onResponse(Call call, Response response) throws IOException {
                        try (ResponseBody responseBody = response.body()) {
                            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                            Headers responseHeaders = response.headers();

                            for (int i = 0, size = responseHeaders.size(); i < size; i++) {
                                System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
                            //   Log.d("demo", "onResponse: " + responseHeaders.name(i) + ": " + responseHeaders.value(i));
                            }

                            String root = response.body().string();

                            JSONObject userJson = new JSONObject(root);

                            name =  userJson.getString("user_fname") + " " + userJson.getString("user_lname");
                            token = userJson.getString("token");

                            String[] split = token.split("[.]");
                            Log.d("demo", "Token " + split[1] +"."+ split[2]);
                            token = split[1] +"."+ split[2];


                            Log.d("demo", "Name main 1: " + name);
                            Intent intent = new Intent(MainActivity.this,inboxActivity.class);
                            intent.putExtra("key", name);
                            intent.putExtra("tokenkey", token);


                            startActivity(intent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }
        });

        signup = findViewById(R.id.bt_signup);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email.setVisibility(View.INVISIBLE);
                password.setVisibility(View.INVISIBLE);
                login.setVisibility(View.INVISIBLE);
                signup.setVisibility(View.INVISIBLE);






                FrameLayout rootLayout = findViewById(android.R.id.content);
                View signView = View.inflate(MainActivity.this, R.layout.signup_sheet, rootLayout);

                Button bt_confirm = signView.findViewById(R.id.bt_confirm);

                bt_confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        EditText email, fName, lName, password, retypePass;
                        email = findViewById(R.id.su_email);
                        fName = findViewById(R.id.su_fName);
                        lName = findViewById(R.id.su_lName);
                        password =  findViewById(R.id.su_choose_pass);
                        retypePass = findViewById(R.id.su_repeat_pass);




                        String emailString = email.getText().toString();
                        String fNameString = fName.getText().toString();
                        String lNameString = lName.getText().toString();
                        String pass = password.getText().toString();
                        String retype = retypePass.getText().toString();

                        if(!pass.equals(retype))
                        {
                            Toast.makeText(MainActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            String corrPass = password.getText().toString();
                        }

                        final OkHttpClient client = new OkHttpClient();

                        RequestBody formBody = new FormBody.Builder()
                                .add("email", emailString)
                                .add("password", pass)
                                .add("fname",fNameString)
                                .add("lname",lNameString)
                                .build();

                        Request request = new Request.Builder()
                                .url("http://ec2-18-234-222-229.compute-1.amazonaws.com/api/signup")
                                .post(formBody)
                                .build();

                        client.newCall(request).enqueue(new Callback() {
                            @Override public void onFailure(Call call, IOException e) {
                                e.printStackTrace();
                            }


                            @Override public void onResponse(Call call, Response response) throws IOException {
                                try (ResponseBody responseBody = response.body()) {
                                    if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                                    Headers responseHeaders = response.headers();
                                    for (int i = 0, size = responseHeaders.size(); i < size; i++) {
                                        System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
                                        Log.d("demo", "onResponse: " + responseHeaders.name(i) + ": " + responseHeaders.value(i));
                                    }

                                    String root = response.body().string();

                                    JSONObject userJson = new JSONObject(root);

                                    name =  userJson.getString("user_fname") + " " + userJson.getString("user_lname");
                                    token = userJson.getString("token");

                                    String[] split = token.split("[.]");
                                    Log.d("demo", "Token " + split[1] +"."+ split[2]);
                                    token = split[1] +"."+ split[2];


                                    Log.d("demo", "Name main 1: " + name);
                                    Intent intent = new Intent(MainActivity.this,inboxActivity.class);
                                    intent.putExtra("key", name);
                                    intent.putExtra("tokenkey", token);


                                    startActivity(intent);


                                    startActivity(intent);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                });






            }
        });

    }
}

//        Request request = new Request.Builder()
//                .url("http://ec2-18-234-222-229.compute-1.amazonaws.com/api/users")
//                .header("Authorization", "BEARER " + getResources().getString(R.string.apikey))
//                .build();
//
//        client.newCall(request).enqueue(new Callback() {
//            @Override public void onFailure(Call call, IOException e) {
//                e.printStackTrace();
//            }
//
//
//            @Override public void onResponse(Call call, Response response) throws IOException {
//                try (ResponseBody responseBody = response.body()) {
//                    if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
//
//                    Headers responseHeaders = response.headers();
//                    for (int i = 0, size = responseHeaders.size(); i < size; i++) {
//                        System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
//                        Log.d("demo", "onResponse: " + responseHeaders.name(i) + ": " + responseHeaders.value(i));
//                    }
//
//                    Log.d("demo", "onResponse: " + responseBody.string());
//                 //   System.out.println(responseBody.string());
//                }
//            }
//        });


//        Request request = new Request.Builder()
//                .url("http://ec2-18-234-222-229.compute-1.amazonaws.com/api/inbox")
//                .header("Authorization", "BEARER " + getResources().getString(R.string.apikey))
//                .build();
//
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                e.printStackTrace();
//            }
//
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                ArrayList<Emails> emails = new ArrayList<>();
//                try (ResponseBody responseBody = response.body()) {
//                    if (!response.isSuccessful())
//                        throw new IOException("Unexpected code " + response);
//
//                    Headers responseHeaders = response.headers();
//                    for (int i = 0, size = responseHeaders.size(); i < size; i++) {
//
//                        Log.d("demo", "onResponse: " + responseHeaders.name(i) + ": " + responseHeaders.value(i));
//                    }
//
//                    String root = response.body().string();
//
//                    JSONObject personJson = new JSONObject(root);
//                    JSONArray jsonArray = personJson.getJSONArray("messages");
//
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        Emails emails1 = new Emails();
//                        JSONObject object = jsonArray.getJSONObject(i);
//                        emails1.fname = object.getString("sender_fname");
//                        emails1.lname = object.getString("sender_lname");
//                        emails1.sender = object.getString("sender_id");
//
//                        Log.d("demo", "Object " + emails1.fname);
//                        Log.d("demo", "Object " + emails1.lname);
//                        Log.d("demo", "Object " + emails1.sender);
//
//                    }
//
//            } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//    });





