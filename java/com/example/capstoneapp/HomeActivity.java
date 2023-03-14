package com.example.capstoneapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
//import android.view.View;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;
import android.widget.Button;

import com.example.capstoneapp.Model.Matches;
import com.example.capstoneapp.Model.Plotting;
import com.example.capstoneapp.Model.User;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HomeActivity extends AppCompatActivity {
    List<Matches> matchesList = new ArrayList<>();
    private String token;
    private static final GlobalClass global = new GlobalClass();
    String matchId;
    private String userId;
    String email;
    String teamName;
    String opponent;
    String date;
    String plot;
    private static int id;
    Button btnstart;
    Button btnRecords;
    Button btnCred;
    Button btnSignOut;
    String dateCreated;
//    private RecyclerView recyclerView;
//    private RecyclerView.Adapter adapter;
//    private RecyclerView.LayoutManager layoutManager;
//    private FetchDataCallback callback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
//
//        recyclerView.setHasFixedSize(true);
//        layoutManager = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(layoutManager);
//        adapter = new RecordsAdapter(matchesList, HomeActivity.this);
//        recyclerView.setAdapter(adapter);

        String token = getIntent().getStringExtra("token");

        btnstart = findViewById(R.id.startGame);
        btnRecords = findViewById(R.id.matches);
        btnCred = findViewById(R.id.credentials);
        btnSignOut = findViewById(R.id.signout);

        btnstart.setOnClickListener(v -> opengameDetailsActivity());

        btnRecords.setOnClickListener(v -> openMatchesActivity());

        btnCred.setOnClickListener(v -> openCredentialsActivity());

        btnSignOut.setOnClickListener(v -> openStartActivity());

    }
    public void opengameDetailsActivity(){
        Intent intent = new Intent(this, gameDetailsActivity.class);
        startActivity(intent);
    }

    public void openMatchesActivity() {
        Intent intent = new Intent(HomeActivity.this, MatchesActivity.class);
        intent.putExtra("token", token);
        intent.putExtra("id", userId);
        startActivity(intent);
    }



    public void openCredentialsActivity() {
        Intent intent = new Intent(this, CredentialsActivity.class);
        intent.putExtra("token", token);
        intent.putExtra("id", userId);
        startActivity(intent);
    }


    public void openStartActivity(){
        Intent intent = new Intent(this, StartActivity.class);
        startActivity(intent);
    }
//    public void fetchData(String userID) {
////        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
////        String token= sharedPreferences.getString("token", "");
////        if (!token.isEmpty()) {
////            // Value is saved in SharedPreferences
////            System.out.println(token);
////        } else {
////            // Value is not saved in SharedPreferences
////        }
//        OkHttpClient client = new OkHttpClient();
//        String url = global.getServerURL() + "/matches?userId=clesg6o3g0001052e5ztx4krl";
//        Request request = new Request.Builder()
//                .url(url)
//                .addHeader("Authorization", "Bearer " + token)
//                .build();
//
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                e.printStackTrace();
//                callback.onFailure();
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                if (response.isSuccessful()) {
//                    String responseData = response.body().string();
//                    List<Matches> matchesList = parseData(responseData);
//                    callback.onSuccess(matchesList);
//                } else {
//                    callback.onFailure();
//                }
//            }
//        });
//    }
//
//    private List<Matches> parseData(String data) {
//        List<Matches> matchesList = new ArrayList<>();
//        try {
//            JSONObject jsonObject = new JSONObject(data);
//            if (jsonObject.has("data")) {
//                JSONArray matchesArray = jsonObject.getJSONArray("data");
//                for (int i = 0; i < matchesArray.length(); i++) {
//                    JSONObject matchObject = matchesArray.getJSONObject(i);
//                    String matchId = matchObject.optString("id","");
//                    String teamName = matchObject.optString("teamName","");
//                    String opponent = matchObject.optString("opponent","");
//                    String date = matchObject.optString("dateCreated","");
//                    String plot = matchObject.optString("plottings","");
////                    JSONArray plottingArray = matchObject.optJSONArray("plottings");
////                    List<Plotting> plottings = new ArrayList<>();
////                    if (plottingArray != null) {
////                        for (int j = 0; j < plottingArray.length(); j++) {
////                            JSONObject plottingObject = plottingArray.getJSONObject(j);
////                            int plotId = plottingObject.optInt("plot_id");
////                            int latitude = plottingObject.optInt("latitude");
////                            int longitude = plottingObject.optInt("longitude");
////                            String type = plottingObject.optString("type");
////                            Plotting plotting = new Plotting(plotId, latitude, longitude, type);
////                            plottings.add(plotting);
////                        }
////                    }
//                    Matches matches = new Matches(matchId, teamName, opponent, date, plot);
//                    matchesList.add(matches);
//                }
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return matchesList;
//    }
//
//    public interface FetchDataCallback {
//        void onSuccess(List<Matches> matchesList);
//        void onFailure();
//    }

}