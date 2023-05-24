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

    private boolean isCueSelected = false; // Gibt an, ob der Cue-Stick ausgewählt ist
    private double cueStartX; // Startposition des Cue-Sticks beim Klicken
    private double cueStartY;

    public BilliardGame() {
        // Initialisierung der Objekte
        poolTable = new PoolTable(WIDTH, HEIGHT);
        cueBall = new Ball((double) WIDTH / 2, (double) HEIGHT / 2, 10, Color.WHITE);
        cue = new Cue((double) WIDTH / 2, (double) HEIGHT / 2);
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

        // Event Handler für Mausklick

        canvas.setOnMouseClicked(event -> {
            if (!isCueSelected && cue.isCueSelected(event.getX(), event.getY())) {
                // Wenn der Cue-Stick noch nicht ausgewählt ist und der Benutzer auf den Cue-Stick klickt
                isCueSelected = true;
                cueStartX = event.getX();
                cueStartY = event.getY();
            }
        });

        // Event Handler für Mausbewegung
        canvas.setOnMouseMoved(event -> {
            if (isCueSelected) {
                // Wenn der Benutzer den Cue-Stick ausgewählt hat
                cue.setAngle(getCueAngle(event.getX(), event.getY()));
            }
        });

        // Event Handler für Mausfreigabe
        canvas.setOnMouseReleased(event -> {
            if (isCueSelected) {
                // Wenn der Benutzer den Cue-Stick freigibt
                double cueEndX = event.getX();
                double cueEndY = event.getY();
                double cuePower = calculateCuePower(cueStartX, cueStartY, cueEndX, cueEndY);

                // Setzen der Stoßkraft und Ausrichtung des Cue-Sticks
                cue.setPower(cuePower);
                cue.setAngle(getCueAngle(event.getX(), event.getY()));

                // Kugel stoßen
                cueBall.shoot(cue.getAngle(), cuePower);

                // Zurücksetzen der Cue-Stick-Selektion
                isCueSelected = false;
            }
        });
    }

    private void update() {
        // Kugeln bewegen
        cueBall.move();

        // Kollisionsprüfung mit den Wänden des Pool-Tischs
        if (cueBall.collidesWithWall(cueBall)) {
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

    private double getCueAngle(double mouseX, double mouseY) {
        double dx = mouseX - cueBall.getX();
        double dy = mouseY - cueBall.getY();
        return Math.atan2(dy, dx);
    }

    private double calculateCuePower(double startX, double startY, double endX, double endY) {
        double distance = Math.sqrt(Math.pow(endX - startX, 2) + Math.pow(endY - startY, 2));
        // Implementieren Sie hier Ihre eigene Logik, um die Stoßkraft basierend auf der Entfernung zu berechnen
        // Zum Beispiel: Verwenden Sie eine Skalierungsfaktor, um die Mausentfernung in eine geeignete Stoßkraft umzurechnen
        return distance * 0.1; // Beispiel: Skalierungsfaktor 0.1
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
