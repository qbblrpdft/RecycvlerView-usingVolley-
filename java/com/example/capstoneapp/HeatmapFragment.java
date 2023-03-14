package com.example.capstoneapp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.maps.android.heatmaps.HeatmapTileProvider;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class HeatmapFragment extends Fragment {
    private String token;
    private GlobalClass global = new GlobalClass();
    private GoogleMap mMap;
    private HeatmapTileProvider mProvider;
    private TileOverlay mOverlay;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_kmeans, container, false);

        // Get the map fragment from the layout
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);

        // Load the map asynchronously
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;

                // Initialize data points
                List<LatLng> dataPoints = new ArrayList<>();

                // Send GET request to MongoDB
                Map<String, Object> params = new HashMap<>();
                global.getRequest(global.getServerURL() + "/match", params, token, getContext(), new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.e("MongoDB Stitch", "Error retrieving data points: ", e);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String responseData = response.body().string();

                        try {
                            // Parse JSON response and add data points to list
                            JSONArray jsonArray = new JSONArray(responseData);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                double latitude = jsonObject.getDouble("latitude");
                                double longitude = jsonObject.getDouble("longitude");
                                LatLng point = new LatLng(latitude, longitude);
                                dataPoints.add(point);
                            }

                            // Create a heatmap tile provider with the data points
                            mProvider = new HeatmapTileProvider.Builder()
                                    .data(dataPoints)
                                    .build();

                            // Add the heatmap tile overlay to the map
                            mOverlay = mMap.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));
                        } catch (JSONException e) {
                            Log.e("getRequest", "Error parsing JSON response: ", e);
                        }
                    }
                });
            }
        });
        return view;
    }
}
