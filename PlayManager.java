import java.util.ArrayList;

/**
 * Play manager class that manages the players.
 * Uses a two dimensional array with the target position(x,y) of all of the players
 */
public class PlayManager {
    private ArrayList<Play> offensivePlays;

    /**
     * Constructs play manager
     * Creates a new arraylist for offensive plays
     * Then calls buildPlays()
     */
    public PlayManager() {
        offensivePlays = new ArrayList<Play>();
        buildPlays();
    }

    /**
     * These are the coordinates for each play.
     * This is for offensive team attacking right(So user's play)
     * Animation class later uses this to update the player's position based off target position
     */
    private void buildPlays() {
        offensivePlays.add(new Play("Pick and Roll", "rim", new int[][] {{430, 315}, {590, 185}, {620, 500}, {500, 150}, {665, 350}}, "C"));
        offensivePlays.add(new Play("Isolation Drive", "rim", new int[][] {{675, 350}, {620, 150}, {620, 520}, {430, 155}, {430, 520}}, "SF"));
        offensivePlays.add(new Play("Kick-Out Three", "three", new int[][] {{520, 350}, {670, 175}, {630, 520}, {430, 165}, {430, 520}}, "SG"));
        offensivePlays.add(new Play("Post Up", "rim", new int[][] {{430, 270}, {615, 155}, {615, 520}, {475, 500}, {675, 365}}, "C"));
    }

    /**
     * Sets player targets for a play. This is kept for compatibility with the spec.
     */
    /**
     * Sets up the the play
     * @param play the play
     * @param offense offensive team
     * @param ball the ball
     */
    public void setupPlay(Play play, Team offense, Ball ball) {
        setupPlay(play, offense, ball, true);
    }

    /**
     * Sets up the play by calling setupPlayStage
     * @param play the play
     * @param offense offensive team
     * @param ball the ball
     * @param attackingRight if the team is attacking right
     */
    public void setupPlay(Play play, Team offense, Ball ball, boolean attackingRight) {
        setupPlayStage(play, offense, ball, attackingRight, getStageCount(play) - 1);
    }

    /**
     * We were originally thinking of varying our stage count but we didn't have enough time so we just ended up choosing three.
     * @param play the play
     * @return how many animation stages the play uses
     */
    public int getStageCount(Play play) {
        return 3;
    }

    /**
     * Positions all offensive players and moves the ball for one stage of the play.
     * Mirrors coordinates if the offense is attacking left.
     * @param play the play being run
     * @param offense the offensive team
     * @param ball the ball
     * @param attackingRight true if the offense attacks the right hoop
     * @param stage the stage index to apply
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

    /**
     * Assigns non-default move speeds to specific players based on the play and stage.
     * Used to make screens, drives, and rolls look natural instead of every player moving at the same pace.
     * @param play the play being run
     * @param players the ordered list of players on the offensive team
     * @param stage the current stage index
     */
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
                c.setMoveSpeed(Player.slowSpeed);
            }
            if (stage == 1) {
                pg.setMoveSpeed(Player.fastSpeed);
                c.setMoveSpeed(Player.slowSpeed);
            }
            if (stage == 2) {
                c.setMoveSpeed(Player.fastSpeed);
            }
        }
        if (play.getName().equals("Isolation Drive")) {
            if (stage == 0) {
                sg.setMoveSpeed(Player.fastSpeed);
                pf.setMoveSpeed(Player.fastSpeed);
            }
            if (stage >= 1) {
                sf.setMoveSpeed(Player.fastSpeed);
            }
        }
        if (play.getName().equals("Kick-Out Three")) {
            if (stage == 1) {
                pg.setMoveSpeed(Player.fastSpeed);
            }
            if (stage == 2) {
                sg.setMoveSpeed(Player.fastSpeed);
            }
        }
        if (play.getName().equals("Post Up")) {
            if (stage == 0 || stage == 1) {
                c.setMoveSpeed(Player.slowSpeed);
            }
            if (stage == 2) {
                c.setMoveSpeed(Player.fastSpeed);
            }
        }
    }

    /**
     * Updates ball possession for the current stage of the play.
     * Handles passes to the shooter at the appropriate stage for each play.
     * @param play the play being run
     * @param offense the offensive team
     * @param ball the ball
     * @param stage the current stage index
     */
    private void moveBallForStage(Play play, Team offense, Ball ball, int stage) {
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
            Player driver;
            if (sf != null) {
                driver = sf;
            } else {
                driver = pg;
            }
            if (stage < 2) {
                ball.setHolder(driver);
            } else {
                ball.passTo(shooter);
            }
            return;
        }

        if (play.getName().equals("Post Up")) {
            // Entry pass comes from the wing/guard area, not always the point guard.
            Player entryPasser;
            if (sg != null) {
                entryPasser = sg;
            } else {
                entryPasser = pg;
            }
            if (stage == 0) {
                ball.setHolder(entryPasser);
            } else {
                Player postTarget;
                if (c != null) {
                    postTarget = c;
                } else {
                    postTarget = shooter;
                }
                ball.passTo(postTarget);
            }
            return;
        }

        ball.setHolder(pg);
    }

    /**
     * Coordinates for one play phase. All coordinates assume the offense attacks the right hoop.
     * Order: PG, SG, SF, PF, C.
     * @param playName the name of the play
     * @param stage the stage index to fetch coordinates for
     * @return a 5x2 array of target coordinates for each player
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

    /**
     * Mirrors an x coordinate to the left side of the court if the offense is attacking left.
     * @param x the original x coordinate assuming attack right
     * @param attackingRight true if the offense attacks the right hoop
     * @return the adjusted x coordinate
     */
    private int mirrorX(int x, boolean attackingRight) {
        if (attackingRight) {
            return x;
        }
        return GamePanel.courtX + GamePanel.courtW - (x - GamePanel.courtX);
    }

    /**
     * Returns the designated shooter for the play.
     * Falls back to the team's best shooter for the shot type if the position is not found.
     * @param play the play being run
     * @param offense the offensive team
     * @return the player who will take the shot
     */
    public Player chooseShooter(Play play, Team offense) {
        Player shooter = offense.getPlayerByPosition(play.getShooterPosition());
        if (shooter == null) {
            shooter = offense.getBestShooter(play.getShot());
        }
        return shooter;
    }

    /**
     * Returns the shot type for the given play.
     * @param play the play to check
     * @return the shot type string, or "mid" if the play is null
     */
    public String getShot(Play play) {
        if (play == null) {
            return "mid";
        }
        return play.getShot();
    }

    /**
     * Returns the full list of available offensive plays.
     * @return the list of offensive plays
     */
    public ArrayList<Play> getAvailablePlays() {
        return offensivePlays;
    }

    /**
     * Finds a play by name and returns it.
     * Returns the first play as a fallback if no match is found.
     * @param name the name of the play to look up
     * @return the matching play, or the first play if not found
     */
    public Play getPlayByName(String name) {
        for (Play p : offensivePlays) {
            if (p.getName().equals(name)) {
                return p;
            }
        }
        return offensivePlays.get(0);
    }

    /**
     * Returns the point guard of the team as the ball handler.
     * Falls back to the first player in the starting five if no PG is found.
     * @param team the team to get the ball handler from
     * @return the ball handling player, or null if the team is empty
     */
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

    /**
     * Returns the team's players ordered by role: PG, SG, SF, PF, C.
     * Any players not matched by position are appended at the end.
     * @param team the team to order
     * @return the ordered list of players
     */
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

    /**
     * Adds a player to the list only if they are not already in it.
     * @param list the list to add to
     * @param player the player to add
     */
    private void addUnique(ArrayList<Player> list, Player player) {
        if (player != null && !list.contains(player)) {
            list.add(player);
        }
    }

    /**
     * Finds a player in the list by position string.
     * Falls back to the player at the default index if no position match is found.
     * @param players the ordered list of players to search
     * @param position the position string to match
     * @param defaultplayer the fallback index if no match is found
     * @return the matched player, or a fallback, or null if the list is empty
     */
    private Player getByRoleList(ArrayList<Player> players, String position, int defaultplayer) {
        for (Player p : players) {
            if (p.getPosition().equals(position)) {
                return p;
            }
        }
        if (players.size() > defaultplayer) {
            return players.get(defaultplayer);
        }
        if (players.size() > 0) {
            return players.get(0);
        }
        return null;
    }
}