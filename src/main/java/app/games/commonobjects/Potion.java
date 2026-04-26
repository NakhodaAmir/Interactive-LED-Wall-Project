package app.games.commonobjects;

import app.display.common.SpriteLocation;
import app.gameengine.model.gameobjects.DynamicGameObject;
import app.gameengine.model.gameobjects.StaticGameObject;

/**
 * A static object that heals the player on collision.
 * 
 * @see StaticGameObject
 */
public class Potion extends StaticGameObject {
    private int healAmount;

    public Potion(double x, double y, int heal) {
        super(x, y);
        this.spriteSheetFilename = "MiniWorldSprites/User Interface/Icons-Essentials.png";
        if (heal >= 0) {
            this.defaultSpriteLocation = new SpriteLocation(0, 1);
        } else {
            this.defaultSpriteLocation = new SpriteLocation(1, 1);
        }

        this.healAmount = heal;
    }

    public int getHealAmount() {
        return healAmount;
    }

    @Override
    public boolean isSolid() {
        return false;
    }

    @Override
    public void collideWithDynamicObject(DynamicGameObject otherObject) {
        if (otherObject.isPlayer()) {
            if (getHealAmount() > 0) {
                otherObject.setHP(otherObject.getHP() + getHealAmount());
            } else {
                otherObject.takeDamage(-getHealAmount());
            }
            this.destroy();
        }

    }
}
