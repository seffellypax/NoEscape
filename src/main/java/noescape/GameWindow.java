package noescape;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Primary application window composed of swappable screen panels.
 * Each screen (name entry, course selection, gameplay, etc.) is its own JPanel.
 *
 * OOP: Encapsulation — private fields, public API only.
 */
public class GameWindow {

    // -------------------------------------------------------------------------
    // Color Palette Constants
    // -------------------------------------------------------------------------
    public static final Color COLOR_BACKGROUND_DARK   = new Color(13,  13,  23);
    public static final Color COLOR_BACKGROUND_CARD   = new Color(22,  22,  40);
    public static final Color COLOR_BACKGROUND_INPUT  = new Color(18,  18,  32);
    public static final Color COLOR_PURPLE            = new Color(160, 80,  220);
    public static final Color COLOR_CYAN              = new Color(60,  200, 220);
    public static final Color COLOR_GREEN             = new Color(60,  200, 100);
    public static final Color COLOR_YELLOW            = new Color(240, 200, 60);
    public static final Color COLOR_RED               = new Color(220, 70,  70);
    public static final Color COLOR_ORANGE            = new Color(230, 140, 50);
    public static final Color COLOR_TEXT              = new Color(210, 210, 230);
    public static final Color COLOR_DIMMED            = new Color(130, 120, 155);
    public static final Color COLOR_BORDER            = new Color(55,  45,  85);

    // -------------------------------------------------------------------------
    // UI Component Fields
    // -------------------------------------------------------------------------
    private JFrame    mainFrame;
    private JLabel    timerLabel;
    private JLabel    roomInfoLabel;
    private JPanel    contentCardPanel;
    private JTextField inputField;
    private JButton   submitButton;
    private JButton   clueButton;
    private JButton   hintButton;

    public GameWindow() {
        buildWindow();
    }

    // -------------------------------------------------------------------------
    // Window Construction
    // -------------------------------------------------------------------------

    private void buildWindow() {
        mainFrame = new JFrame("NO ESCAPE");
        mainFrame.setSize(860, 640);
        mainFrame.setMinimumSize(new Dimension(720, 520));
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLayout(new BorderLayout());
        mainFrame.getContentPane().setBackground(COLOR_BACKGROUND_DARK);
        mainFrame.setLocationRelativeTo(null);

        mainFrame.add(buildHeaderPanel(),  BorderLayout.NORTH);
        mainFrame.add(buildContentArea(),  BorderLayout.CENTER);
        mainFrame.add(buildFooterPanel(),  BorderLayout.SOUTH);

        mainFrame.setVisible(true);
    }

    private JPanel buildHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(8, 8, 18));
        headerPanel.setBorder(new MatteBorder(0, 0, 2, 0, COLOR_PURPLE));
        headerPanel.setPreferredSize(new Dimension(0, 54));

        roomInfoLabel = new JLabel("", SwingConstants.CENTER);
        roomInfoLabel.setFont(new Font("Consolas", Font.PLAIN, 13));
        roomInfoLabel.setForeground(COLOR_DIMMED);

        timerLabel = new JLabel("", SwingConstants.RIGHT);
        timerLabel.setFont(new Font("Consolas", Font.BOLD, 17));
        timerLabel.setForeground(COLOR_YELLOW);
        timerLabel.setBorder(new EmptyBorder(0, 0, 0, 18));

        headerPanel.add(roomInfoLabel, BorderLayout.CENTER);
        headerPanel.add(timerLabel,    BorderLayout.EAST);
        return headerPanel;
    }

    private JPanel buildContentArea() {
        contentCardPanel = new JPanel(new BorderLayout());
        contentCardPanel.setBackground(COLOR_BACKGROUND_DARK);
        contentCardPanel.setBorder(new EmptyBorder(18, 28, 10, 28));
        return contentCardPanel;
    }

    private JPanel buildFooterPanel() {
        JPanel footerPanel = new JPanel(new BorderLayout(8, 0));
        footerPanel.setBackground(COLOR_BACKGROUND_DARK);
        footerPanel.setBorder(new CompoundBorder(
            new MatteBorder(2, 0, 0, 0, COLOR_BORDER),
            new EmptyBorder(10, 28, 14, 28)
        ));

        inputField = new JTextField();
        inputField.setFont(new Font("Consolas", Font.PLAIN, 15));
        inputField.setBackground(COLOR_BACKGROUND_INPUT);
        inputField.setForeground(COLOR_TEXT);
        inputField.setCaretColor(COLOR_PURPLE);
        inputField.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(COLOR_PURPLE, 1, true),
            new EmptyBorder(7, 14, 7, 14)
        ));

        submitButton = createActionButton("Submit", COLOR_GREEN);
        clueButton   = createActionButton("Clue",   COLOR_CYAN);
        hintButton   = createActionButton("Hint",   COLOR_YELLOW);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        buttonPanel.setBackground(COLOR_BACKGROUND_DARK);
        buttonPanel.add(clueButton);
        buttonPanel.add(hintButton);
        buttonPanel.add(submitButton);

        footerPanel.add(inputField,   BorderLayout.CENTER);
        footerPanel.add(buttonPanel,  BorderLayout.EAST);
        return footerPanel;
    }

    private void swapContentPanel(JPanel newPanel) {
        contentCardPanel.removeAll();
        contentCardPanel.add(newPanel, BorderLayout.CENTER);
        contentCardPanel.revalidate();
        contentCardPanel.repaint();
    }

    // -------------------------------------------------------------------------
    // Screen Builders
    // -------------------------------------------------------------------------

    public void showEnterNameScreen() {
        roomInfoLabel.setText("");
        JPanel cardPanel = createCardPanel();
        cardPanel.setLayout(new GridBagLayout());

        JPanel innerPanel = new JPanel();
        innerPanel.setOpaque(false);
        innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.Y_AXIS));

        innerPanel.add(createTitleLabel("NO ESCAPE", COLOR_PURPLE, 36));
        innerPanel.add(Box.createVerticalStrut(8));
        innerPanel.add(createCenteredLabel("An endless campus time-loop", COLOR_DIMMED, 14, Font.PLAIN));
        innerPanel.add(Box.createVerticalStrut(28));
        innerPanel.add(createHorizontalDivider());
        innerPanel.add(Box.createVerticalStrut(28));
        innerPanel.add(createCenteredLabel("You are a student trapped in a time loop.", COLOR_TEXT, 14, Font.PLAIN));
        innerPanel.add(Box.createVerticalStrut(6));
        innerPanel.add(createCenteredLabel("Solve puzzles in each room to break free!", COLOR_TEXT, 14, Font.PLAIN));
        innerPanel.add(Box.createVerticalStrut(32));
        innerPanel.add(createCenteredLabel("What is your name?", COLOR_YELLOW, 16, Font.BOLD));
        innerPanel.add(Box.createVerticalStrut(10));
        innerPanel.add(createCenteredLabel("Type your name below and press  Submit.", COLOR_DIMMED, 13, Font.PLAIN));

        cardPanel.add(innerPanel);
        swapContentPanel(cardPanel);
    }

    public void showChooseCourseScreen(String playerName, ActionListener onComputerScienceSelected, ActionListener onNursingSelected) {
        roomInfoLabel.setText("Choose Your Course");
        JPanel cardPanel = createCardPanel();
        cardPanel.setLayout(new GridBagLayout());

        JPanel innerPanel = new JPanel();
        innerPanel.setOpaque(false);
        innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.Y_AXIS));

        innerPanel.add(createCenteredLabel("Welcome,  " + playerName + "!", COLOR_YELLOW, 20, Font.BOLD));
        innerPanel.add(Box.createVerticalStrut(24));
        innerPanel.add(createCenteredLabel("Choose your course:", COLOR_TEXT, 14, Font.PLAIN));
        innerPanel.add(Box.createVerticalStrut(20));

        JButton computerScienceButton = createLargeCourseButton(
            "💻  Computer Science",
            "+20 bonus seconds  ·  3 attempts per room",
            COLOR_CYAN,
            onComputerScienceSelected
        );
        innerPanel.add(computerScienceButton);
        innerPanel.add(Box.createVerticalStrut(14));

        JButton nursingButton = createLargeCourseButton(
            "🏥  Nursing",
            "No time bonus  ·  5 attempts per room",
            new Color(255, 130, 180),
            onNursingSelected
        );
        innerPanel.add(nursingButton);
        innerPanel.add(Box.createVerticalStrut(20));

        cardPanel.add(innerPanel);
        swapContentPanel(cardPanel);
    }

    public void showSplashScreen(Player player) {
        roomInfoLabel.setText("Ready to Play");
        JPanel cardPanel = createCardPanel();
        cardPanel.setLayout(new GridBagLayout());

        JPanel innerPanel = new JPanel();
        innerPanel.setOpaque(false);
        innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.Y_AXIS));

        innerPanel.add(createTitleLabel("NO ESCAPE", COLOR_PURPLE, 34));
        innerPanel.add(Box.createVerticalStrut(24));
        innerPanel.add(createHorizontalDivider());
        innerPanel.add(Box.createVerticalStrut(18));
        innerPanel.add(createCenteredLabel("Player  :  " + player.getName(),                      COLOR_TEXT,  15, Font.PLAIN));
        innerPanel.add(Box.createVerticalStrut(6));
        innerPanel.add(createCenteredLabel("Course  :  " + player.getCourse(),                    COLOR_CYAN,  15, Font.PLAIN));
        innerPanel.add(Box.createVerticalStrut(6));
        innerPanel.add(createCenteredLabel("Attempts:  " + player.getMaxAttempts() + " per room", COLOR_TEXT,  14, Font.PLAIN));
        innerPanel.add(Box.createVerticalStrut(6));
        innerPanel.add(createCenteredLabel("Bonus   :  +" + player.getBonusSeconds() + " seconds",COLOR_GREEN, 14, Font.PLAIN));
        innerPanel.add(Box.createVerticalStrut(26));
        innerPanel.add(createHorizontalDivider());
        innerPanel.add(Box.createVerticalStrut(22));

        JButton startButton = createLargeCourseButton(
            "▶   START GAME",
            "Begin the time loop",
            COLOR_GREEN,
            event -> {
                inputField.setText("start");
                submitButton.doClick();
            }
        );
        startButton.setMaximumSize(new Dimension(320, 70));
        innerPanel.add(startButton);

        cardPanel.add(innerPanel);
        swapContentPanel(cardPanel);
    }

    public void showRoomScreen(Escapable room, int roomIndex, int totalRooms, Player player,
                               String controllerMessage, Escapable[] allRooms, int activeRoomIndex) {
        roomInfoLabel.setText("Room " + (roomIndex + 1) + " of " + totalRooms + "  —  " + room.getName());

        JPanel cardPanel = createCardPanel();
        cardPanel.setLayout(new BorderLayout(0, 14));

        JPanel topSection = new JPanel();
        topSection.setOpaque(false);
        topSection.setLayout(new BoxLayout(topSection, BoxLayout.Y_AXIS));

        topSection.add(createTitleLabel(room.getName(), COLOR_PURPLE, 20));
        topSection.add(Box.createVerticalStrut(16));

        if (room.isLocked()) {
            topSection.add(createCenteredLabel("🔒  This room is locked.", COLOR_RED, 15, Font.BOLD));
            topSection.add(Box.createVerticalStrut(8));
            topSection.add(createCenteredLabel("Solve the previous room first.", COLOR_DIMMED, 13, Font.PLAIN));
        } else {
            JPanel puzzleBox = new JPanel();
            puzzleBox.setOpaque(true);
            puzzleBox.setBackground(new Color(30, 20, 55));
            puzzleBox.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(COLOR_PURPLE, 1, true),
                new EmptyBorder(14, 24, 14, 24)
            ));
            puzzleBox.setLayout(new BoxLayout(puzzleBox, BoxLayout.Y_AXIS));

            room.showPuzzle();
            String puzzleText = room.getLastMessage().replace("PUZZLE: ", "");

            puzzleBox.add(createCenteredLabel("📝  PUZZLE", COLOR_YELLOW, 13, Font.BOLD));
            puzzleBox.add(Box.createVerticalStrut(10));

            for (String line : wrapTextToLines(puzzleText, 55)) {
                puzzleBox.add(createCenteredLabel(line, COLOR_TEXT, 14, Font.PLAIN));
            }

            JPanel puzzleWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
            puzzleWrapper.setOpaque(false);
            puzzleBox.setMaximumSize(new Dimension(560, 120));
            puzzleWrapper.add(puzzleBox);

            topSection.add(puzzleWrapper);
            topSection.add(Box.createVerticalStrut(14));
            topSection.add(createCenteredLabel("Type your answer below and press  Submit.", COLOR_DIMMED, 13, Font.PLAIN));
            topSection.add(Box.createVerticalStrut(4));
            topSection.add(createCenteredLabel("Use the  Clue  or  Hint  buttons if stuck.", COLOR_DIMMED, 12, Font.PLAIN));
        }

        JPanel bottomSection = new JPanel();
        bottomSection.setOpaque(false);
        bottomSection.setLayout(new BoxLayout(bottomSection, BoxLayout.Y_AXIS));
        bottomSection.add(createHorizontalDivider());
        bottomSection.add(Box.createVerticalStrut(8));
        bottomSection.add(createCenteredLabel("[ " + controllerMessage + " ]", COLOR_DIMMED, 12, Font.ITALIC));
        bottomSection.add(Box.createVerticalStrut(10));
        bottomSection.add(buildRoomProgressMap(allRooms, activeRoomIndex));

        cardPanel.add(topSection,     BorderLayout.CENTER);
        cardPanel.add(bottomSection,  BorderLayout.SOUTH);
        swapContentPanel(cardPanel);
    }

    public void showWinScreen(Player player, int secondsRemaining, String controllerMessage) {
        roomInfoLabel.setText("YOU ESCAPED!");
        JPanel cardPanel = createCardPanel();
        cardPanel.setLayout(new GridBagLayout());

        JPanel innerPanel = new JPanel();
        innerPanel.setOpaque(false);
        innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.Y_AXIS));

        innerPanel.add(createTitleLabel("🎓  YOU ESCAPED!", COLOR_GREEN, 28));
        innerPanel.add(Box.createVerticalStrut(18));
        innerPanel.add(createCenteredLabel("Congratulations,  " + player.getName() + "!", COLOR_YELLOW, 16, Font.BOLD));
        innerPanel.add(Box.createVerticalStrut(6));
        innerPanel.add(createCenteredLabel("Every puzzle solved. Every room conquered. You are finally free.", COLOR_TEXT, 14, Font.PLAIN));
        innerPanel.add(Box.createVerticalStrut(24));
        innerPanel.add(createHorizontalDivider());
        innerPanel.add(Box.createVerticalStrut(14));
        innerPanel.add(createCenteredLabel("Course    :  " + player.getCourse(),           COLOR_CYAN,  14, Font.PLAIN));
        innerPanel.add(Box.createVerticalStrut(6));
        innerPanel.add(createCenteredLabel("Time left :  " + secondsRemaining + " seconds", COLOR_GREEN, 14, Font.PLAIN));
        innerPanel.add(Box.createVerticalStrut(14));
        innerPanel.add(createHorizontalDivider());
        innerPanel.add(Box.createVerticalStrut(20));
        innerPanel.add(createCenteredLabel("[ " + controllerMessage + " ]", COLOR_DIMMED, 12, Font.ITALIC));
        innerPanel.add(Box.createVerticalStrut(20));

        JButton playAgainButton = createLargeCourseButton(
            "🔄   PLAY AGAIN",
            "Start a new game",
            COLOR_GREEN,
            event -> { inputField.setText("restart"); submitButton.doClick(); }
        );
        playAgainButton.setMaximumSize(new Dimension(320, 70));
        innerPanel.add(playAgainButton);
        innerPanel.add(Box.createVerticalStrut(12));

        JButton exitButton = createLargeCourseButton(
            "✖   EXIT GAME",
            "Close the application",
            COLOR_RED,
            event -> System.exit(0)
        );
        exitButton.setMaximumSize(new Dimension(320, 70));
        innerPanel.add(exitButton);

        cardPanel.add(innerPanel);
        swapContentPanel(cardPanel);
    }

    public void showLoopScreen(Player player, String controllerMessage) {
        roomInfoLabel.setText("You Failed!");
        JPanel cardPanel = createCardPanel();
        cardPanel.setLayout(new GridBagLayout());

        JPanel innerPanel = new JPanel();
        innerPanel.setOpaque(false);
        innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.Y_AXIS));

        innerPanel.add(createTitleLabel("💀  YOU FAILED!", COLOR_RED, 28));
        innerPanel.add(Box.createVerticalStrut(20));
        innerPanel.add(createCenteredLabel("Time ran out,  " + player.getName() + ".", COLOR_TEXT, 15, Font.PLAIN));
        innerPanel.add(Box.createVerticalStrut(6));
        innerPanel.add(createCenteredLabel("Better luck next time.", COLOR_TEXT, 14, Font.PLAIN));
        innerPanel.add(Box.createVerticalStrut(24));
        innerPanel.add(createHorizontalDivider());
        innerPanel.add(Box.createVerticalStrut(14));
        innerPanel.add(createCenteredLabel("[ " + controllerMessage + " ]", COLOR_DIMMED, 12, Font.ITALIC));
        innerPanel.add(Box.createVerticalStrut(14));
        innerPanel.add(createHorizontalDivider());
        innerPanel.add(Box.createVerticalStrut(20));

        JButton tryAgainButton = createLargeCourseButton(
            "🔄   TRY AGAIN",
            "Restart the loop",
            COLOR_YELLOW,
            event -> { inputField.setText("restart"); submitButton.doClick(); }
        );
        tryAgainButton.setMaximumSize(new Dimension(320, 70));
        innerPanel.add(tryAgainButton);
        innerPanel.add(Box.createVerticalStrut(12));

        JButton exitButton = createLargeCourseButton(
            "✖   EXIT GAME",
            "Close the application",
            COLOR_RED,
            event -> System.exit(0)
        );
        exitButton.setMaximumSize(new Dimension(320, 70));
        innerPanel.add(exitButton);

        cardPanel.add(innerPanel);
        swapContentPanel(cardPanel);
    }

    // -------------------------------------------------------------------------
    // Room Progress Map
    // -------------------------------------------------------------------------

    private JPanel buildRoomProgressMap(Escapable[] allRooms, int activeRoomIndex) {
        JPanel mapPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        mapPanel.setOpaque(false);

        for (int i = 0; i < allRooms.length; i++) {
            Color chipColor;
            String statusIcon;

            if (allRooms[i].isSolved()) {
                chipColor  = COLOR_GREEN;  statusIcon = "✓";
            } else if (i == activeRoomIndex) {
                chipColor  = COLOR_PURPLE; statusIcon = "►";
            } else if (allRooms[i].isLocked()) {
                chipColor  = COLOR_DIMMED; statusIcon = "🔒";
            } else {
                chipColor  = COLOR_YELLOW; statusIcon = " ";
            }

            JPanel roomChip = new JPanel();
            roomChip.setOpaque(true);
            roomChip.setBackground(new Color(chipColor.getRed(), chipColor.getGreen(), chipColor.getBlue(), 30));
            roomChip.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(chipColor, 1, true),
                new EmptyBorder(5, 10, 5, 10)
            ));
            roomChip.setLayout(new BoxLayout(roomChip, BoxLayout.Y_AXIS));

            JLabel roomNameLabel = new JLabel(statusIcon + "  " + allRooms[i].getName(), SwingConstants.CENTER);
            roomNameLabel.setFont(new Font("Consolas", Font.BOLD, 11));
            roomNameLabel.setForeground(chipColor);
            roomNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            roomChip.add(roomNameLabel);
            mapPanel.add(roomChip);
        }
        return mapPanel;
    }

    // -------------------------------------------------------------------------
    // Component Factory Methods
    // -------------------------------------------------------------------------

    private JPanel createCardPanel() {
        JPanel card = new JPanel();
        card.setBackground(COLOR_BACKGROUND_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(COLOR_BORDER, 1, true),
            new EmptyBorder(28, 32, 28, 32)
        ));
        return card;
    }

    private JLabel createCenteredLabel(String text, Color textColor, int fontSize, int fontStyle) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(new Font("Consolas", fontStyle, fontSize));
        label.setForeground(textColor);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }

    private JLabel createTitleLabel(String text, Color textColor, int fontSize) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(new Font("Consolas", Font.BOLD, fontSize));
        label.setForeground(textColor);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }

    private JPanel createHorizontalDivider() {
        JPanel divider = new JPanel() {
            @Override
            protected void paintComponent(Graphics graphics) {
                super.paintComponent(graphics);
                graphics.setColor(COLOR_BORDER);
                graphics.drawLine(20, getHeight() / 2, getWidth() - 20, getHeight() / 2);
            }
        };
        divider.setOpaque(false);
        divider.setPreferredSize(new Dimension(400, 10));
        divider.setMaximumSize(new Dimension(Integer.MAX_VALUE, 10));
        return divider;
    }

    private JButton createLargeCourseButton(String titleText, String subtitleText,
                                            Color accentColor, ActionListener onClickAction) {
        JButton button = new JButton() {
            @Override
            protected void paintComponent(Graphics graphics) {
                Graphics2D g2d = (Graphics2D) graphics.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color fillColor = getModel().isRollover()
                    ? new Color(accentColor.getRed(), accentColor.getGreen(), accentColor.getBlue(), 55)
                    : new Color(accentColor.getRed(), accentColor.getGreen(), accentColor.getBlue(), 22);
                g2d.setColor(fillColor);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2d.dispose();
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

    private JButton createActionButton(String labelText, Color accentColor) {
        JButton button = new JButton(labelText) {
            @Override
            protected void paintComponent(Graphics graphics) {
                Graphics2D g2d = (Graphics2D) graphics.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color fillColor = getModel().isRollover()
                    ? new Color(accentColor.getRed(), accentColor.getGreen(), accentColor.getBlue(), 60)
                    : new Color(accentColor.getRed(), accentColor.getGreen(), accentColor.getBlue(), 25);
                g2d.setColor(fillColor);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2d.dispose();
                super.paintComponent(graphics);
            }
        };
        button.setFont(new Font("Consolas", Font.BOLD, 13));
        button.setForeground(accentColor);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(accentColor, 1, true),
            new EmptyBorder(7, 18, 7, 18)
        ));
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }

    // -------------------------------------------------------------------------
    // Utility Methods
    // -------------------------------------------------------------------------

    /** Wraps a long string into lines no longer than {@code maxCharsPerLine} characters. */
    private String[] wrapTextToLines(String text, int maxCharsPerLine) {
        if (text.length() <= maxCharsPerLine) return new String[]{ text };
        java.util.List<String> lines = new java.util.ArrayList<>();
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

    // -------------------------------------------------------------------------
    // Public API
    // -------------------------------------------------------------------------

    public void attachListeners(ActionListener onSubmit, ActionListener onClue, ActionListener onHint) {
        submitButton.addActionListener(onSubmit);
        inputField.addActionListener(onSubmit);
        clueButton.addActionListener(onClue);
        hintButton.addActionListener(onHint);
    }

    public void setInputEnabled(boolean isEnabled) {
        submitButton.setEnabled(isEnabled);
        clueButton.setEnabled(isEnabled);
        hintButton.setEnabled(isEnabled);
        inputField.setEnabled(isEnabled);
    }

    /**
     * Shows or hides the Clue and Hint buttons.
     * They should only be visible during active gameplay — not on the name entry,
     * course selection, splash, win, or loop screens.
     */
    public void setClueHintVisible(boolean isVisible) {
        clueButton.setVisible(isVisible);
        hintButton.setVisible(isVisible);
    }

    public void setRoomInfoLabel(String text) {
        roomInfoLabel.setText(text);
    }

    public void showFeedback(String feedbackMessage, Color textColor) {
        Component[] components = contentCardPanel.getComponents();
        if (components.length > 0 && components[0] instanceof JPanel) {
            JPanel cardPanel = (JPanel) components[0];
            Component lastComponent = cardPanel.getComponent(cardPanel.getComponentCount() - 1);

            if (lastComponent instanceof JLabel
                && ((JLabel) lastComponent).getName() != null
                && ((JLabel) lastComponent).getName().equals("feedback")) {
                cardPanel.remove(lastComponent);
            }

            JLabel feedbackLabel = new JLabel(feedbackMessage, SwingConstants.CENTER);
            feedbackLabel.setName("feedback");
            feedbackLabel.setFont(new Font("Consolas", Font.BOLD, 13));
            feedbackLabel.setForeground(textColor);
            feedbackLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            cardPanel.add(feedbackLabel);
            cardPanel.revalidate();
            cardPanel.repaint();
        }
    }

    public JTextField getInputField()   { return inputField;   }
    public JLabel     getTimerLabel()   { return timerLabel;   }
    public JButton    getSubmitButton() { return submitButton; }
    public JButton    getClueButton()   { return clueButton;   }
    public JButton    getHintButton()   { return hintButton;   }

    /** Kept for API compatibility — display is now panel-based. */
    public JTextPane getDisplayArea()   { return null; }
}