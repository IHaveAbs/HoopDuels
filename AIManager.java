import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 * This is the manager for the AI.
 * The AI works by having an arraylist of plays that it chooses from, but if its losing it will try to score more threes to win back more points.
 */
public class AIManager {

    private Team team;
    private Queue<Play> queue;
    private ArrayList<Play> playswhenlosing;

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
    public Player chooseShooter(Team t, Play play) {
        if (t == null || play == null) {
            return null;
        }
        Player shooter = t.getPlayerByPosition(play.getShooterPosition());
        if (shooter == null) {
            shooter = t.getBestShooter(play.getShot());
        }
        return shooter;
    }
    public Team getTeam() {
        return team;
    }
}