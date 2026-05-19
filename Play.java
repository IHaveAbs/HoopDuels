/**
 * Stores the information for one offensive play.
 * A play has a name, a shot type, target positions for five players,
 * and a preferred shooter position.
 */
public class Play {
    private String name;
    private String shotType;
    private int[][] playerTargets;
    private String shooterPosition;

    public Play(String name, String shotType, int[][] playerTargets, String shooterPosition) {
        this.name = name;
        this.shotType = shotType;
        this.playerTargets = playerTargets;
        this.shooterPosition = shooterPosition;
    }

    public String getName() {
        return name;
    }

    public String getShotType() {
        return shotType;
    }

    public int[][] getPlayerTargets() {
        return playerTargets;
    }

    public String getShooterPosition() {
        return shooterPosition;
    }

    public String toString() {
        return name;
    }
}