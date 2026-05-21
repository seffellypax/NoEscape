package noescape;

import javax.swing.SwingUtilities;

/**
 * Main
 * Entry point for NO ESCAPE.
 * Launches the game on the Swing Event Dispatch Thread.
 */
public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Game();
            }
        });
    }
}