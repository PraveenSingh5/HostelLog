package com.example.hostellog;

public class LocationUtils {


    // Earth’s radius in kilometers
    private static final double EARTH_RADIUS = 6371.0;

    // Convert degrees to radians
    private static double toRadians(double degrees) {
        return degrees * Math.PI / 180;
    }

    // Convert radians to degrees
    private static double toDegrees(double radians) {
        return radians * 180 / Math.PI;
    }

    // Calculate the latitude offset for a given distance
    public static double calculateLatitudeOffset(double distanceKm) {
        return toDegrees(distanceKm / EARTH_RADIUS);
    }

    // Calculate the longitude offset for a given distance
    public static double calculateLongitudeOffset(double latitude, double distanceKm) {
        double radiusAtLatitude = EARTH_RADIUS * Math.cos(toRadians(latitude));
        return toDegrees(distanceKm / radiusAtLatitude);
    }



}
