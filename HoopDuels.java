import java.awt.CardLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * This is the HoopDuel class
 * It manages the window frame and the classes connected to it.
 */
public class HoopDuels {
    private JFrame frame;
    private JPanel panels;
    private CardLayout cardLayout;
    private String username;

    public HoopDuels() {
        frame = new JFrame("HoopDuels");
        frame.setSize(1100, 720);

        cardLayout = new CardLayout();
        panels = new JPanel(cardLayout);
        frame.setContentPane(panels);

        showNamePanel();
        frame.setVisible(true);
    }

    public void showNamePanel() {
        panels.add(new NamePanel(this), "NamePanel");
        cardLayout.show(panels, "NamePanel");
    }

    public void showMenuPanel() {
        panels.add(new MenuPanel(this, username), "MenuPanel");
        cardLayout.show(panels, "MenuPanel");
    }

    public void showTeamSelectionPanel() {
        panels.add(new TeamSelectionPanel(this, username), "TeamSelectionPanel");
        cardLayout.show(panels, "TeamSelectionPanel");
    }

    public void showStartingFivePanel(Team team) {
        panels.add(new StartingFivePanel(this, team), "StartingFivePanel");
        cardLayout.show(panels, "StartingFivePanel");
    }

    public void startGame(Team userTeam, Team aiTeam) {
        GamePanel panel = new GamePanel(this, username, userTeam, aiTeam);
        panels.add(panel, "GamePanel");
        cardLayout.show(panels, "GamePanel");
        panel.requestFocusInWindow();
    }
    public void showPostGamePanel(Team user, Team ai) {
        PostGamePanel panel = new PostGamePanel(this, user, ai);
        panels.add(panel, "PostGamePanel");
        cardLayout.show(panels, "PostGamePanel");
    }
    public void restartFromTeamSelection() {
        showTeamSelectionPanel();
    }
    public void setUsername(String u) {
        username = u;
    }
    public static void main(String[] args) {
        new HoopDuels();
    }
}