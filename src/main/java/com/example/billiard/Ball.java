package com.example.billiard;

// Schritt 5: Design der Ball-Klasse

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Ball {
    private double x; // x-Position der Kugel
    private double y; // y-Position der Kugel
    private double radius; // Radius der Kugel
    private Color color; // Farbe der Kugel
    private double dx; // x-Komponente der Geschwindigkeit
    private double dy; // y-Komponente der Geschwindigkeit

    public Ball(double x, double y, double radius, Color color) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.color = color;
        dx = 0; // Anfangsgeschwindigkeit auf 0 setzen
        dy = 0;
    }

    public void setVelocity(double dx, double dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public void move() {
        x += dx; // Bewegung der Kugel basierend auf der Geschwindigkeit
        y += dy;
    }

    public void draw(GraphicsContext gc) {
        gc.setFill(color); // Setzen der Farbe der Kugel
        gc.fillOval(x - radius, y - radius, radius * 2, radius * 2); // Zeichnen der Kugel als Kreis
    }
}
