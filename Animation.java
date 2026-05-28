import java.util.ArrayList;

/**
 * Represents the animation and movement of the players and the ball. Updates using the game tick/frame system
 */
public class Animation {
    private ArrayList<Player> players;
    private Ball ball;

    /**
     * Constructs animation given an array of players and the ball.
     * @param p the players
     * @param b the ball
     */
    public Animation(ArrayList<Player> p, Ball b) {
        players = p;
        ball = b;
    }
    /**
     * Moves the players once towards the target position. 
     * This should happen everytime there is a new frame
     */
    public void moveOnce() {
        for (Player p : players) {
            p.moveToTarget();
        }
        ball.moveTowardTarget();
    }
}
