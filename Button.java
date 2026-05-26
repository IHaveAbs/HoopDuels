import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JPanel;

public class Button {
    private ArrayList<JButton> playB;
    private ArrayList<JButton> defenseB;

    public Button() {
        playB = new ArrayList<JButton>();
        defenseB = new ArrayList<JButton>();
    }

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

    public void showPlayButtons(boolean show) {
        for (JButton b : playB) {
            b.setVisible(show);
        }
    }

    public void showDefenseButtons(boolean show) {
        for (JButton b : defenseB) {
            b.setVisible(show);
        }
    }
}