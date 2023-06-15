package com.example.billiard;


import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class PoolTable {
    private double width;
    private double height;
    private final List<Pocket> pockets;

    private final Pocket topLeftPocket;
    private final Pocket topMiddlePocket;
    private final Pocket topRightPocket;
    private final Pocket bottomLeftPocket;
    private final Pocket bottomMiddlePocket;
    private final Pocket bottomRightPocket;

    final Ball cueBall;

    final ArrayList<Ball> balls;

    // Constructor
    public PoolTable(double width, double height) {
        this.width = width;
        this.height = height;
        // Pockets werden erstellt und der Liste hinzugefügt
        pockets = new ArrayList<>();
        topLeftPocket = new Pocket(0, 0, 20, 20);
        topMiddlePocket = new Pocket(width / 2.075, 0, 20, 20);
        topRightPocket = new Pocket(width - 20, 0, 20, 20);
        bottomLeftPocket = new Pocket(0, height - 20, 20, 20);
        bottomMiddlePocket = new Pocket(width / 2.075, height -10, 20, 20);
        bottomRightPocket = new Pocket(width - 20, height - 20, 20, 20);
        pockets.add(bottomLeftPocket);
        pockets.add(bottomMiddlePocket);
        pockets.add(bottomRightPocket);
        pockets.add(topLeftPocket);
        pockets.add(topMiddlePocket);
        pockets.add(topRightPocket);

        balls = new ArrayList<>();

        cueBall = new Ball(width / 2, height / 2, 10, Color.WHITE);
        Ball ball1 = new Ball(width / 3 - 23, height / 2 + 11, 10, Color.YELLOW);
        Ball ball2 = new Ball(width / 3 - 23, height / 2 - 11, 10, Color.BLUE);
        Ball ball3 = new Ball(width / 3 - 46, height / 2 + 22, 10, Color.RED);
        Ball ball4 = new Ball(width / 3 - 46, height / 2, 10, Color.PURPLE);
        Ball ball5 = new Ball(width / 3 - 46, height / 2 - 22, 10, Color.ORANGE);
        Ball ball6 = new Ball(width / 3 - 69, height / 2 + 33, 10, Color.DARKGREEN);
        Ball ball7 = new Ball(width / 3 - 69, height / 2 + 11, 10, Color.BROWN);
        Ball ball8 = new Ball(width / 3 - 69, height / 2 - 11, 10, Color.BLACK);
        Ball ball9 = new Ball(width / 3 - 69, height / 2 - 33, 10, Color.YELLOW);
        Ball ball10 = new Ball(width / 3 - 92, height / 2 + 44, 10, Color.BLUE);
        Ball ball11 = new Ball(width / 3 - 92, height / 2 + 22, 10, Color.RED);
        Ball ball12 = new Ball(width / 3, height / 2, 10, Color.PURPLE);
        Ball ball13 = new Ball(width / 3 - 92, height / 2, 10, Color.ORANGE);
        Ball ball14 = new Ball(width / 3 - 92, height / 2 - 22, 10, Color.DARKGREEN);
        Ball ball15 = new Ball(width / 3 - 92, height / 2 - 44, 10, Color.BROWN);
        balls.add(cueBall);
        balls.add(ball1);
        balls.add(ball2);
        balls.add(ball3);
        balls.add(ball4);
        balls.add(ball5);
        balls.add(ball6);
        balls.add(ball7);
        balls.add(ball8);
        balls.add(ball9);
        balls.add(ball10);
        balls.add(ball11);
        balls.add(ball12);
        balls.add(ball13);
        balls.add(ball14);
        balls.add(ball15);
    }

    public void update(double newWidth, double newHeight, double heightCoefficient, double widthCoefficient) {

        this.width = newWidth;
        this.height = newHeight;

        for (Ball ball: balls) {
            double ballX = ball.getX() * heightCoefficient;

            double ballY = ball.getY() * widthCoefficient;
            ball.setX(ballX);
            ball.setY(ballY);
        }

        usedNumbers.add(newWidth);
        usedNumbers.add(newHeight);




        double radius = newWidth * 0.0367;

        topLeftPocket.update(0, 0, radius, radius);
        topMiddlePocket.update(newWidth / 2.075, 0, radius, radius);
        topRightPocket.update(newWidth - radius, 0, radius, radius);
        bottomLeftPocket.update(0, newHeight - radius, radius, radius);
        bottomMiddlePocket.update(newWidth / 2.075, newHeight - radius/2, radius, radius);
        bottomRightPocket.update(newWidth - radius, newHeight - radius, radius, radius);


    }
    ArrayList<Double> usedNumbers = new ArrayList<>();
    public boolean proof(double newHeight, double newWidth){
        if (usedNumbers.contains(newHeight)||usedNumbers.contains(newWidth)){
            return false;
        }
        usedNumbers.clear();

        return true;
    }


    // Draw-Methode
    public void draw(Canvas canvas) {
        GraphicsContext gc = canvas.getGraphicsContext2D();

        gc.setFill(Color.YELLOW); // Setzt die Farbe der Taschen auf Schwarz
        for (Pocket pocket : pockets) {
            gc.fillOval(pocket.getX(), pocket.getY(), pocket.getWidth(), pocket.getHeight()); // Zeichnet die Taschen
        }
    }

    // Getter und Setter
    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public List<Pocket> getPockets() {
        return pockets;
    }

}
