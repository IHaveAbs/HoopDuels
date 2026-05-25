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
    private JButton restartGame;
    private JButton exitButton;
    private HoopDuels window;

    public PostGamePanel(HoopDuels w, Team u, Team a) {
        window = w;
        userTeam = u;
        aiTeam = a;

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        displayResults();
    }
    public void displayResults() {
        String winner;
        if (userTeam.getScore() > aiTeam.getScore()) {
            winner = userTeam.getTeamName() + " has won!";
        } else if (aiTeam.getScore() > userTeam.getScore()) {
            winner = aiTeam.getTeamName() + " has won!";
        } else {
            winner = "The Game is tied";
        }

        JLabel title = new JLabel("Final: "+userTeam.getTeamName()+" "+userTeam.getScore()+" - "+aiTeam.getScore()+" "+aiTeam.getTeamName()+"  |  "+winner,JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 23));
        title.setBorder(BorderFactory.createEmptyBorder(25, 0, 15, 0));
        add(title, BorderLayout.NORTH);

        String[] columns = {"Team", "Player", "PTS", "REB", "AST", "FG"};



        int size = userTeam.getStartingFive().size() + aiTeam.getStartingFive().size();
        Object[][] rows = new Object[size][6];
        int row = 0;
        for (Player p : userTeam.getStartingFive()) {
            rows[row++] = playerData(userTeam, p);
        }
        for (Player p : aiTeam.getStartingFive()) {
            rows[row++] = playerData(aiTeam, p);
        }


        JTable table = new JTable(rows, columns);
        table.setFont(new Font("Arial", Font.PLAIN, 13));
        table.setRowHeight(23);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel buttons = new JPanel(new GridLayout(1, 2, 25, 0));
        buttons.setBorder(BorderFactory.createEmptyBorder(20, 350, 25, 350));
        buttons.setOpaque(false);
        restartGame = new JButton("Play Again");
        restartGame.addActionListener(this);
        exitButton = new JButton("Exit");
        exitButton.addActionListener(this);
        buttons.add(restartGame);
        buttons.add(exitButton);
        add(buttons, BorderLayout.SOUTH);
    }
    private Object[] playerData(Team team, Player p) {
        Object[] temp = new Object[] {team.getTeamName(), p.getName(), p.getPoints(), p.getRebounds(), p.getAssists(), p.getFieldGoalsMade() + "/" + p.getFieldGoalsAttempted()};
        return temp;
    }
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == restartGame) {
            window.restartFromTeamSelection();
        } else if (e.getSource() == exitButton) {
            System.exit(0);
        }
    }
}