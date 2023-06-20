package com.example.billiard;

public class Pocket {
    private double x; // x-Position der Tasche
    private double y; // y-Position der Tasche

    public Pocket(double x, double y) {
        this.x = x;
        this.y = y;
    }

    // Getter und Setter für die Attribute
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void update(double x, double y) {
        this.y = y;
        this.x = x;
    }
}
