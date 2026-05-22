package noescape;

import javax.swing.*;
import java.awt.*;

/**
 * OOP:
 *   Inheritance  — extends BasePanel (which extends JPanel)
 *   Abstraction  — implements buildContent() defined in BasePanel
 */
public class NameEntryPanel extends BasePanel {
    public NameEntryPanel() {
        buildContent();
    }

    @Override
    protected void buildContent() {
        setLayout(new GridBagLayout());

        JPanel innerPanel = new JPanel();
        innerPanel.setOpaque(false);
        innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.Y_AXIS));

        innerPanel.add(createTitleLabel("NO ESCAPE", GameWindow.COLOR_PURPLE, 36));
        innerPanel.add(Box.createVerticalStrut(8));
        innerPanel.add(createCenteredLabel("An endless campus time-loop", GameWindow.COLOR_DIMMED, 14, Font.PLAIN));
        innerPanel.add(Box.createVerticalStrut(28));
        innerPanel.add(createHorizontalDivider());
        innerPanel.add(Box.createVerticalStrut(28));
        innerPanel.add(createCenteredLabel("You are a student trapped in a time loop.", GameWindow.COLOR_TEXT, 14, Font.PLAIN));
        innerPanel.add(Box.createVerticalStrut(6));
        innerPanel.add(createCenteredLabel("Solve puzzles in each room to break free!", GameWindow.COLOR_TEXT, 14, Font.PLAIN));
        innerPanel.add(Box.createVerticalStrut(32));
        innerPanel.add(createCenteredLabel("What is your name?", GameWindow.COLOR_YELLOW, 16, Font.BOLD));
        innerPanel.add(Box.createVerticalStrut(10));
        innerPanel.add(createCenteredLabel("Type your name below and press  Submit.", GameWindow.COLOR_DIMMED, 13, Font.PLAIN));

        add(innerPanel);
    }
}