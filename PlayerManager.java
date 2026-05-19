import java.awt.Color;

/**
 * Builds teams with manually entered player data.
 * Stats are placeholder gameplay ratings, not official live NBA stats.
 */
public class PlayerManager {
    private PlayerManager() {
        // Static factory class.
    }

    public static Team buildWarriors() {
        Team t = new Team("Warriors");
        Color c = new Color(255, 210, 40);
        t.addPlayer(new Player("Stephen Curry", "Warriors", "PG", 65, 55, 94, 75, c));
        t.addPlayer(new Player("Brandin Podziemski", "Warriors", "SG", 62, 47, 80, 76, c));
        t.addPlayer(new Player("Jimmy Butler", "Warriors", "SF", 78, 54, 72, 88, c));
        t.addPlayer(new Player("Draymond Green", "Warriors", "PF", 70, 43, 66, 92, c));
        t.addPlayer(new Player("Jonathan Kuminga", "Warriors", "PF", 76, 45, 70, 78, c));
        t.addPlayer(new Player("Buddy Hield", "Warriors", "SG", 58, 48, 86, 70, c));
        t.addPlayer(new Player("Trayce Jackson-Davis", "Warriors", "C", 82, 35, 35, 78, c));
        t.addPlayer(new Player("Moses Moody", "Warriors", "SF", 63, 46, 78, 76, c));
        return t;
    }

    public static Team buildLakers() {
        Team t = new Team("Lakers");
        Color c = new Color(185, 130, 255);
        t.addPlayer(new Player("Luka Doncic", "Lakers", "PG", 74, 58, 86, 76, c));
        t.addPlayer(new Player("Austin Reaves", "Lakers", "SG", 66, 52, 82, 74, c));
        t.addPlayer(new Player("LeBron James", "Lakers", "SF", 82, 55, 78, 86, c));
        t.addPlayer(new Player("Rui Hachimura", "Lakers", "PF", 73, 50, 74, 75, c));
        t.addPlayer(new Player("Jaxson Hayes", "Lakers", "C", 84, 34, 30, 77, c));
        t.addPlayer(new Player("Gabe Vincent", "Lakers", "PG", 58, 45, 75, 74, c));
        t.addPlayer(new Player("Jarred Vanderbilt", "Lakers", "PF", 68, 38, 42, 85, c));
        t.addPlayer(new Player("Dorian Finney-Smith", "Lakers", "SF", 61, 43, 79, 82, c));
        return t;
    }

    public static Team buildCeltics() {
        Team t = new Team("Celtics");
        Color c = new Color(50, 190, 90);
        t.addPlayer(new Player("Jrue Holiday", "Celtics", "PG", 66, 50, 80, 90, c));
        t.addPlayer(new Player("Derrick White", "Celtics", "SG", 68, 50, 83, 86, c));
        t.addPlayer(new Player("Jaylen Brown", "Celtics", "SF", 78, 53, 78, 84, c));
        t.addPlayer(new Player("Jayson Tatum", "Celtics", "PF", 76, 57, 86, 84, c));
        t.addPlayer(new Player("Kristaps Porzingis", "Celtics", "C", 79, 55, 82, 80, c));
        t.addPlayer(new Player("Al Horford", "Celtics", "C", 68, 48, 78, 82, c));
        t.addPlayer(new Player("Payton Pritchard", "Celtics", "PG", 60, 47, 82, 70, c));
        t.addPlayer(new Player("Sam Hauser", "Celtics", "SF", 58, 44, 84, 70, c));
        return t;
    }

    public static Team buildThunder() {
        Team t = new Team("Thunder");
        Color c = new Color(80, 170, 255);
        t.addPlayer(new Player("Shai Gilgeous-Alexander", "Thunder", "PG", 84, 60, 82, 85, c));
        t.addPlayer(new Player("Jalen Williams", "Thunder", "SG", 76, 53, 80, 82, c));
        t.addPlayer(new Player("Luguentz Dort", "Thunder", "SF", 65, 43, 76, 88, c));
        t.addPlayer(new Player("Chet Holmgren", "Thunder", "PF", 78, 52, 78, 88, c));
        t.addPlayer(new Player("Isaiah Hartenstein", "Thunder", "C", 80, 42, 30, 82, c));
        t.addPlayer(new Player("Cason Wallace", "Thunder", "PG", 60, 44, 77, 82, c));
        t.addPlayer(new Player("Aaron Wiggins", "Thunder", "SG", 66, 48, 76, 76, c));
        t.addPlayer(new Player("Alex Caruso", "Thunder", "SG", 60, 44, 75, 90, c));
        return t;
    }

    public static Team[] getAllTeams() {
        return new Team[] { buildWarriors(), buildLakers(), buildCeltics(), buildThunder() };
    }

    public static Team getDefaultAITeam(String userTeamName) {
        Team[] teams = getAllTeams();
        for (Team t : teams) {
            if (!t.getTeamName().equals(userTeamName)) {
                return t;
            }
        }
        return buildCeltics();
    }
}

