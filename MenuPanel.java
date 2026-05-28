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
 * This is the welcome screen after the name screen.
 * Players can choose to start or see instructions
 */
public class MenuPanel extends JPanel implements ActionListener {
    private JButton start;
    private JButton tutorial;
    private HoopDuels window;
    /**
     * Constructs the MenuPaneul using the HoopDuels window and the username
     * 
     * Sets up the welcoming message
     * Creates the buttons for start and how to play/tutorial
     * @param window HoopDuels window
     * @param userName username
     */
    public MenuPanel(HoopDuels window, String userName) {
        this.window = window;
        setLayout(new BorderLayout());
        setBackground(new Color(232, 240, 250));

        JLabel title = new JLabel("Hi " + userName + ", welcome to HoopDuel", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 36));
        title.setBorder(BorderFactory.createEmptyBorder(120, 0, 30, 0));
        add(title, BorderLayout.NORTH);

        JPanel buttons = new JPanel(new GridLayout(2, 1, 20, 20));
        buttons.setBorder(BorderFactory.createEmptyBorder(50, 390, 190, 390));

        start = new JButton("Start");
        start.setFont(new Font("Arial", Font.BOLD, 24));
        start.addActionListener(this);
        buttons.add(start);

        tutorial = new JButton("How To Play");
        tutorial.setFont(new Font("Arial", Font.BOLD, 24));
        tutorial.addActionListener(this);
        buttons.add(tutorial);

        add(buttons, BorderLayout.CENTER);
    }
    /**
     * shows the tutorial/instructions
     */
    public void showTutorial() {
        String rules = "HoopDuel Instructions:\n\n"+"1. Choose your team and starting five.\n"+"2. On offense, choose a play.\n"+"3. Players are represented by circles and the ball will move based on the play.\n"+"4. The shot is calculated using player stats.\n";
        rules += "5. On defense, choose a strategy/modifier to affect the AI's shot.\n";
        rules += "6. When time runs out, the player with higher score wins.\n";
        rules += "7. If tied, there is a sudden death overtime.";
        JOptionPane.showMessageDialog(this, rules, "How To Play", JOptionPane.INFORMATION_MESSAGE);
    }
    /**
     * onclick method for the buttons
     * could be either to start or for the tutorial
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == start) {
            window.showTeamSelectionPanel();
        } else if (e.getSource() == tutorial) {
            showTutorial();
        }
    }
}
