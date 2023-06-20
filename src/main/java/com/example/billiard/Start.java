package com.example.billiard;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Start extends Application {
    double heightCoefficient;
    double widthCoefficient;
    double oldHeightHolder = 500;
    double newHeightHolder = 500;
    double oldWidthHolder = 1000;
    double newWidthHolder = 1000;

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) {
        Image billiardHintergrund = new Image("Billardtisch.jpg");
        ImageView backgroundImageView = new ImageView(billiardHintergrund);
        Canvas canvas = new Canvas();
        GraphicsContext gc = canvas.getGraphicsContext2D();

        backgroundImageView.fitHeightProperty().bind(primaryStage.heightProperty());
        backgroundImageView.fitWidthProperty().bind(primaryStage.widthProperty());

        // Reagieren Sie auf Größenänderungen des Fensters und zeichnen Sie das Bild neu
        primaryStage.widthProperty().addListener((obs, oldWidth, newWidth) -> {
            widthCoefficient = newWidth.doubleValue() / oldWidth.doubleValue();
            canvas.setWidth(newWidth.doubleValue() * 0.8515625);
        });

        primaryStage.heightProperty().addListener((obs, oldHeight, newHeight) -> {
            heightCoefficient = newHeight.doubleValue() / oldHeight.doubleValue();
            canvas.setHeight(newHeight.doubleValue() * 0.833);
        });

        StackPane root = new StackPane();
        root.getChildren().addAll(backgroundImageView, canvas);
        StackPane.setAlignment(canvas, Pos.CENTER);
        Scene scene = new Scene(root);

        primaryStage.setTitle("Billiard Game");
        primaryStage.setScene(scene);
        primaryStage.show();

        BilliardGame game = new BilliardGame(canvas.getWidth(), canvas.getHeight());

        // AnimationTimer, um das Spiel zu zeichnen
        AnimationTimer drawTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                game.draw(gc); // Spielobjekte zeichnen
                if ((oldHeightHolder != newHeightHolder || oldWidthHolder != newWidthHolder) && game.poolTable.proof(newHeightHolder, newWidthHolder)) {
                    game.updateSize(canvas.getWidth(), canvas.getHeight(), heightCoefficient, widthCoefficient);
                }
            }
        };
        drawTimer.start();

        // Thread für die Spiellogik
        Thread gameThread = new Thread(() -> {
            while (true) {
                game.update(System.nanoTime()); // Spiellogik aktualisieren
                try {
                    Thread.sleep(16); // Wartezeit für 60 FPS
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                }
            }
        });
        gameThread.setDaemon(true);
        gameThread.start();

        canvas.heightProperty().addListener((obs, oldHeight, newHeight) -> {
            this.oldHeightHolder = oldHeight.doubleValue();
            this.newHeightHolder = newHeight.doubleValue();
        });
        canvas.widthProperty().addListener((obs, oldWidth, newWidth) -> {
            this.oldWidthHolder = oldWidth.doubleValue();
            this.newWidthHolder = newWidth.doubleValue();
        });

        // Event Handler für Mausklick
        canvas.setOnMouseClicked(event -> {
            // Wenn der Cue-Stick noch nicht ausgewählt ist und der Benutzer auf den Cue-Stick klickt
            game.cueStartX = event.getX();
            game.cueStartY = event.getY();
        });

        // Event Handler für Mausbewegung
        canvas.setOnMouseMoved(event -> game.cue.setAngle(game.getCueAngle(event.getX(), event.getY())));

        // Event Handler für Mausfreigabe
        canvas.setOnMouseReleased(event -> {
            if (game.isCueActive()) {
                // Wenn der Benutzer den Cue-Stick freigibt
                double cueEndX = event.getX();
                double cueEndY = event.getY();
                double cuePower = game.calculateCuePower(game.poolTable.cueBall.getX(), game.poolTable.cueBall.getY(), cueEndX, cueEndY);

                // Setzen der Stoßkraft und Ausrichtung des Cue-Sticks
                game.cue.setAngle(game.getCueAngle(event.getX(), event.getY()));

                // Kugel stoßen
                game.poolTable.cueBall.shoot(game.cue.getAngle(), cuePower);
                game.shots = game.shots + 1;
            }
        });
    }
}
