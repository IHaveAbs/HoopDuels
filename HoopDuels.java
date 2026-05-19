import java.awt.CardLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * Main application class for HoopDuel.
 * Creates the window and switches between screens.
 */
public class HoopDuels {
    private JFrame frame;
    private JPanel cards;
    private CardLayout cardLayout;
    private String userName;
    private Team userTeam;
    private Team aiTeam;

    public HoopDuels() {
        frame = new JFrame("HoopDuel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1100, 720);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        cardLayout = new CardLayout();
        cards = new JPanel(cardLayout);
        frame.setContentPane(cards);

        showNamePanel();
        frame.setVisible(true);
    }

    public void showNamePanel() {
        cards.add(new NamePanel(this), "NamePanel");
        cardLayout.show(cards, "NamePanel");
    }

    public void showMenuPanel() {
        cards.add(new MenuPanel(this, userName), "MenuPanel");
        cardLayout.show(cards, "MenuPanel");
    }

    public void showTeamSelectionPanel() {
        cards.add(new TeamSelectionPanel(this, userName), "TeamSelectionPanel");
        cardLayout.show(cards, "TeamSelectionPanel");
    }

    public void showStartingFivePanel(Team team) {
        this.userTeam = team;
        cards.add(new StartingFivePanel(this, team), "StartingFivePanel");
        cardLayout.show(cards, "StartingFivePanel");
    }

    public void startGame(Team userTeam, Team aiTeam) {
        this.userTeam = userTeam;
        this.aiTeam = aiTeam;
        GamePanel panel = new GamePanel(this, userName, userTeam, aiTeam);
        cards.add(panel, "GamePanel");
        cardLayout.show(cards, "GamePanel");
        panel.requestFocusInWindow();
    }

    public void showPostGamePanel(Team user, Team ai) {
        PostGamePanel panel = new PostGamePanel(this, user, ai);
        cards.add(panel, "PostGamePanel");
        cardLayout.show(cards, "PostGamePanel");
    }

    public void restartFromTeamSelection() {
        userTeam = null;
        aiTeam = null;
        showTeamSelectionPanel();
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public JFrame getFrame() {
        return frame;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new HoopDuels();
            }
        });
    }
}