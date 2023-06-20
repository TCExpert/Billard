package com.example.billiard;


import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Ball {

    protected static Map<String, Long> recentCollisions = new HashMap<>();
    public static final long COLLISION_TIMEOUT = 1000000;
    private final String id; // id zur eindeutigen Identifikation eines Objekts unabhängig von anderen Parametern
    ArrayList<Long> collisionTimestamp = new ArrayList<>();
    private double x; // x-Position der Kugel
    private double y; // y-Position der Kugel
    private double radius; // Radius der Kugel
    private double dx; // x-Komponente der Geschwindigkeit
    private double dy; // y-Komponente der Geschwindigkeit
    private final String imageURL;

    // Constructor
    public Ball(double x, double y, double radius, String id, String imageURL) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.imageURL = imageURL;
        dx = 0; // Anfangsgeschwindigkeit auf 0 setzen
        dy = 0;
        collisionTimestamp.add(0, 295765431406600L);
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
        double distance = Math.sqrt(Math.pow(x - ball.x, 2) + Math.pow(y - ball.y, 2));
        return distance <= radius*2;
    }

    public void checkCollision(Ball otherBall, long now) {
        BallPair collisionPair = new BallPair(this, otherBall);
        BallPair reverseCollisionPair = new BallPair(otherBall, this); // Neues BallPair-Objekt mit umgekehrter Reihenfolge der Bälle erstellen

        if (recentCollisions.containsKey(collisionPair.pairId) || recentCollisions.containsKey(reverseCollisionPair.pairId)) {
            long lastCollisionTime = recentCollisions.getOrDefault(collisionPair.pairId, 0L);
            long lastReverseCollisionTime = recentCollisions.getOrDefault(reverseCollisionPair.pairId, 0L);

            lastCollisionTime = Math.max(lastCollisionTime, lastReverseCollisionTime);

            if (now - lastCollisionTime < COLLISION_TIMEOUT) {
                // Positionsanpassung überspringen, aber den Rest der Berechnungen durchführen
                handleCollisionWith(otherBall, now, collisionPair);
            }
        }
        // Positionsanpassung und Berechnungen durchführen
        adjustPosition(otherBall);
        handleCollisionWith(otherBall, now, collisionPair);
    }


    private boolean overlaps(Ball otherBall) {
        double overlap = radius + otherBall.getRadius() - Math.sqrt(Math.pow(x - otherBall.getX(), 2) + Math.pow(y - otherBall.getY(), 2));
        return overlap > 0;
    }

    public void adjustPosition(Ball otherBall) {
        double collisionAngle = Math.atan2(otherBall.getY() - y, otherBall.getX() - x);
        double overlap = radius + otherBall.getRadius() - Math.sqrt(Math.pow(x - otherBall.getX(), 2) + Math.pow(y - otherBall.getY(), 2));

        if (overlaps(otherBall)) {
            double separation = overlap / 2;
            double separationX = separation * Math.cos(collisionAngle);
            double separationY = separation * Math.sin(collisionAngle);

            // Die Kugeln entlang der Trennungsachse verschieben
            x -= separationX;
            y -= separationY;
        }
    }


    // Methode zur Behandlung der Kollision mit einer anderen Kugel
    public void handleCollisionWith(Ball otherBall, long now, BallPair collisionPair) {
        double collisionAngle = Math.atan2(otherBall.getY() - y, otherBall.getX() - x);
        // Überprüfen, ob die gleiche Kollision innerhalb der Timeout-Zeitspanne erneut auftritt

        // Kollision dokumentieren
        recentCollisions.put(collisionPair.pairId, now);


        // Berechnung der Kollisionsnormen und Tangentialen
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

    public void handleWallCollision(PoolTable poolTable) {
        double collisionX = Math.max(radius, Math.min(x, poolTable.getWidth() - radius));
        double collisionY = Math.max(radius, Math.min(y, poolTable.getHeight() - radius));
        double distanceX = collisionX - x;
        double distanceY = collisionY - y;
        x += distanceX;
        y += distanceY;
        if (collisionX == radius || collisionX == poolTable.getWidth() - radius) {
            dx = -dx;
        }
        if (collisionY == radius || collisionY == poolTable.getHeight() - radius) {
            dy = -dy;
        }
    }


    // Getter und Setter

    public boolean collidesWithPocket(PoolTable poolTable) {
        List<Pocket> pockets = poolTable.getPockets();
        for (Pocket pocket : pockets) {
            double px = pocket.getX();
            double py = pocket.getY();
            double distance = Math.sqrt(Math.pow(x - px, 2) + Math.pow(y - py, 2));
            if (distance <= radius * 2) {
                return true;
            }
        }
        return false;
    }

    public String getId() {
        return id;
    }

    private double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getDx() {
        return dx;
    }

    public double getDy() {
        return dy;
    }

    // Draw-Methode
    public void draw(GraphicsContext gc) {
        Image ballimage = new Image(imageURL);
        gc.drawImage(ballimage, x - radius, y - radius, radius * 2, radius * 2);
    }

    public static class BallPair {
        private final String pairId;

        public BallPair(Ball ball1, Ball ball2) {
            this.pairId = generatePairId(ball1.getId(), ball2.getId());
        }

        private String generatePairId(String id1, String id2) {
            if (id1.equals("cueBall")) {
                return id1 + id2;
            } else if (id2.equals("cueBall")) {
                return id2 + id1;
            } else {
                int num1 = Integer.parseInt(id1);
                int num2 = Integer.parseInt(id2);
                if (num1 < num2) {
                    return id1 + id2;
                } else {
                    return id2 + id1;
                }
            }
        }
    }

}
