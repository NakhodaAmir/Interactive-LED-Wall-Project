package tests;

import java.util.ArrayList;
import java.util.Arrays;

import app.gameengine.model.datastructures.LinkedListNode;
import app.gameengine.utils.PathfindingUtils;
import app.games.commonobjects.Wall;
import org.junit.Assert;
import org.junit.Test;

import app.gameengine.Level;
import app.gameengine.LevelParser;
import app.gameengine.LinearGame;
import app.gameengine.model.ai.Decision;
import app.gameengine.model.ai.roguelike.Heal;
import app.gameengine.model.ai.roguelike.LowHP;
import app.gameengine.model.ai.roguelike.NearPlayer;
import app.gameengine.model.ai.roguelike.ShootHomingProjectile;
import app.gameengine.model.ai.roguelike.ShootPlayer;
import app.gameengine.model.gameobjects.Agent;
import app.gameengine.model.gameobjects.DynamicGameObject;
import app.gameengine.model.gameobjects.Player;
import app.gameengine.model.gameobjects.StaticGameObject;
import app.gameengine.model.physics.Vector2D;
import app.games.commonobjects.Potion;
import app.games.commonobjects.Spike;
import app.games.mario.MarioLevel;
import app.games.roguelikeobjects.DirectionalWall;
import app.games.roguelikeobjects.LevelDoor;
import app.games.roguelikeobjects.Marker;
import app.games.roguelikeobjects.RoguelikeGame;
import app.games.roguelikeobjects.RoguelikeLevel;
import app.games.topdownobjects.Archer;
import app.games.topdownobjects.Demon;
import app.games.topdownobjects.Enemy;
import app.games.topdownobjects.EnemyArrowProjectile;
import app.games.topdownobjects.EnemyHomingProjectile;
import app.games.topdownobjects.Minotaur;
import app.games.topdownobjects.Sorcerer;
import app.games.topdownobjects.TopDownLevel;

import static org.junit.Assert.*;

public class TestTask4 {

    public static final double EPSILON = 1e-5;

    private LinkedListNode<Vector2D> getLastNode(LinkedListNode<Vector2D> head) {
        if (head == null) return null;
        LinkedListNode<Vector2D> current = head;
        while (current.getNext() != null) {
            current = current.getNext();
        }
        return current;
    }
    @Test
    public void testEndNegativeOutOfBounds() {
        LinearGame game = new LinearGame();
        TopDownLevel level = new TopDownLevel(game, 5, 5, "test");
        Vector2D startCoord = new Vector2D(0, 0);
        Vector2D targetCoord = new Vector2D(0, -1);

        assertNull(PathfindingUtils.findPathAvoidWalls(level, startCoord, targetCoord));
        TestUtils.validatePath(PathfindingUtils.findPathAvoidWalls(level, startCoord, targetCoord));
    }

    @Test
    public void testDecimalStartInsideSolid() {
        LinearGame game = new LinearGame();
        TopDownLevel level = new TopDownLevel(game, 5, 5, "test");

        level.addStaticObject(new Wall(1, 1));

        Vector2D startCoord = new Vector2D(1.8, 1.9);
        Vector2D targetCoord = new Vector2D(3, 3);

        assertNull(PathfindingUtils.findPathAvoidWalls(level, startCoord, targetCoord));
        TestUtils.validatePath(PathfindingUtils.findPathAvoidWalls(level, startCoord, targetCoord));
    }

    @Test
    public void testTargetBoxedIn() {
        LinearGame game = new LinearGame();
        TopDownLevel level = new TopDownLevel(game, 5, 5, "test");

        level.addStaticObject(new Wall(3, 4));
        level.addStaticObject(new Wall(4, 3));
        level.addStaticObject(new Wall(3, 3));

        Vector2D startCoord = new Vector2D(0, 0);
        Vector2D targetCoord = new Vector2D(4, 4);

        assertNull(PathfindingUtils.findPathAvoidWalls(level, startCoord, targetCoord));
        TestUtils.validatePath(PathfindingUtils.findPathAvoidWalls(level, startCoord, targetCoord));
    }

    @Test
    public void testOneByOneLevel() {
        LinearGame game = new LinearGame();
        TopDownLevel level = new TopDownLevel(game, 1, 1, "test");

        Vector2D startCoord = new Vector2D(0, 0);
        Vector2D targetCoord = new Vector2D(0, 0);

        LinkedListNode<Vector2D> path = PathfindingUtils.findPathAvoidWalls(level, startCoord, targetCoord);

        assertNotNull(path);
        assertEquals(new Vector2D(0, 0), path.getValue());
        assertNull(path.getNext()); // Path length should be exactly 1
        TestUtils.validatePath(path);
    }

    @Test
    public void testStartOutOfBounds() {
        LinearGame game = new LinearGame();
        TopDownLevel level = new TopDownLevel(game, 5, 5, "test");
        Vector2D startCoord1 = new Vector2D(-1, 0);
        Vector2D targetCoord1 = new Vector2D(3, 3);
        assertNull(PathfindingUtils.findPathAvoidWalls(level, startCoord1, targetCoord1));
        TestUtils.validatePath(PathfindingUtils.findPathAvoidWalls(level, startCoord1, targetCoord1));

        Vector2D startCoord2 = new Vector2D(0, 0);
        Vector2D targetCoord2 = new Vector2D(10, 10);
        assertNull(PathfindingUtils.findPathAvoidWalls(level, startCoord2, targetCoord2));
        TestUtils.validatePath(PathfindingUtils.findPathAvoidWalls(level, startCoord2, targetCoord2));

        level.addStaticObject(new Wall(1, 1));
        Vector2D startCoord3 = new Vector2D(1, 1);
        Vector2D targetCoord3 = new Vector2D(3, 3);
        assertNull(PathfindingUtils.findPathAvoidWalls(level, startCoord3, targetCoord3));
        TestUtils.validatePath(PathfindingUtils.findPathAvoidWalls(level, startCoord3, targetCoord3));

        Vector2D startCoord4 = new Vector2D(3, 3);
        Vector2D targetCoord4 = new Vector2D(1, 1);
        assertNull(PathfindingUtils.findPathAvoidWalls(level, startCoord4, targetCoord4));
        TestUtils.validatePath(PathfindingUtils.findPathAvoidWalls(level, startCoord4, targetCoord4));
    }
    @Test
    public void testNoCornerClipping() {
        LinearGame game = new LinearGame();
        TopDownLevel level = new TopDownLevel(game, 2, 2, "test");

        level.addStaticObject(new Wall(1, 0));
        level.addStaticObject(new Wall(0, 1));

        Vector2D startCoord = new Vector2D(0, 0);
        Vector2D targetCoord = new Vector2D(1, 1);

        LinkedListNode<Vector2D> path = PathfindingUtils.findPathAvoidWalls(level, startCoord, targetCoord);
        TestUtils.validatePath(path);
        assertNull(path);
    }

    @Test
    public void testEndInsideSolid() {
        TopDownLevel level = new TopDownLevel(null, 5, 5, "test");
        level.addStaticObject(new Wall(3, 3));
        Vector2D start = new Vector2D(1, 1);
        Vector2D end = new Vector2D(3, 3);
        assertNull(PathfindingUtils.findPathAvoidWalls(level, start, end));
    }
    @Test
    public void testFindPathAvoidWallStartIsEnd() {
        LinearGame game = new LinearGame();
        TopDownLevel level = new TopDownLevel(game, 10, 10, "level");
        Vector2D startCoord = new Vector2D(2, 2);
        Vector2D targetCoord = new Vector2D(2, 2);
        LinkedListNode<Vector2D> path = PathfindingUtils.findPathAvoidWalls(level, startCoord, targetCoord);
        TestUtils.validatePath(path);
        assertNotNull(path);
        assertEquals(new Vector2D(2, 2), path.getValue());
    }
    @Test
    public void testFindPathAvoidWallStartIsEndNonTile() {
        LinearGame game = new LinearGame();
        TopDownLevel level = new TopDownLevel(game, 5, 5, "level");
        Vector2D startCoord = new Vector2D(2.3, 2.7);
        Vector2D targetCoord = new Vector2D(2.9, 2.1);
        LinkedListNode<Vector2D> path = PathfindingUtils.findPathAvoidWalls(level, startCoord, targetCoord);
        TestUtils.validatePath(path);
        assertNotNull(path);
    }


    @Test
    public void testFindPathAvoidWallOne() {
        LinearGame game = new LinearGame();
        TopDownLevel level = new TopDownLevel(game, 5, 5, "level");
        LinkedListNode<Vector2D> head = PathfindingUtils.findPathAvoidWalls(level, new Vector2D(0,0), new Vector2D(0,0));
        TestUtils.validatePath(head);
        assertNotNull(head);
        assertEquals(new Vector2D(0, 0), head.getValue());
        assertNull(head.getNext());
    }

    @Test
    public void testFindPathAvoidWallNone() {
        LinearGame game = new LinearGame();
        TopDownLevel level = new TopDownLevel(game, 5, 5, "level");

        level.addStaticObject(new Wall(2,0));
        level.addStaticObject(new Wall(2,1));
        level.addStaticObject(new Wall(2,2));
        level.addStaticObject(new Wall(2,3));
        level.addStaticObject(new Wall(2,4));

        LinkedListNode<Vector2D> head = PathfindingUtils.findPathAvoidWalls(level, new Vector2D(0,0), new Vector2D(4,0));
        TestUtils.validatePath(head);

        assertNull(head);
    }

    @Test
    public void testFindPathAvoidWallLargeGrid() {
        LinearGame game = new LinearGame();
        TopDownLevel level = new TopDownLevel(game, 20, 20, "level");

        Vector2D startCoord = new Vector2D(0, 0);
        Vector2D targetCoord = new Vector2D(19, 19);

        for (int x = 0; x < 20; x++) {
            if (x == 9) { continue; }
            level.addStaticObject(new Wall(x, 19-x));
        }

        LinkedListNode<Vector2D> path = PathfindingUtils.findPathAvoidWalls(level, startCoord, targetCoord);
        TestUtils.validatePath(path);
        assertNotNull(path);
        Assert.assertEquals(startCoord, path.getValue());
        Assert.assertEquals(targetCoord, getLastNode(path).getValue());
        TestUtils.validatePath(path);
    }

    @Test
    public void testFindPathAvoidWallValidatePath() {
        LinearGame game = new LinearGame();
        TopDownLevel level = new TopDownLevel(game, 10, 10, "level");

        Vector2D startCoord = new Vector2D(1, 1);
        Vector2D targetCoord = new Vector2D(8, 1);
        LinkedListNode<Vector2D> pathRight = PathfindingUtils.findPathAvoidWalls(level, startCoord, targetCoord);
        assertNotNull(pathRight);
        Assert.assertEquals(pathRight.getValue(), startCoord);
        Assert.assertEquals(getLastNode(pathRight).getValue(), targetCoord);
        TestUtils.validatePath(pathRight);

        Vector2D startCoord1 = new Vector2D(2, 2);
        Vector2D targetCoord1 = new Vector2D(2, 8);
        LinkedListNode<Vector2D> pathDown = PathfindingUtils.findPathAvoidWalls(level, startCoord1, targetCoord1);
        assertNotNull(pathDown);
        Assert.assertEquals(pathDown.getValue(), startCoord1);
        Assert.assertEquals(getLastNode(pathDown).getValue(), targetCoord1);
        TestUtils.validatePath(pathDown);

        Vector2D startCoord2 = new Vector2D(8, 5);
        Vector2D targetCoord2 = new Vector2D(1, 5);
        LinkedListNode<Vector2D> pathLeft = PathfindingUtils.findPathAvoidWalls(level, startCoord2,targetCoord2);
        assertNotNull(pathLeft);
        Assert.assertEquals(pathLeft.getValue(), startCoord2);
        Assert.assertEquals(getLastNode(pathLeft).getValue(), targetCoord2);
        TestUtils.validatePath(pathLeft);

        Vector2D startCoord3 = new Vector2D(5, 8);
        Vector2D targetCoord3 = new Vector2D(5, 1);
        LinkedListNode<Vector2D> pathUp = PathfindingUtils.findPathAvoidWalls(level, startCoord3, targetCoord3);
        assertNotNull(pathUp);
        Assert.assertEquals(pathUp.getValue(), startCoord3);
        Assert.assertEquals(getLastNode(pathUp).getValue(), targetCoord3);
        TestUtils.validatePath(pathUp);

        Vector2D startCoord4 = new Vector2D(1, 1);
        Vector2D targetCoord4 = new Vector2D(8, 8);
        LinkedListNode<Vector2D> pathStairs = PathfindingUtils.findPathAvoidWalls(level, startCoord4, targetCoord4);
        assertNotNull(pathStairs);
        Assert.assertEquals(pathStairs.getValue(), startCoord4);
        Assert.assertEquals(getLastNode(pathStairs).getValue(), targetCoord4);
        TestUtils.validatePath(pathStairs);

        Vector2D startCoord5 = new Vector2D(11, 11);
        Vector2D targetCoord5 = new Vector2D(11, 11);
        LinkedListNode<Vector2D> pathOutOfBounds = PathfindingUtils.findPathAvoidWalls(level, startCoord5, targetCoord5);
        TestUtils.validatePath(pathOutOfBounds);
        Assert.assertNull(pathOutOfBounds);

        Vector2D startCoord6 = new Vector2D(2, 2);
        Vector2D targetCoord6 = new Vector2D(2, 3);
        LinkedListNode<Vector2D> pathAdjacent = PathfindingUtils.findPathAvoidWalls(level, startCoord6, targetCoord6);
        assertNotNull(pathAdjacent);
        Assert.assertEquals(startCoord6, pathAdjacent.getValue());
        Assert.assertEquals(targetCoord6, getLastNode(pathAdjacent).getValue());
        TestUtils.validatePath(pathAdjacent);

        Vector2D startCoord7 = new Vector2D(0, 0);
        Vector2D targetCoord7 = new Vector2D(4, 4);
        LinkedListNode<Vector2D> pathValid = PathfindingUtils.findPathAvoidWalls(level, startCoord7, targetCoord7);
        TestUtils.validatePath(pathValid);
        assertNotNull(pathValid);
    }

    @Test
    public void testFindPathAvoidWall() {
        LinearGame game = new LinearGame();
        TopDownLevel level = new TopDownLevel(game, 9, 5, "level");

        level.addStaticObject(new Wall(0,0));
        level.addStaticObject(new Wall(1,0));
        level.addStaticObject(new Wall(2,0));
        level.addStaticObject(new Wall(3,0));
        level.addStaticObject(new Wall(4,0));
        level.addStaticObject(new Wall(5,0));
        level.addStaticObject(new Wall(6,0));
        level.addStaticObject(new Wall(7,0));
        level.addStaticObject(new Wall(8,0));

        level.addStaticObject(new Wall(0,1));
        level.addStaticObject(new Wall(0,2));
        level.addStaticObject(new Wall(0,3));
        level.addStaticObject(new Wall(8,1));
        level.addStaticObject(new Wall(8,2));
        level.addStaticObject(new Wall(8,3));

        level.addStaticObject(new Wall(4,2));
        level.addStaticObject(new Wall(5,2));
        level.addStaticObject(new Wall(6,2));

        level.addStaticObject(new Wall(0,4));
        level.addStaticObject(new Wall(1,4));
        level.addStaticObject(new Wall(2,4));
        level.addStaticObject(new Wall(3,4));
        level.addStaticObject(new Wall(4,4));
        level.addStaticObject(new Wall(5,4));
        level.addStaticObject(new Wall(6,4));
        level.addStaticObject(new Wall(7,4));
        level.addStaticObject(new Wall(8,4));

        level.addStaticObject(new Wall(2,1));
        level.addStaticObject(new Wall(2,3));

        Vector2D startCoord = new Vector2D(1, 1);
        Vector2D targetCoord = new Vector2D(7, 1);

        LinkedListNode<Vector2D> head = PathfindingUtils.findPathAvoidWalls(level, startCoord, targetCoord);
        TestUtils.validatePath(head);

        Assert.assertEquals(head.getValue(), startCoord);
        Assert.assertEquals(getLastNode(head).getValue(), targetCoord);

        ArrayList<Vector2D> correctPath = new ArrayList<>();
        correctPath.add(new Vector2D(1, 1));
        correctPath.add(new Vector2D(1, 2));
        correctPath.add(new Vector2D(2, 2));
        correctPath.add(new Vector2D(3, 2));
        correctPath.add(new Vector2D(3, 1));
        correctPath.add(new Vector2D(4, 1));
        correctPath.add(new Vector2D(5, 1));
        correctPath.add(new Vector2D(6, 1));
        correctPath.add(new Vector2D(7, 1));

        LinkedListNode<Vector2D> currentNode = head;

        for (Vector2D path : correctPath) {
            if(currentNode == null) {break;}
            assertEquals(path, currentNode.getValue());
            currentNode = currentNode.getNext();
        }

    }

    @Test
    public void testEnemiesHaveDecisionTrees() {
        Agent[] enemies = {
                new Minotaur(1, 1),
                new Demon(1, 1),
                new Archer(1, 1),
                new Sorcerer(1, 1)
        };

        for (Agent enemy : enemies) {
            assertNotNull(enemy.getDecisionTree());
            assertNotNull(enemy.getDecisionTree().getTree());
        }
    }


    @Test
    public void testShootPlayer() {
        LinearGame game = new LinearGame();
        TopDownLevel level = new TopDownLevel(game, 5, 5, "level");
        Player p1 = game.getPlayer();
        Enemy e1 = new Archer(2, 2);
        e1.setDecisionTree(null);
        level.getDynamicObjects().addAll(Arrays.asList(p1, e1));
        game.loadLevel(level);
        p1.setLocation(1, 1);

        assertEquals(2, level.getDynamicObjects().size());
        e1.setVelocity(1, 1);
        Decision decision = new ShootPlayer(e1, "shoot Player", 1);

        decision.doAction(1.5, level);
        assertEquals(new Vector2D(0, 0), e1.getVelocity());
        assertEquals(new Vector2D(-0.7071067811865475, -0.7071067811865475), e1.getOrientation());
        assertEquals(3, level.getDynamicObjects().size());
        assertTrue(level.getDynamicObjects().get(2) instanceof EnemyArrowProjectile);
        assertEquals(10, level.getDynamicObjects().get(2).getVelocity().magnitude(), EPSILON);

        p1.setLocation(2, 3);
        e1.setVelocity(5, 2);
        decision.doAction(1.5, level);
        assertEquals(new Vector2D(0, 0), e1.getVelocity());
        assertEquals(new Vector2D(0, 1), e1.getOrientation());
        assertEquals(4, level.getDynamicObjects().size());
        assertTrue(level.getDynamicObjects().get(3) instanceof EnemyArrowProjectile);
        assertEquals(10, level.getDynamicObjects().get(3).getVelocity().magnitude(), EPSILON);
    }

    @Test
    public void testShootHomingProjectile() {
        LinearGame game = new LinearGame();
        TopDownLevel level = new TopDownLevel(game, 5, 5, "level");
        Player p1 = game.getPlayer();
        Enemy e1 = new Sorcerer(2, 2);
        e1.setDecisionTree(null);
        level.getDynamicObjects().addAll(Arrays.asList(p1, e1));
        game.loadLevel(level);
        p1.setLocation(1, 1);

        assertEquals(2, level.getDynamicObjects().size());
        e1.setVelocity(1, 1);
        Decision decision = new ShootHomingProjectile(e1, "shoot homing", 0.9);

        decision.doAction(1, level);
        assertEquals(new Vector2D(0, 0), e1.getVelocity());
        assertEquals(new Vector2D(-0.7071067811865475, -0.7071067811865475), e1.getOrientation());
        assertEquals(3, level.getDynamicObjects().size());
        assertTrue(level.getDynamicObjects().get(2) instanceof EnemyHomingProjectile);
        assertEquals(10, level.getDynamicObjects().get(2).getVelocity().magnitude(), EPSILON);

        p1.setLocation(2, 3);
        e1.setVelocity(5, 2);
        decision.doAction(1.5, level);
        assertEquals(new Vector2D(0, 0), e1.getVelocity());
        assertEquals(new Vector2D(0, 1), e1.getOrientation());
        assertEquals(4, level.getDynamicObjects().size());
        assertTrue(level.getDynamicObjects().get(3) instanceof EnemyHomingProjectile);
        assertEquals(10, level.getDynamicObjects().get(3).getVelocity().magnitude(), EPSILON);
    }

    @Test
    public void testNearPlayer() {
        LinearGame game = new LinearGame();
        TopDownLevel level = new TopDownLevel(game, 5, 5, "level");
        Player p1 = game.getPlayer();
        Enemy e1 = new Demon(1, 2);
        e1.setDecisionTree(null);
        level.getDynamicObjects().addAll(Arrays.asList(p1, e1));
        game.loadLevel(level);
        p1.setLocation(1, 1);

        Decision decision = new NearPlayer(e1, "near Player", 1);
        assertTrue(decision.decide(1, level));
        e1.setLocation(2, 2);
        assertFalse(decision.decide(1, level));
        e1.setLocation(1, 1);
        assertTrue(decision.decide(1, level));
        e1.setLocation(0.5, 0.5);
        assertTrue(decision.decide(1, level));
        e1.setLocation(-101, 1);
        assertFalse(decision.decide(1, level));

        p1.setLocation(5, 2);
        e1.setLocation(5, 2);
        decision = new NearPlayer(e1, "near Player", 0.5);
        assertTrue(decision.decide(1, level));
        e1.setLocation(5.5, 2);
        assertTrue(decision.decide(1, level));
        e1.setLocation(1, 1);
        assertFalse(decision.decide(1, level));
        e1.setLocation(0.5, 0.5);
        assertFalse(decision.decide(1, level));
        e1.setLocation(-101, 1);
        assertFalse(decision.decide(1, level));
    }

    @Test
    public void testLowHP() {
        LinearGame game = new LinearGame();
        TopDownLevel level = new TopDownLevel(game, 5, 5, "level");
        Enemy e1 = new Minotaur(1, 2);
        e1.setDecisionTree(null);
        level.getDynamicObjects().add(e1);
        game.loadLevel(level);

        Decision decision = new LowHP(e1, "lowHP", 2);
        assertFalse(decision.decide(1, level));
        e1.setHP(1);
        assertTrue(decision.decide(1, level));
        e1.setHP(0);
        assertTrue(decision.decide(1, level));
        e1.setHP(-1);
        assertTrue(decision.decide(1, level));
        e1.setHP(2);
        assertTrue(decision.decide(1, level));
        e1.setHP(3);
        assertFalse(decision.decide(1, level));

        e1 = new Minotaur(1, 2);
        decision = new LowHP(e1, "lowHP", 8);
        assertFalse(decision.decide(1, level));
        e1.setHP(1);
        assertTrue(decision.decide(1, level));
        e1.setHP(0);
        assertTrue(decision.decide(1, level));
        e1.setHP(-1);
        assertTrue(decision.decide(1, level));
        e1.setHP(2);
        assertTrue(decision.decide(1, level));
        e1.setHP(6);
        assertTrue(decision.decide(1, level));
        e1.setHP(8);
        assertTrue(decision.decide(1, level));
        e1.setHP(9);
        assertFalse(decision.decide(1, level));
    }

    @Test
    public void testHeal() {
        LinearGame game = new LinearGame();
        TopDownLevel level = new TopDownLevel(game, 5, 5, "level");
        Enemy e1 = new Demon(1, 2, 10, 10);
        e1.setDecisionTree(null);
        level.getDynamicObjects().add(e1);
        game.loadLevel(level);

        e1.setVelocity(1, 1);
        assertEquals(new Vector2D(1, 1), e1.getVelocity());
        Decision decision = new Heal(e1, "heal", 2, 1);
        e1.setHP(1);
        assertEquals(1, e1.getHP());
        decision.doAction(1.5, level);
        assertEquals(new Vector2D(0, 0), e1.getVelocity());
        assertEquals(3, e1.getHP());
        decision.doAction(1.5, level);
        assertEquals(5, e1.getHP());
        decision.doAction(1.5, level);
        assertEquals(7, e1.getHP());

        decision = new Heal(e1, "heal", 5, 1);
        e1.setHP(1);
        assertEquals(1, e1.getHP());
        decision.doAction(1.5, level);
        assertEquals(new Vector2D(0, 0), e1.getVelocity());
        assertEquals(6, e1.getHP());
        decision.doAction(1.5, level);
        assertEquals(10, e1.getHP());
    }

    @Test
    public void testSpike() {
        Spike spike = new Spike(1, 1);
        Player player = new Player(1, 1, 10);
        assertFalse(player.isDestroyed());
        spike.collideWithDynamicObject(player);
        assertTrue(player.isDestroyed());

        Demon demon = new Demon(1, 1);
        assertFalse(demon.isDestroyed());
        spike.collideWithDynamicObject(demon);
        assertFalse(demon.isDestroyed());
    }

    @Test
    public void testPotion() {
        Potion potion = new Potion(1, 1, 5);
        Player player = new Player(1, 1, 10);
        player.setHP(5);
        assertEquals(5, player.getHP(), EPSILON);
        assertFalse(potion.isDestroyed());
        potion.collideWithDynamicObject(player);
        assertEquals(10, player.getHP(), EPSILON);
        assertTrue(potion.isDestroyed());

        potion = new Potion(1, 1, -5);
        player.setHP(5);
        assertEquals(5, player.getHP(), EPSILON);
        assertFalse(potion.isDestroyed());
        potion.collideWithDynamicObject(player);
        assertEquals(0, player.getHP(), EPSILON);
        assertTrue(potion.isDestroyed());

        potion = new Potion(1, 1, 5);
        Demon demon = new Demon(1, 1);
        demon.setHP(5);
        assertEquals(5, demon.getHP(), EPSILON);
        assertFalse(potion.isDestroyed());
        potion.collideWithDynamicObject(demon);
        assertEquals(5, demon.getHP(), EPSILON);
        assertFalse(potion.isDestroyed());

        potion = new Potion(1, 1, -5);
        demon.setHP(5);
        assertEquals(5, demon.getHP(), EPSILON);
        assertFalse(potion.isDestroyed());
        potion.collideWithDynamicObject(demon);
        assertEquals(5, demon.getHP(), EPSILON);
        assertFalse(potion.isDestroyed());
    }

    @Test
    public void testReadStaticObject() {
        RoguelikeGame game = new RoguelikeGame();
        RoguelikeLevel level = new RoguelikeLevel(game, 100, 100, "test level");
        StaticGameObject object = LevelParser.readStaticObject(game, level,
                new ArrayList<>(Arrays.asList("StaticGameObject", "DirectionalWall", "17", "12")));
        assertNotNull(object);
        assertTrue(object instanceof DirectionalWall);
        assertEquals(new Vector2D(17, 12), object.getLocation());

        object = LevelParser.readStaticObject(game, level,
                new ArrayList<>(Arrays.asList("StaticGameObject", "Potion", "15", "2", "2")));
        assertNotNull(object);
        assertTrue(object instanceof Potion);
        assertEquals(new Vector2D(15, 2), object.getLocation());
        assertEquals(2, ((Potion) object).getHealAmount());

        object = LevelParser.readStaticObject(game, level,
                new ArrayList<>(Arrays.asList("StaticGameObject", "Spike", "8", "4")));
        assertNotNull(object);
        assertTrue(object instanceof Spike);
        assertEquals(new Vector2D(8, 4), object.getLocation());

        object = LevelParser.readStaticObject(game, level,
                new ArrayList<>(Arrays.asList("StaticGameObject", "Marker", "2", "5", "Door")));
        assertNotNull(object);
        assertTrue(object instanceof Marker);
        assertEquals(new Vector2D(2, 5), object.getLocation());
        assertEquals("Door", ((Marker) object).getMarkerID());
    }

    @Test
    public void testReadDynamicObject() {
        RoguelikeGame game = new RoguelikeGame();
        RoguelikeLevel level = new RoguelikeLevel(game, 100, 100, "test level");
        DynamicGameObject object = LevelParser.readDynamicObject(game, level,
                new ArrayList<>(Arrays.asList("DynamicGameObject", "Minotaur", "1", "2", "3", "4")));
        assertNotNull(object);
        assertTrue(object instanceof Minotaur);
        assertEquals(new Vector2D(1, 2), object.getLocation());
        assertEquals(3, object.getMaxHP());
        assertEquals(4, ((Enemy) object).getStrength());

        object = LevelParser.readDynamicObject(game, level,
                new ArrayList<>(Arrays.asList("DynamicGameObject", "Archer", "5", "6", "7", "8")));
        assertNotNull(object);
        assertTrue(object instanceof Archer);
        assertEquals(new Vector2D(5, 6), object.getLocation());
        assertEquals(7, object.getMaxHP());
        assertEquals(8, ((Enemy) object).getStrength());

        object = LevelParser.readDynamicObject(game, level,
                new ArrayList<>(Arrays.asList("DynamicGameObject", "Sorcerer", "9", "10", "11", "12")));
        assertNotNull(object);
        assertTrue(object instanceof Sorcerer);
        assertEquals(new Vector2D(9, 10), object.getLocation());
        assertEquals(11, object.getMaxHP());
        assertEquals(12, ((Enemy) object).getStrength());
    }

    @Test
    public void testParseRoguelikeLevel() {
        RoguelikeGame game = new RoguelikeGame();
        Level level = LevelParser.parseLevel(game, "testing/roguelike.csv");
        assertNotNull(level);
        assertTrue(level instanceof RoguelikeLevel);

        level = LevelParser.parseLevel(game, "testing/mario1.csv");
        assertNotNull(level);
        assertTrue(level instanceof MarioLevel);

        level = LevelParser.parseLevel(game, "testing/medium.csv");
        assertNotNull(level);
        assertTrue(level instanceof TopDownLevel);
    }

}
