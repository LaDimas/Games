package com.javarush.games.snake;
import com.javarush.engine.cell.*;

public class SnakeGame extends Game{
    public static final int WIDTH = 15;
    public static final int HEIGHT = 15;
    private static final int GOAL = 28;
    private Snake snake;
    private Apple apple;
    private boolean isGameStopped;
    private int turnDelay = 0;
    private int score;

    @Override
    public void initialize() {
    setScreenSize(WIDTH, HEIGHT);
    createGame();
    }

    private  void createGame(){
        snake = new Snake(WIDTH / 2, HEIGHT /2);
        score = 0;
        setScore(score);
        createNewApple();
        isGameStopped = false;
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

    private void createNewApple(){
            apple = new Apple(getRandomNumber(WIDTH),getRandomNumber(HEIGHT));
            if (snake.checkCollision(apple)) createNewApple();
        }

    private void drawScene() {
        for (int y = 0; y < HEIGHT; y++){
            for(int x = 0; x < WIDTH; x++){
                setCellValueEx(x,y, Color.DARKSEAGREEN,"");
            }
        }
        apple.draw(this);
        snake.draw(this);
    }

    @Override
    public void onTurn(int step) {
        snake.move(apple);
        if (!apple.isAlive) {
            createNewApple();
            score +=5;
            setScore(score);
            turnDelay -=5;
            setTurnTimer(turnDelay);

        }
        if (!snake.isAlive) gameOver();
        if (snake.getLength() > GOAL) win();
        drawScene();
    }

    @Override
    public void onKeyPress(Key key) {

        switch (key){
            case LEFT: snake.setDirection(Direction.LEFT);
            break;
            case RIGHT: snake.setDirection(Direction.RIGHT);
            break;
            case UP: snake.setDirection(Direction.UP);
            break;
            case DOWN: snake.setDirection(Direction.DOWN);
            break;
            case SPACE: if (isGameStopped) createGame();
            break;
        }
    }
}
