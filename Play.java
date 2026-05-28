/**
 * This is the class for the individual plays in the game.
 * It has field variables for the name of the play,
 * the type of shot, 
 * the target positions for the five players, 
 * and position of the shooter.
 * It also has methods that returns each of these field variables
 */
public class Play {
    private String name;
    private String shotType;
    private int[][] targetPositions;
    private String shooterPosition;

    /**
     * Constructor for Play
     * Constructs Play using name of player, the shot type, the target positions, and the shooter's position
     * 
     * @param name
     * @param shotType
     * @param targetPositions
     * @param shooterPosition
     */
    public Play(String name, String shotType, int[][] targetPositions, String shooterPosition) {
        this.name = name;
        this.shotType = shotType;
        this.targetPositions = targetPositions;
        this.shooterPosition = shooterPosition;
    }
    /**
     * gets the name
     * @return name
     */
    public String getName() {
        return name;
    }
    /**
     * gets the shot type
     * @return shotType
     */
    public String getShot() {
        return shotType;
    }
    /**
     * gets the positions of shooter
     * @return shooterPosition
     */
    public String getShooterPosition() {
        return shooterPosition;
    }
}