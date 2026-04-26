package tests;

import app.gameengine.model.datastructures.LinkedListNode;
import app.gameengine.model.physics.Vector2D;
import org.junit.Assert;

public class TestUtils {
    public static void validatePath(LinkedListNode<Vector2D> node) {
        if (node == null) { return; }

        LinkedListNode<Vector2D> currentNode = node;

        while (currentNode != null) {
            Vector2D currentLocation = currentNode.getValue();
            Assert.assertEquals(Math.floor(currentLocation.getX()), currentLocation.getX(), 0.01);
            Assert.assertEquals(Math.floor(currentLocation.getY()), currentLocation.getY(), 0.01);

            LinkedListNode<Vector2D> nextNode = currentNode.getNext();

            if (nextNode != null) {
                Vector2D nextLocation = nextNode.getValue();
                double dx = Math.abs(currentLocation.getX() - nextLocation.getX());
                double dy = Math.abs(currentLocation.getY() - nextLocation.getY());

                Assert.assertTrue((dx == 1 && dy == 0) || (dx == 0 && dy == 1));
            }

            currentNode = nextNode;
        }
    }
}
