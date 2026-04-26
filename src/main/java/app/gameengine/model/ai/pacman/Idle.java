package app.gameengine.model.ai.pacman;

import app.gameengine.Level;
import app.gameengine.model.ai.Decision;
import app.gameengine.model.gameobjects.Agent;
import app.gameengine.model.physics.Vector2D;
import app.games.pacman.Ghost;

public class Idle extends Decision {
    private Ghost ghost;

    public Idle(Ghost ghost, String name) {
        super(ghost, name);
        this.ghost = ghost;
    }

    @Override
    public boolean decide(double time, Level level) {
        return false;
    }

    @Override
    public void doAction(double time, Level level) {
        if (ghost.getVelocity().magnitude() > 0) { return; }
        Vector2D orientation = ghost.getOrientation();
        ghost.setOrientation(-orientation.getX(), -orientation.getY());
        ghost.followPath(time);
    }
}
