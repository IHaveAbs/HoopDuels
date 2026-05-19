import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Lets the user choose which team they want to use.
 */
public class TeamSelectionPanel extends JPanel implements ActionListener {
    private HashMap<String, Team> teams;
    private JButton warriorsButton;
    private JButton lakersButton;
    private JButton celticsButton;
    private JButton thunderButton;
    private HoopDuels window;
    private Team selectedTeam;

    public TeamSelectionPanel(HoopDuels window, String userName) {
        this.window = window;
        teams = new HashMap<String, Team>();
        buildTeams();

        setLayout(new BorderLayout());
        setBackground(new Color(247, 247, 247));

        JLabel title = new JLabel("Hi " + userName + ", choose your team", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 34));
        title.setBorder(BorderFactory.createEmptyBorder(80, 0, 30, 0));
        add(title, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(40, 260, 180, 260));

        warriorsButton = createButton("Warriors");
        lakersButton = createButton("Lakers");
        celticsButton = createButton("Celtics");
        thunderButton = createButton("Thunder");

        buttonPanel.add(warriorsButton);
        buttonPanel.add(lakersButton);
        buttonPanel.add(celticsButton);
        buttonPanel.add(thunderButton);
        add(buttonPanel, BorderLayout.CENTER);
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 24));
        button.addActionListener(this);
        return button;
    }

    private void buildTeams() {
        Team[] allTeams = PlayerManager.getAllTeams();
        for (Team t : allTeams) {
            teams.put(t.getTeamName(), t);
        }
    }

    public void createTeamButtons() {
        // Buttons are created in the constructor.
    }

    public Team getSelectedTeam() {
        return selectedTeam;
    }

    public void actionPerformed(ActionEvent e) {
        String teamName = ((JButton) e.getSource()).getText();
        selectedTeam = teams.get(teamName);
        window.showStartingFivePanel(selectedTeam);
    }
}