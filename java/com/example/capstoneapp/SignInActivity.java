package com.example.capstoneapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.ArrayMap;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.capstoneapp.Model.User;
import com.example.capstoneapp.Request.VolleyRequest;
import com.example.capstoneapp.Response.MyResponseListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SignInActivity extends AppCompatActivity {
    private int signId;
    private String token;
    private String email;
    private static final GlobalClass global = new GlobalClass();
    private Button btnSignIn;

    private EditText emailLogin;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        btnSignIn = findViewById(R.id.btnEnter);
        emailLogin = findViewById(R.id.editEmailAddress);
        password = findViewById(R.id.editPassword);

        ImageView showPassword = findViewById(R.id.show_icon);
        showPassword.setImageResource(R.drawable.hide);
        showPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(password.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())){
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    showPassword.setImageResource(R.drawable.hide);
                } else{
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    showPassword.setImageResource(R.drawable.show);
                }
            }
        });

        btnSignIn.setOnClickListener(v -> login());
    }

    public void login() {
        String emailSignIn = emailLogin.getText().toString();
        String passwordLogin = password.getText().toString();

        if (TextUtils.isEmpty(emailSignIn)) {
            emailLogin.setError("Please enter your email");
            emailLogin.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(passwordLogin)) {
            password.setError("Please enter a password");
            password.requestFocus();
            return;
        }


        VolleyRequest.loginUser(SignInActivity.this, emailSignIn, passwordLogin, new MyResponseListener() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject data = jsonObject.getJSONObject("data");
                    String userID = data.getString("id");
                    String email = data.getString("email");
                    String token = data.getString("token"); // Extract the token from the response
                    String date = data.getString("dateCreated");

                    // Create a new User object with the extracted data
                    User user = new User(userID, email, null, token);
                    user.setUserID(userID);
                    user.setEmail(email);
                    user.setPassword(null);
                    user.setToken(token);
                    user.setDate(date);

                    // Do something with the extracted data and token
                    Log.d("postRequest", "Login successful. User ID: " + userID);
                    Log.d("postRequest", "Token: " + token);

                    // Store the token for future use, such as for accessing protected resources on the server
                    // You can use SharedPreferences, a local database, or another mechanism to store the token
                    SharedPreferences sharedPreferences = SignInActivity.this.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("id", userID);
                    editor.putString("email", email);
                    editor.putString("dateCreated", date);
                    editor.putString("token", token);
                    editor.apply();

                        // Check if login was successful
                        if (token != null && !token.isEmpty()) {
                            VolleyRequest.setToken(token);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    successToast("WELCOME!");
                                    Intent intent = new Intent(SignInActivity.this, HomeActivity.class);
                                    intent.putExtra("user_id", userID);
                                    intent.putExtra("token", token);
                                    startActivity(intent);
                                }
                            });
                        }
                    } catch (JSONException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(SignInActivity.this, "Failed to parse JSON response!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            } @Override
            public void onError(String errorMessage) {
                if (errorMessage != null && !errorMessage.isEmpty()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (errorMessage.equals("User does not exist")) {
                                errorToast("Email account does not exist!");
                            } else if (errorMessage.equals("Incorrect email or password")) {
                                errorToast("Incorrect email or password!");
                            } else {
                                errorToast("Something went wrong logging in to your account!");
                            }
                        }
                    });
                } else {
                    errorToast("An error occurred while logging in to your account!");
                }
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
        toast.setDuration(Toast.LENGTH_SHORT);
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
