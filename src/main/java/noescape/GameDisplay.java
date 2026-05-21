package noescape;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * GameDisplay
 * Delegates all screen rendering to GameWindow.
 * Keeps Game.java clean — it calls display.showX(), not window directly.
 *
 * OOP: Encapsulation - Game never accesses GameWindow screens directly.
 */
public class GameDisplay {

    private JLabel     timerLabel;
    private GameWindow window;

    public GameDisplay(Object ignored, JLabel timerLabel, GameWindow window) {
        this.timerLabel = timerLabel;
        this.window     = window;
    }

    // Screens
    public void showEnterName() {
        window.showEnterNameScreen();
    }

    public void showChooseCourse(String playerName,
                                 ActionListener csAction,
                                 ActionListener nursingAction) {
        window.showChooseCourseScreen(playerName, csAction, nursingAction);
    }

    public void showSplash(Player player) {
        window.showSplashScreen(player);
    }

    public void showRoom(RoomBehavior room, int index, int total,
                         Player player, String adminMessage,
                         RoomBehavior[] rooms, int currentIndex) {
        window.showRoomScreen(room, index, total, player,
                              adminMessage, rooms, currentIndex);
    }

    public void showWin(Player player, int secondsLeft, String adminMessage) {
        window.showWinScreen(player, secondsLeft, adminMessage);
    }

    public void showLoop(Player player, String adminMessage) {
        window.showLoopScreen(player, adminMessage);
    }

    // Feedback (answer results, clues, hints)
    public void showFeedback(String msg, Color color) {
        window.showFeedback(msg, color);
    }

    // Timer label updates
    public void updateTimer(int secondsLeft) {
        Color color;
        if      (secondsLeft <= 10) color = GameWindow.COL_RED;
        else if (secondsLeft <= 30) color = GameWindow.COL_ORANGE;
        else                        color = GameWindow.COL_YELLOW;
        timerLabel.setForeground(color);
        timerLabel.setText("⏱  " + secondsLeft + "s  ");
    }

    public void setTimerText(String text, Color color) {
        timerLabel.setText(text);
        timerLabel.setForeground(color);
    }
}