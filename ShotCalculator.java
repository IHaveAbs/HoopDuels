/**
 * This is a utility class that calculates whether a shot is made or missed.
 */
public class ShotCalculator {
    public static boolean checkIfShotGoesIn(Player shooter, String shotType, int modifier) {
        int baseChance = getMakeChance(shooter, shotType);
        int temp = applyDefenseModifier(baseChance, modifier);
        int temp1 = (int)(Math.random() * 100)+1;
        if (temp1 <= temp) {
            return true;
        }
        return false;
    }
    public static int getMakeChance(Player shooter, String shotType) {
        return shooter.getShotChance(shotType);
    }
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
    public static int getPointValue(String shotType) {
        if (shotType.equals("three")) {
            return 3;
        }
        return 2;
    }
}