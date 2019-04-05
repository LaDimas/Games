package com.javarush.games.x_snake;
import com.javarush.engine.cell.Color;
import com.javarush.engine.cell.*;
import java.util.ArrayList;
import java.util.List;
import static com.javarush.games.x_snake.X_SnakeGame.*;

public class Snake {
    public static final String HEAD_SIGN = "\uD83D\uDC7E";
    public static final String BODY_SIGN = "\u26AB";
    public List<GameObject> snakeParts = null;
    public List<GameObject> tailParts = new ArrayList<>();
    public Direction direction;
    public Direction oldDirection;
    public boolean isAlive = true;
    public int cutPoint;

    Snake(int x, int y){
    snakeParts = new ArrayList<>();
        for (int i = 0; i < 5; i++){
            this.snakeParts.add(new GameObject(x + i, y));
        }
    }

    Snake(List<GameObject> tailParts){
        snakeParts = new ArrayList<>(tailParts);
    }

    public void draw(Game game){

            for (int i = 0; i < snakeParts.size(); i++){
                if (i == 0){game.setCellValueEx(snakeParts.get(i).x, snakeParts.get(i).y, Color.NONE, HEAD_SIGN,(this instanceof MySnake)? Color.BLACK :Color.BEIGE,75);}

                else {game.setCellValueEx(snakeParts.get(i).x, snakeParts.get(i).y, Color.NONE, BODY_SIGN,(this instanceof MySnake)? Color.BLACK :Color.BEIGE,75);
                }
            }
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

    public Direction getDirection(List<GameObject> object){

        if (object.get(0).y >= apples.get(0).y){
            if (object.get(0).x > apples.get(0).x){
                oldDirection = Direction.LEFT;
            }else
                if ((object.get(0).x < apples.get(0).x)){
                    oldDirection = Direction.RIGHT;
                }else oldDirection = Direction.UP;
        }else {
        if (object.get(0).y <= apples.get(0).y){
            if (object.get(0).x > apples.get(0).x){
                oldDirection = Direction.LEFT;
            }else
            if ((object.get(0).x < apples.get(0).x)){
                oldDirection = Direction.RIGHT;
            }else oldDirection = Direction.DOWN;
        }
        }
        return oldDirection;
    }
    private GameObject createNewHead(){

        List<GameObject> newHead = new ArrayList<>();
        if (this instanceof AnotherSnake){
            this.direction = getDirection(this.snakeParts);
        }

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
    int getLength(){
        return snakeParts.size();
    }

    private void removeTail() {

        snakeParts.remove(snakeParts.get(snakeParts.size() - 1));
    }

    private void dropTail(){
        tailParts.clear();
        int lengthOfTail = this.snakeParts.size() - cutPoint;
        for (int i = 0; i < lengthOfTail; i++){
            tailParts.add(this.snakeParts.get(cutPoint + i));
        }
        for (int i = 0; i < lengthOfTail; i++){
            this.snakeParts.remove(cutPoint);
        }
        X_SnakeGame.snakes.add(new AnotherSnake(tailParts));
//        System.out.println("Всего змей: " + snakes.size());
//        X_SnakeGame.apples.add(new Apple(getRandom(WIDTH),getRandom(HEIGHT)));

    }
    private int getRandom(int i){
        return (int) (Math.random() * ++i);
    }

    private boolean checkCollision(GameObject gameObject){
        for (int i = 0; i < snakeParts.size(); i++){
            if (gameObject.x == snakeParts.get(i).x && gameObject.y == snakeParts.get(i).y) {
                cutPoint = i;
                return true;
            }
        }
        return false;
    }
    public void move(List<Apple> apples){
        GameObject newHead = createNewHead();
        if (this instanceof MySnake){
            if(this.snakeParts.get(1).x == newHead.x && this.snakeParts.get(1).y == newHead.y){
                this.isAlive = false;
            }
        }
        for (int i = 0; i < apples.size(); i++){
            if (!((newHead.x == apples.get(i).x) && (newHead.y == apples.get(i).y))){
                if (newHead.x < 0 || newHead.y < 0 || newHead.x > (WIDTH - 1) || newHead.y > (HEIGHT- 1)){
                   isAlive = false;
                   this.snakeParts.clear();
                   snakes.remove(this);
                } else {
                    if (!checkCollision(newHead)) {
                        snakeParts.add(0, newHead);
                        removeTail();
                    } else {
                        if (this instanceof MySnake){
                        dropTail();
                        }
                        else {
                            snakeParts.add(0, newHead);
                            removeTail();
                            removeTail();
                        }

                        }
                    }
            }
            else {
                apples.remove(i);
                snakeParts.add(0, newHead);
            }
        }
    }
}
