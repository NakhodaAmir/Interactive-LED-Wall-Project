package tests;

import app.gameengine.Level;
import app.gameengine.model.ai.Decision;
import app.gameengine.model.gameobjects.Agent;

public class TestDecision extends Decision {
    private boolean decision;
    private boolean testDecision;

    public TestDecision(Agent agent, String name, boolean decision) {
        super(agent, name);
        this.decision = decision;
        this.testDecision = false;
    }

    public boolean isUsed() {
        return testDecision;
    }

    @Override
    public boolean decide(double time, Level level) {
        return decision;
    }

    @Override
    public void doAction(double time, Level level) {
        testDecision = true;
    }
}
