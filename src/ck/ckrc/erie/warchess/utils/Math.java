package ck.ckrc.erie.warchess.utils;

public class Math {

    public static double distanceOfEuclid(double x1, double y1, double x2, double y2){
        return java.lang.Math.sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2));
    }

    public static double distanceOfManhattan(double x1, double y1, double x2, double y2){
        return java.lang.Math.abs(x1-x2)+ java.lang.Math.abs(y1-y2);
    }

}
