package com.example.capstoneapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class GlobalClass {
    private static final String SHARED_PREFS_NAME = "mySharedPrefs";
    private static String email;
    private static String token;
    private static int userId;
    private static int id;
    private static final String BASE_URL = "https://scoutdroid-be.onrender.com/api";
    private static OkHttpClient client = new OkHttpClient();

    public static String getServerURL() {
        return BASE_URL;
    }
    public static void setToken(String token) {
        GlobalClass.token = token;
    }

    // function to make a GET request
//    public static Call getRequest(String url,Map<String, Object> jsonParams, String token, Context context, Callback callback) {
//        OkHttpClient client = new OkHttpClient();
//
//        Request request = new Request.Builder()
//                .url(url)
//                .addHeader("Authorization", "Bearer " + token) // include token in header
//                .get()
//                .build();
//
//        Call call = client.newCall(request);
//        call.enqueue(callback);
//        return call;
//    }
    public static Call getRequest(String url, Map<String, Object> queryParams, String token, Context context, Callback callback) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer " + token) // include token in header
                .get()
                .build();

        Call call = client.newCall(request);
        call.enqueue(callback);
        return call;
    }




    // function to make a POST request
    public static void postRequest(String url, Map<String, Object> jsonParams, Context context, Callback callback) {
        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),
                (new JSONObject(jsonParams)).toString());

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer " + token) // include token in header
                .post(body)
                .build();

        client.newCall(request).enqueue(callback);
    }

    // function to make a PATCH request
    public void patchRequest(String url, Map<String, Object> jsonParams, Context context, Callback callback) {
        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                (new JSONObject(jsonParams)).toString()
        );

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer " + token)
                .patch(body)
                .build();

        client.newCall(request).enqueue(callback);
    }

    // function to make a DELETE request
    public static void deleteRequest(String url, Map<String, Object> jsonParams, Context context, Callback callback) {
        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),
                (new JSONObject(jsonParams)).toString());

        Request request = new Request.Builder()
                .url(url + "/" + id)
                .addHeader("Authorization", "Bearer " + token) // include token in header
                .delete(body)
                .build();

        client.newCall(request).enqueue(callback);
    }

// Helper method to get token from shared preferences
    public static String getTokenFromSharedPreferences(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getString(token, null);
}
    public static String getEmailFromSharedPreferences(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getString(email, null);
    }
    // Helper method to save token to shared preferences
    public static void saveToken(String token, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        try {
            editor.putString("token", token);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
