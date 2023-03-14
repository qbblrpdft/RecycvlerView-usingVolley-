//package com.example.capstoneapp;
//
//import android.graphics.Color;
//import com.github.mikephil.charting.data.ScatterData;
//import com.github.mikephil.charting.data.ScatterDataSet;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//import java.util.Random;
//
//
//public class ScatterChart {
//
//    private ArrayList<Map> generateEntries(float[] xValues, float[] yValues) {
//        ArrayList<Map> entries = new ArrayList<>();
//        for (int i = 0; i < xValues.length; i++) {
//            entries.add(new Map(xValues[i], yValues[i]));
//        }
//        return entries;
//    }
//
//    private int getRandomColor() {
//        Random rnd = new Random();
//        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
//    }
//    private ScatterData createScatterData(List<Cluster> clusters) {
//        ScatterData data = new ScatterData();
//
//        for (int i = 0; i < clusters.size(); i++) {
//            Cluster cluster = clusters.get(i);
//            List<Point> points = cluster.getPoints();
//
//            float[] xValues = new float[points.size()];
//            float[] yValues = new float[points.size()];
//
//            for (int j = 0; j < points.size(); j++) {
//                Point point = points.get(j);
//                xValues[j] = (float) point.getX();
//                yValues[j] = (float) point.getY();
//            }
//
//            ScatterDataSet dataSet = new ScatterDataSet(generateEntries(xValues, yValues), "Cluster " + i);
//            dataSet.setColor(getRandomColor());
//            data.addDataSet(dataSet);
//        }
//
//        return data;
//    }
//}
