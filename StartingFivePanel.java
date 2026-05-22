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
import java.util.HashSet;

/**
 * Lets the user choose exactly five players before the game starts.
 */
public class StartingFivePanel extends JPanel implements ActionListener {
    private Team selectedTeam;
    private ArrayList<Player> selectedPlayers;
    private JButton startGameButton;
    private HoopDuels window;
    private ArrayList<JCheckBox> checkBoxes;
    private HashSet<String> selectedPositions;

    public StartingFivePanel(HoopDuels window, Team selectedTeam) {
        this.window = window;
        this.selectedTeam = selectedTeam;
        this.selectedPlayers = new ArrayList<Player>();
        this.checkBoxes = new ArrayList<JCheckBox>();
        this.selectedPositions = new HashSet<String>();

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
        return selectedPlayers.size() == 5
        && selectedPositions.contains("PG")
        && selectedPositions.contains("SG")
        && selectedPositions.contains("SF")
        && selectedPositions.contains("PF")
        && selectedPositions.contains("C");
    }

    public ArrayList<Player> getStartingFive() {
        return selectedPlayers;
    }

    public void actionPerformed(ActionEvent e) {
        selectedPlayers.clear();
        selectedPositions.clear();

        for (int i = 0; i < checkBoxes.size(); i++) {
            JCheckBox box = checkBoxes.get(i);
            Player player = selectedTeam.getRoster().get(i);
            if (box.isSelected()) {
                selectedPlayers.add(player);
                selectedPositions.add(player.getPosition());
            }
        }

        if (e.getSource() == startGameButton) {
            if (!validStartingFive()) {
                JOptionPane.showMessageDialog(this, "Please select exactly one PG, SG, SF, PF, and C.");
                return;
            }

            ArrayList<Player> orderedStartingFive = new ArrayList<Player>();
            orderedStartingFive.add(getSelectedPlayerByPosition("PG"));
            orderedStartingFive.add(getSelectedPlayerByPosition("SG"));
            orderedStartingFive.add(getSelectedPlayerByPosition("SF"));
            orderedStartingFive.add(getSelectedPlayerByPosition("PF"));
            orderedStartingFive.add(getSelectedPlayerByPosition("C"));

            selectedTeam.setStartingFive(orderedStartingFive);

            Team aiTeam = PlayerManager.getDefaultAITeam(selectedTeam.getTeamName());
            aiTeam.setStartingFive(firstFive(aiTeam));

            window.startGame(selectedTeam, aiTeam);
            return;
        }

        updateCheckboxes();
        startGameButton.setEnabled(validStartingFive());
    }
    private Player getSelectedPlayerByPosition(String position) {
        for (Player p : selectedPlayers) {
            if (p.getPosition().equals(position)) {
                return p;
            }
        }
        return null;
    }
    private void updateCheckboxes() {
        for (int i = 0; i < checkBoxes.size(); i++) {
            JCheckBox box = checkBoxes.get(i);
            Player player = selectedTeam.getRoster().get(i);
            String position = player.getPosition();

            if (box.isSelected()) {
                box.setEnabled(true);
            } 
            else if (selectedPositions.contains(position)) {
                box.setEnabled(false);
            } 
            else {
                box.setEnabled(true);
            }
        }
    }

    private ArrayList<Player> firstFive(Team team) {
        ArrayList<Player> list = new ArrayList<Player>();
        for (int i = 0; i < 5 && i < team.getRoster().size(); i++) {
            list.add(team.getRoster().get(i));
        }
        return list;
    }
}