//package com.example.capstoneapp;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.graphics.Bitmap;
//import android.graphics.Canvas;
//import android.graphics.Color;
//import android.graphics.Paint;
//import android.graphics.drawable.Drawable;
//import android.os.Bundle;
//import android.util.Log;
//import android.widget.ImageView;
//
//import com.github.mikephil.charting.data.ScatterData;
//
//import java.util.Arrays;
//import java.util.List;
//
//public class KmeansActivity extends AppCompatActivity {
//    private static final String TAG = "KmeansActivity";
//
//    private double[][] touchData;
//    private int numClusters = 2;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_kmeans);
//
//        // Get the touch data from the intent
//        touchData = (double[][]) getIntent().getSerializableExtra("touchData");
//
//        // Perform K-means clustering
//        KMeans kMeans = new KMeans(numClusters, 100, touchData);
//        kMeans.run();
//        List<Cluster> clusters = kMeans.getClusters();
//
//        ScatterChart chart = findViewById(R.id.scatterChart);
//        ScatterData data = createScatterData(clusters);
//        chart.setData(data);
//        chart.invalidate();
//    }
//}