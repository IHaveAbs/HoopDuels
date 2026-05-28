import java.awt.Color;

/**
 * Player manager class that manages the player data
 * Utility class
 */
public class PlayerManager {
    /**
     * builds team for warriors
     * @return warriors team
     */
    public static Team buildWarriors() {
        Team t = new Team("Warriors");
        Color c = new Color(255, 210, 40);
        t.addPlayer(new Player("Stephen Curry", "Warriors", "PG", 65, 55, 94, 75, c));
        t.addPlayer(new Player("Brandin Podziemski", "Warriors", "SG", 62, 47, 80, 76, c));
        t.addPlayer(new Player("Jimmy Butler", "Warriors", "SF", 78, 54, 72, 88, c));
        t.addPlayer(new Player("Draymond Green", "Warriors", "PF", 70, 43, 66, 92, c));
        t.addPlayer(new Player("Quinten Post", "Warriors", "C", 76, 45, 74, 77, c));
        t.addPlayer(new Player("Kristaps Porzingis", "Warriors", "C", 78, 55, 82, 80, c));
        t.addPlayer(new Player("Gui Santos", "Warriors", "PF", 68, 46, 74, 78, c));
        t.addPlayer(new Player("Moses Moody", "Warriors", "SF", 63, 46, 78, 76, c));
        t.addPlayer(new Player("Al Horford", "Warriors", "C", 68, 48, 78, 82, c));
        t.addPlayer(new Player("De'Anthony Melton", "Warriors", "PG", 62, 45, 78, 84, c));
        t.addPlayer(new Player("Seth Curry", "Warriors", "SG", 58, 50, 86, 65, c));
        return t;
    }
    /**
     * builds team for lakers
     * @return lakers team
     */
    public static Team buildLakers() {
        Team t = new Team("Lakers");
        Color c = new Color(185, 130, 255);
        t.addPlayer(new Player("Luka Doncic", "Lakers", "PG", 74, 58, 86, 76, c));
        t.addPlayer(new Player("Austin Reaves", "Lakers", "SG", 66, 52, 82, 74, c));
        t.addPlayer(new Player("LeBron James (the goat)", "Lakers", "SF", 82, 55, 78, 86, c));
        t.addPlayer(new Player("Rui Hachimura", "Lakers", "PF", 73, 50, 74, 75, c));
        t.addPlayer(new Player("Deandre Ayton", "Lakers", "C", 84, 42, 28, 79, c));
        t.addPlayer(new Player("Marcus Smart", "Lakers", "PG", 62, 45, 75, 90, c));
        t.addPlayer(new Player("Jake LaRavia", "Lakers", "SF", 65, 47, 77, 74, c));
        t.addPlayer(new Player("Jaxson Hayes", "Lakers", "C", 84, 34, 30, 77, c));
        t.addPlayer(new Player("Gabe Vincent", "Lakers", "PG", 58, 45, 75, 74, c));
        t.addPlayer(new Player("Jarred Vanderbilt", "Lakers", "PF", 68, 38, 42, 85, c));
        t.addPlayer(new Player("Bronny James", "Lakers", "PG", 58, 40, 70, 75, c));
        t.addPlayer(new Player("Luke Kennard", "Lakers", "SG", 56, 50, 88, 64, c));
        return t;
    }
    /**
     * builds team for celtics
     * @return celtics team
     */
    public static Team buildCeltics() {
        Team t = new Team("Celtics");
        Color c = new Color(50, 190, 90);
        t.addPlayer(new Player("Derrick White", "Celtics", "PG", 68, 50, 83, 86, c));
        t.addPlayer(new Player("Jaylen Brown", "Celtics", "SG", 78, 53, 78, 84, c));
        t.addPlayer(new Player("Jayson Tatum", "Celtics", "SF", 76, 57, 86, 84, c));
        t.addPlayer(new Player("Sam Hauser", "Celtics", "PF", 58, 44, 84, 70, c));
        t.addPlayer(new Player("Nikola Vucevic", "Celtics", "C", 78, 52, 76, 76, c));
        t.addPlayer(new Player("Payton Pritchard", "Celtics", "PG", 60, 47, 82, 70, c));
        t.addPlayer(new Player("Jordan Walsh", "Celtics", "SF", 65, 42, 70, 78, c));
        t.addPlayer(new Player("Neemias Queta", "Celtics", "C", 80, 34, 25, 79, c));
        t.addPlayer(new Player("Baylor Scheierman", "Celtics", "SG", 61, 47, 80, 69, c));
        t.addPlayer(new Player("Amari Williams", "Celtics", "PF", 76, 36, 24, 75, c));
        t.addPlayer(new Player("John Tonje", "Celtics", "SG", 64, 45, 78, 70, c));
        return t;
    }
    /**
     * builds team for thunder
     * @return thunder team
     */
    public static Team buildThunder() {
        Team t = new Team("Thunder");
        Color c = new Color(80, 170, 255);
        t.addPlayer(new Player("Shai Gilgeous-Alexander", "Thunder", "PG", 84, 60, 82, 85, c));
        t.addPlayer(new Player("Jalen Williams", "Thunder", "SG", 76, 53, 80, 82, c));
        t.addPlayer(new Player("Luguentz Dort", "Thunder", "SF", 65, 43, 76, 88, c));
        t.addPlayer(new Player("Chet Holmgren", "Thunder", "PF", 78, 52, 78, 88, c));
        t.addPlayer(new Player("Isaiah Hartenstein", "Thunder", "C", 80, 42, 30, 82, c));
        t.addPlayer(new Player("Cason Wallace", "Thunder", "PG", 60, 44, 77, 82, c));
        t.addPlayer(new Player("Alex Caruso", "Thunder", "SG", 60, 44, 75, 90, c));
        t.addPlayer(new Player("Isaiah Joe", "Thunder", "SG", 58, 45, 84, 72, c));
        t.addPlayer(new Player("Aaron Wiggins", "Thunder", "SF", 66, 48, 76, 76, c));
        t.addPlayer(new Player("Jaylin Williams", "Thunder", "C", 70, 42, 73, 78, c));
        t.addPlayer(new Player("Ajay Mitchell", "Thunder", "PG", 63, 45, 76, 74, c));
        t.addPlayer(new Player("Jared McCain", "Thunder", "SG", 62, 48, 82, 68, c));
        return t;
    }
    /**
     * 
     * @return gets all the teams
     */
    public static Team[] getTeams() {
        return new Team[] { buildWarriors(), buildLakers(), buildCeltics(), buildThunder() };
    }
    /**
     * gets the default AI team (first team that is not same as user's team)
     * makes sure its not same as users
     * @param userTeamName user's team name
     * @return AI's team
     */
    public static Team getDefaultAITeam(String userTeamName) {
        Team[] teams = getTeams();
        for (Team t : teams) {
            if (!t.getTeamName().equals(userTeamName)) {
                return t;
            }
        }
        return buildCeltics();
    }
}

