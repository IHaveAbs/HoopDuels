import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * Visual demo of every offensive play vs every defensive scheme at every stage.
 *
 * Self-contained — reads only {@link GameConfig} constants and replicates the
 * defensive coordinate math inline. The existing game (MainGUI, GameLoop,
 * GameWindow) is untouched.
 *
 * Run:
 *   javac *.java
 *   java PlayDemo
 *
 * 4 plays x 4 defenses x 3 stages = 48 views. Click Next/Prev to walk through them.
 * Each view shows: court, all 10 players at their initial spots, arrows to their
 * targets for that stage, the ball, and a coord legend.
 */
public class PlayDemo extends JFrame {

    // ===================== view model =====================

    static final class View {
        final String play;
        final String defense;
        final int stage;
        final int[][] offInit;   // [5][2]
        final int[][] offTarget; // [5][2]
        final int[][] defInit;   // [5][2]
        final int[][] defTarget; // [5][2]
        final int[]   ballInit;  // [2]
        final int[]   ballTarget;// [2]
        final String  ballHolder;
        View(String play, String defense, int stage,
             int[][] offInit, int[][] offTarget,
             int[][] defInit, int[][] defTarget,
             int[] ballInit, int[] ballTarget, String holder) {
            this.play = play; this.defense = defense; this.stage = stage;
            this.offInit = offInit; this.offTarget = offTarget;
            this.defInit = defInit; this.defTarget = defTarget;
            this.ballInit = ballInit; this.ballTarget = ballTarget;
            this.ballHolder = holder;
        }
    }

    private static final String[] POSITIONS = {"PG", "SG", "SF", "PF", "C"};

    private final ArrayList<View> views = new ArrayList<View>();
    private int idx = 0;
    private final JLabel header = new JLabel();
    private final CourtPanel court = new CourtPanel();
    private final CoordsPanel coords = new CoordsPanel();

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() { new PlayDemo(); }
        });
    }

    public PlayDemo() {
        super("HoopDuel — Play & Defense Demo");
        buildViews();

        setSize(GameConfig.WINDOW_W, GameConfig.WINDOW_H);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(null);

        header.setBounds(10, 10, 1080, 40);
        header.setFont(new Font("Arial", Font.BOLD, 20));
        add(header);

        court.setBounds(0, 55, 810, 615);
        add(court);

        coords.setBounds(810, 55, 280, 555);
        add(coords);

        JButton prev = new JButton("< Prev");
        prev.setBounds(820, 625, 120, 35);
        prev.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                idx = (idx - 1 + views.size()) % views.size();
                refresh();
            }
        });
        add(prev);

        JButton next = new JButton("Next >");
        next.setBounds(960, 625, 120, 35);
        next.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                idx = (idx + 1) % views.size();
                refresh();
            }
        });
        add(next);

        refresh();
        setVisible(true);
    }

    private void refresh() {
        View v = views.get(idx);
        header.setText(String.format("[%d/%d]   %s  vs  %s   —   Stage %d  (%s)",
            idx + 1, views.size(), v.play, v.defense, v.stage,
            stageLabel(v.play, v.stage)));
        court.repaint();
        coords.repaint();
    }

    // ===================== view construction =====================

    private void buildViews() {
        String[] plays    = { "Pick and Roll", "Isolation Drive", "Kick-Out Three", "Post Up" };
        String[] defenses = { "Man-to-Man", "Zone Defense (2-3)", "Protect Paint", "Guard Perimeter" };
        for (String play : plays) {
            for (String def : defenses) {
                int[][] prevOff = GameConfig.USER_BASE_POSITIONS;
                int[][] prevDef = GameConfig.AI_BASE_POSITIONS;
                int[]   prevBall = ballPos(holderFor(play, -1), prevOff);
                for (int stage = 0; stage < GameConfig.STAGES_PER_PLAY; stage++) {
                    int[][] offTarget = stagesFor(play)[stage];
                    int[][] defTarget = computeDefense(def, offTarget, stage, play);
                    String  holder    = holderFor(play, stage);
                    int[]   ballTarget = ballPos(holder, offTarget);
                    views.add(new View(play, def, stage,
                        prevOff, offTarget, prevDef, defTarget,
                        prevBall, ballTarget, holder));
                    prevOff  = offTarget;
                    prevDef  = defTarget;
                    prevBall = ballTarget;
                }
                // Stage 3: the shot. Players hold their stage-2 positions; the ball flies
                // from the shooter to the right hoop (offense always attacks right in the demo).
                String shooter = shooterFor(play);
                int[]  ballTarget = new int[] { GameConfig.RIGHT_HOOP_X, GameConfig.HOOP_Y };
                views.add(new View(play, def, 3,
                    prevOff, prevOff, prevDef, prevDef,
                    prevBall, ballTarget, shooter + " (shot)"));
            }
        }
    }

    private static int[][][] stagesFor(String play) {
        if (play.equals("Pick and Roll"))   return GameConfig.PICK_AND_ROLL_STAGES;
        if (play.equals("Isolation Drive")) return GameConfig.ISOLATION_STAGES;
        if (play.equals("Kick-Out Three"))  return GameConfig.KICK_OUT_STAGES;
        if (play.equals("Post Up"))         return GameConfig.POST_UP_STAGES;
        return GameConfig.PICK_AND_ROLL_STAGES;
    }

    private static String shotTypeFor(String play) {
        if (play.equals("Kick-Out Three")) return "three";
        return "rim";
    }

    /**
     * Mirrors PlayManager.moveBallForStage for static lookup. Stage -1 means
     * "before stage 0" — ball starts with the PG by default.
     */
    private static String holderFor(String play, int stage) {
        if (stage < 0) return "PG";
        if (play.equals("Pick and Roll"))   return stage < 2 ? "PG" : "C";
        if (play.equals("Isolation Drive")) return "SF";
        if (play.equals("Kick-Out Three"))  return stage < 2 ? "SF" : "SG";
        if (play.equals("Post Up"))         return stage == 0 ? "SG" : "C";
        return "PG";
    }

    /** Position of the player who actually takes the shot for a given play. */
    private static String shooterFor(String play) {
        if (play.equals("Pick and Roll"))   return "C";
        if (play.equals("Isolation Drive")) return "SF";
        if (play.equals("Kick-Out Three"))  return "SG";
        if (play.equals("Post Up"))         return "C";
        return "PG";
    }

    private static int[] ballPos(String holder, int[][] offTargets) {
        int i = indexOfPosition(holder);
        return new int[] { offTargets[i][0], offTargets[i][1] };
    }

    private static int indexOfPosition(String pos) {
        for (int i = 0; i < POSITIONS.length; i++) {
            if (POSITIONS[i].equals(pos)) return i;
        }
        return 0;
    }

    // ===================== defensive math (replicated from GameLoop) =====================

    private static int[][] computeDefense(String scheme, int[][] offTargets, int stage, String play) {
        if (scheme.equals("Man-to-Man"))         return manDefense(offTargets);
        if (scheme.equals("Zone Defense (2-3)")) return zoneDefense(stage, play);
        if (scheme.equals("Protect Paint"))      return paintDefense(offTargets);
        if (scheme.equals("Guard Perimeter"))    return perimeterDefense(offTargets);
        return manDefense(offTargets);
    }

    private static int[][] manDefense(int[][] off) {
        int[][] out = new int[5][2];
        for (int i = 0; i < 5; i++) {
            out[i][0] = clampX(off[i][0] + GameConfig.MAN_DEFENSE_GAP);
            out[i][1] = clampY(off[i][1]);
        }
        return out;
    }

    private static int[][] zoneDefense(int stage, String play) {
        boolean collapse = stage >= 1 && !shotTypeFor(play).equals("three");
        if (collapse) return copy(GameConfig.ZONE_COLLAPSED);
        if (stage >= 1 && play.equals("Kick-Out Three")) return copy(GameConfig.ZONE_KICKOUT_RECOVER);
        return copy(GameConfig.ZONE_STANDARD);
    }

    private static int[][] paintDefense(int[][] off) {
        int paintX = GameConfig.COURT_X + GameConfig.COURT_W - GameConfig.PAINT_HELP_INSET;
        int[][] out = new int[5][2];
        for (int i = 0; i < 5; i++) {
            int tx = off[i][0] + GameConfig.SCHEME_DEFENSE_GAP;
            int ty = off[i][1];
            tx = (tx + paintX) / 2;
            ty = (ty + GameConfig.HOOP_Y) / 2;
            out[i][0] = clampX(tx);
            out[i][1] = clampY(ty);
        }
        return out;
    }

    private static int[][] perimeterDefense(int[][] off) {
        int[][] out = new int[5][2];
        for (int i = 0; i < 5; i++) {
            int tx = off[i][0] + GameConfig.PERIMETER_GAP;
            int ty = off[i][1];
            if (ty < GameConfig.HOOP_Y)      ty -= GameConfig.PERIMETER_LIFT;
            else if (ty > GameConfig.HOOP_Y) ty += GameConfig.PERIMETER_LIFT;
            out[i][0] = clampX(tx);
            out[i][1] = clampY(ty);
        }
        return out;
    }

    private static int clampX(int x) {
        return Math.max(GameConfig.COURT_X + GameConfig.CLAMP_MARGIN,
               Math.min(GameConfig.COURT_X + GameConfig.COURT_W - GameConfig.CLAMP_MARGIN, x));
    }

    private static int clampY(int y) {
        return Math.max(GameConfig.COURT_Y + GameConfig.CLAMP_MARGIN,
               Math.min(GameConfig.COURT_Y + GameConfig.COURT_H - GameConfig.CLAMP_MARGIN, y));
    }

    private static int[][] copy(int[][] src) {
        int[][] out = new int[src.length][2];
        for (int i = 0; i < src.length; i++) {
            out[i][0] = src[i][0];
            out[i][1] = src[i][1];
        }
        return out;
    }

    private static String stageLabel(String play, int stage) {
        if (stage == 3) {
            return shooterFor(play) + " releases — ball flies to the rim";
        }
        if (play.equals("Pick and Roll")) {
            if (stage == 0) return "screener walks up";
            if (stage == 1) return "ball handler uses the screen";
            return "roller dives to the rim";
        }
        if (play.equals("Isolation Drive")) {
            if (stage == 0) return "teammates clear out";
            if (stage == 1) return "scorer attacks downhill";
            return "drive finishes at the rim";
        }
        if (play.equals("Kick-Out Three")) {
            if (stage == 0) return "floor is spaced";
            if (stage == 1) return "drive collapses the defense";
            return "kick-out pass to shooter";
        }
        if (play.equals("Post Up")) {
            if (stage == 0) return "center seals on the block";
            if (stage == 1) return "entry pass goes inside";
            return "post player turns to score";
        }
        return "stage " + stage;
    }

    // ===================== rendering =====================

    private static final int PLAYER_SIZE = 38;
    private static final Color OFF_FILL    = new Color(80, 140, 220);
    private static final Color OFF_OUTLINE = new Color(20,  60, 130);
    private static final Color DEF_FILL    = new Color(220, 100, 100);
    private static final Color DEF_OUTLINE = new Color(140, 30,  30);
    private static final Color BALL_FILL   = new Color(230, 115, 30);

    /** Court + players + arrows + ball. */
    private class CourtPanel extends JPanel {
        CourtPanel() { setBackground(new Color(230, 238, 240)); }
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            drawCourt(g2);
            // Offset everything by -55 (this panel starts at y=55 in the frame)
            g2.translate(0, -55);
            View v = views.get(idx);
            // Targets first (hollow), then arrows, then initial (filled) on top
            drawTargets(g2, v.offTarget, OFF_OUTLINE);
            drawTargets(g2, v.defTarget, DEF_OUTLINE);
            drawArrows(g2,  v.offInit, v.offTarget, OFF_OUTLINE);
            drawArrows(g2,  v.defInit, v.defTarget, DEF_OUTLINE);
            drawPlayers(g2, v.offInit, OFF_FILL, OFF_OUTLINE);
            drawPlayers(g2, v.defInit, DEF_FILL, DEF_OUTLINE);
            drawBall(g2, v.ballInit, v.ballTarget);
            g2.translate(0, 55);
        }
    }

    private static void drawCourt(Graphics2D g) {
        int cx = GameConfig.COURT_X, cy = GameConfig.COURT_Y - 55;
        int cw = GameConfig.COURT_W, ch = GameConfig.COURT_H;
        int hoopY = cy + ch / 2;

        g.setColor(new Color(210, 160, 90));
        g.fillRect(cx, cy, cw, ch);
        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(1.5f));
        g.drawRect(cx, cy, cw, ch);

        g.drawLine(cx + cw / 2, cy, cx + cw / 2, cy + ch);
        g.drawOval(cx + cw / 2 - 60, cy + ch / 2 - 60, 120, 120);

        int lbb = cx + 14, rbb = cx + cw - 14;
        g.setColor(Color.WHITE);
        g.fillRect(lbb - 3, hoopY - 36, 6, 72);
        g.fillRect(rbb - 3, hoopY - 36, 6, 72);
        g.setColor(Color.BLACK);
        g.drawRect(lbb - 3, hoopY - 36, 6, 72);
        g.drawRect(rbb - 3, hoopY - 36, 6, 72);
        g.setColor(Color.RED);
        g.drawOval(GameConfig.LEFT_HOOP_X - 12,  hoopY - 12, 24, 24);
        g.drawOval(GameConfig.RIGHT_HOOP_X - 12, hoopY - 12, 24, 24);
        g.setColor(Color.BLACK);
        g.drawLine(lbb, hoopY, GameConfig.LEFT_HOOP_X - 12, hoopY);
        g.drawLine(rbb, hoopY, GameConfig.RIGHT_HOOP_X + 12, hoopY);

        g.drawRect(cx, hoopY - 85, 150, 170);
        g.drawRect(cx + cw - 150, hoopY - 85, 150, 170);
        g.drawArc(cx + cw - 250, hoopY - 170, 230, 340, 90, 180);
        g.drawArc(cx + 20,       hoopY - 170, 230, 340, -90, 180);
    }

    private void drawTargets(Graphics2D g, int[][] pts, Color outline) {
        g.setStroke(new BasicStroke(1.8f));
        g.setColor(outline);
        int s = PLAYER_SIZE - 8;
        for (int i = 0; i < pts.length; i++) {
            g.drawOval(pts[i][0] - s / 2, pts[i][1] - s / 2, s, s);
        }
    }

    private void drawArrows(Graphics2D g, int[][] from, int[][] to, Color color) {
        g.setStroke(new BasicStroke(1.5f));
        g.setColor(color);
        for (int i = 0; i < from.length; i++) {
            if (from[i][0] == to[i][0] && from[i][1] == to[i][1]) continue;
            drawArrow(g, from[i][0], from[i][1], to[i][0], to[i][1]);
        }
    }

    private static void drawArrow(Graphics2D g, int x1, int y1, int x2, int y2) {
        g.drawLine(x1, y1, x2, y2);
        double angle = Math.atan2(y2 - y1, x2 - x1);
        int head = 9;
        int hx1 = x2 - (int) (head * Math.cos(angle - Math.PI / 7));
        int hy1 = y2 - (int) (head * Math.sin(angle - Math.PI / 7));
        int hx2 = x2 - (int) (head * Math.cos(angle + Math.PI / 7));
        int hy2 = y2 - (int) (head * Math.sin(angle + Math.PI / 7));
        g.fillPolygon(new int[]{x2, hx1, hx2}, new int[]{y2, hy1, hy2}, 3);
    }

    private void drawPlayers(Graphics2D g, int[][] pts, Color fill, Color outline) {
        int s = PLAYER_SIZE;
        g.setFont(new Font("Arial", Font.BOLD, 12));
        for (int i = 0; i < pts.length; i++) {
            g.setColor(fill);
            g.fillOval(pts[i][0] - s / 2, pts[i][1] - s / 2, s, s);
            g.setColor(outline);
            g.setStroke(new BasicStroke(1.5f));
            g.drawOval(pts[i][0] - s / 2, pts[i][1] - s / 2, s, s);
            g.setColor(Color.WHITE);
            int tw = g.getFontMetrics().stringWidth(POSITIONS[i]);
            g.drawString(POSITIONS[i], pts[i][0] - tw / 2, pts[i][1] + 4);
        }
    }

    private void drawBall(Graphics2D g, int[] init, int[] target) {
        if (init[0] != target[0] || init[1] != target[1]) {
            g.setColor(BALL_FILL.darker());
            g.setStroke(new BasicStroke(1.2f));
            drawArrow(g, init[0], init[1], target[0], target[1]);
        }
        int s = 14;
        g.setColor(BALL_FILL);
        g.fillOval(init[0] - s / 2, init[1] - s / 2, s, s);
        g.setColor(Color.BLACK);
        g.drawOval(init[0] - s / 2, init[1] - s / 2, s, s);
        // Outline of target ball position
        g.setColor(BALL_FILL.darker());
        g.drawOval(target[0] - s / 2, target[1] - s / 2, s, s);
    }

    // ===================== coords legend panel =====================

    private class CoordsPanel extends JPanel {
        CoordsPanel() { setBackground(new Color(248, 248, 250)); }
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            View v = views.get(idx);

            int y = 22;
            g2.setColor(Color.BLACK);
            g2.setFont(new Font("Arial", Font.BOLD, 14));
            g2.drawString("Offense  (blue)", 14, y); y += 22;
            g2.setFont(new Font("Monospaced", Font.PLAIN, 12));
            for (int i = 0; i < 5; i++) {
                g2.drawString(String.format("  %-3s (%3d,%3d) -> (%3d,%3d)",
                    POSITIONS[i], v.offInit[i][0], v.offInit[i][1],
                    v.offTarget[i][0], v.offTarget[i][1]), 14, y);
                y += 18;
            }
            y += 8;

            g2.setFont(new Font("Arial", Font.BOLD, 14));
            g2.drawString("Defense  (red, " + v.defense + ")", 14, y); y += 22;
            g2.setFont(new Font("Monospaced", Font.PLAIN, 12));
            for (int i = 0; i < 5; i++) {
                g2.drawString(String.format("  %-3s (%3d,%3d) -> (%3d,%3d)",
                    POSITIONS[i], v.defInit[i][0], v.defInit[i][1],
                    v.defTarget[i][0], v.defTarget[i][1]), 14, y);
                y += 18;
            }
            y += 8;

            g2.setFont(new Font("Arial", Font.BOLD, 14));
            g2.drawString("Ball  (holder: " + v.ballHolder + ")", 14, y); y += 22;
            g2.setFont(new Font("Monospaced", Font.PLAIN, 12));
            g2.drawString(String.format("      (%3d,%3d) -> (%3d,%3d)",
                v.ballInit[0], v.ballInit[1], v.ballTarget[0], v.ballTarget[1]), 14, y);
            y += 28;

            g2.setFont(new Font("Arial", Font.PLAIN, 11));
            g2.setColor(new Color(90, 90, 90));
            g2.drawString("Filled circle = initial position", 14, y); y += 16;
            g2.drawString("Hollow circle = target position",  14, y); y += 16;
            g2.drawString("Arrow         = movement",         14, y); y += 16;
            g2.drawString("Offense always attacks the RIGHT hoop.", 14, y);
        }
    }
}
