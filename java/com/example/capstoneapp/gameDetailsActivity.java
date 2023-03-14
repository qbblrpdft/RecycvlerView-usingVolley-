package com.example.capstoneapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.capstoneapp.Model.Matches;
import com.example.capstoneapp.Model.Plotting;
import com.example.capstoneapp.Request.VolleyRequest;
import com.example.capstoneapp.Response.MyResponseListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class gameDetailsActivity extends AppCompatActivity {
    List<Matches> matches = new ArrayList<>();

    private SharedPreferences sharedPreferences;
    private static final GlobalClass global = new GlobalClass();
    Button start;
    EditText teamName;
    EditText opponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_details);
        // Get shared preferences
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        start = findViewById(R.id.btnStart);
        teamName = findViewById(R.id.editTeamName);
        opponent = findViewById(R.id.editOpponent);
//        getMatchDetails();

        start.setOnClickListener(v -> createMatchGame());
    }

    public void createMatchGame() {
        String gameTeamName = teamName.getText().toString().trim();
        String gameOpponent = opponent.getText().toString().trim();

        // Make API request
        VolleyRequest.createMatch(gameDetailsActivity.this, gameTeamName, gameOpponent, new MyResponseListener() {
            @Override
            public void onResponse(String response) {
                // Handle successful response
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject data = jsonObject.getJSONObject("data");
                    String matchId = data.getString("id");
                    String teamName = data.getString("teamName");
                    String opponent = data.getString("opponent");
                    String date = data.getString("dateCreated");

                    Matches match = new Matches(matchId, teamName, opponent, null);
                    match.setMatchId(matchId);
                    match.setTeamName(teamName);
                    match.setOpponent(opponent);
                    match.setDate(date);

                    SharedPreferences sharedPreferences = gameDetailsActivity.this.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("id", matchId);
                    editor.putString("teamName", teamName);
                    editor.putString("opponent", opponent);
                    editor.putString("dateCreated", date);
                    editor.apply();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            successToast("Match created!");
                            Intent intent = new Intent(gameDetailsActivity.this, PlotActivity.class);
                            intent.putExtra("matchId", matchId);
                            startActivity(intent);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            errorToast("Cannot create match");
                        }
                    });
                }
            }

            @Override
            public void onError(String error) {
                // Handle error response
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        errorToast("Cannot create match");
                    }
                });
            }
        });
    }

    public void successToast(String message) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.success_toast,
                (ViewGroup) findViewById(R.id.success_toast_container));

        TextView text = (TextView) layout.findViewById(R.id.success_text);
        text.setText(message);

        Toast toast = new Toast(getApplicationContext());
//        toast.setGravity(Gravity.TOP, 0, 200);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }
    public void errorToast(String message) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.error_toast,
                (ViewGroup) findViewById(R.id.error_toast_container));

        TextView text = (TextView) layout.findViewById(R.id.error_text);
        text.setText(message);

        Toast toast = new Toast(getApplicationContext());
//        toast.setGravity(Gravity.TOP, 0, 200);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }
}