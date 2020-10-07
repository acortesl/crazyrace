package com.crazyrace.util;

public class Conversor {

    public static double dpToPixel(double dp, float density){
        return dp * density;
    }

    public static double pixelToDp(double pixel, float density){
        return pixel / density;
    }
}
