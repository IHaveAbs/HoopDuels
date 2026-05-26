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
    public String getUserName() {
        return nameField.getText().trim();
    }
    public void actionPerformed(ActionEvent e) {
        String name = getUserName();
        if (name.length() == 0) {
            JOptionPane.showMessageDialog(this, "Please enter your name first.");
            return;
        }
        window.setUsername(name);
        window.showMenuPanel();
    }
}