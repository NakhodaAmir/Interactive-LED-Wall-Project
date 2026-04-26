package app.games.roguelikeobjects;

import java.util.*;

import app.gameengine.Game;
import app.gameengine.Level;
import app.gameengine.LevelParser;
import app.gameengine.model.physics.Vector2D;
import app.gameengine.utils.GameUtils;
import app.gameengine.utils.Randomizer;

/**
 * Top-down game that is randomly generated from presets each time.
 * <p>
 * Levels within this game are randomly chosen by the
 * {@link RoguelikeLevelFactory}. Within each level, {@link Marker}s denote the
 * locations of objects which are randomly chosen by either the
 * {@link EnemyFactory} or the {@link CollectibleFactory}.
 *
 * @see RoguelikeLevelFactory
 * @see EnemyFactory
 * @see CollectibleFactory
 */
public class RoguelikeGame extends Game {

    protected static final Vector2D UP_VECTOR = new Vector2D(0, -1);
    protected static final Vector2D RIGHT_VECTOR = new Vector2D(1, 0);
    protected static final Vector2D DOWN_VECTOR = new Vector2D(0, 1);
    protected static final Vector2D LEFT_VECTOR = new Vector2D(-1, 0);
    protected static final ArrayList<Vector2D> DIRECTIONS = new ArrayList<>(
            Arrays.asList(UP_VECTOR, RIGHT_VECTOR, DOWN_VECTOR, LEFT_VECTOR));

    private Vector2D mapSize = new Vector2D(4, 3);
    private int maxRooms = 10;

    private HashMap<String, RoguelikeLevel> levelMap = new HashMap<>();
    private int roomsGenerated;
    private RoguelikeLevel previousLevel;

    public HashMap<String, RoguelikeLevel> getLevelMap() {
        return this.levelMap;
    }

    public RoguelikeLevel getPreviousLevel() {
        return previousLevel;
    }

    public void setGameParameters(Vector2D mapSize, int maxRooms) {
        this.mapSize = mapSize;
        this.maxRooms = maxRooms;
    }

    @Override
    public void init() {
        super.init();
        generateMap();
    }

    @Override
    public void addLevel(Level level) {
        if (level instanceof RoguelikeLevel roguelikeLevel) {
            this.levelMap.put(level.getName(), roguelikeLevel);
        } else {
            System.err.println("** Level could not be added **");
        }
    }

    @Override
    public void changeLevel(String name) {
        Level level = null;
        if (this.levelMap.containsKey(name)) {
            level = this.levelMap.get(name);
        } else {
            level = LevelParser.parseLevel(this, "roguelike/" + name + ".csv");
            if (level != null) {
                this.addLevel(level);
            }
        }

        if (level != null) {
            // Transfer controls for smooth transition
            level.setKeyboardControls(this.currentLevel.getKeyboardControls());
            level.setMouseControls(this.currentLevel.getMouseControls());
            this.loadLevel(level);
        }
    }

    @Override
    public void loadLevel(Level level) {
        super.loadLevel(level);
        if (level instanceof RoguelikeLevel roguelikeLevel) {
            this.previousLevel = roguelikeLevel;
        }
    }

    @Override
    public void resetGame() {
        this.levelMap.clear();
        this.roomsGenerated = 0;
        this.previousLevel = null;

        super.resetGame();
        this.getPlayer().reset();
        this.generateMap();
    }

    @Override
    public void resetCurrentLevel() {
        this.resetGame();
    }

    /**
     * Randomly generate each level within the game, and load the starting level.
     */
//    private void generateMap() {
//        RoguelikeLevel start = getNextLevelToGenerate();
//        Vector2D position = new Vector2D(0, 0);
//        start.initialize(position);
//        this.addLevel(start);
//
//        RoguelikeLevel bossRoom = RoguelikeLevelFactory.getBossLevel(this);
//        position = new Vector2D(1, 0);
//        bossRoom.initialize(position);
//        this.addLevel(bossRoom);
//
//        start.openDoor(bossRoom);
//        bossRoom.openDoor(start);
//
//        this.loadLevel(start);
//    }

    private void generateMap() {
        Vector2D startPos = getRandomStartingPosition();
        Stack<Vector2D> stack = new Stack<>();
        HashSet<Vector2D> visited = new HashSet<>();
        HashMap<Vector2D, RoguelikeLevel> parent = new HashMap<>();
        RoguelikeLevel startLevel = null;

        stack.push(startPos);
        visited.add(startPos);
        parent.put(startPos, null);

        while (!stack.isEmpty()) {
            Vector2D currentPos = stack.pop();

            RoguelikeLevel currentLevel = getNextLevelToGenerate();
            currentLevel.initialize(currentPos);
            this.addLevel(currentLevel);

            RoguelikeLevel parentLevel = parent.get(currentLevel.getLevelLocation());
            if (parentLevel != null) {
                currentLevel.openDoor(parentLevel);
                parentLevel.openDoor(currentLevel);
            }
            else {
                startLevel = currentLevel;
            }
            this.roomsGenerated++;

            if (this.roomsGenerated >= this.maxRooms) {
                this.loadLevel(startLevel);
                return;
            }

            ArrayList<Vector2D> directions = Randomizer.shuffleArrayList(DIRECTIONS);
            for (Vector2D dir : directions) {
                Vector2D neighbor = Vector2D.add(currentPos, dir);

                if (GameUtils.isInBounds(neighbor, this.mapSize) && !visited.contains(neighbor)) {
                    visited.add(neighbor);
                    parent.put(neighbor, currentLevel);
                    stack.push(neighbor);
                }
            }
        }

        this.loadLevel(startLevel);
      }

    private Vector2D getRandomStartingPosition() {
        return new Vector2D(Randomizer.randomInt((int) mapSize.getX()), Randomizer.randomInt((int) mapSize.getY()));
    }

    private RoguelikeLevel getNextLevelToGenerate() {
        if (roomsGenerated <= 0) {
            return RoguelikeLevelFactory.getStartingLevel(this);
        } else if (roomsGenerated < maxRooms - 1) {
            return RoguelikeLevelFactory.getRandomLevel(this);
        }
        return RoguelikeLevelFactory.getBossLevel(this);
    }
}
