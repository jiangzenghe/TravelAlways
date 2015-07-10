package com.imyuu.travel.util;

public class Utils {
    public static final String DAY_NIGHT_MODE = "daynightmode";
    public static final String DEVIATION = "deviationrecalculation";
    public static final String JAM = "jamrecalculation";
    public static final String TRAFFIC = "trafficbroadcast";
    public static final String CAMERA = "camerabroadcast";
    public static final String SCREEN = "screenon";
    public static final String THEME = "theme";
    public static final String ISEMULATOR = "isemulator";


    public static final String ACTIVITYINDEX = "activityindex";

    public static final int SIMPLEHUDNAVIE = 0;
    public static final int EMULATORNAVI = 1;
    public static final int SIMPLEGPSNAVI = 2;
    public static final int SIMPLEROUTENAVI = 3;


    public static final boolean DAY_MODE = false;
    public static final boolean NIGHT_MODE = true;
    public static final boolean YES_MODE = true;
    public static final boolean NO_MODE = false;
    public static final boolean OPEN_MODE = true;
    public static final boolean CLOSE_MODE = false;

    public static void main(String args[]) {
        Utils a = new Utils();
        double lat = 37.524535;
        int zoom = 16;
        System.out.print(a.lat2tile(lat, zoom));

    }

    private int getMercatorLatitude(double lati, int zoom) {
        double maxlat = Math.PI;

        double lat = lati;

        if (lat > 90) lat = lat - 180;
        if (lat < -90) lat = lat + 180;

        // conversion degre=>radians
        double phi = Math.PI * lat / 180;

        double res;
        //double temp = Math.Tan(Math.PI / 4 - phi / 2);
        //res = Math.Log(temp);
        res = 0.5 * Math.log((1 + Math.sin(phi)) / (1 - Math.sin(phi)));
        double maxTileY = Math.pow(2, zoom);
        int result = (int) (((1 - res / maxlat) / 2) * (maxTileY));
        return (result);
    }

    //Functions to compute tiles from lat/lon
    public double long2tile(double lon, int zoom) {
        return (Math.floor((lon + 180) / 360 * Math.pow(2, zoom)));
    }

    public double lat2tile(double lat, int zoom) {
        return (Math.floor((1 - Math.log(Math.tan(lat * Math.PI / 180) + 1 / Math.cos(lat * Math.PI / 180)) / Math.PI) / 2 * Math.pow(2, zoom)));
    }

}
