import java.util.ArrayList;

public class Animation {
    private ArrayList<Player> players;
    private Ball ball;

    public Animation(ArrayList<Player> p, Ball b) {
        players = p;
        ball = b;
    }

    public void moveOnce() {
        for (Player p : players) {
            p.moveToTarget();
        }
        ball.moveTowardTarget();
    }

    public boolean isAllPlayersAtTarget() {
        for (Player p : players) {
            if (!p.isAtTarget()) {
                return false;
            }
        }
        return true;
    }
    public boolean isAnimationFinished() {
        return isAllPlayersAtTarget() && !ball.isMoving();
    }
}
