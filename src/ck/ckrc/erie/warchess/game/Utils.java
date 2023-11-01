package ck.ckrc.erie.warchess.game;

public class Utils {

    public static double distanceOfEuclid(double x1, double y1, double x2, double y2){
        return Math.sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2));
    }

    public static double distanceOfManhattan(double x1, double y1, double x2, double y2){
        return Math.abs(x1-x2)+Math.abs(y1-y2);
    }

}
