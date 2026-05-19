import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

/**
 * Displays final score and player statistics after the game ends.
 */
public class PostGamePanel extends JPanel implements ActionListener {
    private Team userTeam;
    private Team aiTeam;
    private JButton restartButton;
    private JButton exitButton;
    private HoopDuel window;

    public PostGamePanel(HoopDuel window, Team userTeam, Team aiTeam) {
        this.window = window;
        this.userTeam = userTeam;
        this.aiTeam = aiTeam;

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        displayResults();
    }

    public void displayResults() {
        String winner;
        if (userTeam.getScore() > aiTeam.getScore()) {
            winner = userTeam.getTeamName() + " win!";
        } else if (aiTeam.getScore() > userTeam.getScore()) {
            winner = aiTeam.getTeamName() + " win!";
        } else {
            winner = "Game tied";
        }

        JLabel title = new JLabel("Final: " + userTeam.getTeamName() + " " + userTeam.getScore()
            + " - " + aiTeam.getScore() + " " + aiTeam.getTeamName() + "   |   " + winner,
            JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setBorder(BorderFactory.createEmptyBorder(25, 0, 15, 0));
        add(title, BorderLayout.NORTH);

        String[] columns = {"Team", "Player", "PTS", "REB", "AST", "FG"};
        Object[][] rows = buildRows();
        JTable table = new JTable(rows, columns);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setRowHeight(24);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel buttons = new JPanel(new GridLayout(1, 2, 25, 0));
        buttons.setBorder(BorderFactory.createEmptyBorder(20, 350, 25, 350));
        buttons.setOpaque(false);
        restartButton = new JButton("Play Again");
        restartButton.addActionListener(this);
        exitButton = new JButton("Exit");
        exitButton.addActionListener(this);
        buttons.add(restartButton);
        buttons.add(exitButton);
        add(buttons, BorderLayout.SOUTH);
    }

    private Object[][] buildRows() {
        int size = userTeam.getStartingFive().size() + aiTeam.getStartingFive().size();
        Object[][] rows = new Object[size][6];
        int row = 0;
        for (Player p : userTeam.getStartingFive()) {
            rows[row++] = playerRow(userTeam, p);
        }
        for (Player p : aiTeam.getStartingFive()) {
            rows[row++] = playerRow(aiTeam, p);
        }
        return rows;
    }

    private Object[] playerRow(Team team, Player p) {
        return new Object[] {
            team.getTeamName(),
            p.getName(),
            p.getPoints(),
            p.getRebounds(),
            p.getAssists(),
            p.getFieldGoalsMade() + "/" + p.getFieldGoalsAttempted()
        };
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == restartButton) {
            window.restartFromTeamSelection();
        } else if (e.getSource() == exitButton) {
            System.exit(0);
        }
    }
}