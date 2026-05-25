/**
 * Central settings file for HoopDuel.
 *
 * Every tunable number, court coordinate, base position, defensive offset,
 * and play-stage layout in the game lives here. Change a value in this file
 * and the whole game picks it up — no other file holds magic numbers.
 *
 * Sections:
 *   1. Window
 *   2. Timing
 *   3. Court geometry
 *   4. Player and Ball
 *   5. Side panel (button layout)
 *   6. Base positions (where each team lines up between possessions)
 *   7. Defense tuning (man, paint help, perimeter, zone)
 *   8. Plays (final coordinates + per-stage choreography)
 *   9. Misc
 */
public final class GameConfig {
    private GameConfig() {}

    // ===== 1. Window =====
    public static final int WINDOW_W = 1100;
    public static final int WINDOW_H = 720;
    public static final String WINDOW_TITLE = "HoopDuel";

    // ===== 2. Timing =====
    // 720 game-seconds = one 12-minute quarter. We decrement by 6 each real second
    // so a full game takes about two real minutes.
    public static final int START_TIME          = 720;
    public static final int CLOCK_DECREMENT     = 6;
    public static final int FRAME_DELAY_MS      = 16;   // ~62 fps
    public static final int RESULT_PAUSE_FRAMES = 70;   // ~1.1s pause after each shot

    // ===== 3. Court geometry (pixels) =====
    public static final int COURT_X = 30;
    public static final int COURT_Y = 100;
    public static final int COURT_W = 770;
    public static final int COURT_H = 500;
    public static final int RIGHT_HOOP_X = COURT_X + COURT_W - 35;
    public static final int LEFT_HOOP_X  = COURT_X + 35;
    public static final int HOOP_Y       = COURT_Y + COURT_H / 2;
    public static final int CLAMP_MARGIN = 35;

    // ===== 4. Player and Ball =====
    public static final int PLAYER_SIZE         = 46;
    public static final int PLAYER_NORMAL_SPEED = 4;
    public static final int PLAYER_SLOW_SPEED   = 2;
    public static final int PLAYER_SPRINT_SPEED = 8;

    public static final int BALL_SIZE  = 16;
    public static final int BALL_SPEED = 8;

    // ===== 5. Side panel (buttons) =====
    public static final int SIDE_BUTTON_X       = 810;
    public static final int SIDE_BUTTON_Y       = 90;
    public static final int SIDE_BUTTON_W       = 250;
    public static final int SIDE_BUTTON_H       = 35;
    public static final int SIDE_BUTTON_SPACING = 45;

    // ===== Scoreboard (top of window) =====
    public static final int SCOREBOARD_X      = 30;
    public static final int SCOREBOARD_Y      = 10;
    public static final int SCOREBOARD_W      = 770;
    public static final int SCOREBOARD_H      = 40;
    public static final int SCOREBOARD_TEXT_Y = 35;

    // ===== Result message banner =====
    // Drawn during gameplay to show the current play description, shot result,
    // or transition prompt. Sits between the scoreboard and the court top.
    public static final int MESSAGE_BANNER_X = 190;
    public static final int MESSAGE_BANNER_Y = 55;
    public static final int MESSAGE_BANNER_W = 480;
    public static final int MESSAGE_BANNER_H = 40;

    // ===== Defense hint text (only shown when WAITING_DEFENSE) =====
    // Lives below the play log (which ends at y=535) and above the message
    // banner (which starts at y=610), so the three lines must fit in 535..610.
    public static final int DEFENSE_HINT_X       = 815;
    public static final int DEFENSE_HINT_Y_START = 555;
    public static final int DEFENSE_HINT_Y_STEP  = 18;

    // ===== 6. Base positions (role order: PG, SG, SF, PF, C) =====
    // User team always lines up on the left side and attacks the right hoop.
    // AI team always lines up on the right and attacks the left hoop.
    public static final int[][] USER_BASE_POSITIONS =
        {{190,350},{300,185},{300,515},{230,255},{230,445}};
    public static final int[][] AI_BASE_POSITIONS =
        {{610,350},{500,185},{500,515},{570,255},{570,445}};

    // ===== 7. Defense tuning =====
    public static final int MAN_DEFENSE_GAP    = 42;  // gap between man defender and offender
    public static final int SCHEME_DEFENSE_GAP = 38;  // base gap for scheme defenders
    public static final int PAINT_HELP_INSET   = 115; // how far inside the basket the paint help spot sits
    public static final int PERIMETER_GAP      = 28;  // tighter gap when chasing shooters
    public static final int PERIMETER_LIFT     = 18;  // vertical lift when defender is above/below rim

    // 2-3 zone target spots (right-side; mirrored when AI attacks left).
    public static final int[][] ZONE_STANDARD = {
        {545, 275}, {545, 425},
        {675, 230}, {690, 350}, {675, 470}
    };
    public static final int[][] ZONE_COLLAPSED = {
        {560, 305}, {560, 395},
        {655, 285}, {670, 350}, {655, 415}
    };
    public static final int[][] ZONE_KICKOUT_RECOVER = {
        {535, 275}, {535, 425},
        {640, 225}, {675, 350}, {640, 475}
    };

    // ===== 8. Plays =====
    // All coordinates authored for a team attacking the right hoop.
    // PlayManager.mirrorX() flips them when the team attacks the left.
    // Within each row of 5, order is: PG, SG, SF, PF, C.
    public static final int STAGES_PER_PLAY = 3;

    public static final int[][] PICK_AND_ROLL_FINAL =
        {{430,315},{590,185},{620,500},{500,150},{665,350}};
    public static final int[][][] PICK_AND_ROLL_STAGES = {
        {{260,350},{590,180},{610,515},{475,155},{335,350}}, // 0: setup, screener walks up
        {{430,310},{620,175},{630,520},{500,155},{335,350}}, // 1: PG uses the screen
        {{470,250},{620,175},{630,520},{500,155},{665,350}}, // 2: roll man dives, PG drifts to wing
    };

    public static final int[][] ISOLATION_FINAL =
        {{675,350},{620,150},{620,520},{430,155},{430,520}};
    public static final int[][][] ISOLATION_STAGES = {
        {{445,185},{645,180},{355,350},{445,515},{645,520}}, // 0: clear out, scorer up top
        {{445,185},{645,180},{535,350},{445,515},{645,520}}, // 1: scorer attacks the middle
        {{445,185},{645,180},{690,350},{445,515},{645,520}}, // 2: drive to the rim
    };

    public static final int[][] KICK_OUT_FINAL =
        {{520,350},{670,175},{630,520},{430,165},{430,520}};
    public static final int[][][] KICK_OUT_STAGES = {
        {{375,250},{670,175},{355,350},{455,520},{610,520}}, // 0: spread floor
        {{375,250},{670,175},{545,350},{455,520},{610,520}}, // 1: paint touch
        {{395,290},{680,175},{520,385},{455,520},{610,520}}, // 2: kickout to shooter
    };

    public static final int[][] POST_UP_FINAL =
        {{430,270},{615,155},{615,520},{475,500},{675,365}};
    public static final int[][][] POST_UP_STAGES = {
        {{390,260},{545,245},{615,520},{455,500},{635,385}}, // 0: center seals on the block
        {{405,270},{555,245},{625,520},{470,500},{665,370}}, // 1: entry pass
        {{405,270},{555,245},{625,520},{470,500},{700,350}}, // 2: post player turns to score
    };

    // Generic fallback used if a play name is unknown.
    public static final int[][] FALLBACK_STAGE =
        {{300,350},{610,160},{610,520},{450,160},{450,520}};

    // ===== 9. Misc =====
    public static final double REBOUND_DEFENSE_PROBABILITY = 0.70;
}
