import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * This is the first screen and it asks the user for the name
 */
public class NamePanel extends JPanel implements ActionListener {
    private JTextField nameField;
    private JButton continueButton;
    private HoopDuels window;

    /**
     * Constructs NamePanel with HoopDuel window
     * 
     * Asks the user to enter their name and creates a text field for them to enter their name
     * Sets up the continue button
     * @param window HoopDuels window
     */
    public NamePanel(HoopDuels window) {
        this.window = window;
        setLayout(new BorderLayout());
        setBackground(new Color(245, 248, 252));

        JLabel title = new JLabel("HoopDuel", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 54));
        title.setBorder(BorderFactory.createEmptyBorder(70, 0, 30, 0));
        add(title, BorderLayout.NORTH);

        JPanel center = new JPanel(new GridBagLayout());
        center.setOpaque(false);
        GridBagConstraints temp = new GridBagConstraints();
        temp.gridx = 0;
        temp.gridy = 0;
        temp.fill = GridBagConstraints.HORIZONTAL;
        temp.ipadx = 220;
        temp.ipady = 10;

        JLabel prompt = new JLabel("Enter your name:", JLabel.CENTER);
        prompt.setFont(new Font("Arial", Font.BOLD, 24));
        center.add(prompt, temp);

        temp.gridy++;
        nameField = new JTextField();
        nameField.setFont(new Font("Arial", Font.PLAIN, 22));
        center.add(nameField, temp);

        temp.gridy++;
        continueButton = new JButton("Continue");
        continueButton.setFont(new Font("Arial", Font.BOLD, 20));
        continueButton.addActionListener(this);
        center.add(continueButton, temp);

        add(center, BorderLayout.CENTER);
    }
    /**
     * 
     * @return the username
     */
    public String getUserName() {
        return nameField.getText().trim();
    }
    /**
     * Onclick method for if the user enters their name
     */
    public void actionPerformed(ActionEvent e) {
        String name = getUserName();
        window.setUsername(name);
        window.showMenuPanel();
    }
}