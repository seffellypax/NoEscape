package noescape;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * OOP: Encapsulation — {@code Game} never accesses {@code GameWindow} screens directly.
 */
public class GameDisplay {
    private final JLabel timerLabel;
    private final GameWindow gameWindow;

    public GameDisplay(JLabel timerLabel, GameWindow gameWindow) {
        this.timerLabel = timerLabel;
        this.gameWindow = gameWindow;
    }

    public void showEnterName() {
        gameWindow.showEnterNameScreen();
    }

    public void showChooseCourse(String playerName, ActionListener onComputerScienceSelected, ActionListener onNursingSelected) {
        gameWindow.showChooseCourseScreen(playerName, onComputerScienceSelected, onNursingSelected);
    }

    public void showSplash(Player player) {
        gameWindow.showSplashScreen(player);
    }

    public void showRoom(Escapable room, int roomIndex, int totalRooms, Player player, String controllerMessage, Escapable[] allRooms, int activeRoomIndex) {
        gameWindow.showRoomScreen(room, roomIndex, totalRooms, player, controllerMessage, allRooms, activeRoomIndex);
    }

    public void showWin(Player player, int secondsRemaining, String controllerMessage) {
        gameWindow.showWinScreen(player, secondsRemaining, controllerMessage);
    }

    public void showLoop(Player player, String controllerMessage) {
        gameWindow.showLoopScreen(player, controllerMessage);
    }

    public void showFeedback(String feedbackMessage, Color textColor) {
        gameWindow.showFeedback(feedbackMessage, textColor);
    }

    public void updateTimer(int secondsRemaining) {
        Color timerColor;
        if (secondsRemaining <= 10) timerColor = GameWindow.COLOR_RED;
        else if (secondsRemaining <= 30) timerColor = GameWindow.COLOR_ORANGE;
        else timerColor = GameWindow.COLOR_YELLOW;

        timerLabel.setForeground(timerColor);
        timerLabel.setText("⏱  " + secondsRemaining + "s  ");
    }

    public void setTimerText(String text, Color textColor) {
        timerLabel.setText(text);
        timerLabel.setForeground(textColor);
    }
}