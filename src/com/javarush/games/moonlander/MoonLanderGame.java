package com.javarush.games.moonlander;

import com.javarush.engine.cell.*;


public class MoonLanderGame extends Game {
    public static final int WIDTH = 64;
    public static final int HEIGHT = 64;
    private Rocket rocket;
    private int score;
    private GameObject landscape;
    private GameObject platform ;
    private boolean isLeftPressed;
    private boolean isRightPressed;
    private boolean isUpPressed;
    private boolean isGameStopped;

    private void createGameObjects(){
        landscape = new GameObject(0, 25, ShapeMatrix.LANDSCAPE);
        platform = new GameObject(23, MoonLanderGame.HEIGHT - 1, ShapeMatrix.PLATFORM);
        rocket = new Rocket(WIDTH / 2, 0);

    }

    @Override
    public void initialize() {
        setScreenSize(WIDTH, HEIGHT);
        showGrid(false);
        createGame();
    }

    private void createGame(){
        isLeftPressed = false;
        isRightPressed = false;
        isUpPressed = false;
        isGameStopped = false;
        score = 1000;
        createGameObjects();
        drawScene();
        setTurnTimer(50);


    }

    private void drawScene(){
    for (int i = 0; i < HEIGHT; i++){
        for (int j = 0; j < WIDTH; j++){
            setCellColor(i, j, Color.BLACK);
        }
    }
    landscape.draw(this);
    rocket.draw(this);
    }

    @Override
    public void setCellColor(int x, int y, Color color) {
        if (x >= 0 && y < HEIGHT - 1  && y >= 0 && x < WIDTH - 1){
        super.setCellColor(x, y, color);}
    }

    @Override
    public void onTurn(int step) {
        if (score > 0) score--;
        setTurnTimer(50);
        rocket.move(isUpPressed, isLeftPressed, isRightPressed);
        check();
        setScore(score);
        drawScene();
    }

    @Override
    public void onKeyPress(Key key) {
        switch (key) {
            case LEFT: isLeftPressed = true; isRightPressed = false; break;
            case UP: isUpPressed = true;  break;
            case RIGHT: isRightPressed = true; isLeftPressed = false; break;
            case SPACE: if (isGameStopped) createGame(); break;
        }
    }

    @Override
    public void onKeyReleased(Key key) {
        switch (key) {
            case LEFT: isLeftPressed = false; break;
            case UP: isUpPressed = false;  break;
            case RIGHT: isRightPressed = false; break;
        }
    }
    private void check(){
        if (rocket.isCollision(landscape) && !(rocket.isCollision(platform) && rocket.isStopped())){
            gameOver();
        }
        if (rocket.isCollision(platform) && rocket.isStopped()){
            win();
        }
    }
    private void win(){
        rocket.land();
        isGameStopped = true;
        stopTurnTimer();
        showMessageDialog(Color.DARKORANGE, "Мягкая посадка!", Color.AQUA, 65);

    }
    private void gameOver(){
        rocket.crash();
        isGameStopped = true;
        stopTurnTimer();
        showMessageDialog(Color.RED, "Ваш корабль разбился...", Color.AQUA, 45);
        score = 0;
    }

}
