package com.example.hydrationtracker_git.Water;

/**
 * Die Klasse {@code Wasserbedarfsrechner} bietet eine Methode zur Berechnung des täglichen Wasserbedarfs
 * mit den Parametern Alter und Körpergröße einer Person.
 */
public class Wasserbedarfsrechner {

    /**
     * Berechnet den täglichen Wasserbedarf mit dem Parameter Alter und Körpergröße.
     *
     * @param alter Das Alter des Users in Jahren.
     * @param koerpergroesse Die Körpergröße des Users in Zentimetern.
     * @return Der daily Wasserbedarf in Millilitern.
     */
    public static int berechneWasserbedarf(int alter, int koerpergroesse) {
        double koerpergewicht = (koerpergroesse - 100 + alter / 10.0) * 0.9;
        int wasserbedarf = (int) (koerpergewicht * 35);
        return wasserbedarf;
    }
}