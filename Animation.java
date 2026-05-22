import java.util.ArrayList;

public class Animation {
    private ArrayList<Player> players;
    private Ball ball;

    public Animation(ArrayList<Player> players, Ball ball) {
        this.players = players;
        this.ball = ball;
    }

    public void tick() {
        for (Player p : players) {
            p.moveToTarget();
        }
        ball.moveTowardTarget();
    }

    public boolean allPlayersAtTarget() {
        for (Player p : players) {
            if (!p.isAtTarget()) {
                return false;
            }
        }
        return true;
    }

    public boolean animationFinished() {
        return allPlayersAtTarget() && !ball.isMoving();
    }
}
