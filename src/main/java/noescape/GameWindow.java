package noescape;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

/**
 * OOP:
 *   Inheritance   — extends JFrame
 *   Encapsulation — private fields, public API only
 */
public class GameWindow extends JFrame {
    public static final Color COLOR_BACKGROUND_DARK  = new Color(13, 13, 23);
    public static final Color COLOR_BACKGROUND_CARD  = new Color(22, 22, 40);
    public static final Color COLOR_BACKGROUND_INPUT = new Color(18, 18, 32);
    public static final Color COLOR_PURPLE = new Color(160, 80, 220);
    public static final Color COLOR_CYAN = new Color(60, 200, 220);
    public static final Color COLOR_GREEN = new Color(60, 200, 100);
    public static final Color COLOR_YELLOW = new Color(240, 200, 60);
    public static final Color COLOR_RED = new Color(220, 70, 70);
    public static final Color COLOR_ORANGE = new Color(230, 140, 50);
    public static final Color COLOR_TEXT = new Color(210, 210, 230);
    public static final Color COLOR_DIMMED = new Color(130, 120, 155);
    public static final Color COLOR_BORDER = new Color(55, 45, 85);

    private JLabel timerLabel;
    private JLabel roomInfoLabel;
    private JPanel  contentCardPanel;
    private JTextField inputField;
    private JButton submitButton;
    private JButton clueButton;
    private JButton hintButton;

    public GameWindow() {
        buildWindow();
    }

    private void buildWindow() {
        setTitle("NO ESCAPE");
        setSize(860, 640);
        setMinimumSize(new Dimension(720, 520));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(COLOR_BACKGROUND_DARK);
        setLocationRelativeTo(null);

        add(buildHeaderPanel(), BorderLayout.NORTH);
        add(buildContentArea(), BorderLayout.CENTER);
        add(buildFooterPanel(), BorderLayout.SOUTH);

        setVisible(true);
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
        headerPanel.add(timerLabel, BorderLayout.EAST);
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
        clueButton = createActionButton("Clue", COLOR_CYAN);
        hintButton = createActionButton("Hint", COLOR_YELLOW);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        buttonPanel.setBackground(COLOR_BACKGROUND_DARK);
        buttonPanel.add(clueButton);
        buttonPanel.add(hintButton);
        buttonPanel.add(submitButton);

        footerPanel.add(inputField,  BorderLayout.CENTER);
        footerPanel.add(buttonPanel, BorderLayout.EAST);
        return footerPanel;
    }

    private void swapContentPanel(JPanel newPanel) {
        contentCardPanel.removeAll();
        contentCardPanel.add(newPanel, BorderLayout.CENTER);
        contentCardPanel.revalidate();
        contentCardPanel.repaint();
    }

    public void showEnterNameScreen() {
        roomInfoLabel.setText("");
        swapContentPanel(new NameEntryPanel());
    }

    public void showChooseCourseScreen(String playerName, ActionListener onComputerScienceSelected, ActionListener onNursingSelected) {
        roomInfoLabel.setText("Choose Your Course");
        swapContentPanel(new CourseSelectPanel(playerName, onComputerScienceSelected, onNursingSelected));
    }

    public void showSplashScreen(Player player) {
        roomInfoLabel.setText("Ready to Play");
        swapContentPanel(new ReadyPanel(player, inputField, submitButton));
    }

    public void showRoomScreen(Escapable room, int roomIndex, int totalRooms, Player player, String controllerMessage, Escapable[] allRooms, int activeRoomIndex) {
        roomInfoLabel.setText("Room " + (roomIndex + 1) + " of " + totalRooms + "  —  " + room.getName());
        swapContentPanel(new RoomPanel(room, roomIndex, totalRooms, player, controllerMessage, allRooms, activeRoomIndex));
    }

    public void showWinScreen(Player player, int secondsRemaining, String controllerMessage) {
        roomInfoLabel.setText("YOU ESCAPED!");
        swapContentPanel(new WinPanel(player, secondsRemaining, controllerMessage,
            event -> { inputField.setText("restart"); submitButton.doClick(); }));
    }

    public void showLoopScreen(Player player, String controllerMessage) {
        roomInfoLabel.setText("You Failed!");
        swapContentPanel(new LoopPanel(player, controllerMessage,
            event -> { inputField.setText("restart"); submitButton.doClick(); }));
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

    private JButton createActionButton(String labelText, Color accentColor) {
        JButton button = new JButton(labelText) {
            @Override
            protected void paintComponent(Graphics graphics) {
                Graphics2D graphics2D = (Graphics2D) graphics.create();
                graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color fillColor = getModel().isRollover()
                    ? new Color(accentColor.getRed(), accentColor.getGreen(), accentColor.getBlue(), 60)
                    : new Color(accentColor.getRed(), accentColor.getGreen(), accentColor.getBlue(), 25);
                graphics2D.setColor(fillColor);
                graphics2D.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                graphics2D.dispose();
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

    public void setClueHintVisible(boolean isVisible) {
        clueButton.setVisible(isVisible);
        hintButton.setVisible(isVisible);
    }

    public void setRoomInfoLabel(String text) { roomInfoLabel.setText(text); }

    public JTextField getInputField() { 
        return inputField;   
    }
    public JLabel getTimerLabel() { 
        return timerLabel;   
    }
    public JButton getSubmitButton() { 
        return submitButton; 
    }
    public JButton getClueButton() { 
        return clueButton;   
    }
    public JButton getHintButton() { 
        return hintButton;   
    }
}