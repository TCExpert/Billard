package com.example.billiard;


import java.util.ArrayList;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class PoolTable {
    private double width;
    private double height;
    private ArrayList<Pocket> pockets;

    // Constructor
    public PoolTable(double width, double height) {
        this.width = width;
        this.height = height;
        // Pockets werden erstellt und der Liste hinzugefügt
        pockets = new ArrayList<>();
        Pocket topLeftPocket = new Pocket(0 , 0, 20, 20);
        Pocket topMiddlePocket = new Pocket(width/2, 0, 20,20);
        Pocket topRightPocket = new Pocket(width-20,0,20,20);
        Pocket bottomLeftPocket = new Pocket(0,height-20,20,20);
        Pocket bottomMiddlePocket = new Pocket(width/2, height-20, 20, 20);
        Pocket bottomRightPocket = new Pocket(width-20, height-20, 20, 20);
        pockets.add(bottomLeftPocket);
        pockets.add(bottomMiddlePocket);
        pockets.add(bottomRightPocket);
        pockets.add(topLeftPocket);
        pockets.add(topMiddlePocket);
        pockets.add(topRightPocket);
    }

    // Draw-Methode
    public void draw(Canvas canvas) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.GREEN); // Setzt die Farbe des Tischs auf Grün
        gc.fillRect(0, 0, width, height); // Zeichnet das Rechteck für den Tisch

        gc.setFill(Color.BLACK); // Setzt die Farbe der Taschen auf Schwarz
        for (Pocket pocket : pockets) {
            gc.fillOval(pocket.getX(), pocket.getY(), pocket.getWidth(), pocket.getHeight()); // Zeichnet die Taschen
        }
    }

    // Getter und Setter
    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }
}
