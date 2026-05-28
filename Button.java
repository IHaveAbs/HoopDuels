import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * Class for managing the buttons
 */
public class Button {
    private ArrayList<JButton> playB;
    private ArrayList<JButton> defenseB;

    /**
     * Constructs the Button class
     * creates an empty arraylist for all the play buttons
     * creates an empty arraylist for all the defense buttons
     */
    public Button() {
        playB = new ArrayList<JButton>();
        defenseB = new ArrayList<JButton>();
    }
    /**
     * creates the play buttons
     * @param panel panel to create it on
     * @param plays the plays to put on the buttons
     * @param gamePanel the game panel
     */
    public void createPlayButtons(JPanel panel, ArrayList<Play> plays, GamePanel gamePanel) {
        int startX = 810;
        int startY = 90;

        for (int i = 0; i < plays.size(); i++) {
            Play play = plays.get(i);
            JButton button = new JButton(play.getName());
            button.setBounds(startX, startY + i * 45, 250, 35);
            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    gamePanel.runUserPossession(play.getName());
                }
            });
            playB.add(button);
            panel.add(button);
        }
    }
    /**
     * Creates the buttons for defense
     * @param panel panel to add it on
     * @param defenses the list of defenses to put on the buttons
     * @param gamePanel the game panel
     */
    public void createDefenseButtons(JPanel panel, ArrayList<String> defenses, GamePanel gamePanel) {
        int startX = 810;
        int startY = 90;

        for (int i = 0; i < defenses.size(); i++) {
            String defense = defenses.get(i);
            JButton button = new JButton(defense);
            button.setBounds(startX, startY + i * 45, 250, 35);
            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    gamePanel.runAIPossession(defense);
                }
            });
            defenseB.add(button);
            panel.add(button);
        }
    }
    /**
     * shows/hides play buttons
     * @param show true/false for if you want to show the play buttons
     */
    public void showPlayButtons(boolean show) {
        for (JButton b : playB) {
            b.setVisible(show);
        }
    }
    /**
     * shows/hides the defense buttons
     * @param show true/false for if you want to show the defense buttons
     */
    public void showDefenseButtons(boolean show) {
        for (JButton b : defenseB) {
            b.setVisible(show);
        }
    }
}