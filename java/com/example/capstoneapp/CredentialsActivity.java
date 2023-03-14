package com.example.capstoneapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.capstoneapp.Request.VolleyRequest;
import com.example.capstoneapp.Response.MyResponseListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CredentialsActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;

    private GlobalClass global = new GlobalClass();
    private String credId;
    private EditText currentPassword;
    private EditText newPassword;
    private EditText email;

    private EditText conPassword;
    Button update;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credentials);

        currentPassword = findViewById(R.id.editCurrentPassword);
        newPassword = findViewById(R.id.editNewPassword);
        conPassword = findViewById(R.id.editConfirmPassword);
        update = findViewById(R.id.btnUpdate);
        email = findViewById(R.id.editTextEmail);
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        update.setOnClickListener(view -> updatePassword());

    }
    public void updatePassword() {
        String credEmail = email.getText().toString();
        String myPW = currentPassword.getText().toString();
        String newPW = newPassword.getText().toString();
        String confNewPW = conPassword.getText().toString();

        if (!newPW.equals(confNewPW)) {
            conPassword.setError("Passwords do not match");
            conPassword.requestFocus();
            return;
        }

        // Check if new password is null or empty
        if (newPW == null || newPW.isEmpty()) {
            Log.e("patchRequest", "New password is null or empty!");
            return;
        }


        VolleyRequest.updateUserPassword(CredentialsActivity.this, credEmail, newPW, new MyResponseListener() {
            @Override
            public void onResponse(String response) {
                Log.d("updateUserPassword", "Password updated successfully");
                successToast("Password updated successfully");
                Intent intent = new Intent(CredentialsActivity.this, HomeActivity.class);
                startActivity(intent);
            }

            @Override
            public void onError(String error) {
                errorToast("Failed to update password");
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
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }
}
