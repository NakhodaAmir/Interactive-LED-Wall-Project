package app.gameengine.model.physics;

import java.util.ArrayList;

import app.Settings;
import app.gameengine.Level;
import app.gameengine.model.gameobjects.DynamicGameObject;
import app.gameengine.model.gameobjects.StaticGameObject;

/**
 * Physics engine for handling collision detection and resolution within a game.
 * <p>
 * This class is meant to be the used when updating objects to apply physics and
 * object interactions. Whenever objects collide, that should be observed and
 * handled from here.
 * 
 * @see Collidable
 * @see DynamicGameObject
 * @see StaticGameObject
 * @see Level
 */
public class PhysicsEngine {

    /**
     * Update the physics for an entire level. This means updating each dynamic
     * object according to its velocity, and handling all collisions within the
     * level.
     * 
     * @param dt    the time elapsed since the last update, in seconds
     * @param level the level being updated
     */
    public void updateLevel(double dt, Level level) {
        for (DynamicGameObject gameObject : level.getDynamicObjects()) {
            updateObject(dt, gameObject);
        }
        processAllCollisions(level);
    }

    /**
     * Update a single dynamic object according to its velocity.
     * 
     * @param dt     the time elapsed since the last update, in seconds
     * @param object the object being updated
     */
    public void updateObject(double dt, DynamicGameObject object) {
        double deltaX = object.getVelocity().getX() * dt;
        double deltaY = object.getVelocity().getY() * dt;
        object.setLocation(object.getLocation().getX() + deltaX, object.getLocation().getY() + deltaY);
    }

    /**
     * Detect whether a collision between two hitboxes has occurred. A collision is
     * defined as the two hitboxes having an overlapping distance of strictly
     * greater than 0.
     * 
     * @param hitbox1 the first hitbox
     * @param hitbox2 the second hitbox
     * @return {@code true} if a collision is occurring, {@code false} otherwise
     */
    public boolean detectCollision(Hitbox hitbox1, Hitbox hitbox2) {
        double hitbox1XPosition = hitbox1.getLocation().getX();
        double hitbox1YPosition = hitbox1.getLocation().getY();
        double hitbox2XPosition = hitbox2.getLocation().getX();
        double hitbox2YPosition = hitbox2.getLocation().getY();

        double hitbox1XDimension = hitbox1.getDimensions().getX();
        double hitbox1YDimension = hitbox1.getDimensions().getY();
        double hitbox2XDimension = hitbox2.getDimensions().getX();
        double hitbox2YDimension = hitbox2.getDimensions().getY();

        double xOverlapLeft = hitbox1XPosition + hitbox1XDimension - hitbox2XPosition;
        double xOverlapRight = hitbox2XPosition + hitbox2XDimension - hitbox1XPosition;
        double yOverlapTop = hitbox1YPosition + hitbox1YDimension - hitbox2YPosition;
        double yOverlapBottom = hitbox2YPosition + hitbox2YDimension - hitbox1YPosition;

        return Math.min(Math.min(xOverlapLeft, xOverlapRight), Math.min(yOverlapTop, yOverlapBottom)) > 0;
    }

    /**
     * Returns the minimum overlapping distance of two hitboxes. If the hitboxes are
     * colliding, this distance should be greater than 0. Otherwise, they are not
     * colliding.
     * <p>
     * This method assumes that the hitboxes are already colliding, and the results
     * are undefined otherwise.
     * 
     * @param hitbox1 the first hitbox
     * @param hitbox2 the second hitbox
     * @return the minimum overlapping distance
     */
    public double getOverlap(Hitbox hitbox1, Hitbox hitbox2) {
        double hitbox1XPosition = hitbox1.getLocation().getX();
        double hitbox1YPosition = hitbox1.getLocation().getY();
        double hitbox2XPosition = hitbox2.getLocation().getX();
        double hitbox2YPosition = hitbox2.getLocation().getY();

        double hitbox1XDimension = hitbox1.getDimensions().getX();
        double hitbox1YDimension = hitbox1.getDimensions().getY();
        double hitbox2XDimension = hitbox2.getDimensions().getX();
        double hitbox2YDimension = hitbox2.getDimensions().getY();

        double xOverlapLeft = Math.abs(hitbox1XPosition + hitbox1XDimension - hitbox2XPosition);
        double xOverlapRight = Math.abs(hitbox2XPosition + hitbox2XDimension - hitbox1XPosition);
        double yOverlapTop = Math.abs(hitbox1YPosition + hitbox1YDimension - hitbox2YPosition);
        double yOverlapBottom = Math.abs(hitbox2YPosition + hitbox2YDimension - hitbox1YPosition);

        return Math.min(Math.min(xOverlapLeft, xOverlapRight), Math.min(yOverlapTop, yOverlapBottom));
    }
    /**
     * Process all collisions within a level. This means that for each dynamic
     * object, detect which other dynamic or static objects it is colliding with,
     * and defer collision behavior to the respective objects.
     * 
     * @param level the level being updated
     */
    public void processAllCollisions(Level level) {
        ArrayList<DynamicGameObject> dynamicObjects = level.getDynamicObjects();
        ArrayList<StaticGameObject> staticObjects = level.getStaticObjects();

        for (int i = 0; i < dynamicObjects.size(); i++) {
            DynamicGameObject object1 = dynamicObjects.get(i);
            if (Settings.noclip() && object1.isPlayer()) {
                continue;
            }
            for (int j = i + 1; j < dynamicObjects.size(); j++) {
                DynamicGameObject object2 = dynamicObjects.get(j);
                if (Settings.noclip() && object2.isPlayer()) {
                    continue;
                }
                if (detectCollision(object1.getHitbox(), object2.getHitbox())) {
                    object1.collideWithDynamicObject(object2);
                    object2.collideWithDynamicObject(object1);
                }
            }
            for (int j = 0; j < staticObjects.size(); j++) {
                StaticGameObject staticObject = staticObjects.get(j);
                if (detectCollision(object1.getHitbox(), staticObject.getHitbox())) {
                    staticObject.collideWithDynamicObject(object1);
                    object1.collideWithStaticObject(staticObject);
                }
            }
        }
    }
}
