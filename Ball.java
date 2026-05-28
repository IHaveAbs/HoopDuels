import java.awt.Color;
import java.awt.Graphics;

/**
 * Represents the ball for our game.
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

    /**
     * Constructs the ball with the position
     * @param x x position of ball
     * @param y y position of the ball
     */
    public Ball(int x, int y) {
        this.x = x;
        this.y = y;

        targetX = x;
        targetY = y;
        
        this.holder = null;
        this.lastPasser = null;

        moving = false;
    }
    /**
     * Draws the ball using g graphics.
     */
    public void draw(Graphics g) {
        g.setColor(new Color(233, 117, 31));
        g.fillOval((int)x-diameter/2,(int)y-(diameter/2),diameter,diameter);
        g.setColor(Color.BLACK);
        g.drawOval((int)x-diameter/2,(int)y-diameter/2,diameter,diameter);
    }
    /**
     * Moves the ball one step toward the target
     */
    public void moveTowardTarget() {
        //If there is holder and ball not moving then put ball on top of player.
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
    /**
     * Moves ball towards target based on ballspeed. 
     * @param current current pos
     * @param target target pos
     * @return the position
     */
    private int moveOneAxis(int current, int target) {
        if (current < target) {
            return Math.min(current + ballspeed, target);
        }
        if (current > target) {
            return Math.max(current - ballspeed, target);
        }
        return current;
    }
    /**
     * Set the holder of the ball
     * @param player player that you want to hold the ball
     */
    public void setHolder(Player player) {
        holder = player;
        lastPasser = null;
        moving = false;
        x = player.getX()+Player.size/2-2;
        y = player.getY()-Player.size/2+6;
        targetX = x;
        targetY = y;
    }
    /**
     * Passes the ball to the player
     * @param player player that will receive the ball
     */
    public void passTo(Player player) {
        if (holder != null && holder != player) {
            lastPasser = holder;
        }
        holder = player;
        targetX = player.getX()+Player.size/2-2;
        targetY = player.getY()-Player.size/2+6;
        moving = true;
    }
    /**
     * shoots the ball to the hoop
     * @param x1
     * @param y1
     */
    public void shootToHoop(int x1, int y1) {
        holder = null;
        targetX = x1;
        targetY = y1;
        moving = true;
    }
    /**
     * sets the target of the ball
     * @param x
     * @param y
     */
    public void setTarget(int x, int y) {
        targetX = x;
        targetY = y;
        moving = true;
    }
    /**
     * 
     * @return if ball is moving
     */
    public boolean isMoving() {
        return moving;
    }
    /**
     * 
     * @return the holder of ball
     */
    public Player getHolder() {
        return holder;
    }
    /**
     * This is for assists
     * @return last person who passes the ball
     */
    public Player getLastPasser() {
        return lastPasser;
    }
    /**
     * clears the last passer
     */
    public void clearLastPasser() {
        lastPasser = null;
    }
    
}

