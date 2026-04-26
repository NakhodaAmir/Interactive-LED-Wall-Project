package app.games.snake;

import java.util.ArrayList;
import java.util.HashSet;

import app.display.common.Background;
import app.gameengine.Game;
import app.gameengine.Level;
import app.gameengine.model.physics.PhysicsEngine;
import app.gameengine.model.physics.Vector2D;
import app.gameengine.utils.Randomizer;
import app.gameengine.utils.Timer;

/**
 * A level within a game of snake.
 * <p>
 * This level manages logic including timing of player movement, spawning new
 * food, and keeping track of the snake positions.
 * 
 * @see Level
 * @see Snake
 * @see SnakeFood
 */
public class SnakeLevel extends Level {

    private Timer timer;
    private ArrayList<SnakeFood> food = new ArrayList<>();
    private ArrayList<SnakeBody> tail = new ArrayList<>();
    private int lengthIncrease;
    private int startingLength;
    private int numFood;

    public SnakeLevel(Game game, int size, Timer timer, String name) {
        this(game, size, timer, name, 1, 3, 1);
    }

    public SnakeLevel(Game game, int size, Timer timer, String name, int lengthIncrease, int startingLength,
            int numFood) {
        super(game, new PhysicsEngine(), size, size, name);
        this.timer = timer;
        this.lengthIncrease = Math.min(lengthIncrease, size * size - startingLength);
        this.startingLength = Math.min(startingLength, size * size);
        this.numFood = Math.min(numFood, size * size - 2);
        this.keyboardControls = new SnakeControls(game);
        this.setBackground(new Background("snake/snakeColors.png", 3, 0));
    }

    /**
     * Returns the list of {@link SnakeFood} objects currently in the level.
     * 
     * @return the list of food
     */
    public ArrayList<SnakeFood> getFood() {
        return this.food;
    }

    /**
     * Returns the list of {@link SnakeBody} objects currently in the level. This
     * does not include the head of the snake.
     * 
     * @return the list of body segments
     */
    public ArrayList<SnakeBody> getTail() {
        return this.tail;
    }

    /**
     * Surrounds the level with {@link SnakeWall} objects, just out of bounds. This
     * is to prevent the snake from leaving the confines of the level.
     */
    public void wallOffBoundary() {
        for (int x = -1; x <= width; x++) {
            for (int y = -1; y <= height; y++) {
                if (x >= 0 && x <= width - 1 && y >= 0 && y <= height - 1) { continue;}
                addStaticObject(new SnakeWall(x, y));
            }
        }
    }

    /**
     * Randomly choose a new location for food, that is not already occupied. If the
     * snake head and tail fill all available space, it will advance to the next
     * level. If there is no room to spawn food, it will do nothing.
     * <p>
     * Note that food is added to this class's list of food, as well as its list of
     * static objects.
     */
    public void spawnFood() {
        if (1 + tail.size() >= width * height) { this.game.advanceLevel(); return; }

        ArrayList<Vector2D> occupied = new ArrayList<>();
        occupied.add(getPlayer().getLocation());
        for (SnakeBody snakebody : tail) {
            occupied.add(snakebody.getLocation());
        }
        for (SnakeFood snakefood : food) {
            occupied.add(snakefood.getLocation());
        }

        if (occupied.size() >= width * height) { return; }

        Vector2D location = Randomizer.randomIntVector2D(new Vector2D(width, height), occupied);

        SnakeFood snakeFood = new SnakeFood(location.getX(), location.getY(), this);
        food.add(snakeFood);
        addStaticObject(snakeFood);
    }

    /**
     * Increase the length of the snake by creating new body segments. The amount
     * which the length increases by is determined by the lengthIncrease passed into
     * the constructor.
     * <p>
     * Note that body segments are added to this class's list of body segments, as
     * well as its list of static objects.
     */
    public void lengthenSnake() {
        SnakeBody newTail;
        for (int i = 0; i < lengthIncrease; i++) {
            if (tail.isEmpty()) {
                newTail = new SnakeBody(this.getPlayer().getLocation().getX() - this.getPlayer().getOrientation().getX(), this.getPlayer().getLocation().getY() - this.getPlayer().getOrientation().getY());
            }
            else {
                newTail = new SnakeBody(tail.getFirst().getLocation().getX(), tail.getFirst().getLocation().getY());
            }
            tail.addFirst(newTail);
            addStaticObject(newTail);
        }
    }

    /**
     * Create the snake at the center of the level. All body segments start in a
     * stack behind the head of the snake.
     */
    public void spawnSnake() {
        SnakeBody newTail;
        for (int i = 0; i < startingLength - 1; i++) {
            newTail = new SnakeBody(this.getPlayer().getLocation().getX() - this.getPlayer().getOrientation().getX(), this.getPlayer().getLocation().getY() - this.getPlayer().getOrientation().getY());
            tail.addFirst(newTail);
            addStaticObject(newTail);
        }
    }

    /**
     * Move each segment of the snake forward in its direction of travel by one
     * tile, including both body segments and the head of the snake.
     */
    private void moveSnake() {
        double oldXPosition = this.getPlayer().getLocation().getX();
        double oldYPosition = this.getPlayer().getLocation().getY();
        double newXPosition = oldXPosition + this.getPlayer().getOrientation().getX();
        double newYPosition = oldYPosition + this.getPlayer().getOrientation().getY();
        this.getPlayer().setLocation(newXPosition, newYPosition);
        if(tail.isEmpty()) {return;}
        if (tail.size() == 1) {
            tail.getFirst().setLocation(oldXPosition, oldYPosition);
        }
        else {
            SnakeBody lastTail = tail.removeFirst();
            lastTail.setLocation(oldXPosition, oldYPosition);
            tail.add(lastTail);
        }
    }

    @Override
    public void update(double dt) {
        this.food.removeIf(SnakeFood::isDestroyed);
        this.tail.removeIf(SnakeBody::isDestroyed);
        // Move body
        if (this.timer.tick(dt) > 0) {
            this.keyboardControls.processInput(0);
            this.moveSnake();
        }
        super.update(dt);
    }

    @Override
    public void load() {
        super.load();
        this.food.forEach(SnakeFood::destroy);
        this.tail.forEach(SnakeBody::destroy);
        this.food.clear();
        this.tail.clear();

        this.spawnSnake();
        for (int i = 0; i < this.numFood; i++) {
            this.spawnFood();
        }
        this.wallOffBoundary();
    }

    @Override
    public void reset() {
        this.load();
    }

    @Override
    public String getUIString() {
        return String.format("Score: %.0f", this.score);
    }

}
