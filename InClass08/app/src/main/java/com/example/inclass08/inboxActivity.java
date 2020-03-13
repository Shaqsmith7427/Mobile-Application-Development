package com.example.inclass08;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

import static android.preference.PreferenceManager.getDefaultSharedPreferences;
import static com.example.inclass08.MainActivity.MyPREFERENCES;

public class inboxActivity extends AppCompatActivity implements RecyclerAdapter.Interface{


    TextView name;
    RecyclerView recyclerView;
    RecyclerView.Adapter rv_adapter;
    RecyclerView.LayoutManager rv_layout;

    final ArrayList<Emails> emails = new ArrayList<>();
    String setting;
    String key;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);

        Intent intent = getIntent();
        setting = intent.getStringExtra("key");
        key = intent.getStringExtra("tokenkey");

        Log.d("demo", "name: " + setting);
        Log.d("demo", "key:  " + key);


        name = findViewById(R.id.tv_name2);

        name.setText(setting);

        final OkHttpClient client = new OkHttpClient();

                Request request = new Request.Builder()
                .url("http://ec2-18-234-222-229.compute-1.amazonaws.com/api/inbox")
                .header("Authorization", "BEARER " + getResources().getString(R.string.apikey) + "." + key)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }



            @Override
            public void onResponse(Call call, Response response) throws IOException {

                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful())
                        throw new IOException("Unexpected code " + response);

                    Headers responseHeaders = response.headers();
                    for (int i = 0, size = responseHeaders.size(); i < size; i++) {

                        Log.d("demo", "onResponse: " + responseHeaders.name(i) + ": " + responseHeaders.value(i));
                    }

                    String root = response.body().string();

                    JSONObject personJson = new JSONObject(root);
                    JSONArray jsonArray = personJson.getJSONArray("messages");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        Emails emails1 = new Emails();
                        JSONObject object = jsonArray.getJSONObject(i);
                        emails1.fname = object.getString("sender_fname");
                        emails1.lname = object.getString("sender_lname");
                        emails1.id = object.getString("id");
                        emails1.sender = object.getString("sender_id");
                        emails1.subject = object.getString("subject");
                        emails1.message = object.getString("message");
                        emails1.createdAt = object.getString("created_at");
                        emails1.updatedAt = object.getString("updated_at");

                        emails.add(emails1);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView = findViewById(R.id.rv_view);

                        // recyclerView.setHasFixedSize(true);

                        rv_layout = new LinearLayoutManager(inboxActivity.this);
                        recyclerView.setLayoutManager(rv_layout);

                        rv_adapter = new RecyclerAdapter(emails, inboxActivity.this);
                        recyclerView.setAdapter(rv_adapter);
                    }
                });
            }
        });

    }

    @Override
    public void deleteItem(int position) {

        final int count = 0;
        final OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()

                .url("http://ec2-18-234-222-229.compute-1.amazonaws.com/api/inbox/delete/" + count)
                .header("Authorization", "BEARER " + getResources().getString(R.string.apikey) + "." + key)
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
                    emails.remove(count);
                    rv_adapter.notifyDataSetChanged();
                }
            }
        });
        
        
        
    }

}