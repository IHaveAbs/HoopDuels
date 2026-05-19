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
 * First screen. Asks the user for their name.
 */
public class NamePanel extends JPanel implements ActionListener {
    private JTextField nameField;
    private JButton continueButton;
    private HoopDuels window;

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
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 220;
        gbc.ipady = 10;

        JLabel prompt = new JLabel("Enter your name:", JLabel.CENTER);
        prompt.setFont(new Font("Arial", Font.BOLD, 24));
        center.add(prompt, gbc);

        gbc.gridy++;
        nameField = new JTextField();
        nameField.setFont(new Font("Arial", Font.PLAIN, 22));
        center.add(nameField, gbc);

        gbc.gridy++;
        continueButton = new JButton("Continue");
        continueButton.setFont(new Font("Arial", Font.BOLD, 20));
        continueButton.addActionListener(this);
        center.add(continueButton, gbc);

        add(center, BorderLayout.CENTER);
    }

    public String getUserName() {
        return nameField.getText().trim();
    }

    public void actionPerformed(ActionEvent e) {
        String name = getUserName();
        if (name.length() == 0) {
            JOptionPane.showMessageDialog(this, "Please enter your name first.");
            return;
        }
        window.setUserName(name);
        window.showMenuPanel();
    }
}