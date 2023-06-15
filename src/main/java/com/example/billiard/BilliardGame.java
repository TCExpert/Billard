package com.example.billiard;

import javafx.scene.canvas.GraphicsContext;

public class BilliardGame {


    final PoolTable poolTable;

    final Cue cue;
    int shots = 0;
    double cueStartX; // Startposition des Cue-Sticks beim Klicken
    double cueStartY;


    public BilliardGame(double width, double height) {
        // Initialisierung der Objekte

        poolTable = new PoolTable(width, height);

        cue = new Cue(poolTable.cueBall.getX(), poolTable.cueBall.getY());
        // Weitere Initialisierungen können hier erfolgen
    }

    void update() {
        // Kugeln bewegen
        for (Ball ball : poolTable.balls) {
            ball.move();
        }

        // Kollisionsprüfung mit den Wänden des Pool-Tischs
        for (Ball currentBall : poolTable.balls) {
            handleWallCollision(currentBall, poolTable);
        }

        // Kollisionserkennung zwischen Kugeln
        for (int i = 0; i < poolTable.balls.size(); i++) {
            Ball currentBall = poolTable.balls.get(i);

            for (int j = i + 1; j < poolTable.balls.size(); j++) {
                Ball otherBall = poolTable.balls.get(j);

                if (currentBall.collidesWith(otherBall)) {
                    currentBall.handleCollisionWith(otherBall);
                }
            }
        }

        // Kollisionserkennung mit den Taschen
        if (poolTable.balls.removeIf(currentBall -> currentBall.collidesWithPocket(poolTable) && currentBall != poolTable.cueBall) && (poolTable.balls.size() == 1 && poolTable.balls.contains(poolTable.cueBall))) {
            System.out.println(shots);
        }
        if (poolTable.cueBall.collidesWithPocket(poolTable)){
            poolTable.cueBall.setX(poolTable.getWidth()/2);
            poolTable.cueBall.setY(poolTable.getHeight()/2);
        }

        // Position des Cue aktualisieren
        cue.setX(poolTable.cueBall.getX());
        cue.setY(poolTable.cueBall.getY());
    }

    public void updateSize(double width, double height, double heightCoefficient, double widthCoefficient){
        poolTable.update(width,height, heightCoefficient, widthCoefficient);
        for (Ball ball: poolTable.balls){
            ball.setRadius(width*0.0122);
        }
    }

    // Methode zur Behandlung der Kollision mit den Tischwänden
    private void handleWallCollision(Ball ball, PoolTable poolTable) {
        if (ball.collidesWithWall(poolTable)) {
            ball.adjustPositionAndReflectVelocity(poolTable);
        }
    }

    double getCueAngle(double mouseX, double mouseY) {
        double dx = mouseX - poolTable.cueBall.getX();
        double dy = mouseY - poolTable.cueBall.getY();
        return Math.atan2(dy, dx);
    }

    double calculateCuePower(double startX, double startY, double endX, double endY) {
        double distance = Math.sqrt(Math.pow(endX - startX, 2) + Math.pow(endY - startY, 2));
        // Skalierungsfaktor, um die Mausentfernung in eine geeignete Stoßkraft umzurechnen
        return Math.abs(distance * 0.1); // Skalierungsfaktor 0.1
    }

    void draw(GraphicsContext gc) {
        // Hintergrund löschen
        gc.clearRect(0, 0, poolTable.getWidth(), poolTable.getHeight());

        poolTable.draw(gc.getCanvas()); // Pool-Tisch zeichnen
        cue.draw(gc); // Cue zeichnen
        for (Ball ball : poolTable.balls) { // Bälle zeichnen
            ball.draw(gc);
        }
    }
}
