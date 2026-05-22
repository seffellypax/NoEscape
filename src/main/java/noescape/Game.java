package noescape;

/**
 * Core game controller that manages state transitions, player lifecycle,
 * room progression, and the main game loop.
 *
 * OOP Principles Demonstrated:
 *   Encapsulation - private fields exposed only through controlled methods
 *   Polymorphism  - roomSequence[] stored as Escapable type
 *   Abstraction   - Escapable interface defines the room contract
 *   Inheritance   - SplashPanel extends JPanel
 */
public class Game {

    private GameWindow gameWindow;
    private GameDisplay gameDisplay;
    private Player currentPlayer;
    private Escapable[] roomSequence;
    private int activeRoomIndex;
    private GameController gameController;
    private TimerSystem countdownTimer;
    private GameState currentState;
    private EnvironmentLoader environmentLoader;

    public Game() {
        environmentLoader = new EnvironmentLoader(".env");
        gameController    = new GameController();
        activeRoomIndex   = 0;
        currentState      = GameState.ENTER_NAME;

        gameWindow  = new GameWindow();
        gameDisplay = new GameDisplay(
            gameWindow.getDisplayArea(),
            gameWindow.getTimerLabel(),
            gameWindow
        );

        gameWindow.attachListeners(
            event -> processPlayerInput(),
            event -> onClueButtonPressed(),
            event -> onHintButtonPressed()
        );

        showNameEntryScreen();
        startGameLoop();
    }

    // -------------------------------------------------------------------------
    // Screen Transitions
    // -------------------------------------------------------------------------

    private void showNameEntryScreen() {
        currentState = GameState.ENTER_NAME;
        gameDisplay.showEnterName();
        gameDisplay.setTimerText("", GameWindow.COLOR_DIMMED);
        gameWindow.setInputEnabled(false);
        gameWindow.setClueHintVisible(false);
        gameWindow.getSubmitButton().setEnabled(true);
        gameWindow.getInputField().setEnabled(true);
        gameWindow.getInputField().setText("");
        gameWindow.getInputField().requestFocus();
    }

    private void showCourseSelectionScreen() {
        currentState = GameState.CHOOSE_COURSE;
        gameDisplay.showChooseCourse(
            currentPlayer.getName(),
            event -> selectCourse("Computer Science"),
            event -> selectCourse("Nursing")
        );
        gameWindow.setInputEnabled(false);
        gameWindow.setClueHintVisible(false);
        gameWindow.getSubmitButton().setEnabled(true);
        gameWindow.getInputField().setEnabled(true);
        gameWindow.getInputField().setText("");
        gameWindow.getInputField().requestFocus();
    }

    private void selectCourse(String selectedCourse) {
        currentPlayer = new Player(currentPlayer.getName(), selectedCourse);
        showSplashScreen();
    }

    private void showSplashScreen() {
        currentState = GameState.SPLASH;
        SplashPanel.showSplashDialog(currentPlayer.getName(), currentPlayer.getCourse());
        gameDisplay.showSplash(currentPlayer);
        gameDisplay.setTimerText("", GameWindow.COLOR_DIMMED);
        gameWindow.setInputEnabled(false);
        gameWindow.setClueHintVisible(false);
        gameWindow.getSubmitButton().setEnabled(true);
        gameWindow.getInputField().setEnabled(true);
        gameWindow.getInputField().setText("");
        gameWindow.getInputField().requestFocus();
    }

    // -------------------------------------------------------------------------
    // Game Loop
    // -------------------------------------------------------------------------

    private void startGameLoop() {
        javax.swing.Timer gameLoop = new javax.swing.Timer(1000, event -> onTick());
        gameLoop.start();
    }

    private void onTick() {
        if (currentState != GameState.PLAYING) return;
        gameDisplay.updateTimer(countdownTimer.getSecondsRemaining());
        if (countdownTimer.hasTimeExpired()) triggerLoopFailed();
    }

    // -------------------------------------------------------------------------
    // Game Initialization
    // -------------------------------------------------------------------------

    private void startGame() {
        roomSequence = buildRoomSequence();
        int totalSeconds = environmentLoader.getInt("TIMER_SECONDS", 120)
                         + currentPlayer.getBonusSeconds();
        countdownTimer = new TimerSystem(totalSeconds);
        currentState   = GameState.PLAYING;
        countdownTimer.start();
        gameWindow.setClueHintVisible(true);
        gameController.sendMessage("The loop has begun. Find a way out, " + currentPlayer.getName() + ".");
        loadRoom(0);
    }

    private Escapable[] buildRoomSequence() {
        String coursePrefix = currentPlayer.getCourse().contains("Nursing") ? "NR_" : "CS_";

        return new Escapable[]{
            new Classroom(
                environmentLoader.get(coursePrefix + "ROOM1_NAME",   "Classroom 101"),
                false,
                environmentLoader.get(coursePrefix + "ROOM1_PUZZLE", "What do nurses measure to check how fast the heart is beating?"),
                environmentLoader.get(coursePrefix + "ROOM1_ANSWER", "pulse"),
                environmentLoader.get(coursePrefix + "ROOM1_CLUE",   "You can feel it on the wrist or the neck."),
                environmentLoader.get(coursePrefix + "ROOM1_HINT",   "It starts with the letter P.")
            ),
            new LibraryRoom(
                environmentLoader.get(coursePrefix + "ROOM2_NAME",   "Library"),
                true,
                environmentLoader.get(coursePrefix + "ROOM2_PUZZLE", "What is the normal human body temperature in Celsius?"),
                environmentLoader.get(coursePrefix + "ROOM2_ANSWER", "37"),
                environmentLoader.get(coursePrefix + "ROOM2_CLUE",   "It is the standard temperature a nurse checks with a thermometer."),
                environmentLoader.get(coursePrefix + "ROOM2_HINT",   "It is a two-digit number between 36 and 38.")
            ),
            new TsgRoom(
                environmentLoader.get(coursePrefix + "ROOM3_NAME",   "TSG"),
                true,
                environmentLoader.get(coursePrefix + "ROOM3_PUZZLE", "What do nurses use to record and access patient medical information digitally?"),
                environmentLoader.get(coursePrefix + "ROOM3_ANSWER", "EMR"),
                environmentLoader.get(coursePrefix + "ROOM3_CLUE",   "TSG manages the hospital software system nurses use every day."),
                environmentLoader.get(coursePrefix + "ROOM3_HINT",   "Three letters. Stands for Electronic Medical Records.")
            ),
            new SecurityOfficeRoom(
                environmentLoader.get(coursePrefix + "ROOM4_NAME",   "Security Office"),
                true,
                environmentLoader.get(coursePrefix + "ROOM4_PUZZLE", "What is the name of the oath nurses take, named after a famous nurse?"),
                environmentLoader.get(coursePrefix + "ROOM4_ANSWER", "nightingale pledge"),
                environmentLoader.get(coursePrefix + "ROOM4_CLUE",   "Named after the founder of modern nursing."),
                environmentLoader.get(coursePrefix + "ROOM4_HINT",   "Two words. Think of Florence __________.")
            )
        };
    }

    // -------------------------------------------------------------------------
    // Room Navigation
    // -------------------------------------------------------------------------

    private void loadRoom(int roomIndex) {
        activeRoomIndex = roomIndex;
        Escapable targetRoom = roomSequence[roomIndex];
        gameDisplay.showRoom(
            targetRoom,
            roomIndex,
            roomSequence.length,
            currentPlayer,
            gameController.getMessage(),
            roomSequence,
            roomIndex
        );
        gameWindow.setInputEnabled(true);
    }

    // -------------------------------------------------------------------------
    // Input Handling
    // -------------------------------------------------------------------------

    private void processPlayerInput() {
        String rawInput = gameWindow.getInputField().getText().trim();
        gameWindow.getInputField().setText("");
        if (rawInput.isEmpty()) return;

        switch (currentState) {
            case ENTER_NAME   -> handleNameEntry(rawInput);
            case CHOOSE_COURSE -> handleCourseSelection(rawInput);
            case SPLASH       -> handleSplashInput(rawInput);
            case WIN, LOOP    -> handleEndScreenInput(rawInput);
            case PLAYING      -> handleGameplayInput(rawInput);
        }
    }

    private void handleNameEntry(String nameInput) {
        currentPlayer = new Player(nameInput, "Computer Science");
        showCourseSelectionScreen();
    }

    private void handleCourseSelection(String courseInput) {
        if (courseInput.equals("1"))      selectCourse("Computer Science");
        else if (courseInput.equals("2")) selectCourse("Nursing");
        else gameDisplay.showFeedback("Type  1  or  2  to choose your course.", GameWindow.COLOR_YELLOW);
    }

    private void handleSplashInput(String splashInput) {
        if (splashInput.equalsIgnoreCase("start")) startGame();
        else gameDisplay.showFeedback("Click START to begin.", GameWindow.COLOR_YELLOW);
    }

    private void handleEndScreenInput(String endInput) {
        if (endInput.equalsIgnoreCase("restart")) restartGame();
        else gameDisplay.showFeedback("Click the button to play again.", GameWindow.COLOR_YELLOW);
    }

    private void handleGameplayInput(String playerInput) {
        if (playerInput.matches("[1-4]")) {
            int selectedRoomIndex = Integer.parseInt(playerInput) - 1;
            Escapable selectedRoom = roomSequence[selectedRoomIndex];

            if (!selectedRoom.isLocked() || selectedRoom.isSolved()) {
                loadRoom(selectedRoomIndex);
            } else {
                gameDisplay.showFeedback("Room " + playerInput + " is still locked.", GameWindow.COLOR_RED);
            }
            return;
        }

        Escapable activeRoom = roomSequence[activeRoomIndex];

        if (activeRoom.isSolved()) {
            gameDisplay.showFeedback("Already solved! Move to the next room.", GameWindow.COLOR_GREEN);
            return;
        }
        if (activeRoom.isLocked()) {
            gameDisplay.showFeedback("This room is locked.", GameWindow.COLOR_RED);
            return;
        }
        if (activeRoom.getAttempts() >= currentPlayer.getMaxAttempts()) {
            gameDisplay.showFeedback("No more attempts. Press Hint for help.", GameWindow.COLOR_RED);
            return;
        }

        activeRoom.checkAnswer(playerInput);

        if (activeRoom.isSolved()) {
            gameDisplay.showFeedback("✓  Correct!  " + activeRoom.getLastMessage(), GameWindow.COLOR_GREEN);
            onRoomSolved();
        } else {
            gameDisplay.showFeedback(
                "✗  " + activeRoom.getLastMessage()
                    + "  (" + activeRoom.getAttempts() + "/" + currentPlayer.getMaxAttempts() + ")",
                GameWindow.COLOR_RED
            );
        }
    }

    // -------------------------------------------------------------------------
    // Room Solved Logic
    // -------------------------------------------------------------------------

    private void onRoomSolved() {
        if (activeRoomIndex + 1 < roomSequence.length) {
            roomSequence[activeRoomIndex + 1].unlock();
            gameController.sendMessage("Unlocked: " + roomSequence[activeRoomIndex + 1].getName());
        }
        currentPlayer.setProgress(currentPlayer.getProgress() + 1);

        if (areAllRoomsSolved()) {
            triggerWin();
        } else {
            javax.swing.Timer delayTimer = new javax.swing.Timer(900, event -> loadRoom(activeRoomIndex + 1));
            delayTimer.setRepeats(false);
            delayTimer.start();
        }
    }

    // -------------------------------------------------------------------------
    // Button Actions
    // -------------------------------------------------------------------------

    private void onClueButtonPressed() {
        if (currentState != GameState.PLAYING) return;
        Escapable activeRoom = roomSequence[activeRoomIndex];
        activeRoom.showClue();
        gameDisplay.showFeedback("🔍  " + activeRoom.getLastMessage(), GameWindow.COLOR_CYAN);
    }

    private void onHintButtonPressed() {
        if (currentState != GameState.PLAYING) return;
        Escapable activeRoom = roomSequence[activeRoomIndex];
        activeRoom.showHint();

        // Polymorphic side-effect: SecurityOfficeRoom re-locks itself when a hint is used.
        // We immediately re-unlock it so the player can keep playing, but the feedback
        // message (set inside showHint()) already communicates the lockdown penalty.
        if (activeRoom.isLocked()) {
            activeRoom.unlock();
        }

        gameDisplay.showFeedback("💡  " + activeRoom.getLastMessage(), GameWindow.COLOR_YELLOW);
    }

    // -------------------------------------------------------------------------
    // Win / Fail / Restart
    // -------------------------------------------------------------------------

    private void triggerWin() {
        countdownTimer.stop();
        gameController.overrideLoop();
        currentState = GameState.WIN;
        gameDisplay.showWin(currentPlayer, countdownTimer.getSecondsRemaining(), gameController.getMessage());
        gameDisplay.setTimerText("ESCAPED!", GameWindow.COLOR_GREEN);
        gameWindow.setInputEnabled(false);
        gameWindow.setClueHintVisible(false);
        gameWindow.getSubmitButton().setEnabled(true);
        gameWindow.getInputField().setEnabled(true);
    }

    private void triggerLoopFailed() {
        countdownTimer.stop();
        gameController.sendMessage("The loop resets. Try again.");
        currentState = GameState.LOOP;
        gameDisplay.showLoop(currentPlayer, gameController.getMessage());
        gameDisplay.setTimerText("FAILED!", GameWindow.COLOR_RED);
        gameWindow.setInputEnabled(false);
        gameWindow.setClueHintVisible(false);
        gameWindow.getSubmitButton().setEnabled(true);
        gameWindow.getInputField().setEnabled(true);
    }

    private void restartGame() {
        gameController.resetGame();
        activeRoomIndex = 0;
        currentPlayer.reset();
        showNameEntryScreen();
    }

    private boolean areAllRoomsSolved() {
        for (Escapable room : roomSequence) {
            if (!room.isSolved()) return false;
        }
        return true;
    }
}