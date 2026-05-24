import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Controls the AI team's possessions.
 * The AI first uses a Queue of planned plays, then falls back to weighted random plays.
 */
public class AIManager {
    private Team aiTeam;
    private Queue<Play> playQueue;
    private ArrayList<Play> fallbackPlays;

    public AIManager(Team aiTeam, ArrayList<Play> availablePlays) {
        this.aiTeam = aiTeam;
        this.fallbackPlays = new ArrayList<Play>(availablePlays);
        this.playQueue = new LinkedList<Play>();
        resetQueue();
    }

    public Play choosePlay(int aiScore, int userScore, int timeLeft) {
        if (!playQueue.isEmpty()) {
            return playQueue.poll();
        }

        if (fallbackPlays.size() == 0) {
            return null;
        }

        // Simple weighted idea: if AI is losing late, it likes threes more.
        if (aiScore < userScore && timeLeft < 180) {
            for (Play p : fallbackPlays) {
                if (p.getShotType().equals("three") && Math.random() < 0.55) {
                    return p;
                }
            }
        }

        int index = (int) (Math.random() * fallbackPlays.size());
        return fallbackPlays.get(index);
    }

    public Player chooseShooter(Team aiTeam, Play play) {
        if (aiTeam == null || play == null) {
            return null;
        }
        Player shooter = aiTeam.getPlayerByPosition(play.getShooterPosition());
        if (shooter == null) {
            shooter = aiTeam.getBestShooter(play.getShotType());
        }
        return shooter;
    }

    public void runAIPossession(Team aiTeam, String defenseChoice) {
        // GamePanel handles the visual possession. This method is kept for the spec.
        this.aiTeam = aiTeam;
    }

    public void resetQueue() {
        playQueue.clear();
        if (fallbackPlays.size() >= 4) {
            playQueue.add(fallbackPlays.get(0));
            playQueue.add(fallbackPlays.get(2));
            playQueue.add(fallbackPlays.get(1));
            playQueue.add(fallbackPlays.get(3));
        } else {
            for (Play p : fallbackPlays) {
                playQueue.add(p);
            }
        }
    }

    public Team getAiTeam() {
        return aiTeam;
    }
}