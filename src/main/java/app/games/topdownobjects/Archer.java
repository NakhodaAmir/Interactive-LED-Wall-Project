package app.games.topdownobjects;

import app.display.common.SpriteLocation;
import app.gameengine.model.ai.Decision;
import app.gameengine.model.ai.DecisionTree;
import app.gameengine.model.ai.roguelike.MoveTowardPlayer;
import app.gameengine.model.ai.roguelike.ShootPlayer;
import app.gameengine.model.datastructures.BinaryTreeNode;

public class Archer extends Enemy {

    public Archer(double x, double y, int maxHP, int strength) {
        super(x, y, maxHP, strength);
        this.spriteSheetFilename = "MiniWorldSprites/Characters/Monsters/Orcs/ArcherGoblin.png";
        this.defaultSpriteLocation = new SpriteLocation(1, 2);
        this.animations.remove("attack_up");
        this.animations.remove("attack_down");
        this.animations.remove("attack_left");
        this.animations.remove("attack_right");

        BinaryTreeNode<Decision> root = new BinaryTreeNode<>(new ShootPlayer(this, "Move", 5), null, null);
        setDecisionTree(new DecisionTree(root));
    }

    public Archer(double x, double y) {
        this(x, y, 100, 10);
    }

}
