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
import javax.swing.border.Border;
import java.util.HashSet;

/**
 * This is the starting five panel where users can choose the 5 players they want.
 */
public class StartingFivePanel extends JPanel implements ActionListener {
    private Team selectedTeam;
    private ArrayList<Player> selectedPlayers;
    private JButton startGameButton;
    private HoopDuels window;
    private ArrayList<JCheckBox> checkboxes;
    private HashSet<String> positions;

    public StartingFivePanel(HoopDuels w, Team s) {
        window = w;
        selectedTeam = s;
        selectedPlayers = new ArrayList<Player>();
        checkboxes = new ArrayList<JCheckBox>();
        positions = new HashSet<String>();

        setLayout(new BorderLayout());
        setBackground(new Color(255, 255, 255));

        JLabel instructions = new JLabel("Select your 5 starters for "+selectedTeam.getTeamName(), JLabel.CENTER);
        instructions.setFont(new Font("Arial", Font.BOLD, 27));
        instructions.setBorder(BorderFactory.createEmptyBorder(45, 0, 20, 0));
        add(instructions, BorderLayout.NORTH);

        displayRoster(selectedTeam);
    }
    public void displayRoster(Team team) {
        JPanel roster = new JPanel(new GridLayout(0, 2, 13, 13));
        Border temp = BorderFactory.createEmptyBorder(20, 165, 20, 165);
        roster.setBorder(temp);

        for (Player p : team.getPlayers()) {
            String playerDetails = p.getName() + " (" + p.getPosition() + ")" + ": Rim " + p.getShotChance("rim") + " Mid " + p.getShotChance("mid") + " 3PT " + p.getShotChance("three");
            JCheckBox box = new JCheckBox(playerDetails);
            box.setFont(new Font("Arial", Font.PLAIN, 16));
            box.addActionListener(this);
            checkboxes.add(box);
            roster.add(box);
        }

        startGameButton = new JButton("Start Game");
        startGameButton.setFont(new Font("Arial", Font.BOLD, 23));
        startGameButton.addActionListener(this);

        add(roster, BorderLayout.CENTER);


        JPanel south = new JPanel();
        south.add(startGameButton);
        add(south, BorderLayout.SOUTH);
    }
    public boolean startingFiveOK() {
        if (selectedPlayers.size() == 5 && positions.contains("PG") && positions.contains("SG") && positions.contains("SF") && positions.contains("PF") && positions.contains("C")) {
            return true;
        }
        return false;
        
    }

    public ArrayList<Player> getStartingFive() {
        return selectedPlayers;
    }

    public void actionPerformed(ActionEvent e) {
        selectedPlayers.clear();
        positions.clear();

        for (int i = 0; i < checkboxes.size(); i++) {
            JCheckBox box = checkboxes.get(i);
            Player player = selectedTeam.getPlayers().get(i);
            if (box.isSelected()) {
                selectedPlayers.add(player);
                positions.add(player.getPosition());
            }
        }

        if (e.getSource() == startGameButton) {
            if (!startingFiveOK()) {
                JOptionPane.showMessageDialog(this, "Please select exactly one PG, SG, SF, PF, and C.");
                return;
            }

            ArrayList<Player> orderedStartingFive = new ArrayList<Player>();
            orderedStartingFive.add(getSelectedPlayerByPosition("PG"));
            orderedStartingFive.add(getSelectedPlayerByPosition("SG"));
            orderedStartingFive.add(getSelectedPlayerByPosition("SF"));
            orderedStartingFive.add(getSelectedPlayerByPosition("PF"));
            orderedStartingFive.add(getSelectedPlayerByPosition("C"));

            selectedTeam.canSetStartingFive(orderedStartingFive);

            Team aiTeam = PlayerManager.getDefaultAITeam(selectedTeam.getTeamName());
            aiTeam.canSetStartingFive(firstFive(aiTeam));

            window.startGame(selectedTeam, aiTeam);
            return;
        }

        updatecheckboxes();
        startGameButton.setEnabled(startingFiveOK());
    }
    private Player getSelectedPlayerByPosition(String position) {
        for (Player p : selectedPlayers) {
            if (p.getPosition().equals(position)) {
                return p;
            }
        }
        return null;
    }
    private void updatecheckboxes() {
        for (int i = 0; i < checkboxes.size(); i++) {
            JCheckBox box = checkboxes.get(i);
            Player player = selectedTeam.getPlayers().get(i);
            String position = player.getPosition();

            if (box.isSelected()) {
                box.setEnabled(true);
            } 
            else if (positions.contains(position)) {
                box.setEnabled(false);
            } 
            else {
                box.setEnabled(true);
            }
        }
    }

    private ArrayList<Player> firstFive(Team team) {
        ArrayList<Player> list = new ArrayList<Player>();
        for (int i = 0; i < 5 && i < team.getPlayers().size(); i++) {
            list.add(team.getPlayers().get(i));
        }
        return list;
    }
}