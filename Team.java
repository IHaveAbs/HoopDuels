import java.util.ArrayList;

/**
 * This is the team class and stores data such as name, field goal scores and attempts
 */
public class Team {
    private String teamName;
    private ArrayList<Player> players;
    private ArrayList<Player> startingFive;
    private int score;
    private int FGScores;
    private int totalFGAttempted;

    public Team(String name) {
        teamName = name;
        players = new ArrayList<Player>();
        startingFive = new ArrayList<Player>();
        score = 0;
        FGScores = 0;
        totalFGAttempted = 0;
    }
    public void addPlayer(Player player) {
        players.add(player);
    }
    /**
     * Sets the starting lineup. Exactly five players are required.
     *
     * @param players selected players
     * @return true if the lineup was accepted, false otherwise
     */
    public boolean canSetStartingFive(ArrayList<Player> players) {
        if (players == null || players.size() != 5) {
            return false;
        }
        startingFive = new ArrayList<Player>(players);
        return true;
    }
    public ArrayList<Player> getPlayers() {
        return players;
    }
    public ArrayList<Player> getStartingFive() {
        return startingFive;
    }
    public void addScore(int points) {
        score += points;
    }
    public void addShot(boolean didMake) {
        totalFGAttempted++;
        if (didMake) {
            FGScores++;
        }
    }
    public int getScore() {
        return score;
    }
    public Player getRandomPlayer() {
        ArrayList<Player> temp = startingFive;
        if (temp.size() == 0) {
            return null;
        }
        int index = (int)(Math.random() * temp.size());
        return temp.get(index);
    }
    public Player getBestShooter(String shotType) {
        ArrayList<Player> temp = startingFive;
        if (temp.size() == 0) {
            return null;
        }
        Player best = temp.get(0);
        for (Player p : temp) {
            if (p.getShotChance(shotType) > best.getShotChance(shotType)) {
                best = p;
            }
        }
        return best;
    }

    public Player getPlayerByPosition(String position) {
        ArrayList<Player> temp = startingFive;
        for (Player p : temp) {
            if (p.getPosition().equals(position)) {
                return p;
            }
        }
        if (temp.size() > 0) {
            return temp.get(0);
        }
        return null;
    }

    public Player findPlayer(String name) {
        for (Player p : players) {
            if (p.getName().equals(name)) {
                return p;
            }
        }
        return null;
    }
    public String getTeamName() {
        return teamName;
    }
    public int getFGScores() {
        return FGScores;
    }
    public int getTotalFGAttempted() {
        return totalFGAttempted;
    }
}