import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.Timer;

public class GamePanel extends JPanel implements ActionListener {
    public static final int COURT_X = 30;
    public static final int COURT_Y = 100;
    public static final int COURT_W = 740;
    public static final int COURT_H = 500;
    private static final int RIGHT_HOOP_X = COURT_X + COURT_W - 35;
    private static final int LEFT_HOOP_X = COURT_X + 35;
    private static final int HOOP_Y = COURT_Y + COURT_H / 2;
    private static final int START_TIME = 720;
    private static final int CLOCK_DECREMENT = 6;
    private static final int FRAME_DELAY = 16;
    private static final int RESULT_PAUSE_FRAMES = 70;
    
    private HoopDuel window;
    private String userName;
    private Team userTeam;
    private Team aiTeam;
    private Ball ball;
    private Scoreboard scoreboard;
    private PlayManager playManager;
    private DefenseManager defenseManager;
    private AIManager aiManager;
    private PlayLog playLog;
    private Timer animTimer;
    private int timeLeft;
    private boolean userTurn;
    private boolean animationRunning;
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
    
    private ArrayList<Player> allCourtPlayers;
    private ArrayList<JButton> playButtons;
    private ArrayList<JButton> defenseButtons;

    public GamePanel(HoopDuel window, String userName, Team userTeam, Team aiTeam) {
        this.window = window;
        this.userName = userName;
        this.userTeam = userTeam;
        this.aiTeam = aiTeam;

        setLayout(null);
        setBackground(new Color(230, 238, 240));
        setFocusable(true);

        PlayManager = new PlayManager();
        defenseManager = new DefenseManager();
        aiManager = new AIManager(aiTeam, playManager.getAvailablePlays());
        playLog = new PlayLog();
        timeLeft = START_TIME;
        scoreboard = new Scoreboard(userTeam, aiTeam, timeLeft);
        ball = new Ball(200, 250);
        allCourtPlayers = new ArrayList<Player>();
        playButtons = new ArrayList<JButton>();
        defenseButtons = new ArrayList<JButton>();
        
        userTurn = true;
        animationRunning = false;
        overtime = false;
        state = "WAITING_USER_PLAY";
        resultMessage = "Choose an offensive play.";
        resultPause = 0;
        clockCounter = 0;
        activeDefense = "Man-to-Man";
        currentPlayStage = 0;
        totalPlayStages = 0;

        
    }
}


