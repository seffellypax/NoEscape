package noescape;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * OOP:
 *   Inheritance  — extends BasePanel (which extends JPanel)
 *   Abstraction  — implements buildContent() defined in BasePanel
 */
public class CourseSelectPanel extends BasePanel {

    private final String playerName;
    private final ActionListener onComputerScienceSelected;
    private final ActionListener onNursingSelected;

    public CourseSelectPanel(String playerName, ActionListener onComputerScienceSelected, ActionListener onNursingSelected) {
        this.playerName = playerName;
        this.onComputerScienceSelected = onComputerScienceSelected;
        this.onNursingSelected = onNursingSelected;
        buildContent();
    }

    @Override
    protected void buildContent() {
        setLayout(new GridBagLayout());

        JPanel innerPanel = new JPanel();
        innerPanel.setOpaque(false);
        innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.Y_AXIS));

        innerPanel.add(createCenteredLabel("Welcome,  " + playerName + "!", GameWindow.COLOR_YELLOW, 20, Font.BOLD));
        innerPanel.add(Box.createVerticalStrut(24));
        innerPanel.add(createCenteredLabel("Choose your course:", GameWindow.COLOR_TEXT, 14, Font.PLAIN));
        innerPanel.add(Box.createVerticalStrut(20));

        innerPanel.add(createLargeCourseButton(
            "💻  Computer Science",
            "+20 bonus seconds  ·  3 attempts per room",
            GameWindow.COLOR_CYAN,
            onComputerScienceSelected
        ));
        innerPanel.add(Box.createVerticalStrut(14));

        innerPanel.add(createLargeCourseButton(
            "🏥  Nursing",
            "No time bonus  ·  5 attempts per room",
            new Color(255, 130, 180),
            onNursingSelected
        ));
        innerPanel.add(Box.createVerticalStrut(20));

        add(innerPanel);
    }
}