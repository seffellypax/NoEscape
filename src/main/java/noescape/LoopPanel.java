package noescape;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * OOP:
 *   Inheritance  — extends BasePanel (which extends JPanel)
 *   Abstraction  — implements buildContent() defined in BasePanel
 */
public class LoopPanel extends BasePanel {
    private final Player player;
    private final String controllerMessage;
    private final ActionListener onTryAgain;

    public LoopPanel(Player player, String controllerMessage, ActionListener onTryAgain) {
        this.player = player;
        this.controllerMessage = controllerMessage;
        this.onTryAgain = onTryAgain;
        buildContent();
    }

    @Override
    protected void buildContent() {
        setLayout(new GridBagLayout());

        JPanel innerPanel = new JPanel();
        innerPanel.setOpaque(false);
        innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.Y_AXIS));

        innerPanel.add(createTitleLabel("💀  YOU FAILED!", GameWindow.COLOR_RED, 28));
        innerPanel.add(Box.createVerticalStrut(20));
        innerPanel.add(createCenteredLabel("Time ran out,  " + player.getName() + ".", GameWindow.COLOR_TEXT, 15, Font.PLAIN));
        innerPanel.add(Box.createVerticalStrut(6));
        innerPanel.add(createCenteredLabel("Better luck next time.", GameWindow.COLOR_TEXT, 14, Font.PLAIN));
        innerPanel.add(Box.createVerticalStrut(24));
        innerPanel.add(createHorizontalDivider());
        innerPanel.add(Box.createVerticalStrut(14));
        innerPanel.add(createCenteredLabel("[ " + controllerMessage + " ]", GameWindow.COLOR_DIMMED, 12, Font.ITALIC));
        innerPanel.add(Box.createVerticalStrut(14));
        innerPanel.add(createHorizontalDivider());
        innerPanel.add(Box.createVerticalStrut(20));

        JButton tryAgainButton = createLargeCourseButton(
            "🔄   TRY AGAIN", "Restart the loop", GameWindow.COLOR_YELLOW, onTryAgain
        );
        tryAgainButton.setMaximumSize(new Dimension(320, 70));
        innerPanel.add(tryAgainButton);
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