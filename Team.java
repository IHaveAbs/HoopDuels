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

    /**
     * Constructs the team based off the name
     * @param name the name
     */
    public Team(String name) {
        teamName = name;
        players = new ArrayList<Player>();
        startingFive = new ArrayList<Player>();
        score = 0;
        FGScores = 0;
        totalFGAttempted = 0;
    }
    /**
     * adds a player
     * @param player the player
     */
    public void addPlayer(Player player) {
        players.add(player);
    }
    /**
     *  This sets the starting lineup
     *
     * @param players selected players
     * @return whether the lineup was accepted
     */
    public boolean canSetStartingFive(ArrayList<Player> players) {
        if (players == null || players.size() != 5) {
            return false;
        }
        startingFive = new ArrayList<Player>(players);
        return true;
    }
    /**
     * 
     * @return the players
     */
    public ArrayList<Player> getPlayers() {
        return players;
    }
    /**
     * 
     * @return the starting five
     */
    public ArrayList<Player> getStartingFive() {
        return startingFive;
    }
    /**
     * adds the points
     * @param points points to be added
     */
    public void addScore(int points) {
        score += points;
    }
    /**
     * adds the shot
     * @param didMake whether the shot was made
     */
    public void addShot(boolean didMake) {
        totalFGAttempted++;
        if (didMake) {
            FGScores++;
        }
    }
    /**
     * 
     * @return the score
     */
    public int getScore() {
        return score;
    }
    /**
     * 
     * @return a random player
     */
    public Player getRandomPlayer() {
        ArrayList<Player> temp = startingFive;
        if (temp.size() == 0) {
            return null;
        }
        int index = (int)(Math.random() * temp.size());
        return temp.get(index);
    }
    /**
     * 
     * @param shotType the shot type
     * @return the best shooter by shot type
     */
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
    /**
     * gets the player by position
     * @param position the position
     * @return player
     */
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
    /**
     * finds a player by name
     * @param name name 
     * @return player
     */
    public Player findPlayer(String name) {
        for (Player p : players) {
            if (p.getName().equals(name)) {
                return p;
            }
        }
        return null;
    }
    /**
     * 
     * @return team name
     */
    public String getTeamName() {
        return teamName;
    }
    /**
     * 
     * @return field goal scores
     */
    public int getFGScores() {
        return FGScores;
    }
    /**
     * 
     * @return total field goals attempted
     */
    public int getTotalFGAttempted() {
        return totalFGAttempted;
    }
}