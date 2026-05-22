package noescape;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * OOP:
 *   Inheritance  — extends BasePanel (which extends JPanel)
 *   Abstraction  — implements buildContent() defined in BasePanel
 */
public class WinPanel extends BasePanel {

    private final Player player;
    private final int secondsRemaining;
    private final String controllerMessage;
    private final ActionListener onPlayAgain;

    public WinPanel(Player player, int secondsRemaining, String controllerMessage, ActionListener onPlayAgain) {
        this.player = player;
        this.secondsRemaining  = secondsRemaining;
        this.controllerMessage = controllerMessage;
        this.onPlayAgain = onPlayAgain;
        buildContent();
    }

    @Override
    protected void buildContent() {
        setLayout(new GridBagLayout());

        JPanel innerPanel = new JPanel();
        innerPanel.setOpaque(false);
        innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.Y_AXIS));

        innerPanel.add(createTitleLabel("🎓  YOU ESCAPED!", GameWindow.COLOR_GREEN, 28));
        innerPanel.add(Box.createVerticalStrut(18));
        innerPanel.add(createCenteredLabel("Congratulations,  " + player.getName() + "!", GameWindow.COLOR_YELLOW, 16, Font.BOLD));
        innerPanel.add(Box.createVerticalStrut(6));
        innerPanel.add(createCenteredLabel("The clock stopped. The loop dissolved. You found the way out.", GameWindow.COLOR_TEXT, 14, Font.PLAIN));
        innerPanel.add(Box.createVerticalStrut(24));
        innerPanel.add(createHorizontalDivider());
        innerPanel.add(Box.createVerticalStrut(14));
        innerPanel.add(createCenteredLabel("Course    :  " + player.getCourse(),            GameWindow.COLOR_CYAN,  14, Font.PLAIN));
        innerPanel.add(Box.createVerticalStrut(6));
        innerPanel.add(createCenteredLabel("Time left :  " + secondsRemaining + " seconds", GameWindow.COLOR_GREEN, 14, Font.PLAIN));
        innerPanel.add(Box.createVerticalStrut(14));
        innerPanel.add(createHorizontalDivider());
        innerPanel.add(Box.createVerticalStrut(20));
        innerPanel.add(createCenteredLabel("[ " + controllerMessage + " ]", GameWindow.COLOR_DIMMED, 12, Font.ITALIC));
        innerPanel.add(Box.createVerticalStrut(20));

        JButton playAgainButton = createLargeCourseButton(
            "🔄   PLAY AGAIN", "Start a new game", GameWindow.COLOR_GREEN, onPlayAgain
        );
        playAgainButton.setMaximumSize(new Dimension(320, 70));
        innerPanel.add(playAgainButton);
        innerPanel.add(Box.createVerticalStrut(12));

        JButton exitButton = createLargeCourseButton(
            "✖   EXIT GAME", "Close the application", GameWindow.COLOR_RED,
            event -> System.exit(0)
        );
        exitButton.setMaximumSize(new Dimension(320, 70));
        innerPanel.add(exitButton);

        add(innerPanel);
    }
}