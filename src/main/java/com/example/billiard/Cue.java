package com.example.billiard;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;

public class Cue {
    private double x; // x-Position des Cues
    private double y; // y-Position des Cues
    private double angle; // Winkel des Cues
    // Kraft des Stoßes
    double cueLength; // Länge des Cues
    double cueWidth; // Breite des Cues
    double lineDash;
    String imageURL = "CueStick.png";

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
        gc.setLineWidth(cueWidth); // Setzt die Breite der Linie

        // Invertiert den Winkel um 180 Grad
        double invertedAngle = getAngle() + Math.PI;

        // Berechnet die Endposition des Cues basierend auf invertiertem Winkel und Länge
        double endX = x + Math.cos(invertedAngle) * cueLength;
        double endY = y + Math.sin(invertedAngle) * cueLength;

        // Lade das Bild und wende es als Textur an
        Image cueImage = new Image(imageURL);
        ImagePattern cueImagePattern = new ImagePattern(cueImage);

        // Zeichnet den Cue als Linie vom Startpunkt zum Endpunkt
        gc.setLineDashes(0);
        gc.setStroke(cueImagePattern);
        gc.strokeLine(x, y, endX, endY);

        // Zeichnet die Hilfslinie
        gc.setLineDashes(lineDash);
        gc.setStroke(Color.WHITE);
        gc.strokeLine(x, y, mouseX, mouseY);
    }


}
