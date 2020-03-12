package com.example.inclass08;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;
import java.util.ArrayList;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        email = findViewById(R.id.et_email);
        password = findViewById(R.id.et_password);

        login = findViewById(R.id.bt_login);

        ArrayList<Emails> emails = new ArrayList<>();

//        login.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//             String emailString = email.getText().toString();
//             String passString = password.getText().toString();

                final OkHttpClient client = new OkHttpClient();
//                RequestBody formBody = new FormBody.Builder()
//                        .add("email", emailString)
//                        .add("password", passString)
//                        .build();
//
//                Request request = new Request.Builder()
//                        .url("http://ec2-18-234-222-229.compute-1.amazonaws.com/api/login")
//                        .post(formBody)
//                        .build();
//
//                client.newCall(request).enqueue(new Callback() {
//                    @Override public void onFailure(Call call, IOException e) {
//
//                    }
//
//                    @Override public void onResponse(Call call, Response response) throws IOException {
//                        try (ResponseBody responseBody = response.body()) {
//                            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
//
//                            Headers responseHeaders = response.headers();
//                            for (int i = 0, size = responseHeaders.size(); i < size; i++) {
//                                System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
//                                Log.d("demo", "onResponse: " + responseHeaders.name(i) + ": " + responseHeaders.value(i));
//                            }
//
//                            Log.d("demo", "onResponse: " + responseBody.string());
//                        }
//                    }
//                });
//
//            }
//        });





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



        Request request = new Request.Builder()
                .url("http://ec2-18-234-222-229.compute-1.amazonaws.com/api/inbox")
                .header("Authorization", "BEARER " + getResources().getString(R.string.apikey))
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

                    Log.d("demo", "onResponse: " + responseBody.string());
                    //   System.out.println(responseBody.string());

                }
            }
        });

    }
}
