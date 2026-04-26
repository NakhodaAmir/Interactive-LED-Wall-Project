package app.gameengine.model.ai.roguelike;

import app.gameengine.Level;
import app.gameengine.model.ai.Decision;
import app.gameengine.model.gameobjects.Agent;
import app.gameengine.utils.Timer;

public class Heal extends Decision {
    private int healAmount;
    private Timer timer;
    public Heal(Agent agent, String name, int healAmount, double timer) {
        super(agent, name);
        this.healAmount = healAmount;
        this.timer = new Timer(timer);
    }

    @Override
    public boolean decide(double time, Level level) {
        return false;
    }

    @Override
    public void doAction(double time, Level level) {
        getAgent().setVelocity(0, 0);
        if (timer.tick(time) > 0) {
            getAgent().setHP(getAgent().getHP() + healAmount);
        }
    }
}
