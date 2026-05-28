import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * this is the panel for team selection.
 * Creates buttons for the teams where players can choose from.
 */
public class TeamSelectionPanel extends JPanel implements ActionListener {
    private HashMap<String, Team> teams;
    private JButton warriorsSelect;
    private JButton lakersSelect;
    private JButton celticsSelect;
    private JButton thunderSelect;
    private HoopDuels window;
    private Team selected;
    /**
     * Constructs team selection panel based off HoopDuels window and the name
     * @param w the window 
     * @param name the name
     */
    public TeamSelectionPanel(HoopDuels w, String name) {
        window = w;
        teams = new HashMap<String, Team>();
        buildTeams();

        setLayout(new BorderLayout());
        setBackground(new Color(255, 255, 255));

        JLabel instruction = new JLabel("Choose your team, " + name, JLabel.CENTER);
        instruction.setFont(new Font("Arial", Font.BOLD, 33));
        add(instruction, BorderLayout.NORTH);

        JPanel buttons = new JPanel(new GridLayout(2, 2, 20, 20));
        warriorsSelect = makeButton("Warriors");
        lakersSelect = makeButton("Lakers");
        celticsSelect = makeButton("Celtics");
        thunderSelect = makeButton("Thunder");
        buttons.add(warriorsSelect);
        buttons.add(lakersSelect);
        buttons.add(celticsSelect);
        buttons.add(thunderSelect);
        add(buttons, BorderLayout.CENTER);
    }
    /**
     * utility method for creating the button
     * @param text the text for button 
     * @return the created button
     */
    private JButton makeButton(String text) {
        JButton temp = new JButton(text);
        temp.setFont(new Font("Arial", Font.BOLD, 23));
        temp.addActionListener(this);
        return temp;
    }
    /**
     * builds the team by referencing PlayManager's getTeams()
     */
    private void buildTeams() {
        Team[] all = PlayerManager.getTeams();
        for (Team t : all) {
            teams.put(t.getTeamName(), t);
        }
    }
    /**
     * 
     * @return the selected players
     */
    public Team getselected() {
        return selected;
    }
    /**
     * on click for going to the next step which is the starting five.
     */
    public void actionPerformed(ActionEvent e) {
        JButton temp = (JButton)e.getSource();
        String name = temp.getText();
        selected = teams.get(name);
        window.showStartingFivePanel(selected);
    }
}