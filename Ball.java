import java.awt.Color;
import java.awt.Graphics;

/**
 * This is the ball class
 * The ball class represents the ball and has fields for position, holder, etc.
 */
public class Ball implements Drawable {
    private int x;
    private int y;
    private int targetX;
    private int targetY;
    private int diameter = 16;
    private int ballspeed = 8;
    private Player holder;
    private Player lastPasser;
    private boolean moving;


    public Ball(int x, int y) {
        this.x = x;
        this.y = y;

        targetX = x;
        targetY = y;
        
        this.holder = null;
        this.lastPasser = null;

        moving = false;
    }

    public void draw(Graphics g) {
        g.setColor(new Color(233, 117, 31));
        g.fillOval((int)x-diameter/2,(int)y-(diameter/2),diameter,diameter);
        g.setColor(Color.BLACK);
        g.drawOval((int)x-diameter/2,(int)y-diameter/2,diameter,diameter);
    }

    public void moveTowardTarget() {
        if (!moving && holder != null) {
            x = (int)holder.getX()+Player.size/2-2;
            y = (int)holder.getY()-Player.size/2+6;
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
            return Math.min(current + ballspeed, target);
        }
        if (current > target) {
            return Math.max(current - ballspeed, target);
        }
        return current;
    }

    public void setHolder(Player player) {
        holder = player;
        lastPasser = null;
        moving = false;
        if (player != null) {
            x = player.getX() + Player.size / 2 - 2;
            y = player.getY() - Player.size / 2 + 6;
            targetX = x;
            targetY = y;
        }
    }

    public void passTo(Player player) {
        if (holder != null && holder != player) {
            lastPasser = holder;
        }
        holder = player;
        if (player != null) {
            // Aim at the player's target instead of only the current location.
            // This makes passes look cleaner when the receiver is cutting or rolling.
            targetX = player.getTargetX() + Player.size / 2 - 2;
            targetY = player.getTargetY() - Player.size / 2 + 6;
            moving = true;
        }
    }

    public void shootToHoop(int x1, int y1) {
        holder = null;
        targetX = x1;
        targetY = y1;
        moving = true;
    }
    public void setTarget(int x, int y) {
        targetX = x;
        targetY = y;
        moving = true;
    }
    public boolean isAtTarget() {
        return (!moving) && (x == targetX) && (y == targetY);
    }
    public boolean isMoving() {
        return moving;
    }
    public Player getHolder() {
        return holder;
    }
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

