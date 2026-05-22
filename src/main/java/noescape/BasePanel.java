package noescape;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * OOP:
 *   Inheritance  — extends JPanel; all screen panels extend BasePanel
 *   Abstraction  — declares buildContent() as abstract, forcing each
 *                  subclass to define its own screen layout
 *   Encapsulation — shared helper methods are protected, not public
 */
public abstract class BasePanel extends JPanel {
    protected BasePanel() {
        setBackground(GameWindow.COLOR_BACKGROUND_CARD);
        setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(GameWindow.COLOR_BORDER, 1, true),
            new EmptyBorder(28, 32, 28, 32)
        ));
    }

    protected abstract void buildContent();

    protected JLabel createCenteredLabel(String text, Color textColor, int fontSize, int fontStyle) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(new Font("Consolas", fontStyle, fontSize));
        label.setForeground(textColor);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }

    protected JLabel createTitleLabel(String text, Color textColor, int fontSize) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(new Font("Consolas", Font.BOLD, fontSize));
        label.setForeground(textColor);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }

    protected JPanel createHorizontalDivider() {
        JPanel divider = new JPanel() {
            @Override
            protected void paintComponent(Graphics graphics) {
                super.paintComponent(graphics);
                graphics.setColor(GameWindow.COLOR_BORDER);
                graphics.drawLine(20, getHeight() / 2, getWidth() - 20, getHeight() / 2);
            }
        };
        divider.setOpaque(false);
        divider.setPreferredSize(new Dimension(400, 10));
        divider.setMaximumSize(new Dimension(Integer.MAX_VALUE, 10));
        return divider;
    }

    protected JButton createLargeCourseButton(String titleText, String subtitleText, Color accentColor, ActionListener onClickAction) {
        JButton button = new JButton() {
            @Override
            protected void paintComponent(Graphics graphics) {
                Graphics2D graphics2D = (Graphics2D) graphics.create();
                graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color fillColor = getModel().isRollover()
                    ? new Color(accentColor.getRed(), accentColor.getGreen(), accentColor.getBlue(), 55)
                    : new Color(accentColor.getRed(), accentColor.getGreen(), accentColor.getBlue(), 22);
                graphics2D.setColor(fillColor);
                graphics2D.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                graphics2D.dispose();
                super.paintComponent(graphics);
            }
        };
        button.setLayout(new BoxLayout(button, BoxLayout.Y_AXIS));
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(accentColor, 1, true),
            new EmptyBorder(14, 28, 14, 28)
        ));
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setMaximumSize(new Dimension(460, 80));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titleLabel = new JLabel(titleText, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Consolas", Font.BOLD, 16));
        titleLabel.setForeground(accentColor);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitleLabel = new JLabel(subtitleText, SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Consolas", Font.PLAIN, 12));
        subtitleLabel.setForeground(new Color(accentColor.getRed(), accentColor.getGreen(), accentColor.getBlue(), 180));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        button.add(titleLabel);
        button.add(Box.createVerticalStrut(4));
        button.add(subtitleLabel);
        button.addActionListener(onClickAction);
        return button;
    }

    protected String[] wrapTextToLines(String text, int maxCharsPerLine) {
        if (text.length() <= maxCharsPerLine) return new String[]{ text };
        List<String> lines = new ArrayList<>();
        String[] words = text.split(" ");
        StringBuilder currentLine = new StringBuilder();
        for (String word : words) {
            if (currentLine.length() + word.length() + 1 > maxCharsPerLine && currentLine.length() > 0) {
                lines.add(currentLine.toString().trim());
                currentLine = new StringBuilder();
            }
            currentLine.append(word).append(" ");
        }
        if (currentLine.length() > 0) lines.add(currentLine.toString().trim());
        return lines.toArray(new String[0]);
    }
}