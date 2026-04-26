package app.games.commonobjects;

import app.display.common.SpriteLocation;
import app.gameengine.model.gameobjects.DynamicGameObject;
import app.gameengine.model.gameobjects.StaticGameObject;

/**
 * A {@code StaticGameObject} that prevents collision by moving any
 * {@code DynamicGameObject}s that collide with it.
 */
public class Wall extends StaticGameObject {

    public Wall(double x, double y) {
        super(x, y);
        this.spriteSheetFilename = "MiniWorldSprites/Ground/Cliff.png";
        this.defaultSpriteLocation = new SpriteLocation(3, 0);
    }

    @Override
    public void collideWithDynamicObject(DynamicGameObject otherObject) {
        double objectXPosition = otherObject.getHitbox().getLocation().getX();
        double objectYPosition = otherObject.getHitbox().getLocation().getY();
        double wallXPosition = getHitbox().getLocation().getX();
        double wallYPosition = getHitbox().getLocation().getY();

        double objectXDimension = otherObject.getHitbox().getDimensions().getX();
        double objectYDimension = otherObject.getHitbox().getDimensions().getY();
        double wallXDimension = getHitbox().getDimensions().getX();
        double wallYDimension = getHitbox().getDimensions().getY();

        double xOverlapRight = objectXPosition + objectXDimension - wallXPosition;
        double xOverlapLeft = wallXPosition + wallXDimension - objectXPosition;
        double yOverlapTop = wallYPosition + wallYDimension - objectYPosition;
        double yOverlapBottom = objectYPosition + objectYDimension - wallYPosition;

        if (Math.min(xOverlapRight, xOverlapLeft) < Math.min(yOverlapTop, yOverlapBottom)) {
            if (xOverlapRight < xOverlapLeft) {
                otherObject.setLocation(otherObject.getLocation().getX() - xOverlapRight, otherObject.getLocation().getY());
            }
            else {
                otherObject.setLocation(otherObject.getLocation().getX() + xOverlapLeft, otherObject.getLocation().getY());
            }
            otherObject.setVelocity(0, otherObject.getVelocity().getY());
       }
        else {
            if (yOverlapTop < yOverlapBottom) {
                otherObject.setLocation(otherObject.getLocation().getX(), otherObject.getLocation().getY() + yOverlapTop);
            }
            else {
                otherObject.setLocation(otherObject.getLocation().getX(), otherObject.getLocation().getY() - yOverlapBottom);
            }
            otherObject.setVelocity(otherObject.getVelocity().getX(), 0);
        }
    }
}
