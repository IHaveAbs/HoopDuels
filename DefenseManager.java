import java.util.ArrayList;

/**
 * Defense manager class for managing the defensive modifiers.
 * Stores defensive strategies and returns shot modifiers for each strategy.
 */
public class DefenseManager {
    private ArrayList<String> defensiveOptions;
    private String selectedDefense;
    /**
     * Constructs the DefenseManager.
     * adds all the defensive options to defensiveOptions
     * default is man to man
     */
    public DefenseManager() {
        defensiveOptions = new ArrayList<String>();
        defensiveOptions.add("Man-to-Man");
        defensiveOptions.add("Zone Defense");
        defensiveOptions.add("Protect Paint");
        defensiveOptions.add("Guard Perimeter");
        selectedDefense = "Man-to-Man";
    }
    /**
     * sets the defense
     * @param defenseName name of defense
     */
    public void setDefense(String defenseName) {
        if (defensiveOptions.contains(defenseName)) {
            selectedDefense = defenseName;
        }
    }
    /**
     * Gets the change in percentage of the shot after the modifier
     * @param shotType the type of shot
     * @return the change in percentage of the shot after the modifier
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
    /**
     * 
     * @return all the defensive options
     */
    public ArrayList<String> getDefensiveOptions() {
        return defensiveOptions;
    }
}