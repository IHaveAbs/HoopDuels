import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * Main welcome screen after the name screen.
 */
public class MenuPanel extends JPanel implements ActionListener {
    private JButton startButton;
    private JButton tutorialButton;
    private HoopDuels window;

    public MenuPanel(HoopDuels window, String userName) {
        this.window = window;
        setLayout(new BorderLayout());
        setBackground(new Color(232, 240, 250));

        JLabel title = new JLabel("Hi " + userName + ", welcome to HoopDuel", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 36));
        title.setBorder(BorderFactory.createEmptyBorder(120, 0, 30, 0));
        add(title, BorderLayout.NORTH);

        JPanel buttons = new JPanel(new GridLayout(2, 1, 20, 20));
        buttons.setOpaque(false);
        buttons.setBorder(BorderFactory.createEmptyBorder(50, 390, 190, 390));

        startButton = new JButton("Start");
        startButton.setFont(new Font("Arial", Font.BOLD, 24));
        startButton.addActionListener(this);
        buttons.add(startButton);

        tutorialButton = new JButton("How To Play");
        tutorialButton.setFont(new Font("Arial", Font.BOLD, 24));
        tutorialButton.addActionListener(this);
        buttons.add(tutorialButton);

        add(buttons, BorderLayout.CENTER);
    }

    public void showTutorial() {
        String rules = "HoopDuel Instructions:\n\n"
            + "1. Choose your team and starting five.\n"
            + "2. On offense, choose a play.\n"
            + "3. Player circles and the ball will animate.\n"
            + "4. The shot is calculated using player stats.\n"
            + "5. On defense, choose a strategy to affect the AI shot.\n"
            + "6. When time runs out, the higher score wins.\n"
            + "7. If tied, sudden-death overtime starts.";
        JOptionPane.showMessageDialog(this, rules, "How To Play", JOptionPane.INFORMATION_MESSAGE);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == startButton) {
            window.showTeamSelectionPanel();
        } else if (e.getSource() == tutorialButton) {
            showTutorial();
        }
    }
}
