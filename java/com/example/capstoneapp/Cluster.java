package com.example.capstoneapp;

import java.util.ArrayList;
import java.util.List;

public class Cluster {
    private Point centroid;
    private List<Point> points;

    public Cluster(Point centroid) {
        this.centroid = centroid;
        points = new ArrayList<>();
    }

    public Point getCentroid() {
        return centroid;
    }

    public List<Point> getPoints() {
        return points;
    }

    public void addPoint(Point point) {
        points.add(point);
    }

    public void clear() {
        points.clear();
    }

    public void calculateCentroid() {
        int sumX = 0;
        int sumY = 0;
        for (Point point : points) {
            sumX += point.getX();
            sumY += point.getY();
        }
        int numPoints = points.size();
        centroid = new Point(sumX / numPoints, sumY / numPoints);
    }
}

