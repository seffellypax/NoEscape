package noescape;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {

        // Create the player (name and course will come from a setup screen later)
        Player player = new Player("Alice", "Bachelor of Science in Computer Science");

        // Create the game logic
        Game game = new Game(player);
        game.startGame();

        // Launch the GUI on Swing's thread
        SwingUtilities.invokeLater(() -> new GameGUI(game));
    }
}