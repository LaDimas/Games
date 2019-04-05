package com.javarush.games.snake;
import com.javarush.engine.cell.*;
import java.util.ArrayList;
import java.util.List;

public class SnakeGame extends Game {
    public static final int WIDTH = 15;
    public static final int HEIGHT = 15;
    private static final int GOAL = 20;
    public static List<Snake> snakes = new ArrayList<>();
    public static List<Apple> apples = new ArrayList<>();
    private Snake mySnake;
    private AnotherSnake anotherSnake;
    private boolean isGameStopped;
    private int turnDelay = 0;
    private int score;


    @Override
    public void initialize() {
        setScreenSize(WIDTH, HEIGHT);
        createGame();
    }

    private  void createGame(){
        isGameStopped = false;
        score = 0;
        snakes.clear();
        apples.clear();
        mySnake = new MySnake(WIDTH / 2, HEIGHT /2);
        mySnake.direction = Direction.LEFT;
        snakes.add(mySnake);
        createNewApple();
        drawScene();
        turnDelay = 300;
        setTurnTimer(turnDelay);
    }

    private void gameOver(){
        stopTurnTimer();
        isGameStopped = true;
        showMessageDialog(Color.GREY,"Game Over",Color.RED,75);
    }

    private void win(){
        stopTurnTimer();
        isGameStopped = true;
        showMessageDialog(Color.GREY,"Победа!!!",Color.GOLDENROD,75);
    }

    public void createNewApple(){
        Apple apple = new Apple(getRandomNumber(WIDTH),getRandomNumber(HEIGHT));
        apples.add(apple);
    }

    private void drawScene() {
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                setCellValueEx(x, y, Color.DARKSEAGREEN, "");
            }
        }
        for (Snake snake  : snakes) {
            if (snake.isAlive)snake.draw(this);
        }

        for (Apple apple : apples) {
            apple.draw(this);
        }
    }

    private void moveSnakes(){

        if (snakes.size() > 1){
            for (int i = 1; i < snakes.size(); i++) {
                snakes.get(i).move(apples);
                if (!snakes.get(0).isAlive)isGameStopped = true;
                if(apples.isEmpty()){
                    score -= 1;
                    setScore(score);
                    createNewApple();
                }
            }
        }
        snakes.get(0).move(apples);

    }

    @Override
    public void onTurn(int step) {
        if (!isGameStopped) {
            moveSnakes();
            if (apples.size() == 0) {
                createNewApple();
                score += 5;
                setScore(score);
                turnDelay -= 2;
                setTurnTimer(turnDelay);
            }
            if (!mySnake.isAlive) {
                gameOver();
            }
            if (mySnake.getLength() > GOAL || score > 70) {
                win();
            }
            if (score < 0) {
                gameOver();
            }
            drawScene();
        }
    }

    @Override
    public void onKeyPress(Key key) {
        try {

            switch (key) {
                case LEFT:
                    mySnake.setDirection(Direction.LEFT);
                    break;
                case RIGHT:
                    mySnake.setDirection(Direction.RIGHT);
                    break;
                case UP:
                    mySnake.setDirection(Direction.UP);
                    break;
                case DOWN:
                    mySnake.setDirection(Direction.DOWN);
                    break;
                case SPACE:
                    if (isGameStopped) createGame();
                    break;
            }
        }catch (Exception e){}
    }





}
