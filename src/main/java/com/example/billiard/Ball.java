package com.example.billiard;


import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Ball {
    private double x; // x-Position der Kugel
    private double y; // y-Position der Kugel
    private final double radius; // Radius der Kugel
    private double angle; // Winkel der Kugel
    private double power; // Schlagkraft des Cue
    private final Color color; // Farbe der Kugel

    private double dx; // x-Komponente der Geschwindigkeit
    private double dy; // y-Komponente der Geschwindigkeit

    // Constructor
    public Ball(double x, double y, double radius, Color color) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.color = color;
        dx = 0; // Anfangsgeschwindigkeit auf 0 setzen
        dy = 0;
    }

    // Bewegungsmethoden
    public void setVelocity(double dx, double dy) {
        this.dx = dx; // Geschwindigkeit wird aktualisiert
        this.dy = dy;
    }

    public void shoot(double angle, double power) {
        this.angle = angle;
        this.power = power;
        dx = Math.cos(angle) * power; // Berechnung der Geschwindigkeit basierend auf Winkel und Kraft
        dy = Math.sin(angle) * power;
        setVelocity(dx, dy);
    }

    public void applyFriction() {
        // Basis-Reibungskoeffizient
        double baseFrictionCoefficient = 0.01;

        // Geschwindigkeitsabhängiger Faktor
        double speedFactor = 1 + 0.1 * Math.sqrt(dx * dx + dy * dy);

        // Verringern Sie die Geschwindigkeit der Kugel um den kombinierten Reibungskoeffizienten
        dx = dx * (1 - baseFrictionCoefficient * speedFactor);
        dy = dy * (1 - baseFrictionCoefficient * speedFactor);
    }


    public void move() {
        x += dx; // Bewegung der Kugel basierend auf der Geschwindigkeit
        y += dy;
        applyFriction();
    }

    // Methoden prüfen, ob es eine Kollision überhaupt gibt
    public boolean collidesWith(Ball ball) {
        double distance = Math.sqrt(Math.pow(x - ball.x, 2) + Math.pow(y - ball.y, 2)); // Berechnen des Abstands zwischen den Kugeln
        return distance < radius + ball.radius; // Kollision, wenn der Abstand kleiner als die Summe der Radien ist
    }

    public boolean collidesWithWall(PoolTable poolTable) {
        double height = poolTable.getHeight();
        double width = poolTable.getWidth();
        return x - radius < 0 || x + radius > width || y - radius < 0 || y + radius > height; // Kollision, wenn die Kugel den Tischrand berührt
    }

    // Getter und Setter
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getDx() {
        return dx;
    }

    public double getDy() {
        return dy;
    }

    // Draw-Methode
    public void draw(GraphicsContext gc) {
        gc.setFill(color); // Setzen der Farbe der Kugel
        gc.fillOval(x - radius, y - radius, radius * 2, radius * 2); // Zeichnen der Kugel als Kreis
    }
}
