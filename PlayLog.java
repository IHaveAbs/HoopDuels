import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

/**
 * This is the playlog class, draws the play log and logs events that happen.
 */
public class PlayLog implements Drawable {
    private ArrayList<String> logs;
    public PlayLog() {
        logs = new ArrayList<String>();
    }
    public void addMessage(String message) {
        logs.add(message);
    }
    public String getRecentMessage() {
        if (logs.size() == 0) {
            return "";
        }
        return logs.get(logs.size() - 1);
    }
    public ArrayList<String> getlogs() {
        return logs;
    }
    public void draw(Graphics g) {
        int x = 813;
        int y = 283;
        int w = 252;
        int h = 252;
        g.setColor(new Color(255, 255, 255));
        g.fillRoundRect(x, y, w, h, 14, 14);
        g.setColor(Color.BLACK);
        g.drawRoundRect(x, y, w, h, 14, 14);
        g.drawString("Play Log", x + 12, y + 24);

        int start = Math.max(0, logs.size() - 9);
        int currline = y + 48;
        for (int i = start; i < logs.size(); i++) {
            String msg = logs.get(i);
            if (msg.length() > 32) {
                msg = msg.substring(0, 29) + "...";
            }
            g.drawString("- " + msg, x+12, currline);
            currline += 21;
        }
    }
}
