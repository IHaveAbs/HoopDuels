import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * Main gameplay panel. Draws the court and runs the turn-based possession loop.
 */
public class GamePanel extends JPanel implements ActionListener {
    public static int courtX = 30;
    public static int courtY = 100;
    public static int courtW = 740;
    public static int courtH = 500;

    private int rightHoopX = courtX + courtW - 35;
    private int leftHoopX = courtX + 35;
    private int hoopY = courtY + courtH / 2;

    // 720 seconds = one 12-minute quarter. We decrease by 6 each real second for demo speed.
    private int startTime = 720;
    private int clockDecrement = 6;
    private int frameDelay = 16;
    private int pauseTimeInFrames = 70;

    private HoopDuels window;
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
    private String stateOfGame;
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
    private Court court;
    private Button buttons;

    private ArrayList<Player> allCourtPlayers;

    public GamePanel(HoopDuels window, String userName, Team userTeam, Team aiTeam) {
        this.window = window;
        this.userTeam = userTeam;
        this.aiTeam = aiTeam;

        setLayout(null);
        setBackground(new Color(230, 238, 240));
        setFocusable(true);

        playManager = new PlayManager();
        defenseManager = new DefenseManager();
        aiManager = new AIManager(aiTeam, playManager.getAvailablePlays());
        playLog = new PlayLog();
        timeLeft = startTime;
        scoreboard = new ScoreBoard(userTeam, aiTeam, timeLeft);
        ball = new Ball(200, 250);
        allCourtPlayers = new ArrayList<Player>();
        court = new Court();
        buttons = new Button();

        userTurn = true;
        overtime = false;
        stateOfGame = "waitingforuser";
        resultMessage = "Choose an offensive play.";
        resultPause = 0;
        clockCounter = 0;
        activeDefense = "Man-to-Man";
        currentPlayStage = 0;
        totalPlayStages = 0;

        setupInitialPositions();
        animation = new Animation(allCourtPlayers, ball);
        buttons.createPlayButtons(this, playManager.getAvailablePlays(), this);
        buttons.createDefenseButtons(this, defenseManager.getDefensiveOptions(), this);
        buttons.showPlayButtons(true);
        buttons.showDefenseButtons(false);  

        animTimer = new Timer(frameDelay, this);
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

    private void transitionToAIOffense() {
        // This fixes the old issue: the user team must sprint back to the left side
        // because the AI attacks the left hoop after the user's possession ends.
        setTargetsFromArray(userTeam, getUserBasePositions(), Player.fastSpeed);
        setTargetsFromArray(aiTeam, getAIBasePositions(), Player.fastSpeed);
        ball.setHolder(playManager.getBallHandler(aiTeam));
    }

    private void setTargetsForUserOffense() {
        setTargetsFromArray(userTeam, getUserBasePositions(), Player.fastSpeed);
        setTargetsFromArray(aiTeam, getAIBasePositions(), Player.fastSpeed);
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

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        court.drawCourt(g);
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
        if (stateOfGame.equals("waitingforuser")) {
            g.drawString("Choose Offensive Play", 815, 75);
        } else if (stateOfGame.equals("waitingfordefense")) {
            g.drawString("Choose Defensive Strategy", 815, 75);
            g.setFont(new Font("Arial", Font.PLAIN, 12));
            g.drawString("Zone: -10 rim, +10 three", 815, 550);
            g.drawString("Paint: -15 rim, +15 three", 815, 570);
            g.drawString("Perimeter: +10 rim, -15 three", 815, 590);
        } else if (stateOfGame.equals("aitransition")) {
            g.drawString("Transition Defense", 815, 75);
        } else if (stateOfGame.equals("usertransition")) {
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
        updateClock();
        animation.moveOnce();

        if (stateOfGame.equals("aitransition")) {
            if (isAllPlayersAtTarget() && !ball.isMoving()) {
                beginPlayAnimation(false);
            }
        } else if (stateOfGame.equals("usertransition")) {
            if (isAllPlayersAtTarget() && !ball.isMoving()) {
                for (Player p : allCourtPlayers) {
                    p.resetMoveSpeed();
                }
                stateOfGame = "waitingforuser";
                resultMessage = "Choose your next offensive play.";
                buttons.showPlayButtons(true);
                buttons.showDefenseButtons(false);
            }
        } else if (stateOfGame.equals("useranimating") || stateOfGame.equals("aianimating")) {
            if (isAllPlayersAtTarget() && !ball.isMoving()) {
                if (currentPlayStage < totalPlayStages - 1) {
                    currentPlayStage++;
                    applyCurrentPlayStage();
                } else {
                    if (resultPause == 0) {
                        finishShotAnimation();
                        resultPause = pauseTimeInFrames;
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
        if (stateOfGame.equals("gameover") || overtime) {
            return;
        }
        clockCounter += frameDelay;
        if (clockCounter >= 1000) {
            clockCounter = 0;
            timeLeft -= clockDecrement;
            if (timeLeft < 0) {
                timeLeft = 0;
            }
            scoreboard.setTimeLeft(timeLeft);
        }
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
        if (!stateOfGame.equals("waitingforuser")) {
            return;
        }
        activePlay = play;
        activeShotType = play.getShot();
        activeShooter = playManager.chooseShooter(play, userTeam);
        userTurn = true;
        currentPlayStage = 0;
        totalPlayStages = playManager.getStageCount(play);
        resultPause = 0;
        buttons.showPlayButtons(false);
        buttons.showDefenseButtons(false);
        stateOfGame = "useranimating";
        resultMessage = userTeam.getTeamName() + " set up " + play.getName();
        playLog.addMessage(userTeam.getTeamName() + ": " + play.getName());
        applyCurrentPlayStage();
    }

    public void runAIPossession(String defenseName) {
        if (!stateOfGame.equals("waitingfordefense")) {
            return;
        }
        defenseManager.setDefense(defenseName);
        activeDefense = defenseName;
        userTurn = false;
        activePlay = aiManager.choosePlay(aiTeam.getScore(), userTeam.getScore(), timeLeft);
        activeShotType = activePlay.getShot();
        activeShooter = aiManager.chooseShooter(aiTeam, activePlay);
        currentPlayStage = 0;
        totalPlayStages = playManager.getStageCount(activePlay);
        resultPause = 0;
        buttons.showDefenseButtons(false);
        buttons.showPlayButtons(false);
        stateOfGame = "aitransition";
        resultMessage = "Sprint back into " + defenseName + "!";
        playLog.addMessage("Defense: " + defenseName);
        transitionToAIOffense();
    }

    private void beginPlayAnimation(boolean userOnOffense) {
        userTurn = userOnOffense;
        if (userTurn) {
            stateOfGame = "useranimating";
            resultMessage = userTeam.getTeamName() + " set up " + activePlay.getName();
        } else {
            stateOfGame = "aianimating";
            resultMessage = "AI runs " + activePlay.getName() + " vs " + activeDefense;
            playLog.addMessage("AI: " + activePlay.getName());
        }
        currentPlayStage = 0;
        resultPause = 0;
        applyCurrentPlayStage();
    }

    private void applyCurrentPlayStage() {
        Team offense;
        if (userTurn) {
            offense = userTeam;
        } else {
            offense = aiTeam;
        }
        Team defense;
        if (userTurn) {
            defense = aiTeam;
        } else {
            defense = userTeam;
        }
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
        Team offense;
        if (userOnOffense) {
            offense = userTeam;
        } else {
            offense = aiTeam;
        }
        String teamName;
        if (userOnOffense) {
            teamName = userTeam.getTeamName();
        } else {
            teamName = "AI";
        }

        Player pg = offense.getStartingFive().get(0);
        Player sg = offense.getStartingFive().get(1);
        Player sf = offense.getStartingFive().get(2);
        Player pf = offense.getStartingFive().get(3);
        Player c = offense.getStartingFive().get(4);

        if (play.getName().equals("Pick and Roll")) {
            if (stage == 0) {
                return c.getName() + " walks up to set a screen for " + pg.getName();
            } 
            if (stage == 1){
                return pg.getName() + " uses " + c.getName() + "'s screen up top";
            }
            return c.getName() + " rolls to the rim for the pass";
        }

        if (play.getName().equals("Isolation Drive")) {
            if (stage == 0) {
                return teamName + " clears out for " + sf.getName();
            }
            if (stage == 1) {
                return sf.getName() + " attacks downhill in isolation";
            }
            return sf.getName() + " drives hard to the rim";
        }

        if (play.getName().equals("Kick-Out Three")) {
            if (stage == 0) {
                return sg.getName() + " spaces to the three-point line";
            }
            if (stage == 1) {
                return sf.getName() + " drives and collapses the defense";
            }
            return sf.getName() + " kicks it out to " + sg.getName() + " for three";
        }

        if (play.getName().equals("Post Up")) {
            if (stage == 0) {
                return c.getName() + " seals his defender on the block";
            }
            if (stage == 1) {
                return sg.getName() + " throws the entry pass to " + c.getName();
            }
            return c.getName() + " turns toward the rim to score";
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
        int offset;
        if (offenseAttackingRight) {
            offset = 42;
        } else {
            offset = -42;
        }

        for (int i = 0; i < defenders.size(); i++) {
            Player defender = defenders.get(i);
            Player offensivePlayer = offensivePlayers.get(i % offensivePlayers.size());
            int targetX = offensivePlayer.getTargetX() + offset;
            int targetY = offensivePlayer.getTargetY();
            defender.setTarget(clampX(targetX), clampY(targetY));
            defender.setMoveSpeed(Player.normalSpeed);
        }
    }

    /**
     * Scheme defense is not just five fixed dots. The defenders match the AI movement,
     * but their positions are adjusted based on the chosen scheme.
     */
    private void setSchemeDefense(Team defense, Team offense, boolean offenseAttackingRight, String scheme, int stage) {
        ArrayList<Player> defenders = defense.getStartingFive();
        ArrayList<Player> offensivePlayers = offense.getStartingFive();
        int speed;
        if (stage == 0) {
            speed = Player.fastSpeed;
        } else {
            speed = Player.normalSpeed;
        }

        if (scheme.equals("Zone Defense")) {
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

        int betweenOffset;
        if (offenseAttackingRight) {
            betweenOffset = 38;
        } else {
            betweenOffset = -38;
        }

        for (int i = 0; i < defenders.size(); i++) {
            Player defender = defenders.get(i);
            Player offensivePlayer = offensivePlayers.get(i % offensivePlayers.size());
            int attackerX = offensivePlayer.getTargetX();
            int attackerY = offensivePlayer.getTargetY();

            int targetX = attackerX + betweenOffset;
            int targetY = attackerY;

            if (scheme.equals("Protect Paint")) {
                // Paint defense sends help toward the rim, so it follows cutters but collapses inside.
                int paintX;
                if (offenseAttackingRight) {
                    paintX = courtX + courtW - 115;
                } else {
                    paintX = courtX + 115;
                }
                targetX = (targetX + paintX) / 2;
                targetY = (targetY + hoopY) / 2;
            } else if (scheme.equals("Guard Perimeter")) {
                // Perimeter defense chases shooters tighter and stays higher/wider.
                int perimeterOffset;
                if (offenseAttackingRight) {
                    perimeterOffset = 28;
                } else {
                    perimeterOffset = -28;
                }
                targetX = attackerX + perimeterOffset;
                if (attackerY < hoopY) {
                    targetY = attackerY - 18;
                } else if (attackerY > hoopY) {
                    targetY = attackerY + 18;
                }
            }

            defender.setTarget(clampX(targetX), clampY(targetY));
            defender.setMoveSpeed(speed);
        }
    }

    /**
     * Returns clean 2-3 zone spots. The first two defenders form the top line,
     * and the last three form the back line. For rim attacks, the whole zone
     * shrinks toward the paint to make it look harder to get the ball inside.
     */
    private int[][] getTwoThreeZoneTargets(boolean offenseAttackingRight, int stage) {
        boolean collapse = false;
        if (stage >= 1 && activeShotType != null && !activeShotType.equals("three")) {
            collapse = true;
        }
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
            mirrored[i][0] = courtX + courtW - (rightSide[i][0] - courtX);
            mirrored[i][1] = rightSide[i][1];
        }
        return mirrored;
    }

    private int clampX(int x) {
        return Math.max(courtX + 35, Math.min(courtX + courtW - 35, x));
    }

    private int clampY(int y) {
        return Math.max(courtY + 35, Math.min(courtY + courtH - 35, y));
    }

    private void finishShotAnimation() {
        int modifier = 0;
        Team scoringTeam;
        if (userTurn) {
            scoringTeam = userTeam;
        } else {
            scoringTeam = aiTeam;
        }
        if (!userTurn) {
            modifier = defenseManager.getShotModifier(activeShotType);
        }

        // Use the player who actually has the ball at the end of the animation.
        // This fixes the old bug where the log could say the passer scored.
        Player ballHolder = ball.getHolder();
        if (ballHolder != null) {
            activeShooter = ballHolder;
        }

        boolean made = ShotCalculator.checkIfShotGoesIn(activeShooter, activeShotType, modifier);
        int points = ShotCalculator.getPointValue(activeShotType);
        activeShooter.recordShot(made);
        scoringTeam.addShot(made);

        if (made) {
            scoringTeam.addScore(points);
            activeShooter.addPoints(points);

            Player passer = ball.getLastPasser();
            if (passer != null && passer != activeShooter) {
                passer.addAssist();
                playLog.addMessage(passer.getName() + " assisted " + activeShooter.getName());
            }

            int shootHoopX;
            if (userTurn) {
                shootHoopX = rightHoopX;
            } else {
                shootHoopX = leftHoopX;
            }
            ball.shootToHoop(shootHoopX, hoopY);
            resultMessage = activeShooter.getName() + " made a " + points + " pointer!";
            playLog.addMessage(activeShooter.getName() + " made " + points + " pts");
        } else {
            // Miss animation bounces the ball near the rim.
            int missHoop;
            if (userTurn) {
                missHoop = rightHoopX;
            } else {
                missHoop = leftHoopX;
            }
            int temp;
            if (userTurn) {
                temp = -35;
            } else {
                temp = 35;
            }
            int missX = missHoop + temp;
            ball.setTarget(missX, hoopY - 45);
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
        Team defendingTeam;
        if (shootingTeam == userTeam) {
            defendingTeam = aiTeam;
        } else {
            defendingTeam = userTeam;
        }
        Team reboundTeam;
        if (Math.random() < 0.70) {
            reboundTeam = defendingTeam;
        } else {
            reboundTeam = shootingTeam;
        }
        return reboundTeam.getRandomPlayer();
    }

    private void finishPossessionAfterPause() {
        scoreboard.nextPossession();

        if (overtime && userTeam.getScore() != aiTeam.getScore()) {
            endGame();
            return;
        }

        if (userTurn) {
            stateOfGame = "waitingfordefense";
            resultMessage = "Choose your defense for the AI possession.";
            buttons.showDefenseButtons(true);
            buttons.showPlayButtons(false);
            if (aiTeam.getStartingFive().size() > 0) {
                ball.setHolder(playManager.getBallHandler(aiTeam));
            }
        } else {
            stateOfGame = "usertransition";
            resultMessage = "Your team pushes back into offense.";
            buttons.showPlayButtons(false);
            buttons.showDefenseButtons(false);
            setTargetsForUserOffense();
        }
    }

    public void checkGameOver() {
        if (stateOfGame.equals("gameover")) {
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
        stateOfGame = "gameover";
        buttons.showPlayButtons(false);
        buttons.showDefenseButtons(false);
        animTimer.stop();
        window.showPostGamePanel(userTeam, aiTeam);
    }
    private boolean isAllPlayersAtTarget() {
        for (Player p : allCourtPlayers) {
            if (!p.isAtTarget()) {
                return false;
            }
        }
        return true;
    }
}





