package app.gameengine.model.ai.roguelike;

import app.gameengine.Level;
import app.gameengine.model.ai.Decision;
import app.gameengine.model.gameobjects.Agent;

public class LowHP extends Decision {
    private int healthThreshold;
    public LowHP(Agent agent, String name, int healthThreshold) {
        super(agent, name);
        this.healthThreshold = healthThreshold;
    }

    @Override
    public boolean decide(double time, Level level) {
        return getAgent().getHP() <= healthThreshold;
    }

    @Override
    public void doAction(double time, Level level) { }
}
