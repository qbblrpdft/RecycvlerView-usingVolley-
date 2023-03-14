package com.example.capstoneapp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class KMeans {
    private int k;
    private int maxIterations;
    private List<Point> points;
    private List<Cluster> clusters;

    public KMeans(int k, int maxIterations, double[][] points) {
        this.k = k;
        this.maxIterations = maxIterations;
        this.points = new ArrayList<>();

        // Convert 2D array of doubles to List of Points
        for (double[] point : points) {
            this.points.add(new Point(point[0], point[1]));
        }
    }

    public void run() {
        // Initialize centroids
        List<Point> centroids = initializeCentroids(k);

        for (int i = 0; i < maxIterations; i++) {
            // Assign points to clusters
            clusters = assignPointsToClusters(centroids);

            // Update centroids
            centroids = updateCentroids(clusters);
        }
    }

    private List<Point> initializeCentroids(int k) {
        List<Point> centroids = new ArrayList<>();
        int numPoints = points.size();
        Random random = new Random();

        for (int i = 0; i < k; i++) {
            Point randomPoint = points.get(random.nextInt(numPoints));
            centroids.add(randomPoint);
        }

        return centroids;
    }

    private List<Cluster> assignPointsToClusters(List<Point> centroids) {
        List<Cluster> clusters = new ArrayList<>();
        for (Point centroid : centroids) {
            clusters.add(new Cluster(centroid));
        }

        for (Point point : points) {
            double minDistance = Double.MAX_VALUE;
            Cluster minCluster = null;
            for (Cluster cluster : clusters) {
                double distance = point.distanceTo(cluster.getCentroid());
                if (distance < minDistance) {
                    minDistance = distance;
                    minCluster = cluster;
                }
            }
            minCluster.addPoint(point);
        }

        return clusters;
    }

    private List<Point> updateCentroids(List<Cluster> clusters) {
        List<Point> centroids = new ArrayList<>();
        for (Cluster cluster : clusters) {
            cluster.calculateCentroid();
            centroids.add(cluster.getCentroid());
            cluster.clear();
        }
        return centroids;
    }

    public List<Cluster> getClusters() {
        return clusters;
    }
}
