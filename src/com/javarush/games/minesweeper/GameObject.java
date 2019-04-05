package com.javarush.games.minesweeper;

public class GameObject {
    public int x, y;
    public boolean isMine = false;
    public int countMineNeighbors;
    public boolean isOpen = false;
    public boolean isFlag = false;

    public GameObject(int x, int y, boolean m){
        this.x = x;
        this.y = y;
        this.isMine = m;
    }
}
