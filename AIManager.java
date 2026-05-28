import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Represents the AI bot for our game.
 * 
 */
public class AIManager {

    private Team team;
    private Queue<Play> queue;
    private ArrayList<Play> playswhenlosing;
    /**
     * Constructs the AI for the game given the team and the plays it could make.
     * Creates a queue for the starting plays.
     * @param t the team
     * @param plays the plays that could be made
     */
    public AIManager(Team t, ArrayList<Play> plays) {
        team = t;
        playswhenlosing = new ArrayList<Play>(plays);
        queue = new LinkedList<Play>();
        
        queue.clear();
        if (playswhenlosing.size() >= 4) {
            queue.add(playswhenlosing.get(0));
            queue.add(playswhenlosing.get(2));
            queue.add(playswhenlosing.get(1));
            queue.add(playswhenlosing.get(3));
        } else {
            for (Play p : playswhenlosing) {
                queue.add(p);
            }
        }
    }
    /**
     * The AI works by having an queue of plays that it chooses from.
     * The AI would then start choosing plays randomly.
     * If its losing and there is 3 minutes left it will try to score more threes to win back more points.
     * @param ai_score ai's score
     * @param user_score user's score
     * @param timeleft time left in the game
     * @return the play
     */
    public Play choosePlay(int ai_score, int user_score, int timeleft) {
        if (!queue.isEmpty()) {
            return queue.poll();
        }
        if (playswhenlosing.size() == 0) {
           return null;
        }
        if (ai_score < user_score && timeleft < 180) {
            for (Play p : playswhenlosing) {
                if (p.getShot().equals("three") && Math.random() < 0.70) {
                    return p;
                }
            }
        }

        int randomplay = (int) (Math.random()*playswhenlosing.size());
        return playswhenlosing.get(randomplay);
    }
    /**
     * Chooses the shooter based off the play.
     * @param t the team
     * @param play the play
     * @return the shooter
     */
    public Player chooseShooter(Team t, Play play) {
        Player shooter = t.getPlayerByPosition(play.getShooterPosition());
        if (shooter == null) {
            shooter = t.getBestShooter(play.getShot());
        }
        return shooter;
    }
    /**
     * 
     * @return the team.
     */
    public Team getTeam() {
        return team;
    }
}