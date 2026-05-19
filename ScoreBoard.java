
public class ScoreBoard {
    private int possessionNumber;
    private int timeLeft;
    private Team userTeam;
    private Team aiTeam;

    public ScoreBoard(Team userteam, Team aiteam, int timeleft) {
        this.userTeam = userteam;
        this.aiTeam = aiteam;
        this.timeLeft = timeleft;
        possessionNumber = 1;
    }
    public void draw(Graphics g, Team user, Team ai) {

    }
    public void updateScore(Team t, int points) {
        t.addScore(points);
    }
    public String getClockDisplay() {
        return (timeLeft/60)+":"+(timeLeft%60);
    }
    public boolean isGameOver() {
        if (timeLeft>0) {
            return false;
        }
        return true;
    }
    public String getWinner() {

    }
    public void nextPossession() {
        possessionNumber++;
    }
    public int getPossession() {
        return possessionNumber;
    }
    public void setTimeLeft(int timeleft) {
        timeLeft = Math.max(0, timeleft);
    }
    public int getTimeLeft() {
        return timeLeft;
    }


}