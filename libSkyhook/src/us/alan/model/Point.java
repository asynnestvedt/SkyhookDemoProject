package us.alan.model;

public class Point {
    public double latitude;
    public double longitude;
    public double elevation = 0;

    public Point(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Point(double latitude, double longitude, double elevation) {
        this(latitude, longitude);
        this.elevation = elevation;
    }
}
