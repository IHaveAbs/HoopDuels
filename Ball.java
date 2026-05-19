import java.awt.Color;
import java.awt.Graphics;

/**
 * Represents the basketball. It can follow a player, move during passes,
 * or move toward the hoop during shot animations.
 */
public class Ball implements Drawable {
    private int x;
    private int y;
    private int targetX;
    private int targetY;
    private Player currentHolder;
    private Player lastPasser;
    private boolean moving;

    private static final int SIZE = 16;
    private static final int SPEED = 8;

    public Ball(int x, int y) {
        this.x = x;
        this.y = y;
        this.targetX = x;
        this.targetY = y;
        this.currentHolder = null;
        this.lastPasser = null;
        this.moving = false;
    }

    public void draw(Graphics g) {
        g.setColor(new Color(230, 115, 30));
        g.fillOval(x - SIZE / 2, y - SIZE / 2, SIZE, SIZE);
        g.setColor(Color.BLACK);
        g.drawOval(x - SIZE / 2, y - SIZE / 2, SIZE, SIZE);
    }

    public void moveTowardTarget() {
        if (!moving && currentHolder != null) {
            x = currentHolder.getX() + Player.SIZE / 2 - 2;
            y = currentHolder.getY() - Player.SIZE / 2 + 6;
            return;
        }

        x = moveOneAxis(x, targetX);
        y = moveOneAxis(y, targetY);
        if (x == targetX && y == targetY) {
            moving = false;
        }
    }

    private int moveOneAxis(int current, int target) {
        if (current < target) {
            return Math.min(current + SPEED, target);
        }
        if (current > target) {
            return Math.max(current - SPEED, target);
        }
        return current;
    }

    public void setHolder(Player player) {
        currentHolder = player;
        lastPasser = null;
        moving = false;
        if (player != null) {
            x = player.getX() + Player.SIZE / 2 - 2;
            y = player.getY() - Player.SIZE / 2 + 6;
            targetX = x;
            targetY = y;
        }
    }

    public void passTo(Player player) {
        if (currentHolder != null && currentHolder != player) {
            lastPasser = currentHolder;
        }
        currentHolder = player;
        if (player != null) {
            // Aim at the player's target instead of only the current location.
            // This makes passes look cleaner when the receiver is cutting or rolling.
            targetX = player.getTargetX() + Player.SIZE / 2 - 2;
            targetY = player.getTargetY() - Player.SIZE / 2 + 6;
            moving = true;
        }
    }

    public void shootToHoop(int hoopX, int hoopY) {
        currentHolder = null;
        targetX = hoopX;
        targetY = hoopY;
        moving = true;
    }

    public void setTarget(int x, int y) {
        targetX = x;
        targetY = y;
        moving = true;
    }

    public boolean isAtTarget() {
        return !moving && x == targetX && y == targetY;
    }

    public boolean isMoving() {
        return moving;
    }

    public Player getCurrentHolder() {
        return currentHolder;
    }

    /**
     * Returns the last player who made a pass before the current shot.
     * This is used so assists go to the actual passer instead of always
     * going to the point guard.
     */
    public Player getLastPasser() {
        return lastPasser;
    }

    public void clearLastPasser() {
        lastPasser = null;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}

