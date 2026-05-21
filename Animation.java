import java.util.ArrayList;

public class Animation {
    private Team userTeam;
    private Team aiTeam;
    private Ball ball;

    public Animation(Team userTeam, Team aiTeam, Ball ball) {
        this.userTeam = userTeam;
        this.aiTeam = aiTeam;
        this.ball = ball;
    }

    public void tick() {
        for (Player p : userTeam.getStartingFive()) {
            p.moveToTarget();
        }
        for (Player p : aiTeam.getStartingFive()){
            p.moveToTarget();
        }
        ball.moveToTarget();
    }

    public void resetFormation(Team team, int[][] formation) {
        ArrayList<Player> five = team.getStartingFive();
        for (int i = 0; i < five.size() && i < formation.length; i++)
            five.get(i).setTarget(formation[i][0], formation[i][1]);
    }

    public void goToFormation(Team team, int[][] formation) {
        ArrayList<Player> five = team.getStartingFive();
        for (int i = 0; i < five.size() && i < formation.length; i++){
            five.get(i).setPosition(formation[i][0], formation[i][1]);
        }
    }
}
