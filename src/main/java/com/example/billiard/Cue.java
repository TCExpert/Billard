package com.example.billiard;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Cue {
    private double x; // x-Position des Cues
    private double y; // y-Position des Cues
    private double angle; // Winkel des Cues
    private double power; // Kraft des Stoßes

    public Cue(double x, double y) {
        this.x = x;
        this.y = y;
        angle = 0; // Anfangswinkel auf 0 setzen
        power = 0; // Anfangskraft auf 0 setzen
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public double getAngle() {
        return angle;
    }

    public void setPower(double power) {
        this.power = power;
    }

    public double getPower() {
        return power;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void draw(GraphicsContext gc) {
        double cueLength = 147; // Länge des Cues

        gc.setStroke(Color.BLACK); // Setzt die Farbe des Cues auf Weiß
        gc.setLineWidth(4); // Setzt die Breite der Linie

        // Berechnet die Endposition des Cues basierend auf Winkel und Länge
        double endX = x + Math.cos(angle) * cueLength;
        double endY = y + Math.sin(angle) * cueLength;

        // Zeichnet den Cue als Linie vom Startpunkt zum Endpunkt
        gc.strokeLine(x, y, endX, endY);
    }
}
