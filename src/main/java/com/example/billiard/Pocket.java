package com.example.billiard;

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

    public void update(double x, double y, double width, double height){
        this.height = height;
        this.width = width;
        this.y = y;
        this.x = x;
    }
}
