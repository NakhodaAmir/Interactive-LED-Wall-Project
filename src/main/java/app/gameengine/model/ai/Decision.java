package app.gameengine.model.ai;

import app.gameengine.Level;
import app.gameengine.model.gameobjects.Agent;

public abstract class Decision {
    private Agent agent;
    private String name;

    public Decision(Agent agent, String name) {
        this.agent = agent;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Agent getAgent() {
        return agent;
    }

    public abstract boolean decide(double time, Level level);
    public abstract void doAction(double time, Level level);

}
