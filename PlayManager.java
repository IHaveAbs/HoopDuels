import java.util.ArrayList;

/**
 * Creates and manages offensive plays.
 * It sets target coordinates for players and chooses the shooter.
 */
public class PlayManager {
    private ArrayList<Play> offensivePlays;

    public PlayManager() {
        offensivePlays = new ArrayList<Play>();
        buildPlays();
    }

    private void buildPlays() {
        // Final coordinates are written for a team attacking the right hoop.
        // GamePanel uses the phase methods below for the actual animation.
        offensivePlays.add(new Play("Pick and Roll", "rim", new int[][] {
            {430, 315}, {590, 185}, {620, 500}, {500, 150}, {665, 350}
        }, "C"));

        offensivePlays.add(new Play("Isolation Drive", "rim", new int[][] {
            {675, 350}, {620, 150}, {620, 520}, {430, 155}, {430, 520}
        }, "SF"));

        offensivePlays.add(new Play("Kick-Out Three", "three", new int[][] {
            {520, 350}, {670, 175}, {630, 520}, {430, 165}, {430, 520}
        }, "SG"));

        offensivePlays.add(new Play("Post Up", "rim", new int[][] {
            {430, 270}, {615, 155}, {615, 520}, {475, 500}, {675, 365}
        }, "C"));
    }

    /**
     * Sets player targets for a play. This is kept for compatibility with the spec.
     */
    public void setupPlay(Play play, Team offense, Ball ball) {
        setupPlay(play, offense, ball, true);
    }

    /**
     * Sets player targets and ball holder for a play. This is the older one-stage version.
     */
    public void setupPlay(Play play, Team offense, Ball ball, boolean attackingRight) {
        setupPlayStage(play, offense, ball, attackingRight, getStageCount(play) - 1);
    }

    /**
     * Returns how many animation stages this play uses.
     */
    public int getStageCount(Play play) {
        return 3;
    }

    /**
     * Applies one phase of the play. Targets are in role order: PG, SG, SF, PF, C.
     */
    public void setupPlayStage(Play play, Team offense, Ball ball, boolean attackingRight, int stage) {
        ArrayList<Player> players = getPlayersByRoleOrder(offense);
        int[][] targets = getTargetsForStage(play.getName(), stage);

        for (int i = 0; i < players.size() && i < targets.length; i++) {
            Player p = players.get(i);
            int tx = mirrorX(targets[i][0], attackingRight);
            int ty = targets[i][1];
            p.setTarget(tx, ty);
            p.resetMoveSpeed();
        }

        setSpecialSpeeds(play, players, stage);
        moveBallForStage(play, offense, ball, stage);
    }

    private void setSpecialSpeeds(Play play, ArrayList<Player> players, int stage) {
        Player pg = getByRoleList(players, "PG", 0);
        Player sg = getByRoleList(players, "SG", 1);
        Player sf = getByRoleList(players, "SF", 2);
        Player pf = getByRoleList(players, "PF", 3);
        Player c = getByRoleList(players, "C", 4);

        // Screens should not look like teleporting. The screener walks into position first,
        // then the ball handler bursts around it.
        if (play.getName().equals("Pick and Roll")) {
            if (stage == 0) {
                c.setMoveSpeed(Player.SLOW_SPEED);
            }
            if (stage == 1) {
                pg.setMoveSpeed(Player.SPRINT_SPEED);
                c.setMoveSpeed(Player.SLOW_SPEED);
            }
            if (stage == 2) {
                c.setMoveSpeed(Player.SPRINT_SPEED);
            }
        }
        if (play.getName().equals("Isolation Drive")) {
            if (stage == 0) {
                sg.setMoveSpeed(Player.SPRINT_SPEED);
                pf.setMoveSpeed(Player.SPRINT_SPEED);
            }
            if (stage >= 1) {
                sf.setMoveSpeed(Player.SPRINT_SPEED);
            }
        }
        if (play.getName().equals("Kick-Out Three")) {
            if (stage == 1) {
                pg.setMoveSpeed(Player.SPRINT_SPEED);
            }
            if (stage == 2) {
                sg.setMoveSpeed(Player.SPRINT_SPEED);
            }
        }
        if (play.getName().equals("Post Up")) {
            if (stage == 0 || stage == 1) {
                c.setMoveSpeed(Player.SLOW_SPEED);
            }
            if (stage == 2) {
                c.setMoveSpeed(Player.SPRINT_SPEED);
            }
        }
    }

    private void moveBallForStage(Play play, Team offense, Ball ball, int stage) {
        if (ball == null) {
            return;
        }

        Player pg = getBallHandler(offense);
        Player sg = offense.getPlayerByPosition("SG");
        Player sf = offense.getPlayerByPosition("SF");
        Player c = offense.getPlayerByPosition("C");
        Player shooter = chooseShooter(play, offense);

        if (play.getName().equals("Pick and Roll")) {
            // PG runs the action, then hits the rolling center.
            if (stage < 2) {
                ball.setHolder(pg);
            } else {
                ball.passTo(shooter);
            }
            return;
        }

        if (play.getName().equals("Isolation Drive")) {
            // Isolation is a self-created bucket. The iso player should have the ball
            // from the start so nobody gets a cheap assist.
            ball.setHolder(shooter);
            return;
        }

        if (play.getName().equals("Kick-Out Three")) {
            // A wing drives and kicks to the shooting guard. This spreads assists
            // around instead of giving every assist to the point guard.
            Player driver = sf != null ? sf : pg;
            if (stage < 2) {
                ball.setHolder(driver);
            } else {
                ball.passTo(shooter);
            }
            return;
        }

        if (play.getName().equals("Post Up")) {
            // Entry pass comes from the wing/guard area, not always the point guard.
            Player entryPasser = sg != null ? sg : pg;
            if (stage == 0) {
                ball.setHolder(entryPasser);
            } else {
                ball.passTo(c != null ? c : shooter);
            }
            return;
        }

        ball.setHolder(pg);
    }

    /**
     * Coordinates for one play phase. All coordinates assume the offense attacks the right hoop.
     * Order: PG, SG, SF, PF, C.
     */
    private int[][] getTargetsForStage(String playName, int stage) {
        if (playName.equals("Pick and Roll")) {
            if (stage == 0) {
                // Setup: guard waits at top, screener walks up, spacing goes to corners/wings.
                return new int[][] {{260,350},{590,180},{610,515},{475,155},{335,350}};
            }
            if (stage == 1) {
                // Screen: PG uses screen tight, center is planted, spacing stays wide.
                return new int[][] {{430,310},{620,175},{630,520},{500,155},{335,350}};
            }
            // Roll: center dives to rim, PG drifts to wing passing lane.
            return new int[][] {{470,250},{620,175},{630,520},{500,155},{665,350}};
        }

        if (playName.equals("Isolation Drive")) {
            if (stage == 0) {
                // Clear out: the iso scorer gets the ball alone at the top/wing.
                // Everyone else is spaced in corners and lifted slots so nobody stacks
                // on top of the ball handler.
                return new int[][] {{445,185},{645,180},{355,350},{445,515},{645,520}};
            }
            if (stage == 1) {
                // First move: scorer attacks the middle while the other four stay wide.
                return new int[][] {{445,185},{645,180},{535,350},{445,515},{645,520}};
            }
            // Finish: scorer gets downhill to the rim.
            return new int[][] {{445,185},{645,180},{690,350},{445,515},{645,520}};
        }

        if (playName.equals("Kick-Out Three")) {
            if (stage == 0) {
                // Spread floor: wing has the ball, shooter is spaced outside.
                return new int[][] {{375,250},{670,175},{355,350},{455,520},{610,520}};
            }
            if (stage == 1) {
                // Paint touch: wing drives into the lane so the defense collapses.
                return new int[][] {{375,250},{670,175},{545,350},{455,520},{610,520}};
            }
            // Kickout: ball is thrown back out to the shooter for a catch-and-shoot three.
            return new int[][] {{395,290},{680,175},{520,385},{455,520},{610,520}};
        }

        if (playName.equals("Post Up")) {
            if (stage == 0) {
                // Seal: center walks to the block while the shooting guard gets an entry angle.
                return new int[][] {{390,260},{545,245},{615,520},{455,500},{635,385}};
            }
            if (stage == 1) {
                // Entry: wing/guard feeds the post, center seals closer to the rim.
                return new int[][] {{405,270},{555,245},{625,520},{470,500},{665,370}};
            }
            // Finish: post player turns middle/baseline toward rim.
            return new int[][] {{405,270},{555,245},{625,520},{470,500},{700,350}};
        }

        return new int[][] {{300,350},{610,160},{610,520},{450,160},{450,520}};
    }

    private int mirrorX(int x, boolean attackingRight) {
        if (attackingRight) {
            return x;
        }
        return GamePanel.COURT_X + GamePanel.COURT_W - (x - GamePanel.COURT_X);
    }

    public Player chooseShooter(Play play, Team offense) {
        if (play == null || offense == null) {
            return null;
        }

        // The shooter should match the play animation.
        // For example, Kick-Out Three should go to the SG on the wing instead of
        // automatically crediting the team's best shooter every time.
        String preferred = play.getShooterPosition();
        Player shooter = offense.getPlayerByPosition(preferred);

        if (shooter == null) {
            shooter = offense.getBestShooter(play.getShotType());
        }
        return shooter;
    }

    public String getShotType(Play play) {
        if (play == null) {
            return "mid";
        }
        return play.getShotType();
    }

    public ArrayList<Play> getAvailablePlays() {
        return offensivePlays;
    }

    public Play getPlayByName(String name) {
        for (Play p : offensivePlays) {
            if (p.getName().equals(name)) {
                return p;
            }
        }
        return offensivePlays.get(0);
    }

    public Player getBallHandler(Team team) {
        Player pg = team.getPlayerByPosition("PG");
        if (pg != null) {
            return pg;
        }
        if (team.getStartingFive().size() > 0) {
            return team.getStartingFive().get(0);
        }
        return null;
    }

    private ArrayList<Player> getPlayersByRoleOrder(Team team) {
        ArrayList<Player> result = new ArrayList<Player>();
        addUnique(result, team.getPlayerByPosition("PG"));
        addUnique(result, team.getPlayerByPosition("SG"));
        addUnique(result, team.getPlayerByPosition("SF"));
        addUnique(result, team.getPlayerByPosition("PF"));
        addUnique(result, team.getPlayerByPosition("C"));

        ArrayList<Player> startingFive = team.getStartingFive();
        for (Player p : startingFive) {
            addUnique(result, p);
        }
        return result;
    }

    private void addUnique(ArrayList<Player> list, Player player) {
        if (player != null && !list.contains(player)) {
            list.add(player);
        }
    }

    private Player getByRoleList(ArrayList<Player> players, String position, int backupIndex) {
        for (Player p : players) {
            if (p.getPosition().equals(position)) {
                return p;
            }
        }
        if (players.size() > backupIndex) {
            return players.get(backupIndex);
        }
        if (players.size() > 0) {
            return players.get(0);
        }
        return null;
    }
}