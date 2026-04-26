package app.gameengine.model.ai.roguelike;

import app.gameengine.Level;
import app.gameengine.model.ai.Decision;
import app.gameengine.model.gameobjects.Agent;
import app.gameengine.utils.PathfindingUtils;

public class MoveTowardPlayer extends Decision {
    public MoveTowardPlayer(Agent agent, String name) {
        super(agent, name);
    }

    @Override
    public boolean decide(double time, Level level) {
        return false;
    }

    @Override
    public void doAction(double time, Level level) {
        if (getAgent().getPath() == null) {
            getAgent().setPath(PathfindingUtils.findPath(getAgent().getLocation(), level.getPlayer().getLocation()));
        }
        else {
            getAgent().followPath(time);
        }

    }
}
