package noescape;
import javax.swing.*;
import java.awt.*;

public class GameGUI extends JFrame{

    // ── Reference to game logic ─────────────────────────────
    private Game game;

    // ── Swing components ────────────────────────────────────
    private JFrame window;
    private JTextArea  displayArea;
    private JTextField inputField;
    private JButton submitButton;
    private JButton clueButton;
    private JButton hintButton;
    private JLabel roomLabel;
    private JLabel timerLabel;

    // Ticks every second to update the countdown display
    private javax.swing.Timer countdownTimer;

    // ── Constructor ─────────────────────────────────────────
    public GameGUI(Game game) {
        this.game = game;
        buildWindow();
        startCountdown();
        loadCurrentRoom();
    }

    // ── buildWindow() ───────────────────────────────────────
    // Assembles the full window from its three sections.
    private void buildWindow() {
        window = new JFrame("No Escape!");
        window.setSize(650, 500);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLayout(new BorderLayout(0, 0));
        window.setResizable(false);
        window.setLocationRelativeTo(null);

        window.add(buildTopBar(),      BorderLayout.NORTH);
        window.add(buildDisplayArea(), BorderLayout.CENTER);
        window.add(buildBottomPanel(), BorderLayout.SOUTH);

        window.setVisible(true);
    }

    // ── buildTopBar() ───────────────────────────────────────
    // Shows the current room name on the left and timer on the right.
    private JPanel buildTopBar() {
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(new Color(25, 25, 35));
        topBar.setBorder(BorderFactory.createEmptyBorder(8, 14, 8, 14));

        roomLabel = new JLabel("Room: " + game.getCurrentRoom().getName());
        roomLabel.setForeground(new Color(80, 220, 200));
        roomLabel.setFont(new Font("Consolas", Font.BOLD, 14));

        timerLabel = new JLabel("Time: 5:00");
        timerLabel.setForeground(new Color(255, 210, 60));
        timerLabel.setFont(new Font("Consolas", Font.BOLD, 14));
        timerLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        topBar.add(roomLabel,  BorderLayout.WEST);
        topBar.add(timerLabel, BorderLayout.EAST);

        return topBar;
    }

    // ── buildDisplayArea() ──────────────────────────────────
    // The scrollable text area where the puzzle and all feedback appear.
    private JScrollPane buildDisplayArea() {
        displayArea = new JTextArea();
        displayArea.setEditable(false);
        displayArea.setLineWrap(true);
        displayArea.setWrapStyleWord(true);
        displayArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        displayArea.setBackground(new Color(15, 15, 20));
        displayArea.setForeground(new Color(215, 215, 215));
        displayArea.setBorder(BorderFactory.createEmptyBorder(12, 14, 12, 14));

        return new JScrollPane(displayArea);
    }

    // ── buildBottomPanel() ──────────────────────────────────
    // Two rows: clue/hint buttons on top, answer input below.
    private JPanel buildBottomPanel() {
        JPanel bottomPanel = new JPanel(new BorderLayout(0, 4));
        bottomPanel.setBackground(new Color(20, 20, 28));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(6, 10, 10, 10));

        // Row 1: Clue and Hint buttons
        clueButton = new JButton("Show Clue");
        hintButton = new JButton("Show Hint");
        styleButton(clueButton, new Color(0, 100, 140));
        styleButton(hintButton, new Color(120, 80, 0));

        JPanel buttonRow = new JPanel(new GridLayout(1, 2, 8, 0));
        buttonRow.setBackground(new Color(20, 20, 28));
        buttonRow.add(clueButton);
        buttonRow.add(hintButton);

        // Row 2: Answer input field and Submit button
        inputField = new JTextField();
        inputField.setFont(new Font("Consolas", Font.PLAIN, 14));
        inputField.setBackground(new Color(35, 35, 45));
        inputField.setForeground(Color.WHITE);
        inputField.setCaretColor(Color.WHITE);
        inputField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(70, 70, 90)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        submitButton = new JButton("Submit");
        styleButton(submitButton, new Color(0, 130, 60));
        submitButton.setPreferredSize(new Dimension(90, 36));

        JPanel inputRow = new JPanel(new BorderLayout(6, 0));
        inputRow.setBackground(new Color(20, 20, 28));
        inputRow.add(inputField,   BorderLayout.CENTER);
        inputRow.add(submitButton, BorderLayout.EAST);

        bottomPanel.add(buttonRow, BorderLayout.NORTH);
        bottomPanel.add(inputRow,  BorderLayout.SOUTH);

        // ── Wire up actions ──────────────────────
        clueButton.addActionListener(e -> onClueClicked());
        hintButton.addActionListener(e -> onHintClicked());
        submitButton.addActionListener(e -> onSubmitClicked());
        inputField.addActionListener(e -> onSubmitClicked()); // Enter key also submits

        return bottomPanel;
    }

    // ── styleButton() ───────────────────────────────────────
    // Applies a consistent look to every button.
    private void styleButton(JButton button, Color bgColor) {
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Consolas", Font.BOLD, 13));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
    }

    // ── loadCurrentRoom() ───────────────────────────────────
    // Clears the display and shows the current room's puzzle.
    // Called when the game starts and after each correct answer.
    private void loadCurrentRoom() {
        Room room = game.getCurrentRoom();

        roomLabel.setText("Room " + (game.getCurrentRoomIndex() + 1) +
                          " of " + game.getRooms().length +
                          ":   " + room.getName());

        displayArea.setText("");

        print("============================================");
        print("  " + room.getName().toUpperCase());
        print("============================================");
        print("");

        // POLYMORPHISM: getPuzzle() runs the correct version
        // for whichever room (Library, TSG, or SecurityOffice) it is
        print(room.getPuzzle());
        print("");
        print("Attempts left: " + game.getAttemptsLeft());
        print("");
        print("Type your answer and press Submit.");
        print("Need help? Use Show Clue or Show Hint.");
        print("");
    }

    // ── onSubmitClicked() ───────────────────────────────────
    // Called when the player presses Submit or hits Enter.
    private void onSubmitClicked() {
        if (game.isGameOver() || game.isGameWon()) return;

        String answer = inputField.getText().trim();
        if (answer.isEmpty()) return;

        inputField.setText("");
        print("> " + answer);

        boolean correct = game.submitAnswer(answer);

        if (correct) {
            print("Correct! Well done.");
            print("");

            if (game.isGameWon()) {
                showEndMessage(true, "YOU ESCAPED!");
            } else {
                print("Moving to the next room...");
                print("");
                loadCurrentRoom();
            }

        } else {
            if (game.isGameOver()) {
                print("Wrong answer — no attempts left.");
                showEndMessage(false, "GAME OVER — Out of attempts.");
            } else {
                print("Wrong answer. " + game.getAttemptsLeft() + " attempt(s) left.");
            }
        }
    }

    // ── onClueClicked() ─────────────────────────────────────
    private void onClueClicked() {
        if (game.isGameOver() || game.isGameWon()) return;
        print("");
        // POLYMORPHISM: getClue() runs the correct version for this room
        print(game.getCurrentRoom().getClue());
        print("");
    }

    // ── onHintClicked() ─────────────────────────────────────
    private void onHintClicked() {
        if (game.isGameOver() || game.isGameWon()) return;
        print("");
        // POLYMORPHISM: getHint() runs the correct version for this room
        print(game.getCurrentRoom().getHint());
        print("");
    }

    // ── startCountdown() ────────────────────────────────────
    // Fires every second to update the timer label.
    private void startCountdown() {
        countdownTimer = new javax.swing.Timer(1000, e -> {
            game.checkTimer();

            int seconds = game.getTimerSystem().getSecondsRemaining();
            int min     = seconds / 60;
            int sec     = seconds % 60;
            timerLabel.setText(String.format("Time: %d:%02d", min, sec));

            // Timer turns red when under 60 seconds
            timerLabel.setForeground(seconds <= 60
                ? Color.RED
                : new Color(255, 210, 60));

            if (game.isGameOver()) {
                countdownTimer.stop();
                showEndMessage(false, "GAME OVER — Time's up!");
            }
        });
        countdownTimer.start();
    }

    // ── showEndMessage() ────────────────────────────────────
    // Displays a win or lose message and locks the input.
    private void showEndMessage(boolean won, String message) {
        countdownTimer.stop();
        lockInput();
        print("");
        print("********************************************");
        print("  " + message);
        if (won) {
            print("  Player: " + game.getPlayer().getName());
        }
        print("********************************************");
    }

    // ── lockInput() ─────────────────────────────────────────
    // Disables all input controls when the game ends.
    private void lockInput() {
        inputField.setEditable(false);
        submitButton.setEnabled(false);
        clueButton.setEnabled(false);
        hintButton.setEnabled(false);
    }

    // ── print() ─────────────────────────────────────────────
    // Appends a line of text to the display area.
    private void print(String text) {
        displayArea.append(text + "\n");
        displayArea.setCaretPosition(displayArea.getDocument().getLength());
    }
}