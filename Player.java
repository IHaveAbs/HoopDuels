import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.HashMap;

/**
 * Represents the individual players in HoopDuel.
 * Stores court position, target position, shooting stats, defense rating,
 * and other statistics.
 */
public class Player implements Drawable {
    private String name;
    private String teamName;
    private String position;
    private int x;
    private int y;
    private int targetX;
    private int targetY;
    private HashMap<String, Integer> shootingStats;
    private int defenseRating;
    private int points;
    private int rebounds;
    private int assists;
    private int fieldGoalsMade;
    private int fieldGoalsAttempted;
    private Color color;
    private int moveSpeed;
    public static int size = 46;
    public static int normalSpeed = 4;
    public static int slowSpeed = 2;
    public static int fastSpeed = 8;

    /**
     * Constructs the Player using basic stats like name, team name, position, rim shooting percentage chance, mid shooting percentage chance
     * three shooting percentage chance, defense rating, and the color based off the team.
     * 
     * 
     * @param name
     * @param teamName
     * @param position
     * @param rimFG
     * @param midFG
     * @param threeFG
     * @param defenseRating
     * @param color
     */
    public Player(String name, String teamName, String position,
                  int rimFG, int midFG, int threeFG, int defenseRating, Color color) {
        this.name = name;
        this.teamName = teamName;
        this.position = position;
        x = 0;
        y = 0;
        targetX = 0;
        targetY = 0;
        this.shootingStats = new HashMap<String, Integer>();
        shootingStats.put("rim", rimFG);
        shootingStats.put("mid", midFG);
        shootingStats.put("three", threeFG);
        this.defenseRating = defenseRating;
        this.points = 0;
        this.rebounds = 0;
        this.assists = 0;
        this.fieldGoalsMade = 0;
        this.fieldGoalsAttempted = 0;
        this.color = color;
        this.moveSpeed = normalSpeed;
    }
    /**
     * Draws the player as a circle
     * 
     * @param g Swing graphics
     */
    public void draw(Graphics g) {
        g.setColor(color);
        g.fillOval(x-size/2, y-size/2, size, size);

        g.setColor(Color.BLACK);
        g.drawOval(x-size/2,y-size/2, size, size);

        String label = getInitials();
        FontMetrics temp = g.getFontMetrics();
        g.drawString(label, x-temp.stringWidth(label)/2, y+temp.getAscent()/3);
    }

    /**
     * Moves the player one step towards the target
     */
    public void moveToTarget() {
        x = moveOneAxis(x, targetX);
        y = moveOneAxis(y, targetY);
    }
    /**
     * moves the player one step for each axis
     * @param current current postion
     * @param target target position
     * @return the position
     */
    private int moveOneAxis(int current, int target) {
        if (current < target) {
            return Math.min(current + moveSpeed, target);
        }
        if (current > target) {
            return Math.max(current - moveSpeed, target);
        }
        return current;
    }
    /**
     * 
     * @return if the player is at the target
     */
    public boolean isAtTarget() {
        return (x == targetX && y == targetY);
    }
    /**
     * sets the target position
     * @param x
     * @param y
     */
    public void setTarget(int x, int y) {
        targetX = x;
        targetY = y;
    }
    /**
     * sets the current position
     * @param x
     * @param y
     */
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
        targetX = x;
        targetY = y;
    }
    /**
     * sets the movespeed based off play
     * @param moveSpeed
     */
    public void setMoveSpeed(int moveSpeed) {
        if (moveSpeed <= 0) {
            this.moveSpeed = normalSpeed;
        } else {
            this.moveSpeed = moveSpeed;
        }
    }
    /**
     * Resets the movespeed to default
     */
    public void resetMoveSpeed() {
        moveSpeed = normalSpeed;
    }
    /**
     * adds points to player who shoots
     * @param points points to be added
     */
    public void addPoints(int points) {
        if (points > 0) {
            this.points += points;
        }
    }
    /**
     * add assist to player
     */
    public void addAssist() {
        assists++;
    }
    /**
     * add rebound to player
     */
    public void addRebound() {
        rebounds++;
    }
    /**
     * Records a shot
     * Increases attempts
     * Increases made if the shot was made
     * @param made whether the shot was made
     */
    public void recordShot(boolean made) {
        fieldGoalsAttempted++;
        if (made) {
            fieldGoalsMade++;
        }
    }
    /**
     * Returns the chance of the shot
     * @param shotType the type of shot
     * @return the shot chance
     */
    public int getShotChance(String shotType) {
        return shootingStats.get(shotType);
    }
    private String getInitials() {
        String[] temp = name.split(" ");
        String initials = "";
        for (int i = 0; i < temp.length && i < 2; i++) {
            if (temp[i].length() > 0) {
                String temp1 = temp[i].substring(0, 1).toUpperCase();
                initials += temp1;
            }
        }
        return initials;
    }
    /**
     * 
     * @return name of player
     */
    public String getName() {
        return name;
    }
    /**
     * 
     * @return name of team
     */
    public String getTeamName() {
        return teamName;
    }
    /**
     * 
     * @return name of position
     */
    public String getPosition() {
        return position;
    }
    /**
     * 
     * @return the points of the player
     */
    public int getPoints() {
        return points;
    }
    /**
     * 
     * @return how many rebounds the player has
     */
    public int getRebounds() {
        return rebounds;
    }
    /**
     * 
     * @return amount of assists the player has
     */
    public int getAssists() {
        return assists;
    }
    /**
     * 
     * @return field goals made by player
     */
    public int getFieldGoalsMade() {
        return fieldGoalsMade;
    }
    /**
     * 
     * @return field goals attempted by player
     */
    public int getFieldGoalsAttempted() {
        return fieldGoalsAttempted;
    }
    /**
     * 
     * @return x position of player
     */
    public int getX() {
        return x;
    }
    /**
     * 
     * @return y position of player
     */
    public int getY() {
        return y;
    }
    /**
     * 
     * @return target position of player (X)
     */
    public int getTargetX() {
        return targetX;
    }
    /**
     * 
     * @return target position of player (Y)
     */
    public int getTargetY() {
        return targetY;
    }
}