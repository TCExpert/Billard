package com.example.billiard;

// Schritt 7: Design der Pocket-Klasse

public class Pocket {


    private double x; // x-Position der Tasche
    private double y; // y-Position der Tasche
    private double width; // Breite der Tasche
    private double height; // Höhe der Tasche

    public Pocket(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    // Getter und Setter für die Attribute
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    // Weitere Methoden können hier hinzugefügt werden
}
