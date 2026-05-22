import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.HashMap;

/**
 * Represents one basketball player in HoopDuel.
 * Stores court position, target position, shooting stats, defense rating,
 * and in-game box score statistics.
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

    public static final int SIZE = 46;
    public static final int NORMAL_SPEED = 4;
    public static final int SLOW_SPEED = 2;
    public static final int SPRINT_SPEED = 8;

    /**
     * Creates a player with basic stats.
     */
    public Player(String name, String teamName, String position,
                  int rimFG, int midFG, int threeFG, int defenseRating, Color color) {
        this.name = name;
        this.teamName = teamName;
        this.position = position;
        this.x = 0;
        this.y = 0;
        this.targetX = 0;
        this.targetY = 0;
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
        this.moveSpeed = NORMAL_SPEED;
    }

    /**
     * Copy constructor. Used so each new game has fresh player stats.
     */
    public Player(Player other) {
        this.name = other.name;
        this.teamName = other.teamName;
        this.position = other.position;
        this.x = other.x;
        this.y = other.y;
        this.targetX = other.targetX;
        this.targetY = other.targetY;
        this.shootingStats = new HashMap<String, Integer>(other.shootingStats);
        this.defenseRating = other.defenseRating;
        this.points = 0;
        this.rebounds = 0;
        this.assists = 0;
        this.fieldGoalsMade = 0;
        this.fieldGoalsAttempted = 0;
        this.color = other.color;
        this.moveSpeed = NORMAL_SPEED;
    }

    public void draw(Graphics g) {
        g.setColor(color);
        g.fillOval(x - SIZE / 2, y - SIZE / 2, SIZE, SIZE);

        g.setColor(Color.BLACK);
        g.drawOval(x - SIZE / 2, y - SIZE / 2, SIZE, SIZE);

        String label = getInitials();
        FontMetrics fm = g.getFontMetrics();
        int textWidth = fm.stringWidth(label);
        int textHeight = fm.getAscent();
        g.drawString(label, x - textWidth / 2, y + textHeight / 3);
    }

    /**
     * Moves the player a small step toward the target location.
     */
    public void moveToTarget() {
        x = moveOneAxis(x, targetX);
        y = moveOneAxis(y, targetY);
    }

    private int moveOneAxis(int current, int target) {
        if (current < target) {
            return Math.min(current + moveSpeed, target);
        }
        if (current > target) {
            return Math.max(current - moveSpeed, target);
        }
        return current;
    }

    public boolean isAtTarget() {
        return x == targetX && y == targetY;
    }

    public void setTarget(int x, int y) {
        this.targetX = x;
        this.targetY = y;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
        this.targetX = x;
        this.targetY = y;
    }

    public void setMoveSpeed(int moveSpeed) {
        if (moveSpeed <= 0) {
            this.moveSpeed = NORMAL_SPEED;
        } else {
            this.moveSpeed = moveSpeed;
        }
    }

    public void resetMoveSpeed() {
        moveSpeed = NORMAL_SPEED;
    }

    public void addPoints(int points) {
        if (points > 0) {
            this.points += points;
        }
    }

    public void addAssist() {
        assists++;
    }

    public void addRebound() {
        rebounds++;
    }

    public void recordShot(boolean made) {
        fieldGoalsAttempted++;
        if (made) {
            fieldGoalsMade++;
        }
    }

    public void resetStats() {
        points = 0;
        rebounds = 0;
        assists = 0;
        fieldGoalsMade = 0;
        fieldGoalsAttempted = 0;
    }

    public int getShotChance(String shotType) {
        Integer chance = shootingStats.get(shotType);
        if (chance == null) {
            return 50;
        }
        return chance;
    }

    public double distanceFrom(int otherX, int otherY) {
        int dx = x - otherX;
        int dy = y - otherY;
        return Math.sqrt(dx * dx + dy * dy);
    }

    private String getInitials() {
        String[] parts = name.split(" ");
        String initials = "";
        for (int i = 0; i < parts.length && i < 2; i++) {
            if (parts[i].length() > 0) {
                initials += parts[i].substring(0, 1).toUpperCase();
            }
        }
        return initials;
    }

    public String getName() {
        return name;
    }

    public String getTeamName() {
        return teamName;
    }

    public String getPosition() {
        return position;
    }

    public int getDefenseRating() {
        return defenseRating;
    }

    public int getPoints() {
        return points;
    }

    public int getRebounds() {
        return rebounds;
    }

    public int getAssists() {
        return assists;
    }

    public int getFieldGoalsMade() {
        return fieldGoalsMade;
    }

    public int getFieldGoalsAttempted() {
        return fieldGoalsAttempted;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getTargetX() {
        return targetX;
    }

    public int getTargetY() {
        return targetY;
    }

    public Color getColor() {
        return color;
    }
}