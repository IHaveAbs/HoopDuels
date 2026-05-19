import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

/**
 * Stores and draws play-by-play messages.
 */
public class PlayLog implements Drawable {
    private ArrayList<String> messages;

    public PlayLog() {
        messages = new ArrayList<String>();
    }

    public void addMessage(String message) {
        messages.add(message);
    }

    public String getRecentMessage() {
        if (messages.size() == 0) {
            return "";
        }
        return messages.get(messages.size() - 1);
    }

    public ArrayList<String> getMessages() {
        return messages;
    }

    public void draw(Graphics g) {
        int x = 810;
        int y = 285;
        int w = 250;
        int h = 250;
        g.setColor(new Color(245, 245, 245));
        g.fillRoundRect(x, y, w, h, 14, 14);
        g.setColor(Color.BLACK);
        g.drawRoundRect(x, y, w, h, 14, 14);
        g.drawString("Play Log", x + 12, y + 24);

        int start = Math.max(0, messages.size() - 9);
        int lineY = y + 48;
        for (int i = start; i < messages.size(); i++) {
            String msg = messages.get(i);
            if (msg.length() > 32) {
                msg = msg.substring(0, 29) + "...";
            }
            g.drawString("- " + msg, x + 12, lineY);
            lineY += 21;
        }
    }

    public void clear() {
        messages.clear();
    }
}
