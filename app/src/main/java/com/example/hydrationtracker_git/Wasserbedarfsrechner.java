package com.example.hydrationtracker_git;

public class Wasserbedarfsrechner {

    // Methode zur Berechnung des täglichen Wasserbedarfs
    public static int berechneWasserbedarf(int alter, int koerpergroesse) {
        // Beispiel-Formel: 35ml Wasser pro kg Körpergewicht pro Tag
        // Schätzung: Körpergewicht in kg = (Körpergröße - 100 + Alter / 10) * 0.9
        double koerpergewicht = (koerpergroesse - 100 + alter / 10.0) * 0.9;
        int wasserbedarf = (int) (koerpergewicht * 35); // in ml
        return wasserbedarf;
    }
}
