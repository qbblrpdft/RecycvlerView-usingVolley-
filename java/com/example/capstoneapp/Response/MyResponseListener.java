package com.example.capstoneapp.Response;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.example.capstoneapp.Request.VolleyRequest;

public class MyResponseListener implements VolleyRequest.VolleyResponseListener {
    @Override
    public void onResponse(String response) {
            Log.d("VolleyRequest", "Success: " + response);
    }

    @Override
    public void onError(String errorMessage) {
//        if(errorMessage != null){
//            if (errorMessage.contains("400")) {
//                Log.d("VolleyError", "Bad request: " + errorMessage);
//            } else if (errorMessage.contains("403")) {
//                Log.d("VolleyError", "Forbidden: " + errorMessage);
//            } else if (errorMessage.contains("404")) {
//                Log.d("VolleyError", "Not found: " + errorMessage);
//            } else {
//                Log.d("VolleyError", "Error: " + errorMessage);
//            }
//        } else {
//            Log.d("VolleyError", "Error message is null");
//        }
    }
}


