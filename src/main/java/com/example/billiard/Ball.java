package com.example.billiard;


import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.List;

public class Ball {

    private double x; // x-Position der Kugel
    private double y; // y-Position der Kugel
    private double radius; // Radius der Kugel
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
        double distance = Math.sqrt(Math.pow(x - ball.x, 2) + Math.pow(y - ball.y, 2)); // Berechnen des Abstands zwischen den Kugelzentren
        return distance < radius + ball.radius; // Kollision, wenn der Abstand zwischen den Radien überlappt
    }


    // Methode zur Behandlung der Kollision mit einer anderen Kugel
    public void handleCollisionWith(Ball otherBall) {
        // Berechnung der Kollisionsnormen und Tangentialen
        double collisionAngle = Math.atan2(otherBall.getY() - y, otherBall.getX() - x);
        double normalAngle = collisionAngle + Math.PI / 2;

        // Berechnung der Geschwindigkeiten in Normal- und Tangentialrichtung
        double currentBallNormalVelocity = dx * Math.cos(normalAngle) + dy * Math.sin(normalAngle);
        double currentBallTangentVelocity = dx * Math.cos(collisionAngle) + dy * Math.sin(collisionAngle);
        double otherBallNormalVelocity = otherBall.getDx() * Math.cos(normalAngle) + otherBall.getDy() * Math.sin(normalAngle);
        double otherBallTangentVelocity = otherBall.getDx() * Math.cos(collisionAngle) + otherBall.getDy() * Math.sin(collisionAngle);

        // Berechnung der finalen Geschwindigkeiten
        double currentBallVelocityXAfterCollision = otherBallNormalVelocity * Math.cos(normalAngle) + currentBallTangentVelocity * Math.cos(collisionAngle);
        double currentBallVelocityYAfterCollision = otherBallNormalVelocity * Math.sin(normalAngle) + currentBallTangentVelocity * Math.sin(collisionAngle);
        double otherBallVelocityXAfterCollision = currentBallNormalVelocity * Math.cos(normalAngle) + otherBallTangentVelocity * Math.cos(collisionAngle);
        double otherBallVelocityYAfterCollision = currentBallNormalVelocity * Math.sin(normalAngle) + otherBallTangentVelocity * Math.sin(collisionAngle);

        // Setzen der neuen Geschwindigkeiten
        setVelocity(currentBallVelocityXAfterCollision, currentBallVelocityYAfterCollision);
        otherBall.setVelocity(otherBallVelocityXAfterCollision, otherBallVelocityYAfterCollision);
    }


    // Methode zur Überprüfung der Kollision mit den Tischwänden
    public boolean collidesWithWall(PoolTable poolTable) {
        double height = poolTable.getHeight();
        double width = poolTable.getWidth();
        return x - radius <= 0 || x + radius >= width || y - radius <= 0 || y + radius >= height;
    }

    public void adjustPositionAndReflectVelocity(PoolTable poolTable) {
        double collisionX = Math.max(radius, Math.min(x, poolTable.getWidth() - radius));
        double collisionY = Math.max(radius, Math.min(y, poolTable.getHeight() - radius));
        adjustPosition(collisionX, collisionY);
        reflectVelocity(collisionX, collisionY, poolTable);
    }

    private void adjustPosition(double collisionX, double collisionY) {
        double distanceX = collisionX - x;
        double distanceY = collisionY - y;
        x += distanceX;
        y += distanceY;
    }

    private void reflectVelocity(double collisionX, double collisionY, PoolTable poolTable) {
        if (collisionX == radius || collisionX == poolTable.getWidth() - radius) {
            dx = -dx;
        }
        if (collisionY == radius || collisionY == poolTable.getHeight() - radius) {
            dy = -dy;
        }
    }



    public boolean collidesWithPocket(PoolTable poolTable) {
        List<Pocket> pockets = poolTable.getPockets();
        for (Pocket pocket : pockets){
            double px = pocket.getX();
            double py = pocket.getY();
            double distance = Math.sqrt(Math.pow(x - px, 2) + Math.pow(y - py, 2));
            if (distance <= radius*2){
                return true;
            }
        }
        return false;
    }

    // Getter und Setter

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

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
        gc.fillOval(x-radius , y -radius, radius * 2, radius * 2); // Zeichnen der Kugel als Kreis
    }
}
