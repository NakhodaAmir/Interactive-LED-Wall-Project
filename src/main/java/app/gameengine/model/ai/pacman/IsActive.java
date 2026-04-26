package app.gameengine.model.ai.pacman;

import app.gameengine.Level;
import app.gameengine.model.ai.Decision;
import app.gameengine.model.gameobjects.Agent;
import app.games.pacman.Ghost;

public class IsActive extends Decision {
    private Ghost ghost;

    public IsActive(Ghost ghost, String name) {
        super(ghost, name);
        this.ghost = ghost;
    }

    @Override
    public boolean decide(double time, Level level) {
        String state = ghost.getState();
        return state.equals("Chase") || state.equals("Scatter");
    }

    @Override
    public void doAction(double time, Level level) {

    }
}
