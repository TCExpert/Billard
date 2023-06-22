package com.example.billiard;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Cue {
    // Kraft des Stoßes
    double cueLength; // Länge des Cues
    double cueWidth; // Breite des Cues
    double lineDash; // Abstand zwischen den Strichen
    private double x; // x-Position des Cues
    private double y; // y-Position des Cues
    private double angle; // Winkel des Cues

    public Cue(double x, double y, double cueLength, double cueWidth, double lineDash) {
        this.lineDash = lineDash;
        this.cueWidth = cueWidth;
        this.cueLength = cueLength;
        this.x = x;
        this.y = y;
        angle = 0; // Anfangswinkel auf 0 setzen
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public void setCueWidth(double cueWidth) {
        this.cueWidth = cueWidth;
    }

    public void setCueLength(double cueLength) {
        this.cueLength = cueLength;
    }

    public void setLineDash(double lineDash) {
        this.lineDash = lineDash;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void draw(GraphicsContext gc, double mouseX, double mouseY) {
        double cueTipLength = cueLength * 0.1; // Länge der Cue-Spitze
        double cueStickLength = cueLength * 0.5; // Länge des Cue-Stabs
        double cueHandleLength = cueLength * 0.3; // Länge des Cue-Griffs

        // Invertiert den Winkel um 180 Grad
        double invertedAngle = getAngle() + Math.PI;

        // Berechnet die Koordinaten für die Spitze des Cues
        double cueTipX = x + Math.cos(invertedAngle) * cueTipLength;
        double cueTipY = y + Math.sin(invertedAngle) * cueTipLength;

        // Berechnet die Koordinaten für das Ende des Cue-Stabs
        double cueStickX = cueTipX + Math.cos(invertedAngle) * cueStickLength;
        double cueStickY = cueTipY + Math.sin(invertedAngle) * cueStickLength;

        // Berechnet die Koordinaten für das Ende des Cue-Griffs
        double cueHandleX = cueStickX + Math.cos(invertedAngle) * cueHandleLength;
        double cueHandleY = cueStickY + Math.sin(invertedAngle) * cueHandleLength;

        // Zeichnet die blaue Spitze des Cues
        gc.setLineDashes(0);
        gc.setLineWidth(cueWidth);
        gc.setStroke(Color.DARKCYAN);
        gc.strokeLine(x, y, cueTipX, cueTipY);

        // Zeichnet den hellbraunen hölzernen Stab des Cues
        gc.setStroke(Color.SANDYBROWN);
        gc.strokeLine(cueTipX, cueTipY, cueStickX, cueStickY);

        // Zeichnet den schwarzen Griff des Cues
        gc.setStroke(Color.BLACK);
        gc.strokeLine(cueStickX, cueStickY, cueHandleX, cueHandleY);

        // Zeichnet die Hilfslinie
        gc.setLineDashes(lineDash);
        gc.setStroke(Color.WHITE);
        gc.strokeLine(x, y, mouseX, mouseY);
    }


}
