package app.gameengine;

import app.gameengine.model.datastructures.LinkedListNode;
import app.gameengine.model.gameobjects.Player;

/**
 * A {@code Game} subclass that organizes levels linearly, such that each level
 * has a single "next" level. This is implemented using the
 * {@link LinkedListNode} class.
 * <p>
 * This class implements the functionality to advance to the next level, as well
 * as additional functionality to replace or remove levels by name.
 * 
 * @see LinkedListNode
 * @see Game
 * @see Level
 */
public class LinearGame extends Game {
    private LinkedListNode<Level> head;
    private LinkedListNode<Level> tail;

    public LinearGame() {
        super();
        head = null;
        tail = null;
    }

    public LinearGame(Player player) {
        super(player);
        head = null;
        tail = null;
    }

    public LinkedListNode<Level> getLevelList() {
        return head;
    }

    public void setLevelList(LinkedListNode<Level> node) {
        head = node;
        tail = getTail();
    }

    public void addLevel(Level level) {
        LinkedListNode<Level> nodeToAdd = new LinkedListNode<>(level, null);
        if (head == null) {
            setLevelList(nodeToAdd);
        }
        else {
            tail.setNext(nodeToAdd);
            tail = nodeToAdd;
        }
    }

    public void advanceLevel() {
        if (head == null) {return; }
        advanceLevelHelper(head);
    }

    public void removeLevelByName(String name) {
        if (head == null) {return;}
        if (head.getValue().getName().equals(name)) { head = head.getNext(); return; }
        removeLevelByNameHelper(name, head.getNext(), head);
    }

    private void removeLevelByNameHelper(String string, LinkedListNode<Level> node, LinkedListNode<Level> prevNode) {
        if (node.getValue() == null) {return; }
        if (node.getValue().getName().equals(string)) {
            prevNode.setNext(node.getNext());
        }
        else {
            if (node.getNext() == null) {return; }
            removeLevelByNameHelper(string, node.getNext(), node);
        }
    }

    private void advanceLevelHelper(LinkedListNode<Level> node) {
        if (node.getNext() == null) {return;}
        if (node.getValue().getName().equals(getCurrentLevel().getName())) {
            loadLevel(node.getNext().getValue());
        }
        else {
            advanceLevelHelper(node.getNext());
        }
    }

    private LinkedListNode<Level> getTail() {
        if (head == null) {return null; }
        return getTailHelper(head);
    }

    private LinkedListNode<Level> getTailHelper(LinkedListNode<Level> node) {
        if (node.getNext() == null) {
            return node;
        }
        else {
            return getTailHelper(node.getNext());
        }
    }
}
