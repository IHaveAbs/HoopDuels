import java.awt.CardLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * This is the HoopDuel class
 * It manages the window frame and the classes connected to it.
 * Uses Cardlayout to switch between the cards for the panels
 */
public class HoopDuels {
    private JFrame frame;
    private JPanel panels;
    private CardLayout cardLayout;
    private String username;
    /**
     * Constructs HoopDuel and the frame for it.
     * Essentially the window for the game.
     * 
     * Sets up the CardLayout as well.
     */
    public HoopDuels() {
        frame = new JFrame("HoopDuels");
        frame.setSize(1100, 720);

        cardLayout = new CardLayout();
        panels = new JPanel(cardLayout);
        frame.setContentPane(panels);

        showNamePanel();
        frame.setVisible(true);
    }
    /**
     * shows the name panel
     */
    public void showNamePanel() {
        panels.add(new NamePanel(this), "NamePanel");
        cardLayout.show(panels, "NamePanel");
    }
    /**
     * shows the menu panel
     */
    public void showMenuPanel() {
        panels.add(new MenuPanel(this, username), "MenuPanel");
        cardLayout.show(panels, "MenuPanel");
    }
    /**
     * shows the team selection panel
     */
    public void showTeamSelectionPanel() {
        panels.add(new TeamSelectionPanel(this, username), "TeamSelectionPanel");
        cardLayout.show(panels, "TeamSelectionPanel");
    }
    /**
     * shows the starting five panel
     * @param team the team for the starting five
     */
    public void showStartingFivePanel(Team team) {
        panels.add(new StartingFivePanel(this, team), "StartingFivePanel");
        cardLayout.show(panels, "StartingFivePanel");
    }
    /**
     * shows the start game panel
     * @param userTeam user's team
     * @param aiTeam ai's team
     */
    public void startGame(Team userTeam, Team aiTeam) {
        GamePanel panel = new GamePanel(this, username, userTeam, aiTeam);
        panels.add(panel, "GamePanel");
        cardLayout.show(panels, "GamePanel");
        panel.requestFocusInWindow();
    }
    /**
     * shows the post game panel
     * @param user user's team
     * @param ai ai's team
     * 
     */
    public void showPostGamePanel(Team user, Team ai) {
        PostGamePanel panel = new PostGamePanel(this, user, ai);
        panels.add(panel, "PostGamePanel");
        cardLayout.show(panels, "PostGamePanel");
    }
    /**
     * Restarts the game from team selection
     * this is for players who want to replay after finishing their game.
     */
    public void restartFromTeamSelection() {
        showTeamSelectionPanel();
    }
    /**
     * sets the username
     * @param u username
     */
    public void setUsername(String u) {
        username = u;
    }
    /**
     * main function for the game, creates a new HoopDuels.
     * @param args
     */
    public static void main(String[] args) {
        new HoopDuels();
    }
}