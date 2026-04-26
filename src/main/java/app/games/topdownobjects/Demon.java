package app.games.topdownobjects;

import app.gameengine.model.ai.Decision;
import app.gameengine.model.ai.DecisionTree;
import app.gameengine.model.ai.roguelike.MoveTowardPlayer;
import app.gameengine.model.datastructures.BinaryTreeNode;
import app.gameengine.model.gameobjects.Agent;
import app.gameengine.model.gameobjects.DynamicGameObject;

/**
 * An {@link Enemy} with basic behavior and a Demon sprite.
 * 
 * @see Enemy
 * @see Agent
 * @see DynamicGameObject
 */
public class Demon extends Enemy {

    public Demon(double x, double y, int maxHP, int strength) {
        super(x, y, maxHP, strength);
        this.spriteSheetFilename = "MiniWorldSprites/Characters/Monsters/Demons/ArmouredRedDemon.png";

        BinaryTreeNode<Decision> root = new BinaryTreeNode<>(new MoveTowardPlayer(this, "Move"), null, null);
        setDecisionTree(new DecisionTree(root));
    }

    public Demon(double x, double y) {
        this(x, y, 100, 30);
    }

}
