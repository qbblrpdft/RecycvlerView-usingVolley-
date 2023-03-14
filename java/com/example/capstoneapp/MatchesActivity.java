package com.example.capstoneapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.capstoneapp.Model.Matches;
import com.example.capstoneapp.Model.Plotting;
import com.example.capstoneapp.Request.VolleyRequest;
import com.example.capstoneapp.Response.MyResponseListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MatchesActivity extends AppCompatActivity {
    ArrayList<Matches> matchesList;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);

        recyclerView = findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        matchesList = new ArrayList<>();

        getMatchesData();

        adapter = new RecordsAdapter(matchesList, MatchesActivity.this);
        recyclerView.setAdapter(adapter);
    }

    private void getMatchesData() {
        String userId = getIntent().getStringExtra("userId");
        VolleyRequest.getMatchByUserID(this, userId, new MyResponseListener() {
            public void onResponse(JSONObject response) {
                try {
                    int statusCode = response.getInt("statusCode");
                    if (statusCode == 200) {
                        JSONArray array = response.getJSONArray("data");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject singleObject = array.getJSONObject(i);
                            Matches singleModel = new Matches(
                                    singleObject.getString("matchId"),
                                    singleObject.getString("teamName"),
                                    singleObject.getString("opponent"),
                                    singleObject.getString("date")
                            );
                            matchesList.add(singleModel);
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyDataSetChanged();
                                Log.d("url", "onResponse: " + matchesList.size());
                            }
                        });
                    } else {
                        String message = response.getString("message");
                        Log.e("url", "Error: " + message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("url", "Response: " + response);
                }
            }

            @Override
            public void onError(String error) {
                Log.e("url", "Error: " + error);
            }
        });
    }
}


