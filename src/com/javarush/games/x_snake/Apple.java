package com.javarush.games.x_snake;

import com.javarush.engine.cell.*;


public class Apple extends GameObject{
    private static final String APPLE_SIGN = "\uD83C\uDF4E";
    public boolean isAlive = true;

    public Apple(int x, int y) {
        super(x, y);
    }

    public void draw(Game game){
            game.setCellValueEx(x, y, Color.NONE, APPLE_SIGN, isAlive ? Color.GREEN : Color.RED, 75);
    }
}
