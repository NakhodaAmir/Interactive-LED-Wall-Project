package app.gameengine.model.ai.pacman;

import app.gameengine.Level;
import app.gameengine.model.ai.Decision;
import app.gameengine.model.physics.Vector2D;
import app.gameengine.utils.PacmanUtils;
import app.games.pacman.Ghost;
import app.games.pacman.PacmanGame;

public class Flee extends Decision {
    private Ghost ghost;
    private PacmanGame pacmanGame;
    private Vector2D lastLocation;

    public Flee(Ghost ghost, PacmanGame pacmanGame, String name) {
        super(ghost, name);
        this.ghost = ghost;
        this.pacmanGame = pacmanGame;
        this.lastLocation = null;
    }

    @Override
    public boolean decide(double time, Level level) {
        return ghost.getState().equals("Frightened");
    }

    @Override
    public void doAction(double time, Level level) {
        if (PacmanUtils.canAct(ghost, time, lastLocation)) {
            Vector2D randDir = PacmanUtils.getRandomDirection(PacmanUtils.getValidDirs(pacmanGame.getCurrentLevel(), ghost));
            if (randDir != null) {
                ghost.setOrientation(randDir.getX(), randDir.getY());
            }
            lastLocation = Vector2D.round(ghost.getLocation());
        }
        ghost.followPath(time);
    }
}
