import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * Main gameplay panel.
 * Connects the logic of the game with the rest of the classes
 */
public class GamePanel extends JPanel implements ActionListener {
    public static int courtX = 30;
    public static int courtY = 100;
    public static int courtW = 740;
    public static int courtH = 500;

    private int rightHoopX = courtX + courtW - 35;
    private int leftHoopX = courtX + 35;
    private int hoopY = courtY + courtH / 2;

    // One game is 12 minutes we made the timer decrement by 6 seconds to make it go by quicker
    private int startTime = 720;
    private int clockDecrement = 6;
    //frame delay is 16 ms so fps is 62.5 or about 60fps
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

    /**
     * Constructs the Game Panel using the window, the username, the user's team and the ai's team.
     * Creates all of the instances for the object
     * Sets up the field and the buttons
     * Lastly it also sets up the Timer and PlayLog
     * @param window the window
     * @param userName username the player entered
     * @param userTeam the team of user
     * @param aiTeam the team of ai
     */
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
    /**
     * Puts all the players in their starting positions
     */
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
     * 
     * @return a two dimensional array of the user's 5 player's positions (x, y)
     */
    private int[][] getUserBasePositions() {
        return new int[][] {{190,350},{300,185},{300,515},{230,255},{230,445}};
    }

    /**
     * 
     * @return a two dimensional array of the AI's 5 player's positions (x, y)
     */
    private int[][] getAIBasePositions() {
        return new int[][] {{610,350},{500,185},{500,515},{570,255},{570,445}};
    }

    /**
     * This transitions the offense to AI after the user makes their play
     */
    private void transitionToAIOffense() {
        setTargetsFromArray(userTeam, getUserBasePositions(), Player.fastSpeed);
        setTargetsFromArray(aiTeam, getAIBasePositions(), Player.fastSpeed);
        ball.setHolder(playManager.getBallHandler(aiTeam));
    }
    /**
     * Returns both teams to normal position for user offense.
     */
    private void setTargetsForUserOffense() {
        setTargetsFromArray(userTeam, getUserBasePositions(), Player.fastSpeed);
        setTargetsFromArray(aiTeam, getAIBasePositions(), Player.fastSpeed);
        ball.setHolder(playManager.getBallHandler(userTeam));
    }
    /**
     * Sets the targets for all the players on the team
     * @param team the team
     * @param targets target positions
     * @param speed speed to move
     */
    private void setTargetsFromArray(Team team, int[][] targets, int speed) {
        ArrayList<Player> players = team.getStartingFive();
        for (int i = 0; i < players.size() && i < targets.length; i++) {
            Player p = players.get(i);
            p.setTarget(targets[i][0], targets[i][1]);
            p.setMoveSpeed(speed);
        }
    }
    /**
     * Draws the court, players, ball, scoreboard, playlog, sidepanels and result message.
     * @param g swing graphics
     */
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
    /**
     * Draws the side panel text
     * @param g swing graphics
     */
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
    /**
     * Draws the result message
     * @param g swing graphics
     */
    private void drawResultMessage(Graphics g) {
        if (resultMessage != null && resultMessage.length() > 0) {
            g.setColor(new Color(255, 255, 255, 210));
            g.fillRoundRect(190, 36, 480, 45, 15, 15);
            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.BOLD, 16));
            g.drawString(resultMessage, 210, 64);
        }
    }
    /**
     * Updates the game for every frame
     * Call updateGame() which essentially updates the positions of everything.
     * Calls repaint() afterwards to make all of the changes show up.
     */
    public void actionPerformed(ActionEvent e) {
        updateGame();
        repaint();
    }
    /**
     * Updates the game for every frame.
     * Updates the clock and moves the players and ball
     * Checks the state of the game for if its ai's transition, user's transition and if game is still animating
     * Checks if game is over
     */
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
    /**
     * updates the clock based off frameDelay(16ms)
     */
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
    /**
     * Draws all the players
     * @param g swing graphics
     */
    public void drawPlayers(Graphics g) {
        for (Player p : allCourtPlayers) {
            p.draw(g);
        }
    }
    /**
     * Looks for the play then runs the play for user
     * @param playName 
     */
    public void runUserPossession(String playName) {
        Play play = playManager.getPlayByName(playName);
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
    /**
     * Looks for the play then runs the play for AI
     * @param defenseName
     */
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
    /**
     * Begins the play animation
     * @param userOnOffense whether or not user is on offense
     */
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
    /**
     * Depending on the state of the game, we set up the players into the right positions for the play
     * 
     * We then update the resulting message
     */
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
            setDefensiveTargets(defense, offense, attackingRight, "Man-to-Man", currentPlayStage);
        } else {
            setDefensiveTargets(defense, offense, attackingRight, activeDefense, currentPlayStage);
        }

        resultMessage = getStageMessage(activePlay, currentPlayStage, userTurn);
    }
    /**
     * Kind of like an announcer that announces what is going on in the game
     * @param play the play being ran
     * @param stage the index of the stage
     * @param userOnOffense whether the user is on offense
     * @return the message for what is going on
     */
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
     * Moves the defensive team to guard the offensive team making plays
     * @param defense defensive team
     * @param offense offensive team
     * @param offenseAttackingRight if the offensive team is going right
     * @param modifier the defense modifier
     * @param stage the stage of the game.
     */
    private void setDefensiveTargets(Team defense, Team offense, boolean offenseAttackingRight, String modifier, int stage) {
        if (modifier.equals("Man-to-Man")) {
            setManDefense(defense, offense, offenseAttackingRight);
            return;
        }
        setModifierDefense(defense, offense, offenseAttackingRight, modifier, stage);
    }
    /**
     * In basketball, a player usually has an assinged player that they have to guard.
     * This class uses the offset to make the defensive team set up each of the players to guard someone in the opposing team.
     * 
     * @param defense defensive team
     * @param offense offensive team
     * @param offenseAttackingRight whether the offense is attacking to the right.
     */ 
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
     * Modifier defense is not just five fixed dots. The defenders match the AI movement,
     * but their positions are adjusted based on the chosen modifier.
     */
    /**
     * Setups the defense for the user based off the modifier they chose.
     * Similar to setManDefense where the user's players will try to guard an assigned player 
     * but is slightly different based off modifier
     * 
     * @param defense defensive team
     * @param offense offensive team
     * @param offenseAttackingRight whether the offense is attacking right
     * @param modifier defense modifier
     * @param stage the stage of the game
     */
    private void setModifierDefense(Team defense, Team offense, boolean offenseAttackingRight, String modifier, int stage) {
        ArrayList<Player> defenders = defense.getStartingFive();
        ArrayList<Player> offensivePlayers = offense.getStartingFive();
        int speed;
        if (stage == 0) {
            speed = Player.fastSpeed;
        } else {
            speed = Player.normalSpeed;
        }

        if (modifier.equals("Zone Defense")) {
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

            if (modifier.equals("Protect Paint")) {
                int paintX;
                if (offenseAttackingRight) {
                    paintX = courtX + courtW - 115;
                } else {
                    paintX = courtX + 115;
                }
                targetX = (targetX + paintX) / 2;
                targetY = (targetY + hoopY) / 2;
            } else if (modifier.equals("Guard Perimeter")) {
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
     * Sets up the two three zone, and when offense try to get into the paint
     * The defense will collapse making it harder to score
     * The two three zone is where two defender are on top and three defenders at the bottom
     * 
     * @param offenseAttackingRight whether the offense is attacking the right
     * @param stage stage of the game
     * @return a two dimensional array of size 5 of the target positions(x,y) for the zone defenders
     */
    private int[][] getTwoThreeZoneTargets(boolean offenseAttackingRight, int stage) {
        boolean collapse = false;
        if (stage >= 1 && activeShotType != null && !activeShotType.equals("three")) {
            collapse = true;
        }
        int[][] rightSide;

        if (collapse) {
            rightSide = new int[][] {{560, 305}, {560, 395}, {655, 285}, {670, 350}, {655, 415}};
        } else if (stage >= 1 && activePlay != null && activePlay.getName().equals("Kick-Out Three")) {
            rightSide = new int[][] {{535, 275}, {535, 425}, {640, 225}, {675, 350}, {640, 475}};
        } else {
            rightSide = new int[][] {{545, 275}, {545, 425},{675, 230}, {690, 350}, {675, 470}};
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
    /**
     * makes sure the players are in bounds
     * @param x
     * @return the x coordiante so that it is in bounds
     */
    private int clampX(int x) {
        return Math.max(courtX + 35, Math.min(courtX + courtW - 35, x));
    }
    /**
     * makes sure the players are in bounds
     * @param y
     * @return the y coordiante so that it is in bounds
     */
    private int clampY(int y) {
        return Math.max(courtY + 35, Math.min(courtY + courtH - 35, y));
    }
    /**
     * This method is for the shot at the end of the play
     * The ball can either make where it awards the points and puts the ball at the rim or
     * The ball can miss where we show that the ball misses
     */
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
    /**
     * Chooses who gets the rebound if the shot is missed
     * 70% chance that the team defending get rebound
     * @param shootingTeam
     * @return player who gets rebound
     */
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
    /**
     * Moves the possession of the ball to the next team
     * Checks for overtime
     * Sets up the buttons as well
     */
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
    /**
     * Checks whether the game is over by looking at the timeLeft and if the game went into overtime.
     * 
     */
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
    /**
     * Ends the game by removing buttons, stopping timer and showing the post game panel of player stats during the game.
     */
    private void endGame() {
        stateOfGame = "gameover";
        buttons.showPlayButtons(false);
        buttons.showDefenseButtons(false);
        animTimer.stop();
        window.showPostGamePanel(userTeam, aiTeam);
    }
    /**
     * Checks if all the players are at target
     * @return true/false if the players are all at target
     */
    private boolean isAllPlayersAtTarget() {
        for (Player p : allCourtPlayers) {
            if (!p.isAtTarget()) {
                return false;
            }
        }
        return true;
    }
}





