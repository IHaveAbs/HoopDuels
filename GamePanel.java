import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * Main gameplay panel. Draws the court and runs the turn-based possession loop.
 */
public class GamePanel extends JPanel implements ActionListener {
    public static final int COURT_X = 30;
    public static final int COURT_Y = 100;
    public static final int COURT_W = 740;
    public static final int COURT_H = 500;

    private static final int RIGHT_HOOP_X = COURT_X + COURT_W - 35;
    private static final int LEFT_HOOP_X = COURT_X + 35;
    private static final int HOOP_Y = COURT_Y + COURT_H / 2;

    // 720 seconds = one 12-minute quarter. We decrease by 6 each real second for demo speed.
    private static final int START_TIME = 720;
    private static final int CLOCK_DECREMENT = 6;
    private static final int FRAME_DELAY = 16;
    private static final int RESULT_PAUSE_FRAMES = 70;

    private HoopDuels window;
    private String userName;
    private Team userTeam;
    private Team aiTeam;
    private Ball ball;
    private ScoreBoard scoreboard;
    private PlayManager playManager;
    private DefenseManager defenseManager;
    private AIManager aiManager;
    private PlayLog playLog;
    private Timer animTimer;
    private int timeLeft;
    private boolean userTurn;
    private boolean overtime;
    private String state;
    private String resultMessage;
    private int resultPause;
    private int clockCounter;
    private Play activePlay;
    private Player activeShooter;
    private String activeShotType;
    private String activeDefense;
    private int currentPlayStage;
    private int totalPlayStages;
    private Animation animation;
    private GameState gameState;

    private ArrayList<Player> allCourtPlayers;
    private ArrayList<JButton> playButtons;
    private ArrayList<JButton> defenseButtons;

    public GamePanel(HoopDuels window, String userName, Team userTeam, Team aiTeam) {
        this.window = window;
        this.userName = userName;
        this.userTeam = userTeam;
        this.aiTeam = aiTeam;

        setLayout(null);
        setBackground(new Color(230, 238, 240));
        setFocusable(true);

        playManager = new PlayManager();
        defenseManager = new DefenseManager();
        aiManager = new AIManager(aiTeam, playManager.getAvailablePlays());
        playLog = new PlayLog();
        timeLeft = START_TIME;
        scoreboard = new ScoreBoard(userTeam, aiTeam, timeLeft);
        ball = new Ball(200, 250);
        allCourtPlayers = new ArrayList<Player>();
        playButtons = new ArrayList<JButton>();
        defenseButtons = new ArrayList<JButton>();

        userTurn = true;
        animation = false;
        overtime = false;
        state = "WAITING_USER_PLAY";
        resultMessage = "Choose an offensive play.";
        resultPause = 0;
        clockCounter = 0;
        activeDefense = "Man-to-Man";
        currentPlayStage = 0;
        totalPlayStages = 0;

        setupInitialPositions();
        animation = new Animation(allCourtPlayers, ball);
        createPlayButtons();
        createDefenseButtons();
        showPlayButtons(true);
        showDefenseButtons(false);

        animTimer = new Timer(FRAME_DELAY, this);
        animTimer.start();
        playLog.addMessage("Welcome, " + userName + "!");
        playLog.addMessage("Choose your first play.");
    }

    private void setupInitialPositions() {
        int[][] userPositions = getUserBasePositions();
        int[][] aiPositions = getAIBasePositions();

        for (int i = 0; i < userTeam.getStartingFive().size(); i++) {
            Player p = userTeam.getStartingFive().get(i);
            p.setPosition(userPositions[i][0], userPositions[i][1]);
            allCourtPlayers.add(p);
        }
        for (int i = 0; i < aiTeam.getStartingFive().size(); i++) {
            Player p = aiTeam.getStartingFive().get(i);
            p.setPosition(aiPositions[i][0], aiPositions[i][1]);
            allCourtPlayers.add(p);
        }
        if (userTeam.getStartingFive().size() > 0) {
            ball.setHolder(playManager.getBallHandler(userTeam));
        }
    }

    /**
     * User offense always starts on the left and attacks the right hoop.
     * When the AI has the ball, the user should sprint back to this left side to defend.
     */
    private int[][] getUserBasePositions() {
        return new int[][] {{190,350},{300,185},{300,515},{230,255},{230,445}};
    }

    /**
     * AI starts on the right side. On AI possessions it attacks left; on user possessions it defends right.
     */
    private int[][] getAIBasePositions() {
        return new int[][] {{610,350},{500,185},{500,515},{570,255},{570,445}};
    }

    private void setTransitionTargetsForAIOffense() {
        // This fixes the old issue: the user team must sprint back to the left side
        // because the AI attacks the left hoop after the user's possession ends.
        setTargetsFromArray(userTeam, getUserBasePositions(), Player.SPRINT_SPEED);
        setTargetsFromArray(aiTeam, getAIBasePositions(), Player.SPRINT_SPEED);
        ball.setHolder(playManager.getBallHandler(aiTeam));
    }

    private void setTransitionTargetsForUserOffense() {
        setTargetsFromArray(userTeam, getUserBasePositions(), Player.SPRINT_SPEED);
        setTargetsFromArray(aiTeam, getAIBasePositions(), Player.SPRINT_SPEED);
        ball.setHolder(playManager.getBallHandler(userTeam));
    }

    private void setTargetsFromArray(Team team, int[][] targets, int speed) {
        ArrayList<Player> players = team.getStartingFive();
        for (int i = 0; i < players.size() && i < targets.length; i++) {
            Player p = players.get(i);
            p.setTarget(targets[i][0], targets[i][1]);
            p.setMoveSpeed(speed);
        }
    }

    private void createPlayButtons() {
        ArrayList<Play> plays = playManager.getAvailablePlays();
        int startX = 810;
        int startY = 90;
        for (int i = 0; i < plays.size(); i++) {
            Play play = plays.get(i);
            JButton button = new JButton(play.getName());
            button.setBounds(startX, startY + i * 45, 250, 35);
            button.addActionListener(new PlayButtonListener(play));
            playButtons.add(button);
            add(button);
        }
    }

    private void createDefenseButtons() {
        ArrayList<String> defenses = defenseManager.getDefensiveOptions();
        int startX = 810;
        int startY = 90;
        for (int i = 0; i < defenses.size(); i++) {
            String defense = defenses.get(i);
            JButton button = new JButton(defense);
            button.setBounds(startX, startY + i * 45, 250, 35);
            button.addActionListener(new DefenseButtonListener(defense));
            defenseButtons.add(button);
            add(button);
        }
    }

    private void showPlayButtons(boolean show) {
        for (JButton b : playButtons) {
            b.setVisible(show);
        }
    }

    private void showDefenseButtons(boolean show) {
        for (JButton b : defenseButtons) {
            b.setVisible(show);
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawCourt(g);
        drawPlayers(g);
        ball.draw(g);
        scoreboard.draw(g, userTeam, aiTeam);
        playLog.draw(g);
        drawSidePanelText(g);
        drawResultMessage(g);
    }

    private void drawSidePanelText(Graphics g) {
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 16));
        if (state.equals("WAITING_USER_PLAY")) {
            g.drawString("Choose Offensive Play", 815, 75);
        } else if (state.equals("WAITING_DEFENSE")) {
            g.drawString("Choose Defensive Strategy", 815, 75);
            g.setFont(new Font("Arial", Font.PLAIN, 12));
            g.drawString("Zone: -10 rim, +10 three", 815, 540);
            g.drawString("Paint: -15 rim, +15 three", 815, 560);
            g.drawString("Perimeter: +10 rim, -15 three", 815, 580);
        } else if (state.equals("AI_TRANSITION")) {
            g.drawString("Transition Defense", 815, 75);
        } else if (state.equals("USER_TRANSITION")) {
            g.drawString("Teams Resetting", 815, 75);
        } else {
            g.drawString("Simulation Running", 815, 75);
        }
    }

    private void drawResultMessage(Graphics g) {
        if (resultMessage != null && resultMessage.length() > 0) {
            g.setColor(new Color(255, 255, 255, 210));
            g.fillRoundRect(190, 36, 480, 45, 15, 15);
            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.BOLD, 16));
            g.drawString(resultMessage, 210, 64);
        }
    }

    public void actionPerformed(ActionEvent e) {
        updateGame();
        repaint();
    }

    public void updateGame() {
        animation.tick();

        if (state.equals("AI_TRANSITION")) {
            if (allPlayersAtTarget() && !ball.isMoving()) {
                beginPlayAnimation(false);
            }
        } else if (state.equals("USER_TRANSITION")) {
            if (allPlayersAtTarget() && !ball.isMoving()) {
                for (Player p : allCourtPlayers) {
                    p.resetMoveSpeed();
                }
                state = "WAITING_USER_PLAY";
                resultMessage = "Choose your next offensive play.";
                showPlayButtons(true);
                showDefenseButtons(false);
            }
        } else if (state.equals("USER_ANIMATING") || state.equals("AI_ANIMATING")) {
            if (allPlayersAtTarget() && !ball.isMoving()) {
                if (currentPlayStage < totalPlayStages - 1) {
                    currentPlayStage++;
                    applyCurrentPlayStage();
                } else {
                    if (resultPause == 0) {
                        finishShotAnimation();
                        resultPause = RESULT_PAUSE_FRAMES;
                    } else {
                        resultPause--;
                        if (resultPause == 0) {
                            finishPossessionAfterPause();
                        }
                    }
                }
            }
        }

        checkGameOver();
    }

    private void updateClock() {
        if (state.equals("GAME_OVER") || overtime) {
            return;
        }
        clockCounter += FRAME_DELAY;
        if (clockCounter >= 1000) {
            clockCounter = 0;
            timeLeft -= CLOCK_DECREMENT;
            if (timeLeft < 0) {
                timeLeft = 0;
            }
            scoreboard.setTimeLeft(timeLeft);
        }
    }

    public void drawCourt(Graphics g) {
        g.setColor(new Color(210, 160, 90));
        g.fillRect(COURT_X, COURT_Y, COURT_W, COURT_H);
        g.setColor(Color.BLACK);
        g.drawRect(COURT_X, COURT_Y, COURT_W, COURT_H);

        // Center line and circle
        g.drawLine(COURT_X + COURT_W / 2, COURT_Y, COURT_X + COURT_W / 2, COURT_Y + COURT_H);
        g.drawOval(COURT_X + COURT_W / 2 - 60, COURT_Y + COURT_H / 2 - 60, 120, 120);

        // Hoops/backboards drawn from a top-down view.
        // Left basket faces right: backboard is on the left, rim extends into the court.
        // Right basket faces left: backboard is on the right, rim extends into the court.
        int leftBackboardX = COURT_X + 14;
        int rightBackboardX = COURT_X + COURT_W - 14;
        g.setColor(Color.WHITE);
        g.fillRect(leftBackboardX - 3, HOOP_Y - 36, 6, 72);
        g.fillRect(rightBackboardX - 3, HOOP_Y - 36, 6, 72);
        g.setColor(Color.BLACK);
        g.drawRect(leftBackboardX - 3, HOOP_Y - 36, 6, 72);
        g.drawRect(rightBackboardX - 3, HOOP_Y - 36, 6, 72);
        g.setColor(Color.RED);
        g.drawOval(LEFT_HOOP_X - 12, HOOP_Y - 12, 24, 24);
        g.drawOval(RIGHT_HOOP_X - 12, HOOP_Y - 12, 24, 24);
        g.setColor(Color.BLACK);
        g.drawLine(leftBackboardX, HOOP_Y, LEFT_HOOP_X - 12, HOOP_Y);
        g.drawLine(rightBackboardX, HOOP_Y, RIGHT_HOOP_X + 12, HOOP_Y);

        // Paint/key areas
        g.drawRect(COURT_X, HOOP_Y - 85, 150, 170);
        g.drawRect(COURT_X + COURT_W - 150, HOOP_Y - 85, 150, 170);

        // Simple three-point arc suggestion
        g.drawArc(COURT_X + COURT_W - 250, HOOP_Y - 170, 230, 340, 90, 180);
        g.drawArc(COURT_X + 20, HOOP_Y - 170, 230, 340, -90, 180);
    }

    public void drawPlayers(Graphics g) {
        for (Player p : allCourtPlayers) {
            p.draw(g);
        }
    }

    public void runUserPossession(String playName) {
        Play play = playManager.getPlayByName(playName);
        runUserPossession(play);
    }

    private void runUserPossession(Play play) {
        if (!state.equals("WAITING_USER_PLAY")) {
            return;
        }
        activePlay = play;
        activeShotType = play.getShotType();
        activeShooter = playManager.chooseShooter(play, userTeam);
        userTurn = true;
        currentPlayStage = 0;
        totalPlayStages = playManager.getStageCount(play);
        resultPause = 0;
        showPlayButtons(false);
        showDefenseButtons(false);
        state = "USER_ANIMATING";
        animationRunning = true;
        resultMessage = userTeam.getTeamName() + " set up " + play.getName();
        playLog.addMessage(userTeam.getTeamName() + ": " + play.getName());
        applyCurrentPlayStage();
    }

    public void runAIPossession(String defenseName) {
        if (!state.equals("WAITING_DEFENSE")) {
            return;
        }
        defenseManager.setDefense(defenseName);
        activeDefense = defenseName;
        userTurn = false;
        activePlay = aiManager.choosePlay(aiTeam.getScore(), userTeam.getScore(), timeLeft);
        activeShotType = activePlay.getShotType();
        activeShooter = aiManager.chooseShooter(aiTeam, activePlay);
        currentPlayStage = 0;
        totalPlayStages = playManager.getStageCount(activePlay);
        resultPause = 0;
        showDefenseButtons(false);
        showPlayButtons(false);
        state = "AI_TRANSITION";
        animationRunning = true;
        resultMessage = "Sprint back into " + defenseName + "!";
        playLog.addMessage("Defense: " + defenseName);
        setTransitionTargetsForAIOffense();
    }

    private void beginPlayAnimation(boolean userOnOffense) {
        userTurn = userOnOffense;
        if (userTurn) {
            state = "USER_ANIMATING";
            resultMessage = userTeam.getTeamName() + " set up " + activePlay.getName();
        } else {
            state = "AI_ANIMATING";
            resultMessage = "AI runs " + activePlay.getName() + " vs " + activeDefense;
            playLog.addMessage("AI: " + activePlay.getName());
        }
        currentPlayStage = 0;
        resultPause = 0;
        applyCurrentPlayStage();
    }

    private void applyCurrentPlayStage() {
        Team offense = userTurn ? userTeam : aiTeam;
        Team defense = userTurn ? aiTeam : userTeam;
        boolean attackingRight = userTurn;

        playManager.setupPlayStage(activePlay, offense, ball, attackingRight, currentPlayStage);

        if (userTurn) {
            // The AI plays mostly man-to-man against the user's play so the defenders actually guard.
            setDefensiveTargets(defense, offense, attackingRight, "Man-to-Man", currentPlayStage);
        } else {
            // The user's chosen defense now moves to the correct side of the court and shows the scheme.
            setDefensiveTargets(defense, offense, attackingRight, activeDefense, currentPlayStage);
        }

        resultMessage = getStageMessage(activePlay, currentPlayStage, userTurn);
    }

    private String getStageMessage(Play play, int stage, boolean userOnOffense) {
        String teamName = userOnOffense ? userTeam.getTeamName() : "AI";
        if (play.getName().equals("Pick and Roll")) {
            if (stage == 0) return teamName + ": screener walks up to set the pick";
            if (stage == 1) return teamName + ": ball handler uses the screen";
            return teamName + ": roller dives to the rim";
        }
        if (play.getName().equals("Isolation Drive")) {
            if (stage == 0) return teamName + ": teammates clear out";
            if (stage == 1) return teamName + ": scorer attacks downhill";
            return teamName + ": drive finishes at the rim";
        }
        if (play.getName().equals("Kick-Out Three")) {
            if (stage == 0) return teamName + ": floor is spaced";
            if (stage == 1) return teamName + ": drive collapses the defense";
            return teamName + ": kick-out pass to shooter";
        }
        if (play.getName().equals("Post Up")) {
            if (stage == 0) return teamName + ": post player seals on the block";
            if (stage == 1) return teamName + ": entry pass goes inside";
            return teamName + ": post player turns to score";
        }
        return teamName + ": " + play.getName();
    }

    /**
     * Moves the defensive team to realistic targets based on the offense and the chosen scheme.
     * Even in zone/paint/perimeter defenses, the defenders still react to the AI players' cuts,
     * passes, and rolls instead of standing still in one spot.
     */
    private void setDefensiveTargets(Team defense, Team offense, boolean offenseAttackingRight, String scheme, int stage) {
        if (scheme == null || scheme.equals("Man-to-Man")) {
            setManDefense(defense, offense, offenseAttackingRight);
            return;
        }
        setSchemeDefense(defense, offense, offenseAttackingRight, scheme, stage);
    }

    private void setManDefense(Team defense, Team offense, boolean offenseAttackingRight) {
        ArrayList<Player> defenders = defense.getStartingFive();
        ArrayList<Player> offensivePlayers = offense.getStartingFive();
        int offset = offenseAttackingRight ? 42 : -42;

        for (int i = 0; i < defenders.size(); i++) {
            Player defender = defenders.get(i);
            Player offensivePlayer = offensivePlayers.get(i % offensivePlayers.size());
            int tx = offensivePlayer.getTargetX() + offset;
            int ty = offensivePlayer.getTargetY();
            defender.setTarget(clampX(tx), clampY(ty));
            defender.setMoveSpeed(Player.NORMAL_SPEED);
        }
    }

    /**
     * Scheme defense is not just five fixed dots. The defenders match the AI movement,
     * but their positions are adjusted based on the chosen scheme.
     */
    private void setSchemeDefense(Team defense, Team offense, boolean offenseAttackingRight, String scheme, int stage) {
        ArrayList<Player> defenders = defense.getStartingFive();
        ArrayList<Player> offensivePlayers = offense.getStartingFive();
        int speed = stage == 0 ? Player.SPRINT_SPEED : Player.NORMAL_SPEED;

        if (scheme.equals("Zone Defense (2-3)")) {
            // A 2-3 zone should look like two guards up top and three players across
            // the back line. It should not look like five random man-to-man matchups.
            // When the offense drives/post-ups, the zone collapses toward the key.
            int[][] zoneTargets = getTwoThreeZoneTargets(offenseAttackingRight, stage);
            for (int i = 0; i < defenders.size() && i < zoneTargets.length; i++) {
                Player defender = defenders.get(i);
                defender.setTarget(zoneTargets[i][0], zoneTargets[i][1]);
                defender.setMoveSpeed(speed);
            }
            return;
        }

        int betweenOffset = offenseAttackingRight ? 38 : -38;

        for (int i = 0; i < defenders.size(); i++) {
            Player defender = defenders.get(i);
            Player offensivePlayer = offensivePlayers.get(i % offensivePlayers.size());
            int ox = offensivePlayer.getTargetX();
            int oy = offensivePlayer.getTargetY();

            int tx = ox + betweenOffset;
            int ty = oy;

            if (scheme.equals("Protect Paint")) {
                // Paint defense sends help toward the rim, so it follows cutters but collapses inside.
                int paintX = offenseAttackingRight ? COURT_X + COURT_W - 115 : COURT_X + 115;
                tx = (tx + paintX) / 2;
                ty = (ty + HOOP_Y) / 2;
            } else if (scheme.equals("Guard Perimeter")) {
                // Perimeter defense chases shooters tighter and stays higher/wider.
                int perimeterOffset = offenseAttackingRight ? 28 : -28;
                tx = ox + perimeterOffset;
                if (oy < HOOP_Y) {
                    ty = oy - 18;
                } else if (oy > HOOP_Y) {
                    ty = oy + 18;
                }
            }

            defender.setTarget(clampX(tx), clampY(ty));
            defender.setMoveSpeed(speed);
        }
    }

    /**
     * Returns clean 2-3 zone spots. The first two defenders form the top line,
     * and the last three form the back line. For rim attacks, the whole zone
     * shrinks toward the paint to make it look harder to get the ball inside.
     */
    private int[][] getTwoThreeZoneTargets(boolean offenseAttackingRight, int stage) {
        boolean collapse = stage >= 1 && activeShotType != null && !activeShotType.equals("three");
        int[][] rightSide;

        if (collapse) {
            // Two up top, three collapsed around the lane/key.
            rightSide = new int[][] {
                {560, 305}, {560, 395},
                {655, 285}, {670, 350}, {655, 415}
            };
        } else if (stage >= 1 && activePlay != null && activePlay.getName().equals("Kick-Out Three")) {
            // Start collapsed from the drive, but begin recovering outward late.
            rightSide = new int[][] {
                {535, 275}, {535, 425},
                {640, 225}, {675, 350}, {640, 475}
            };
        } else {
            // Standard 2-3 shape: two guards near the elbows/top, three across the baseline.
            rightSide = new int[][] {
                {545, 275}, {545, 425},
                {675, 230}, {690, 350}, {675, 470}
            };
        }

        if (offenseAttackingRight) {
            return rightSide;
        }

        int[][] mirrored = new int[rightSide.length][2];
        for (int i = 0; i < rightSide.length; i++) {
            mirrored[i][0] = COURT_X + COURT_W - (rightSide[i][0] - COURT_X);
            mirrored[i][1] = rightSide[i][1];
        }
        return mirrored;
    }

    private int clampX(int x) {
        return Math.max(COURT_X + 35, Math.min(COURT_X + COURT_W - 35, x));
    }

    private int clampY(int y) {
        return Math.max(COURT_Y + 35, Math.min(COURT_Y + COURT_H - 35, y));
    }

    private void finishShotAnimation() {
        int modifier = 0;
        Team scoringTeam = userTurn ? userTeam : aiTeam;
        if (!userTurn) {
            modifier = defenseManager.getShotModifier(activeShotType);
        }

        // Use the player who actually has the ball at the end of the animation.
        // This fixes the old bug where the log could say the passer scored.
        Player ballHolder = ball.getCurrentHolder();
        if (ballHolder != null) {
            activeShooter = ballHolder;
        }

        boolean made = ShotCalculator.shotMade(activeShooter, activeShotType, modifier);
        int points = ShotCalculator.getPointValue(activeShotType);
        activeShooter.recordShot(made);
        scoringTeam.addShotAttempt(made);

        if (made) {
            scoringTeam.addScore(points);
            activeShooter.addPoints(points);

            Player passer = ball.getLastPasser();
            if (passer != null && passer != activeShooter) {
                passer.addAssist();
                playLog.addMessage(passer.getName() + " assisted " + activeShooter.getName());
            }

            ball.shootToHoop(userTurn ? RIGHT_HOOP_X : LEFT_HOOP_X, HOOP_Y);
            resultMessage = activeShooter.getName() + " made a " + points + " pointer!";
            playLog.addMessage(activeShooter.getName() + " made " + points + " pts");
        } else {
            // Miss animation bounces the ball near the rim.
            int missX = (userTurn ? RIGHT_HOOP_X : LEFT_HOOP_X) + (userTurn ? -35 : 35);
            ball.setTarget(missX, HOOP_Y - 45);
            Player rebounder = chooseRebounderAfterMiss(scoringTeam);
            if (rebounder != null) {
                rebounder.addRebound();
                playLog.addMessage(rebounder.getName() + " grabbed the rebound");
            }
            resultMessage = activeShooter.getName() + " missed.";
            playLog.addMessage(activeShooter.getName() + " missed");
        }
        ball.clearLastPasser();
    }

    private Player chooseRebounderAfterMiss(Team shootingTeam) {
        Team defendingTeam = shootingTeam == userTeam ? aiTeam : userTeam;
        Team reboundTeam = Math.random() < 0.70 ? defendingTeam : shootingTeam;
        return reboundTeam.getRandomPlayer();
    }

    private void finishPossessionAfterPause() {
        animationRunning = false;
        scoreboard.nextPossession();

        if (overtime && userTeam.getScore() != aiTeam.getScore()) {
            endGame();
            return;
        }

        if (userTurn) {
            state = "WAITING_DEFENSE";
            resultMessage = "Choose your defense for the AI possession.";
            showDefenseButtons(true);
            showPlayButtons(false);
            if (aiTeam.getStartingFive().size() > 0) {
                ball.setHolder(playManager.getBallHandler(aiTeam));
            }
        } else {
            state = "USER_TRANSITION";
            resultMessage = "Your team pushes back into offense.";
            showPlayButtons(false);
            showDefenseButtons(false);
            setTransitionTargetsForUserOffense();
        }
    }

    public void checkGameOver() {
        if (state.equals("GAME_OVER")) {
            return;
        }
        if (timeLeft <= 0 && !overtime) {
            if (userTeam.getScore() == aiTeam.getScore()) {
                overtime = true;
                resultMessage = "Sudden-death overtime: next score wins!";
                playLog.addMessage("Overtime begins");
            } else {
                endGame();
            }
        }
    }

    private void endGame() {
        state = "GAME_OVER";
        showPlayButtons(false);
        showDefenseButtons(false);
        animTimer.stop();
        window.showPostGamePanel(userTeam, aiTeam);
    }
    
    private boolean allPlayersAtTarget() {
        for (Player p : allCourtPlayers) {
            if (!p.isAtTarget()) {
                return false;
            }
        }
        return true;
    }

    private class PlayButtonListener implements ActionListener {
        private Play play;

        public PlayButtonListener(Play play) {
            this.play = play;
        }

        public void actionPerformed(ActionEvent e) {
            runUserPossession(play);
        }
    }

    private class DefenseButtonListener implements ActionListener {
        private String defense;

        public DefenseButtonListener(String defense) {
            this.defense = defense;
        }

        public void actionPerformed(ActionEvent e) {
            if (state.equals("WAITING_DEFENSE")) {
                runAIPossession(defense);
            }
        }
    }
}
public void showResult(String message) {
    GameState.setCurrentMessage(message);
    Timer clear = new Timer(1200, e -> {
        GameState.setCurrentMessage("");
        ((Timer)e.getSource()).stop();
    });
    clear.setRepeats(false);
    clear.start();
}



