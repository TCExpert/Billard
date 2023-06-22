package com.example.billiard;


import java.util.ArrayList;
import java.util.List;

public class PoolTable {
    final Ball cueBall;
    final ArrayList<Ball> balls;
    private final List<Pocket> pockets;

    private final Pocket topLeftPocket;
    private final Pocket topMiddlePocket;
    private final Pocket topRightPocket;
    private final Pocket bottomLeftPocket;
    private final Pocket bottomMiddlePocket;
    private final Pocket bottomRightPocket;
    private final boolean isFilling;
    ArrayList<Double> usedNumbers = new ArrayList<>();
    private double width;
    private double height;

    // Constructor
    public PoolTable(double width, double height) {
        this.width = width;
        this.height = height;
        // Pockets werden erstellt und der Liste hinzugefügt
        pockets = new ArrayList<>();
        topLeftPocket = new Pocket(0, 0);
        topMiddlePocket = new Pocket(width / 2.075, 0);
        topRightPocket = new Pocket(width - 20, 0);
        bottomLeftPocket = new Pocket(0, height - 20);
        bottomMiddlePocket = new Pocket(width / 2.075, height - 10);
        bottomRightPocket = new Pocket(width - 20, height - 20);
        pockets.add(bottomLeftPocket);
        pockets.add(bottomMiddlePocket);
        pockets.add(bottomRightPocket);
        pockets.add(topLeftPocket);
        pockets.add(topMiddlePocket);
        pockets.add(topRightPocket);

        balls = new ArrayList<>();

        cueBall = new Ball(width / 2, height / 2, width * 0.0122, "cueBall", "cueBall.png");
        Ball ball1 = new Ball(width / 3 - 23, height / 2 + 11, width * 0.0122, "1", "Ball1.png");
        Ball ball2 = new Ball(width / 3 - 23, height / 2 - 11, width * 0.0122, "2", "Ball2.png");
        Ball ball3 = new Ball(width / 3 - 46, height / 2 + 22, width * 0.0122, "3", "Ball3.png");
        Ball ball4 = new Ball(width / 3 - 46, height / 2, width * 0.0122, "4", "Ball4.png");
        Ball ball5 = new Ball(width / 3 - 46, height / 2 - 22, width * 0.0122, "5", "Ball5.png");
        Ball ball6 = new Ball(width / 3 - 69, height / 2 + 33, width * 0.0122, "6", "Ball6.png");
        Ball ball7 = new Ball(width / 3 - 69, height / 2 + 11, width * 0.0122, "7", "Ball7.png");
        Ball ball8 = new Ball(width / 3 - 69, height / 2 - 11, width * 0.0122, "8", "Ball8.png");
        Ball ball9 = new Ball(width / 3 - 69, height / 2 - 33, width * 0.0122, "9", "Ball9.png");
        Ball ball10 = new Ball(width / 3 - 92, height / 2 + 44, width * 0.0122, "10", "Ball10.png");
        Ball ball11 = new Ball(width / 3 - 92, height / 2 + 22, width * 0.0122, "11", "Ball11.png");
        Ball ball12 = new Ball(width / 3, height / 2, width * 0.0122, "12", "Ball12.png");
        Ball ball13 = new Ball(width / 3 - 92, height / 2, width * 0.0122, "13", "Ball13.png");
        Ball ball14 = new Ball(width / 3 - 92, height / 2 - 22, width * 0.0122, "14", "Ball14.png");
        Ball ball15 = new Ball(width / 3 - 92, height / 2 - 44, width * 0.0122, "15", "Ball15.png");
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


        isFilling = false;
    }

    public void update(double newWidth, double newHeight, double heightCoefficient, double widthCoefficient) {

        this.width = newWidth;
        this.height = newHeight;

        for (Ball ball : balls) {
            ball.setX(ball.getX() * heightCoefficient);
            ball.setY(ball.getY() * widthCoefficient);
        }

        usedNumbers.add(newWidth);
        usedNumbers.add(newHeight);

        double radius = newWidth * 0.0367;

        topLeftPocket.update(0, 0);
        topMiddlePocket.update(newWidth / 2.075, 0);
        topRightPocket.update(newWidth - radius, 0);
        bottomLeftPocket.update(0, newHeight - radius);
        bottomMiddlePocket.update(newWidth / 2.075, newHeight - radius / 2);
        bottomRightPocket.update(newWidth - radius, newHeight - radius);
    }

    public boolean proof(double newHeight, double newWidth) {
        if (usedNumbers.contains(newHeight) || usedNumbers.contains(newWidth)) {
            return false;
        }
        usedNumbers.clear();
        return true;
    }

    // Getter und Setter


    public boolean isFilling() {
        return isFilling;
    }

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
