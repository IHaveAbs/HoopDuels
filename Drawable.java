import java.awt.Graphics;

/**
 * Interface for objects that can be drawn
 * allows us to use a for each loop to draw all the objects
 */
public interface Drawable {
    /**
     * Draws the object
     *
     * @param g this is the Graphics
     */
    void draw(Graphics g);
}

