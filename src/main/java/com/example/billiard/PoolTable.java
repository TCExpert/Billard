package com.example.billiard;

// Schritt 4: Design der PoolTable-Klasse

import java.util.ArrayList;
import java.util.List;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class PoolTable {
    private double width;
    private double height;
    private List<Pocket> pockets;

    public PoolTable(double width, double height) {
        this.width = width;
        this.height = height;
        pockets = new ArrayList<>();
    }

    public void addPocket(Pocket pocket) {
        pockets.add(pocket);
    }

    public void draw(Canvas canvas) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.GREEN); // Setzt die Farbe des Tischs auf Grün
        gc.fillRect(0, 0, width, height); // Zeichnet das Rechteck für den Tisch

        gc.setFill(Color.BLACK); // Setzt die Farbe der Taschen auf Schwarz
        for (Pocket pocket : pockets) {
            gc.fillOval(pocket.getX(), pocket.getY(), pocket.getWidth(), pocket.getHeight()); // Zeichnet die Taschen
        }
    }

    public Ball[] getBalls() {

    }
}
