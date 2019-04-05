package com.javarush.games.snake;

import java.util.List;

public class AnotherSnake extends Snake {

    public AnotherSnake(List<GameObject> tailParts) {
        super(tailParts);
        this.direction = getDirection(tailParts);
    }
}

