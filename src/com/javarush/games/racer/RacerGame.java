package com.javarush.games.racer;

import com.javarush.engine.cell.*;
import com.javarush.games.racer.road.RoadManager;
import com.javarush.games.racer.road.RoadObject;

import java.util.List;

public class RacerGame extends Game {
    public static final int WIDTH = 64;
    public static final int HEIGHT = 90;
    public static final int CENTER_X = WIDTH / 2;
    public static final int ROADSIDE_WIDTH = 14;
    private static final int RACE_GOAL_CARS_COUNT = 40;
    private int score;
    private RoadMarking roadMarking;
    private PlayerCar player;
    private RoadManager roadManager;
    private FinishLine finishLine;
    private boolean isGameStopped;
    private ProgressBar progressBar;

    @Override
    public void initialize() {
        showGrid(false);
        setScreenSize(WIDTH, HEIGHT);
        createGame();
    }

    private void createGame(){
        isGameStopped = false;
        roadMarking = new RoadMarking();
        player = new PlayerCar();
        roadManager = new RoadManager();
        finishLine = new FinishLine();
        progressBar = new ProgressBar(RACE_GOAL_CARS_COUNT);
        score = 3500;
        setTurnTimer(40);
        drawScene();
    }
    private void drawScene(){
        drawField();
        roadMarking.draw(this);
        player.draw(this);
        roadManager.draw(this);
        finishLine.draw(this);
        progressBar.draw(this);

    }

    private void gameOver(){
        isGameStopped = true;
        stopTurnTimer();
        player.stop();
        showMessageDialog(Color.GRAY, "Упс...", Color.BEIGE, 50);
    }

    private void win(){
        isGameStopped = true;
        showMessageDialog(Color.GRAY, "ПОБЕДА!!!", Color.GOLD, 60);
        stopTurnTimer();
    }

    private void moveAll(){
        roadMarking.move(player.speed);
        roadManager.move(player.speed);
        finishLine.move(player.speed);
        player.move();
        progressBar.move(roadManager.getPassedCarsCount());
    }

    @Override
    public void onTurn(int step) {
        if (finishLine.isCrossed(player)){
            win();
            drawScene();
        }else {
            if (roadManager.getPassedCarsCount() >= RACE_GOAL_CARS_COUNT){
                finishLine.show();
            }
            if (roadManager.checkCrush(player)) {
                gameOver();
                drawScene();
            }else
                {
                moveAll();
                roadManager.generateNewRoadObjects(this);
                score-=5;
                setScore(score);
                drawScene();
                }
        }
    }

    @Override
    public void onKeyPress(Key key) {
        switch (key){
            case LEFT: {
                player.setDirection(Direction.LEFT);
                break;
            }
            case RIGHT: {
                player.setDirection(Direction.RIGHT);
                break;
            }
            case SPACE: {
                if (isGameStopped) createGame();
                break;
            }
            case UP: {
                player.setSpeed(2);
                break;
            }
            default: {
                player.setDirection(Direction.NONE);
            }
        }
    }

    @Override
    public void onKeyReleased(Key key) {
        if ((key == Key.RIGHT)&&(player.getDirection()== Direction.RIGHT)) {
            player.setDirection(Direction.NONE);
        }
        if ((key == Key.LEFT)&&(player.getDirection()== Direction.LEFT)) {
            player.setDirection(Direction.NONE);
        }
        if (key == Key.UP) player.speed = 1;
    }

    private void drawField(){
        for (int i = 0; i < HEIGHT; i++){
            for (int j = 0; j < ROADSIDE_WIDTH; j++){
                setCellColor(j, i, Color.GREEN);
                setCellColor(j+ WIDTH - ROADSIDE_WIDTH, i , Color.GREEN);
            }
        }
        for (int i = 0; i < HEIGHT; i++){
            for (int j = ROADSIDE_WIDTH; j < WIDTH - ROADSIDE_WIDTH; j++){
                setCellColor(j, i, Color.GREY);
            }
        }
        for (int i = 0; i < HEIGHT; i++) {
                setCellColor(CENTER_X, i, Color.WHITE);
        }

    }

    @Override
    public void setCellColor(int x, int y, Color color) {
        if ((x > - 1 && x < WIDTH) && (y > - 1 && y < HEIGHT)) super.setCellColor(x, y, color);
    }
}
