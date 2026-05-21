import java.awt.Graphics;

/**
 * Objects that can be drawn on the HoopDuel court implement this interface.
 */
public interface Drawable {
    /**
     * Draws this object.
     *
     * @param g the Graphics object used for drawing
     */
    void draw(Graphics g);
}

