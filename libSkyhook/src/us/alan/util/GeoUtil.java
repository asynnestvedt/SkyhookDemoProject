package us.alan.util;

import us.alan.model.Point;

public class GeoUtil {

    /**
     * taken from https://stackoverflow.com/questions/3694380/calculating-distance-between-two-points-using-latitude-longitude
     */

    /**
     * Calculate distance between two points in latitude and longitude taking
     * into account height difference. If you are not interested in height
     * difference pass 0.0. Uses Haversine method as its base.
     *
     * lat1, lon1 Start point lat2, lon2 End point el1 Start altitude in meters
     * @returns double - Distance in Meters
     */
    public static double distance(Point p1, Point p2) {

        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(p2.latitude - p1.latitude);
        double lonDistance = Math.toRadians(p2.longitude - p1.longitude);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(p1.latitude)) * Math.cos(Math.toRadians(p2.longitude))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        double height = p1.elevation - p2.elevation;

        distance = Math.pow(distance, 2) + Math.pow(height, 2);

        return Math.sqrt(distance);
    }
}
