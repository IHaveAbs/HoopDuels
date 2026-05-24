/**
 * Utility class that calculates whether a shot is made or missed.
 */
public class ShotCalculator {
    private ShotCalculator() {
        // Utility class; no objects needed.
    }

    public static boolean shotMade(Player shooter, String shotType, int defenseModifier) {
        int baseChance = getBaseChance(shooter, shotType);
        int finalChance = applyDefenseModifier(baseChance, defenseModifier);
        int roll = (int) (Math.random() * 100) + 1;
        return roll <= finalChance;
    }

    public static int getBaseChance(Player shooter, String shotType) {
        if (shooter == null) {
            return 50;
        }
        return shooter.getShotChance(shotType);
    }

    public static int applyDefenseModifier(int baseChance, int modifier) {
        int finalChance = baseChance + modifier;
        if (finalChance < 5) {
            finalChance = 5;
        }
        if (finalChance > 95) {
            finalChance = 95;
        }
        return finalChance;
    }

    public static int getPointValue(String shotType) {
        if ("three".equals(shotType)) {
            return 3;
        }
        return 2;
    }
}