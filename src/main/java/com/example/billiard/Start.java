package com.example.billiard;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Start extends Application {

    private static final String TEXT_STYLE = "-fx-text-fill: white; -fx-font-size: 40px; -fx-font-weight: bold";
    private static final String BUTTON_STYLE = "-fx-font-size: 20px; -fx-background-color: white";
    private static final String CONTENT_BOX_STYLE = "-fx-background-color: rgba(0, 0, 0, 0.5); -fx-padding: 20px;";
    double heightCoefficient;
    double widthCoefficient;
    double oldHeightHolder = 500;
    double newHeightHolder = 500;
    double oldWidthHolder = 1000;
    double newWidthHolder = 1000;
    int shots;
    Image billiardHintergrund = new Image("Billardtisch.jpg");
    ImageView backgroundImageView = new ImageView(billiardHintergrund);
    Canvas canvas = new Canvas(558.625, 332.36699999999996);
    GraphicsContext gc = canvas.getGraphicsContext2D();
    BilliardGame game = new BilliardGame(canvas.getWidth(), canvas.getHeight());
    private Scene startScene;
    private Scene gameScene;
    private Scene endScene;
    private Stage stage;


    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) {
        stage = primaryStage;


        startScreen();
        gameScreen(stage);
        endScreen();


        backgroundImageView.fitHeightProperty().bind(stage.heightProperty());
        backgroundImageView.fitWidthProperty().bind(stage.widthProperty());

        stage.setFullScreen(true);
        stage.setFullScreenExitHint("");
        stage.setScene(startScene);
        stage.setTitle("Billiard Game");
        stage.show();
    }

    public void startScreen() {
        StackPane startPane = new StackPane();

        Label titleLabel = new Label("Billiard");
        titleLabel.setStyle(TEXT_STYLE);

        Button startButton = new Button("Spiel starten");
        startButton.setStyle(BUTTON_STYLE);

        startButton.setOnAction(event -> showGameScene());

        VBox contentBox = new VBox(20);
        contentBox.setAlignment(Pos.CENTER);
        contentBox.setStyle(CONTENT_BOX_STYLE);
        contentBox.getChildren().addAll(titleLabel, startButton);


        backgroundImageView.setPreserveRatio(true);
        backgroundImageView.fitWidthProperty().bind(startPane.widthProperty());
        backgroundImageView.fitHeightProperty().bind(startPane.heightProperty());

        startPane.getChildren().addAll(backgroundImageView, contentBox);
        StackPane.setAlignment(contentBox, Pos.CENTER);

        startScene = new Scene(startPane);
    }

    public void gameScreen(Stage primaryStage) {
        ImageView backgroundImageView = new ImageView(billiardHintergrund);


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
        gameScene = new Scene(root);

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
                shots = shots + 1;
                if (game.isGameOver()) {
                    showEndScreen();
                }
            }
        });


    }


    public void endScreen() {
        StackPane endPane = new StackPane();

        ImageView backgroundImageView = new ImageView(billiardHintergrund);
        backgroundImageView.setPreserveRatio(true);
        backgroundImageView.fitWidthProperty().bind(endPane.widthProperty());
        backgroundImageView.fitHeightProperty().bind(endPane.heightProperty());

        Label titleLabel = new Label("Billiard");
        titleLabel.setStyle(TEXT_STYLE);

        Label shotsLabel = new Label("Du hast " + shots + " Stöße gebraucht, um alle Kugeln zu versenken!");
        shotsLabel.setStyle(TEXT_STYLE);

        Button restartButton = new Button("Neustart");
        restartButton.setStyle(BUTTON_STYLE);
        restartButton.setOnAction(event -> {
            BilliardGame newGame = createGame(canvas); // Neues Game-Objekt erstellen
            shots = 0; // Zurücksetzen der Stöße auf 0
            game = newGame; // Aktualisiertes Game-Objekt setzen
            showGameScene();
        });


        VBox contentBox = new VBox(20);
        contentBox.setAlignment(Pos.CENTER);
        contentBox.setStyle(CONTENT_BOX_STYLE);
        contentBox.getChildren().addAll(titleLabel, shotsLabel, restartButton);

        endPane.getChildren().addAll(backgroundImageView, contentBox);
        StackPane.setAlignment(contentBox, Pos.CENTER);

        endScene = new Scene(endPane);
    }

    private BilliardGame createGame(Canvas canvas) {
        return new BilliardGame(canvas.getWidth(), canvas.getHeight());
    }


    private void showGameScene() {
        stage.setScene(gameScene);
        stage.setFullScreen(true);
        stage.setFullScreenExitHint("");
    }

    private void showEndScreen() {
        endScreen();
        stage.setScene(endScene);
        stage.setFullScreen(true);
        stage.setFullScreenExitHint("");
    }
}
