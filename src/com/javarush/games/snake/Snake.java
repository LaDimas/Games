package com.javarush.games.snake;
import com.javarush.engine.cell.*;
import java.util.ArrayList;
import java.util.List;
import static com.javarush.games.snake.SnakeGame.HEIGHT;
import static com.javarush.games.snake.SnakeGame.WIDTH;

public class Snake {
    private static final String HEAD_SIGN = "\uD83D\uDC7E";
    private static final String BODY_SIGN = "\u26AB";
    public boolean isAlive = true;
    public Direction direction = Direction.LEFT;

    private List<GameObject> snakeParts = new ArrayList<>();

    public Snake(int x, int y){
        for (int i = 0; i < 3; i++){
            snakeParts.add(new GameObject(x + i, y));
        }
    }
    public int getLength(){
        return snakeParts.size();
    }

    public void draw(Game game){

        for (int i = 0; i < snakeParts.size(); i++){
            if (i == 0){game.setCellValueEx(snakeParts.get(i).x, snakeParts.get(i).y, Color.NONE, HEAD_SIGN,(isAlive)? Color.BLACK :Color.RED,75);}

            else {game.setCellValueEx(snakeParts.get(i).x, snakeParts.get(i).y, Color.NONE, BODY_SIGN,(isAlive)? Color.BLACK :Color.RED,75);}
        }
     }

    public GameObject createNewHead(){
         List<GameObject> newHead = new ArrayList<>();
         switch (direction){
             case UP: newHead.add(new GameObject(snakeParts.get(0).x, snakeParts.get(0).y - 1));
             break;
             case DOWN: newHead.add(new GameObject(snakeParts.get(0).x, snakeParts.get(0).y + 1));
             break;
             case LEFT: newHead.add(new GameObject(snakeParts.get(0).x - 1, snakeParts.get(0).y));
             break;
             case RIGHT: newHead.add(new GameObject(snakeParts.get(0).x + 1, snakeParts.get(0).y));
             break;

         }
        return newHead.get(0);
    }

    public void removeTail(){
        snakeParts.remove(snakeParts.get(snakeParts.size() - 1));
     }

    public void setDirection(Direction direction){

        switch (this.direction) {
            case LEFT:
            case RIGHT:
                if ((snakeParts.get(0).x) == (snakeParts.get(1).x))return;
                break;
            case UP:
            case DOWN:
                if ((snakeParts.get(0).y) == (snakeParts.get(1).y)) return;
                break;
        }
        this.direction = direction;
    }



    public void move(Apple apple){
        GameObject newHead = createNewHead();
        if ((newHead.x != apple.x) || (newHead.y != apple.y)){
            if (newHead.x < 0 || newHead.y < 0 || newHead.x > WIDTH - 1|| newHead.y > HEIGHT - 1){
                    isAlive = false;
                } else {
                if (!checkCollision(newHead)) {
                snakeParts.add(0, newHead);
                removeTail();
                } else {
                    isAlive = false;

                }
            }
        } else {
            apple.isAlive = false;
            if (!checkCollision(newHead)) {
                snakeParts.add(0, newHead);
            }else isAlive = false;
        }
    }

    public boolean checkCollision(GameObject gameObject){
        for (int i = 0; i < snakeParts.size(); i++){
            if (gameObject.x == snakeParts.get(i).x && gameObject.y == snakeParts.get(i).y) {
                return true;
            }
        }
        return false;
    }
}
