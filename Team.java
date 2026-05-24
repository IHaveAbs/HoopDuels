import java.util.ArrayList;

/**
 * Represents one team. Stores roster, starting five, score, and team shooting stats.
 */
public class Team {
    private String teamName;
    private ArrayList<Player> roster;
    private ArrayList<Player> startingFive;
    private int score;
    private int totalFGMade;
    private int totalFGAttempted;

    public Team(String teamName) {
        this.teamName = teamName;
        roster = new ArrayList<Player>();
        startingFive = new ArrayList<Player>();
        score = 0;
        totalFGMade = 0;
        totalFGAttempted = 0;
    }

    /**
     * Deep copies another team so new games do not share player stat objects.
     */
    public Team(Team other) {
        this(other.teamName);
        for (Player p : other.roster) {
            roster.add(new Player(p));
        }
        for (int i = 0; i < other.startingFive.size(); i++) {
            Player original = other.startingFive.get(i);
            Player copy = findPlayerByName(original.getName());
            if (copy != null) {
                startingFive.add(copy);
            }
        }
    }

    public void addPlayer(Player player) {
        roster.add(player);
    }

    /**
     * Sets the starting lineup. Exactly five players are required.
     *
     * @param players selected players
     * @return true if the lineup was accepted, false otherwise
     */
    public boolean setStartingFive(ArrayList<Player> players) {
        if (players == null || players.size() != 5) {
            return false;
        }
        startingFive = new ArrayList<Player>(players);
        return true;
    }

    public ArrayList<Player> getRoster() {
        return roster;
    }

    public ArrayList<Player> getStartingFive() {
        return startingFive;
    }

    public void addScore(int points) {
        if (points > 0) {
            score += points;
        }
    }

    public void addShotAttempt(boolean made) {
        totalFGAttempted++;
        if (made) {
            totalFGMade++;
        }
    }

    public void resetGameStats() {
        score = 0;
        totalFGMade = 0;
        totalFGAttempted = 0;
        for (Player p : roster) {
            p.resetStats();
        }
    }

    public int getScore() {
        return score;
    }

    public double getFGPercentage() {
        if (totalFGAttempted == 0) {
            return 0.0;
        }
        return (double) totalFGMade / totalFGAttempted;
    }

    public Player getRandomPlayer() {
        ArrayList<Player> list = startingFive.size() == 5 ? startingFive : roster;
        if (list.size() == 0) {
            return null;
        }
        int index = (int) (Math.random() * list.size());
        return list.get(index);
    }

    public double getAverageRating() {
        if (startingFive.size() == 0) {
            return 0.0;
        }
        int total = 0;
        for (Player p : startingFive) {
            total += p.getDefenseRating();
        }
        return (double) total / startingFive.size();
    }

    public Player getBestShooter(String shotType) {
        ArrayList<Player> list = startingFive.size() == 5 ? startingFive : roster;
        if (list.size() == 0) {
            return null;
        }
        Player best = list.get(0);
        for (Player p : list) {
            if (p.getShotChance(shotType) > best.getShotChance(shotType)) {
                best = p;
            }
        }
        return best;
    }

    public Player getPlayerByPosition(String position) {
        ArrayList<Player> list = startingFive.size() == 5 ? startingFive : roster;
        for (Player p : list) {
            if (p.getPosition().equals(position)) {
                return p;
            }
        }
        if (list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    public Player findPlayerByName(String name) {
        for (Player p : roster) {
            if (p.getName().equals(name)) {
                return p;
            }
        }
        return null;
    }

    public String getTeamName() {
        return teamName;
    }

    public int getTotalFGMade() {
        return totalFGMade;
    }

    public int getTotalFGAttempted() {
        return totalFGAttempted;
    }
    public void resetForNewGame() {
        score = 0;
        totalFGMade = 0;
        totalFGAttempted = 0;
        for (Player p : roster){
            p.resetStats();
        }
        startingFive.clear();
    }
}