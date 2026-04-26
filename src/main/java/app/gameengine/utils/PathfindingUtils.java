package app.gameengine.utils;

import app.gameengine.Level;
import app.gameengine.model.datastructures.LinkedListNode;
import app.gameengine.model.gameobjects.DynamicGameObject;
import app.gameengine.model.gameobjects.StaticGameObject;
import app.gameengine.model.physics.Vector2D;

import java.util.*;

public class PathfindingUtils {
    public static LinkedListNode<Vector2D> findPath(Vector2D start, Vector2D target) {
        Vector2D startCoord = Vector2D.floor(start);
        Vector2D targetCoord = Vector2D.floor(target);
        int xDir = (targetCoord.getX() - startCoord.getX() > 0) ? 1 : -1;
        int yDir = (targetCoord.getY() - startCoord.getY() > 0) ? 1 : -1;
        LinkedListNode<Vector2D> head = new LinkedListNode<>(startCoord, null);
        LinkedListNode<Vector2D> currentNode = head;

        while (!currentNode.getValue().equals(targetCoord)) {
            Vector2D nextCoord = null;
            if (currentNode.getValue().getX() != targetCoord.getX()) {
                nextCoord = new Vector2D(currentNode.getValue().getX() + xDir, currentNode.getValue().getY());
            } else if (currentNode.getValue().getY() != targetCoord.getY()) {
                nextCoord = new Vector2D(currentNode.getValue().getX(), currentNode.getValue().getY() + yDir);
            }
            LinkedListNode<Vector2D> nextCoordNode = new LinkedListNode<>(nextCoord, null);
            currentNode.setNext(nextCoordNode);
            currentNode = nextCoordNode;
        }

        return head;
    }

    public static LinkedListNode<Vector2D> findPathAvoidWalls(Level level, Vector2D start, Vector2D end){
        if (!GameUtils.isInBounds(level, start) || !GameUtils.isInBounds(level, end)) { return null; }
        Vector2D startCoord = Vector2D.floor(start);
        Vector2D targetCoord = Vector2D.floor(end);
        HashMap<Vector2D, Vector2D> parentList = new HashMap<>();

        HashMap<Vector2D, DynamicGameObject> dynamicGameObjectHashMap = new HashMap<>();
        for (DynamicGameObject object : level.getDynamicObjects()) {
            dynamicGameObjectHashMap.put(Vector2D.floor(object.getLocation()), object);
        }
        HashMap<Vector2D, StaticGameObject> staticGameObjectHashMap = new HashMap<>();
        for (StaticGameObject object : level.getStaticObjects()) {
            staticGameObjectHashMap.put(Vector2D.floor(object.getLocation()), object);
        }
        DynamicGameObject dynamicStartObject = dynamicGameObjectHashMap.get(startCoord);
        StaticGameObject staticStartObject = staticGameObjectHashMap.get(startCoord);
        if (dynamicStartObject != null && dynamicStartObject.isSolid()){ return null; }
        if (staticStartObject != null && staticStartObject.isSolid()){ return null; }
        DynamicGameObject dynamicTargetObject = dynamicGameObjectHashMap.get(targetCoord);
        StaticGameObject staticTargetObject = staticGameObjectHashMap.get(targetCoord);
        if (dynamicTargetObject != null && dynamicTargetObject.isSolid()){ return null; }
        if (staticTargetObject != null && staticTargetObject.isSolid()){ return null; }

        Queue<Vector2D> openList = new ArrayDeque<>();
        HashSet<Vector2D> visitedList = new HashSet<>();
        openList.add(startCoord);
        visitedList.add(startCoord);
        parentList.put(startCoord, null);


        while(!openList.isEmpty()) {
            Vector2D currentNode = openList.remove();

            if (currentNode.equals(targetCoord)) { break; }

            for (int x = -1; x <= 1; x++) {
                for (int y = -1; y <= 1; y++) {
                    if ((x == 0 && y == 0) || (x == -1 && y == -1) || (x == -1 && y == 1) || (x == 1 && y == -1) || (x == 1 && y == 1)) { continue; }
                    Vector2D neighbour = new Vector2D(currentNode.getX() + x, currentNode.getY() + y);
                    if (!GameUtils.isInBounds(level, neighbour)) { continue; }
                    DynamicGameObject dynamicNeighbour = dynamicGameObjectHashMap.get(neighbour);
                    if(dynamicNeighbour != null && dynamicNeighbour.isSolid()) {continue;}
                    StaticGameObject staticNeighbour = staticGameObjectHashMap.get(neighbour);
                    if(staticNeighbour != null && staticNeighbour.isSolid()) {continue;}
                    if (!visitedList.contains(neighbour)) {
                        visitedList.add(neighbour);
                        openList.add(neighbour);
                        parentList.put(neighbour, currentNode);
                    }
                }
            }
        }

        if (!parentList.containsKey(targetCoord)) {
            return null;
        }
        LinkedListNode<Vector2D> head = null;
        Vector2D currentPathNode = targetCoord;

        while (currentPathNode != null) {
            LinkedListNode<Vector2D> node = new LinkedListNode<>(currentPathNode, head);
            head = node;
            currentPathNode = parentList.get(currentPathNode);
        }

        return head;
    }
}
