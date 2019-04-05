package com.javarush.games.minesweeper;

import com.javarush.engine.cell.*;

import java.util.ArrayList;
import java.util.List;

public class MinesweeperGame extends Game {
    private static final int SIDE = 9;
    private static final String MINE = "\uD83D\uDCA3";
    private int countMinesOnField = 0;
    private int countFlags = 0;
    private int countClosedTiles = SIDE * SIDE;
    private boolean isGameStopped;
    private int  score = 0;
    private int neighbors = 0;
    private static final String FLAG = "\uD83D\uDEA9";
    private GameObject[][] gameField = new GameObject[SIDE][SIDE];


    @Override
    public void initialize() {
        setScreenSize(SIDE, SIDE);
        createGame();
    }

    private void createGame(){
        for (int y = 0; y < SIDE; y++){
            for (int x = 0; x < SIDE; x++){
                setCellValueEx(x, y, Color.DARKORANGE, "");
                boolean isMine;
                
                int r = getRandomNumber(5);
                if (r == 3) {
                    isMine = true;
                    countMinesOnField ++;
                }
                else {isMine = false;}
                gameField [y][x] = new GameObject(x, y, isMine);
            }
        }
        countFlags = countMinesOnField;
        countMineNeighbors();
    }

    @Override
    public void onMouseLeftClick(int x, int y) {
        if (!isGameStopped){
            try {
                openTile(x, y);
            } catch (Exception e){

            }
        }

        if (isGameStopped){
            restart();
        }
    }

    @Override
    public void onMouseRightClick(int x, int y) {
        markTile(x, y);
    }

    private void win(){
            isGameStopped = true;

            showMessageDialog(Color.PEACHPUFF, "ПОБЕДА!", Color.DARKGOLDENROD, 90);

    }
    private void restart(){
        isGameStopped = false;
        score = 0;
        countMinesOnField = 0;
        countClosedTiles = SIDE*SIDE;
        setScore(score);
        createGame();

    }

    private void gameOver(){
        isGameStopped = true;
        showMessageDialog(Color.DARKRED, "Game OVER", Color.RED, 50);
    }

    private void markTile(int x, int y){
        if (!gameField[y][x].isOpen && !isGameStopped){
            if (countFlags > 0 && !gameField[y][x].isFlag) {
                setCellColor(x, y, Color.DARKRED);
                setCellValue(x, y, FLAG);
                gameField[y][x].isFlag = true;
                countFlags--;
            }else { if (gameField[y][x].isFlag){
                setCellColor(x, y, Color.DARKORANGE);
                setCellValue(x, y, "");
                gameField[y][x].isFlag = false;
                countFlags++;
            }
            }
        }
    }

    private void openTile(int x, int y){
        if(!isGameStopped && !gameField[y][x].isOpen && !gameField[y][x].isFlag) {
            gameField[y][x].isOpen = true;
            setCellColor(x, y, Color.BEIGE);

                if (gameField[y][x].isMine) {
                    gameField[y][x].isOpen = true;
                    setCellValueEx(x, y, Color.RED, MINE);
                    gameOver();
                    return;
                } else {
                    if (gameField[y][x].countMineNeighbors != 0) {
                        gameField[y][x].isOpen = true;
                        score = score + 5;
                        setCellColor(x, y, Color.BEIGE);
                        setCellNumber(x, y, gameField[y][x].countMineNeighbors);
                    }
                    if (gameField[y][x].countMineNeighbors == 0) {
                        setCellColor(x, y, Color.BEIGE);
                        setCellValue(x, y, "");

                        List<GameObject> list = getNeighbors(gameField[y][x]);
                        for (int i = 0; i < list.size(); i++) {
                            if (!list.get(i).isOpen)
                                openTile(list.get(i).x, list.get(i).y);
                        }
                    }
                }
                setScore(score);
                countClosedTiles--;

                if (countClosedTiles == countMinesOnField) win();
        }
    }




    private void countMineNeighbors() {
        List<GameObject> list = new ArrayList<>();
        for (int y = 0; y < SIDE; y++) {
            for (int x = 0; x < SIDE; x++) {
                if (!gameField[y][x].isMine) {
                    list.clear();
                    list = getNeighbors(gameField[y][x]);
                    for (int m = 0; m < list.size(); m++) {
                        if (list.get(m).isMine) {
                             gameField[y][x].countMineNeighbors++;
                        }
                    }
                }
            }
        }
    }

    private List<GameObject> getNeighbors (GameObject gameObject){
        List<GameObject> list = new ArrayList<>();
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                if ((gameObject.x + j >= 0) && ((gameObject.y + i) >= 0)){
                   if (!((j == 0)&&(i == 0))) {
                      if(((gameObject.x + j < SIDE) && (gameObject.y + i) < SIDE)) {
                         list.add(gameField[gameObject.y + i][gameObject.x + j]);
                      }
                   }
                }
            }
        }
        return list;
    }
}

