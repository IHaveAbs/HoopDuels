/**
 * This is a utility class that calculates whether a shot is made or missed.
 */
public class ShotCalculator {
    /**
     * checks if the shot goes in
     * @param shooter the shooter
     * @param shotType the shot type
     * @param modifier the modifier
     * @return if the shot went in
     */
    public static boolean checkIfShotGoesIn(Player shooter, String shotType, int modifier) {
        int baseChance = getMakeChance(shooter, shotType);
        int temp = applyDefenseModifier(baseChance, modifier);
        int temp1 = (int)(Math.random() * 100)+1;
        if (temp1 <= temp) {
            return true;
        }
        return false;
    }
    /**
     * Gets the chance of making the shot
     * @param shooter the shooter
     * @param shotType the shot type
     * @return gets the chance of making the shot
     */
    public static int getMakeChance(Player shooter, String shotType) {
        return shooter.getShotChance(shotType);
    }
    /**
     * applies the defense modifier
     * essentially just adds them
     * @param baseChance the base chance 
     * @param modifier the modifier
     * @return the final chance
     */
    public static int applyDefenseModifier(int baseChance, int modifier) {
        int temp = baseChance + modifier;
        if (temp < 5) {
            temp = 5;
        }
        if (temp > 95) {
            temp = 95;
        }
        return temp;
    }
    /**
     * gets the points value whether its a three or any other shot
     * @param shotType
     * @return the point value
     */
    public static int getPointValue(String shotType) {
        if (shotType.equals("three")) {
            return 3;
        }
        return 2;
    }
}