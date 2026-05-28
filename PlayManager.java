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
     * Sets the positions for all of the offensive players and moves the ball for a stage of the play.
     * If we are attacking to the left we simply mirror the coordinates
     * @param play the play
     * @param offense the offensive team
     * @param ball the ball
     * @param attackingRight whether we are attacking right
     * @param stage the stage 
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
     * Assigns the move speeds to players based off stage
     * 
     * @param play the play
     * @param players the players
     * @param stage the current stage
     */
    private void setSpecialSpeeds(Play play, ArrayList<Player> players, int stage) {
        Player pg = getByRoleList(players, "PG", 0);
        Player sg = getByRoleList(players, "SG", 1);
        Player sf = getByRoleList(players, "SF", 2);
        Player pf = getByRoleList(players, "PF", 3);
        Player c = getByRoleList(players, "C", 4);

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
     * Updates who is in possession of the ball depending on the current stage
     * Handles passes to the shooter at the appropriate stage for each play.
     * @param play the play
     * @param offense the offensive team
     * @param ball the ball
     * @param stage the current stage
     */
    private void moveBallForStage(Play play, Team offense, Ball ball, int stage) {
        Player pg = getBallHandler(offense);
        Player sg = offense.getPlayerByPosition("SG");
        Player sf = offense.getPlayerByPosition("SF");
        Player c = offense.getPlayerByPosition("C");
        Player shooter = chooseShooter(play, offense);

        if (play.getName().equals("Pick and Roll")) {
            if (stage < 2) {
                ball.setHolder(pg);
            } else {
                ball.passTo(shooter);
            }
            return;
        }
        if (play.getName().equals("Isolation Drive")) {

            ball.setHolder(shooter);
            return;
        }
        if (play.getName().equals("Kick-Out Three")) {

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
     * 
     * The coordinates for each play depending on the stage.
     * 
     * @param playName the name of the play
     * @param stage the stage index to fetch coordinates for
     * @return a 5x2 array of target coordinates for each player
     */
    private int[][] getTargetsForStage(String playName, int stage) {
        if (playName.equals("Pick and Roll")) {
            if (stage == 0) {
                return new int[][] {{260,350},{590,180},{610,515},{475,155},{335,350}};
            }
            if (stage == 1) {
                return new int[][] {{430,310},{620,175},{630,520},{500,155},{335,350}};
            }
            return new int[][] {{470,250},{620,175},{630,520},{500,155},{665,350}};
        }

        if (playName.equals("Isolation Drive")) {
            if (stage == 0) {

                return new int[][] {{445,185},{645,180},{355,350},{445,515},{645,520}};
            }
            if (stage == 1) {
                return new int[][] {{445,185},{645,180},{535,350},{445,515},{645,520}};
            }
            return new int[][] {{445,185},{645,180},{690,350},{445,515},{645,520}};
        }

        if (playName.equals("Kick-Out Three")) {
            if (stage == 0) {
                return new int[][] {{375,250},{670,175},{355,350},{455,520},{610,520}};
            }
            if (stage == 1) {
                return new int[][] {{375,250},{670,175},{545,350},{455,520},{610,520}};
            }
            return new int[][] {{395,290},{680,175},{520,385},{455,520},{610,520}};
        }

        if (playName.equals("Post Up")) {
            if (stage == 0) {
                return new int[][] {{390,260},{545,245},{615,520},{455,500},{635,385}};
            }
            if (stage == 1) {
                return new int[][] {{405,270},{555,245},{625,520},{470,500},{665,370}};
            }
            return new int[][] {{405,270},{555,245},{625,520},{470,500},{700,350}};
        }

        return new int[][] {{300,350},{610,160},{610,520},{450,160},{450,520}};
    }

    /**
     * Mirrors the x coordinate if we are attacking left
     * @param x original x position
     * @param attackingRight whether we are attacking right
     * @return final x coordinate
     */
    private int mirrorX(int x, boolean attackingRight) {
        if (attackingRight) {
            return x;
        }
        return GamePanel.courtX+GamePanel.courtW-(x-GamePanel.courtX);
    }

    /**
     * Chooses the shooter for the play.
     * @param play the play
     * @param offense the offensive team
     * @return the player who will take the shot
     */
    public Player chooseShooter(Play play, Team offense) {
        Player shooter = offense.getPlayerByPosition(play.getShooterPosition());
        return shooter;
    }

    /**
     * Returns the shot type for the given play.
     * @param play the play 
     * @return the shot type
     */
    public String getShot(Play play) {
        return play.getShot();
    }

    /**
     * Returns the list of available plays.
     * @return the list of offensive plays
     */
    public ArrayList<Play> getAvailablePlays() {
        return offensivePlays;
    }

    /**
     * Finds a play by name and returns it.
     * 
     * @param name the name of the play 
     * @return the play that matches
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
     * 
     * @param team the team to get the ball handler from
     * @return the ball handling player(pg)
     */
    public Player getBallHandler(Team team) {
        Player pg = team.getPlayerByPosition("PG");
        if (pg != null) {
            return pg;
        }

        return null;
    }

    /**
     * Returns the team's players by role
     * 
     * @param team the team 
     * @return the list of players
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
     * Adds a player to the list if they are not already in it.
     * @param list the list to add to
     * @param player the player to add
     */
    private void addUnique(ArrayList<Player> list, Player player) {
        if (player != null && !list.contains(player)) {
            list.add(player);
        }
    }

    /**
     * Finds a player in the list
     * @param players the list of players to search
     * @param position the position to match
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