package com.example.billiard;

// Schritt 8: Design der BilliardGame-Klasse

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class BilliardGame extends Application {
    private static final int WIDTH = 800; // Breite des Fensters
    private static final int HEIGHT = 600; // Höhe des Fensters

    private PoolTable poolTable;
    private Ball cueBall;
    private Cue cue;

    public BilliardGame() {
        // Initialisierung der Objekte
        poolTable = new PoolTable(WIDTH, HEIGHT);
        cueBall = new Ball(WIDTH / 2, HEIGHT / 2, 10, Color.WHITE);
        cue = new Cue(WIDTH / 2, HEIGHT / 2);
        // Weitere Initialisierungen können hier erfolgen
    }

    @Override
    public void start(Stage primaryStage) {
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        StackPane root = new StackPane(canvas);
        Scene scene = new Scene(root);

        primaryStage.setTitle("Billiard Game");
        primaryStage.setScene(scene);
        primaryStage.show();

        GraphicsContext gc = canvas.getGraphicsContext2D();

        // AnimationTimer, um das Spiel zu aktualisieren und zu zeichnen
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update(); // Spiellogik aktualisieren
                draw(gc); // Spielobjekte zeichnen
            }
        };
        timer.start();
    }

    private void update() {
        // Kugeln bewegen
        cueBall.move();

        // Kollisionsprüfung mit den Wänden des Pool-Tischs
        if (poolTable.collidesWithWall(cueBall)) {
            // Handle Kollision mit den Wänden
            // Zum Beispiel Richtung umkehren
            cueBall.setVelocity(-cueBall.getDx(), -cueBall.getDy());
        }

        // Kollisionserkennung zwischen Kugeln
        for (Ball ball : poolTable.getBalls()) {
            if (ball != cueBall && cueBall.collidesWith(ball)) {
                // Handle Kollision zwischen cueBall und ball
                // Zum Beispiel Richtungen anpassen
                // Implementieren Sie geeignete Physik-Logik für den Stoß
            }
        }
    }

    private void draw(GraphicsContext gc) {
        // Hintergrund löschen
        gc.clearRect(0, 0, WIDTH, HEIGHT);

        poolTable.draw(gc.getCanvas()); // Pool-Tisch zeichnen
        cueBall.draw(gc); // Weiße Kugel zeichnen
        cue.draw(gc); // Cue zeichnen
        // Weitere Spielobjekte können hier gezeichnet werden
    }

    public static void main(String[] args) {
        launch(args);
    }
}
