package app.gameengine.model.ai.roguelike;

import app.gameengine.Level;
import app.gameengine.model.ai.Decision;
import app.gameengine.model.gameobjects.Agent;
import app.gameengine.model.physics.Vector2D;
import app.gameengine.utils.Timer;
import app.games.topdownobjects.EnemyArrowProjectile;

public class ShootPlayer extends Decision {
    private Timer timer;

    public ShootPlayer(Agent agent, String name, double timer) {
        super(agent, name);
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
            Vector2D direction = Vector2D.sub(level.getPlayer().getLocation(), getAgent().getLocation());
            direction.normalize();
            getAgent().setOrientation(direction.getX(), direction.getY());
            getAgent().fireProjectile(new EnemyArrowProjectile(getAgent().getLocation().getX(), getAgent().getLocation().getY()), 10, level);
        }
    }
}
