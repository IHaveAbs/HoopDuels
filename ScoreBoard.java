import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

/**
 * Draws the scoreboard. It reads scores directly from Team objects and does not
 * store its own duplicate scores.
 */
public class ScoreBoard implements Drawable {
    private int possessionNumber;
    private int timeLeft;
    private Team userTeam;
    private Team aiTeam;

    public ScoreBoard(Team userTeam, Team aiTeam, int timeLeft) {
        this.userTeam = userTeam;
        this.aiTeam = aiTeam;
        this.timeLeft = timeLeft;
        this.possessionNumber = 1;
    }

    public void draw(Graphics g) {
        draw(g, userTeam, aiTeam);
    }

    public void draw(Graphics g, Team user, Team ai) {
        g.setColor(new Color(30, 30, 30));
        g.fillRoundRect(30, 15, 730, 70, 15, 15);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 18));
        g.drawString(user.getTeamName() + ": " + user.getScore(), 55, 42);
        g.drawString("AI " + ai.getTeamName() + ": " + ai.getScore(), 250, 42);
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
        String sec = seconds < 10 ? "0" + seconds : "" + seconds;
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

    public int getTimeLeft() {
        return timeLeft;
    }
}
