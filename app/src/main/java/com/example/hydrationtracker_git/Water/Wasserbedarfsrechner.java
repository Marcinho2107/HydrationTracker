package com.example.hydrationtracker_git.Water;

public class Wasserbedarfsrechner {


    public static int berechneWasserbedarf(int alter, int koerpergroesse) {

        double koerpergewicht = (koerpergroesse - 100 + alter / 10.0) * 0.9;
        int wasserbedarf = (int) (koerpergewicht * 35); // in ml
        return wasserbedarf;
    }
}
