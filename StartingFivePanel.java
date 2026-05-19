import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * Lets the user choose exactly five players before the game starts.
 */
public class StartingFivePanel extends JPanel implements ActionListener {
    private Team selectedTeam;
    private ArrayList<Player> selectedPlayers;
    private JButton startGameButton;
    private HoopDuel window;
    private ArrayList<JCheckBox> checkBoxes;

    public StartingFivePanel(HoopDuel window, Team selectedTeam) {
        this.window = window;
        this.selectedTeam = selectedTeam;
        this.selectedPlayers = new ArrayList<Player>();
        this.checkBoxes = new ArrayList<JCheckBox>();

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JLabel title = new JLabel("Select exactly 5 starters for the " + selectedTeam.getTeamName(), JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setBorder(BorderFactory.createEmptyBorder(45, 0, 20, 0));
        add(title, BorderLayout.NORTH);

        displayRoster(selectedTeam);
    }

    public void displayRoster(Team team) {
        JPanel rosterPanel = new JPanel(new GridLayout(0, 2, 14, 14));
        rosterPanel.setBorder(BorderFactory.createEmptyBorder(20, 160, 20, 160));
        rosterPanel.setOpaque(false);

        for (Player p : team.getRoster()) {
            JCheckBox box = new JCheckBox(p.getName() + " (" + p.getPosition() + ")"
                + " | Rim " + p.getShotChance("rim")
                + " Mid " + p.getShotChance("mid")
                + " 3PT " + p.getShotChance("three"));
            box.setFont(new Font("Arial", Font.PLAIN, 16));
            box.addActionListener(this);
            checkBoxes.add(box);
            rosterPanel.add(box);
        }

        startGameButton = new JButton("Start Game");
        startGameButton.setFont(new Font("Arial", Font.BOLD, 22));
        startGameButton.setEnabled(false);
        startGameButton.addActionListener(this);

        add(rosterPanel, BorderLayout.CENTER);
        JPanel south = new JPanel();
        south.setOpaque(false);
        south.add(startGameButton);
        add(south, BorderLayout.SOUTH);
    }

    public boolean validStartingFive() {
        return selectedPlayers.size() == 5;
    }

    public ArrayList<Player> getStartingFive() {
        return selectedPlayers;
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == startGameButton) {
            if (!validStartingFive()) {
                JOptionPane.showMessageDialog(this, "Please select exactly 5 players.");
                return;
            }
            selectedTeam.setStartingFive(selectedPlayers);
            Team aiTeam = PlayerManager.getDefaultAITeam(selectedTeam.getTeamName());
            aiTeam.setStartingFive(firstFive(aiTeam));
            window.startGame(selectedTeam, aiTeam);
            return;
        }

        selectedPlayers.clear();
        for (int i = 0; i < checkBoxes.size(); i++) {
            JCheckBox box = checkBoxes.get(i);
            if (box.isSelected()) {
                selectedPlayers.add(selectedTeam.getRoster().get(i));
            }
        }

        if (selectedPlayers.size() > 5) {
            JCheckBox clicked = (JCheckBox) e.getSource();
            clicked.setSelected(false);
            selectedPlayers.clear();
            for (int i = 0; i < checkBoxes.size(); i++) {
                if (checkBoxes.get(i).isSelected()) {
                    selectedPlayers.add(selectedTeam.getRoster().get(i));
                }
            }
            JOptionPane.showMessageDialog(this, "You can only select 5 starters.");
        }

        startGameButton.setEnabled(validStartingFive());
    }

    private ArrayList<Player> firstFive(Team team) {
        ArrayList<Player> list = new ArrayList<Player>();
        for (int i = 0; i < 5 && i < team.getRoster().size(); i++) {
            list.add(team.getRoster().get(i));
        }
        return list;
    }
}