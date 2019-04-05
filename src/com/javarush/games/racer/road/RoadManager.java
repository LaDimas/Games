package com.javarush.games.racer.road;

import com.javarush.engine.cell.Game;
import com.javarush.games.racer.GameObject;
import com.javarush.games.racer.PlayerCar;
import com.javarush.games.racer.RacerGame;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RoadManager {
    public static final int LEFT_BORDER = RacerGame.ROADSIDE_WIDTH;
    public static final int RIGHT_BORDER = RacerGame.WIDTH - LEFT_BORDER;
    private static  final int FIRST_LANE_POSITION = 16;
    private static  final int  FOURTH_LANE_POSITION = 44;
    private static final int PLAYER_CAR_DISTANCE = 12;
    private int passedCarsCount = 0;
    public List<RoadObject> items = new ArrayList<>();

    private RoadObject createRoadObject(RoadObjectType type, int x, int y){
        if (type == RoadObjectType.THORN)
            return new Thorn(x, y);
        if (type == RoadObjectType.DRUNK_CAR)
            return new MovingCar(x, y);
        else return new Car(type, x, y);
    }

    private void generateThorn(Game game){
        if (game.getRandomNumber(100) < 10 && !isThornExists()) {
            addRoadObject(RoadObjectType.THORN, game);
        }
    }

    private boolean isRoadSpaceFree(RoadObject object){
        int count = 0;
        for (RoadObject roadObject : items){
            if (roadObject.isCollisionWithDistance(object, PLAYER_CAR_DISTANCE)){
                count ++;
            }
        }
        if (count == items.size() || count == 0)
            return true;
        else return false;
    }

    public void generateNewRoadObjects(Game game){
        generateThorn(game);
        generateRegularCar(game);
        generateMovingCar(game);
    }

    private void generateRegularCar(Game game){
        int carTypeNumber = game.getRandomNumber(4);
        if (game.getRandomNumber(100) < 15) {
            addRoadObject(RoadObjectType.values()[carTypeNumber], game);
        }

    }
     private void generateMovingCar(Game game){
         if (game.getRandomNumber(100) < 10 && !isMovingCarExists()) {
             addRoadObject(RoadObjectType.DRUNK_CAR, game);
         }
     }

    private void addRoadObject(RoadObjectType type, Game game) {
        int y = -1 * RoadObject.getHeight(type);
        int x = game.getRandomNumber(FIRST_LANE_POSITION, FOURTH_LANE_POSITION);
        RoadObject roadObject = createRoadObject(type, x, y);
        boolean test = isRoadSpaceFree(roadObject);
        boolean check = isRoadFree(roadObject);
        if (roadObject != null && test){
            items.add(roadObject);
        }
    }
    private boolean isRoadFree(RoadObject object){
        boolean result = false;
        for (RoadObject roadObject : items){
            if (roadObject.isCollisionWithDistance(object, 12)){
                result = true;
            }else result = false;
        }
        return result;
    }


    public int getPassedCarsCount() {
        return passedCarsCount;
    }

    public void draw(Game game){
        for (RoadObject object : items){
            object.draw(game);
        }
    }

    public void move(int boost){
        for (RoadObject object : items){
            object.move(boost + object.speed, items);
        }
        deletePassedItems();
    }

    public boolean checkCrush(PlayerCar playerCar){
        for (RoadObject object : items) {
            if (object.isCollision(playerCar)) {
                return true;
            }
        }
        return false;

    }

    private boolean isThornExists(){
        for (RoadObject object : items) {
            if (object.type.equals(RoadObjectType.THORN)) {
                return true;
            }
        }
    return false;
    }
    private boolean isMovingCarExists(){
        for (RoadObject object : items) {
            if (object.type.equals(RoadObjectType.DRUNK_CAR)) {
                return true;
            }
        }
        return false;
    }

    private void deletePassedItems(){
        Iterator iterator = items.iterator();
        while(iterator.hasNext()){
            RoadObject item = (RoadObject)iterator.next();
            if(item.y >= RacerGame.HEIGHT){
                if(!(item instanceof Thorn)) passedCarsCount++;
                iterator.remove();
            }
        }
//        List<RoadObject> itemsCopy = items;
//        for (int i = 0; i < itemsCopy.size();){
//            if (itemsCopy.get(i).y >= RacerGame.HEIGHT){
//                items.remove(itemsCopy.get(i));
//                if (!itemsCopy.get(i).type.equals(RoadObjectType.THORN)){
//                    passedCarsCount ++;
//                }
//            } else i++;
    }
}
