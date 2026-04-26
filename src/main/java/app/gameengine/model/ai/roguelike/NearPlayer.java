package app.gameengine.model.ai.roguelike;

import app.gameengine.Level;
import app.gameengine.model.ai.Decision;
import app.gameengine.model.gameobjects.Agent;
import app.gameengine.model.physics.Vector2D;

public class NearPlayer extends Decision {
    private double distanceThreshold;
    public NearPlayer(Agent agent, String name, double distanceThreshold) {
        super(agent, name);
        this.distanceThreshold = distanceThreshold;
    }

    @Override
    public boolean decide(double time, Level level) {
        return Vector2D.euclideanDistance(getAgent().getLocation(), level.getPlayer().getLocation()) <= distanceThreshold;
    }

    @Override
    public void doAction(double time, Level level) { }
}
