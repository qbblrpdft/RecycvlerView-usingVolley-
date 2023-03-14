package com.example.capstoneapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.example.capstoneapp.Model.User;
import com.example.capstoneapp.Request.VolleyRequest;
import com.example.capstoneapp.Response.MyResponseListener;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.OkHttpClient;

public class SignUpActivity extends AppCompatActivity {
//    private static GlobalClass global = new GlobalClass();
    EditText emailRegister;
    EditText password;
    EditText conPassword;
    Button signUp;
    OkHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        emailRegister = findViewById(R.id.editEmail);
        password = findViewById(R.id.editPassword);
        conPassword = findViewById(R.id.editConfirmPassword);
        signUp = findViewById(R.id.btnSignUp);
        client = new OkHttpClient();

        ImageView showPassword = findViewById(R.id.show_pw);
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

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });
    }
    public void register(){
        String userEmail = emailRegister.getText().toString().trim();
        String userPassword = password.getText().toString().trim();
        String userConPassword = conPassword.getText().toString().trim();

        if (TextUtils.isEmpty(userEmail)) {
            emailRegister.setError("Please enter your email");
            emailRegister.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(userPassword)) {
            password.setError("Please enter a password");
            password.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(userConPassword)) {
            conPassword.setError("Please confirm your password");
            conPassword.requestFocus();
            return;
        }

        if (!userPassword.equals(userConPassword)) {
            conPassword.setError("Passwords do not match");
            conPassword.requestFocus();
            return;
        }

        VolleyRequest.registerUser(this, userEmail, userPassword, new MyResponseListener() {
            public void onErrorResponse(VolleyError error) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (error instanceof NetworkError) {
                            errorToast("Registration failed due to a network error!");
                        } else if (error instanceof ServerError) {
                            errorToast("Something went wrong with registering your account!");
                        } else if (error instanceof AuthFailureError) {
                            errorToast("Password does not match, please try again!");
                        } else if (error instanceof ParseError) {
                            errorToast("Failed to parse JSON response!");
                        } else if (error instanceof NoConnectionError) {
                            errorToast("No internet connection!");
                        } else if (error instanceof TimeoutError) {
                            errorToast("Connection timeout!");
                        } else if (error.networkResponse != null && error.networkResponse.data != null) {
                            String message = new String(error.networkResponse.data);
                            if (message.contains("already exists")) {
                                errorToast("This user already exists, please try another!");
                            } else {
                                errorToast("Something went wrong with registering your account!");
                            }
                        } else {
                            errorToast("Something went wrong with registering your account!");
                        }
                    }
                });
            }
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    // Check if registration was successful
                    if (jsonObject.getInt("status") == 201) {
                        JSONObject data = jsonObject.getJSONObject("data");
                        String userID = data.getString("id");
                        String email = data.getString("email");
                        String token = data.getString("token"); // Extract the token from the response
                        String date = data.getString("dateCreated");

                        // Create a new User object with the extracted data
                        User user = new User(userID, email, userPassword, token);
                        user.setUserID(userID);
                        user.setEmail(email);
                        user.setPassword(userPassword);
                        user.setToken(token);
                        user.setDate(date);

                        // Do something with the extracted data and token
                        Log.d("VolleyRequest", "Registration successful. User ID: " + userID + ", Email: " + email);
                        Log.d("VolleyRequest", "Token: " + token);

                        // Store the token for future use, such as for accessing protected resources on the server
                        // You can use SharedPreferences, a local database, or another mechanism to store the token
                        // Create a new User object with the extracted data and store it in SharedPreferences
                        SharedPreferences sharedPreferences = SignUpActivity.this.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("id", userID);
                        editor.putString("email", email);
                        editor.putString("dateCreated", date);
                        editor.putString("token", token);
                        editor.apply();

                        runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    successToast("You have successfully registered!");
                                    Log.d("SignUpActivity", "Starting StartActivity...");
                                    Intent intent = new Intent(SignUpActivity.this, StartActivity.class);
                                    startActivity(intent);
                                }
                            });
                    } else {
                        // Handle unsuccessful registration
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                errorToast("Something went wrong while registering your account!");
                            }
                        });
                    }
                } catch (JSONException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            errorToast("Failed to parse JSON response!");
                        }
                    });
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