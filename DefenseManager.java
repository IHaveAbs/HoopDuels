import java.util.ArrayList;

/**
 * Stores defensive strategies and returns shot modifiers for each strategy.
 */
public class DefenseManager {
    private ArrayList<String> defensiveOptions;
    private String selectedDefense;

    public DefenseManager() {
        defensiveOptions = new ArrayList<String>();
        defensiveOptions.add("Man-to-Man");
        defensiveOptions.add("Zone Defense");
        defensiveOptions.add("Protect Paint");
        defensiveOptions.add("Guard Perimeter");
        selectedDefense = "Man-to-Man";
    }
    public void setDefense(String defenseName) {
        if (defensiveOptions.contains(defenseName)) {
            selectedDefense = defenseName;
        }
    }
    /**
     * Returns a percentage-point modifier for the AI shot chance.
     * Negative values make it harder for the AI to score.
     */
    public int getShotModifier(String shotType) {
        if (selectedDefense.equals("Man-to-Man")) {
            return 0;
        }
        if (selectedDefense.equals("Zone Defense")) {
            if (shotType.equals("rim")) {
                return -10;
            }
            if (shotType.equals("mid")) {
                return -5;
            }
            if (shotType.equals("three")) {
                return 10;
            }
        }
        if (selectedDefense.equals("Protect Paint")) {
            if (shotType.equals("rim")) {
                return -15;
            }
            if (shotType.equals("mid")) {
                return 0;
            }
            if (shotType.equals("three")) {
                return 15;
            }
        }
        if (selectedDefense.equals("Guard Perimeter")) {
            if (shotType.equals("rim")) {
                return 10;
            }
            if (shotType.equals("mid")) {
                return -5;
            }
            if (shotType.equals("three")) {
                return -15;
            }
        }
        return 0;
    }

    public ArrayList<String> getDefensiveOptions() {
        return defensiveOptions;
    }
}