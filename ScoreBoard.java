import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

/**
 * This is the class for scoreboard. This class reads from the user and the ai and then displays it in the scoreboard
 */
public class ScoreBoard implements Drawable {
    private int possessionNumber;
    private int timeLeft;
    private Team user;
    private Team ai;

    public ScoreBoard(Team user, Team ai, int timeLeft) {
        this.user = user;
        this.ai = ai;
        this.timeLeft = timeLeft;
        possessionNumber = 1;
    }
    public void draw(Graphics g) {
        draw(g, user, ai);
    }
    public void draw(Graphics g, Team u, Team a) {
        g.setColor(new Color(30, 30, 30));
        g.fillRoundRect(30, 15, 730, 70, 15, 15);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 18));
        g.drawString(u.getTeamName() + ": " + u.getScore(), 55, 42);
        g.drawString("AI " + a.getTeamName() + ": " + a.getScore(), 250, 42);
        g.drawString("Clock: " + getClockDisplay(), 480, 42);
        g.drawString("Possession: " + possessionNumber, 620, 42);
        g.setFont(new Font("Arial", Font.PLAIN, 13));
    }
    public void updateScore(Team team, int points) {
        team.addScore(points);
    }
    public String getClockDisplay() {
        int minutes = timeLeft / 60;
        int seconds = timeLeft % 60;
        String sec;
        if (seconds < 10) {
            sec = "0" + seconds;
        }
        else {
            sec = "" + seconds;
        }
        return minutes + ":" + sec;
    }
    public boolean isGameOver() {
        return timeLeft <= 0;
    }

    public String getWinner(Team user, Team ai) {
        if (user.getScore() > ai.getScore()) {
            return user.getTeamName();
        }
        if (ai.getScore() > user.getScore()) {
            return ai.getTeamName();
        }
        return "Tie";
    }

    public void nextPossession() {
        possessionNumber++;
    }

    public int getPossessionNumber() {
        return possessionNumber;
    }

    public void setTimeLeft(int timeLeft) {
        this.timeLeft = Math.max(0, timeLeft);
    }
}