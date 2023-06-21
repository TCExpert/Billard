package com.example.billiard;

import javafx.scene.canvas.GraphicsContext;

import java.util.ConcurrentModificationException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.example.billiard.Ball.COLLISION_TIMEOUT;
import static com.example.billiard.Ball.recentCollisions;

public class BilliardGame {

    Logger logger = Logger.getLogger(BilliardGame.class.getName());

    final PoolTable poolTable;

    final Cue cue;
    int shots = 0;
    double cueStartX; // Startposition des Cue-Sticks beim Klicken
    double cueStartY;

    double mouseX;
    double mouseY;


    public BilliardGame(double width, double height) {
        // Initialisierung der Objekte
        poolTable = new PoolTable(width, height);
        // Kugeln sind innerhalb des PoolTable initialisiert
        cue = new Cue(poolTable.cueBall.getX(), poolTable.cueBall.getY(), width*0.26, height*0.01328, width*0.05586);
    }

    void update(long now) {
        // Kugeln bewegen
        for (Ball ball : poolTable.balls) {
            ball.move();
        }

        // Kollisionsprüfung mit den Wänden des Pool-Tischs
        for (Ball currentBall : poolTable.balls) {
            if (currentBall.collidesWithWall(poolTable)) {
                currentBall.handleWallCollision(poolTable);
            }
        }

        // Kollisionserkennung zwischen Kugeln
        for (int i = 0; i < poolTable.balls.size(); i++) {
            Ball currentBall = poolTable.balls.get(i);

            for (int j = i + 1; j < poolTable.balls.size(); j++) {
                Ball otherBall = poolTable.balls.get(j);

                if (currentBall.collidesWith(otherBall)) {
                    currentBall.checkCollision(otherBall, now);
                }
            }
        }
        recentCollisions.entrySet().removeIf(entry -> now - entry.getValue() > COLLISION_TIMEOUT);

        // Kollisionserkennung mit den Taschen
        if (poolTable.balls.removeIf(currentBall -> currentBall.collidesWithPocket(poolTable) && currentBall != poolTable.cueBall) && (poolTable.balls.size() == 1 && poolTable.balls.contains(poolTable.cueBall)) && isGameOver()) {
            logger.log(Level.INFO, () -> "Du hast so viele Stöße gebraucht: " + shots);
        }
        if (poolTable.cueBall.collidesWithPocket(poolTable)) {
            poolTable.cueBall.setX(poolTable.getWidth() / 2);
            poolTable.cueBall.setY(poolTable.getHeight() / 2);
            poolTable.cueBall.setVelocity(0,0);
        }

        // Position des Cue aktualisieren
        cue.setX(poolTable.cueBall.getX());
        cue.setY(poolTable.cueBall.getY());
    }

    public void updateSize(double width, double height, double heightCoefficient, double widthCoefficient) {
        poolTable.update(width, height, heightCoefficient, widthCoefficient);
        for (Ball ball : poolTable.balls) {
            ball.setRadius(width * 0.0122);
            cue.setCueLength(width*0.26);
            cue.setCueWidth(height*0.01328);
            cue.setLineDash(width*0.05586);
        }
    }
    public boolean isGameOver(){
        return poolTable.balls.size() == 1 && !poolTable.isFilling();
    }

    double getCueAngle(double mouseX, double mouseY) {
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        double dx = mouseX - poolTable.cueBall.getX();
        double dy = mouseY - poolTable.cueBall.getY();
        return Math.atan2(dy, dx);
    }

    double calculateCuePower(double startX, double startY, double endX, double endY) {
        double distance = Math.sqrt(Math.pow(endX - startX, 2) + Math.pow(endY - startY, 2));
        // Skalierungsfaktor, um die Mausentfernung in Stoßkraft umzurechnen
        return Math.abs(distance * 0.1); // Skalierungsfaktor 0.1
    }

    public boolean isCueActive(){
        for (Ball ball : poolTable.balls) {
            if (Math.abs(ball.getDx()) >0.009 && Math.abs(ball.getDy()) >0.009) {
                return false;
            }
        }
        return true;
    }

    void draw(GraphicsContext gc) {
        // Hintergrund löschen
        gc.clearRect(0, 0, poolTable.getWidth(), poolTable.getHeight());

        //poolTable.draw(gc.getCanvas()); // Pool-Tisch zeichnen
        if (isCueActive()) {
            cue.draw(gc, mouseX, mouseY); // Cue zeichnen
        }
        try {
            for (Ball ball : poolTable.balls) { // Bälle zeichnen
                ball.draw(gc);
            }
        } catch (ConcurrentModificationException e){
            logger.log(Level.WARNING, "Oh es gab einen Fehler der das Spiel nicht beeinflusst. Mach mit dieser Info was du willst");
        }

    }
}
