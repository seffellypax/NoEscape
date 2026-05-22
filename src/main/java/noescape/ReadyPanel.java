package noescape;

import javax.swing.*;
import java.awt.*;

/**
 * OOP:
 *   Inheritance  — extends BasePanel (which extends JPanel)
 *   Abstraction  — implements buildContent() defined in BasePanel
 */
public class ReadyPanel extends BasePanel {
    private final Player player;
    private final JTextField inputField;
    private final JButton submitButton;

    public ReadyPanel(Player player, JTextField inputField, JButton submitButton) {
        this.player = player;
        this.inputField = inputField;
        this.submitButton = submitButton;
        buildContent();
    }

    @Override
    protected void buildContent() {
        setLayout(new GridBagLayout());

        JPanel innerPanel = new JPanel();
        innerPanel.setOpaque(false);
        innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.Y_AXIS));

        innerPanel.add(createTitleLabel("NO ESCAPE", GameWindow.COLOR_PURPLE, 34));
        innerPanel.add(Box.createVerticalStrut(24));
        innerPanel.add(createHorizontalDivider());
        innerPanel.add(Box.createVerticalStrut(18));
        innerPanel.add(createCenteredLabel("Player :  " + player.getName(),GameWindow.COLOR_TEXT,15,Font.PLAIN));
        innerPanel.add(Box.createVerticalStrut(6));
        innerPanel.add(createCenteredLabel("Course :  " + player.getCourse(),GameWindow.COLOR_CYAN,15,Font.PLAIN));
        innerPanel.add(Box.createVerticalStrut(6));
        innerPanel.add(createCenteredLabel("Attempts:  " + player.getMaxAttempts() + " per room",GameWindow.COLOR_TEXT,14,Font.PLAIN));
        innerPanel.add(Box.createVerticalStrut(6));
        innerPanel.add(createCenteredLabel("Bonus :  +" + player.getBonusSeconds() + " seconds",GameWindow.COLOR_GREEN,14,Font.PLAIN));
        innerPanel.add(Box.createVerticalStrut(26));
        innerPanel.add(createHorizontalDivider());
        innerPanel.add(Box.createVerticalStrut(22));

        JButton startButton = createLargeCourseButton(
            "▶   START GAME",
            "Begin the time loop",
            GameWindow.COLOR_GREEN,
            event -> {
                inputField.setText("start");
                submitButton.doClick();
            }
        );
        startButton.setMaximumSize(new Dimension(320, 70));
        innerPanel.add(startButton);

        add(innerPanel);
    }
}