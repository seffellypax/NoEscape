import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.Timer;

// --- Interfaces (from UML) ---

interface Location {
    void puzzle();
    void timerPuzzle();
    void findClue();
    void hint();
    void attempt();
    void gameOver();
}

// --- Player ---

class Player {
    String name;
    int progress;
    String course;
    int bonusSeconds;  // extra time added to timer
    int maxAttempts;   // max attempts allowed per room

    Player(String name, String course) {
        this.name     = name;
        this.course   = course;
        this.progress = 0;

        // Course bonuses
        if (course.contains("Computer Science")) {
            this.bonusSeconds = 20;
            this.maxAttempts  = 3;
        } else if (course.contains("Nursing")) {
            this.bonusSeconds = 0;
            this.maxAttempts  = 5;
        } else {
            this.bonusSeconds = 0;
            this.maxAttempts  = 3;
        }
    }
}

// --- TimerSystem ---

class TimerSystem {
    long startTime;
    int timeLimit;
    boolean running;

    TimerSystem(int timeLimitSeconds) {
        this.timeLimit = timeLimitSeconds;
        this.running   = false;
    }

    void start() {
        startTime = System.currentTimeMillis();
        running   = true;
    }

    boolean hasTimeExpired() {
        if (!running) return false;
        return getSecondsRemaining() <= 0;
    }

    int getSecondsRemaining() {
        long elapsed = (System.currentTimeMillis() - startTime) / 1000;
        return (int) Math.max(0, timeLimit - elapsed);
    }
}

// --- Administrator ---

class Administrator {
    String  message;
    boolean isOverridden;

    Administrator() {
        this.message      = "";
        this.isOverridden = false;
    }

    void sendMessage(String msg) { this.message = msg; }
    void resetGame()             { isOverridden = false; }
}

// --- Room Definitions ---

class RoomData {
    String   name;
    String   description;
    String[] puzzleLines;
    String   answer;
    String   hint;
    String   clue;
    String   successMsg;
    Color    color;

    RoomData(String name, String desc, String[] puzzle, String answer,
             String hint, String clue, String success, Color color) {
        this.name        = name;
        this.description = desc;
        this.puzzleLines = puzzle;
        this.answer      = answer;
        this.hint        = hint;
        this.clue        = clue;
        this.successMsg  = success;
        this.color       = color;
    }
}

// --- Main Game Window ---

public class Game extends JFrame {

    // Palette
    static final Color BG_DARK     = new Color(12, 10, 18);
    static final Color BG_MID      = new Color(22, 18, 35);
    static final Color BG_CARD     = new Color(30, 25, 48);
    static final Color ACCENT_CYAN = new Color(0, 220, 200);
    static final Color ACCENT_GOLD = new Color(255, 195, 50);
    static final Color ACCENT_RED  = new Color(220, 60, 80);
    static final Color TEXT_MAIN   = new Color(230, 225, 245);
    static final Color TEXT_DIM    = new Color(140, 130, 165);

    // Rooms
    static final RoomData[] ROOMS = {
        new RoomData(
            "The Library",
            "Dusty shelves tower around you. A coded message is carved into the desk...",
            new String[]{
                "Decode the Caesar cipher (shift 3):",
                "",
                "  HVFDSH  WKH  URRP",
                "",
                "What does it say? (3 words, no spaces)"
            },
            "ESCAPTHEROOM",
            "Each letter is shifted forward by 3 in the alphabet. H->E, V->S...",
            "You find a crumpled note: 'The keypad in Security needs a year.'",
            "The bookshelf slides open revealing a hidden passage!",
            new Color(30, 60, 100)
        ),
        new RoomData(
            "The TSG Lab",
            "Blinking servers fill the room. A terminal shows a logic puzzle...",
            new String[]{
                "Solve the sequence to unlock the door:",
                "",
                "  2, 6, 12, 20, 30, ?",
                "",
                "Enter the next number:"
            },
            "42",
            "Look at the differences: 4, 6, 8, 10, 12... each gap increases by 2.",
            "A post-it reads: 'Server room password rotates every prime number of days.'",
            "Access granted! The terminal opens a ventilation shaft above you!",
            new Color(20, 65, 45)
        ),
        new RoomData(
            "Security Office",
            "Monitors everywhere. A keypad guards the final exit door...",
            new String[]{
                "The keypad has 4 digits.",
                "",
                "CLUE: 'Two score and two' centuries",
                "      plus the Fibonacci 7th term.",
                "",
                "Enter the 4-digit code:"
            },
            "2021",
            "7th Fibonacci = 13. Think: 2008 + 13 = 2021",
            "You did it! The final door clicks open.",
            "ESCAPED! The alarm silences. You are FREE!",
            new Color(70, 25, 35)
        )
    };

    // Game state
    Player        player;
    TimerSystem   timerSystem;
    Administrator admin;
    int     currentRoom    = 0;
    int     totalAttempts  = 0;   // overall attempts across all rooms
    int     roomAttempts   = 0;   // attempts used in current room
    int     hintsUsed      = 0;
    boolean clueFound      = false;
    boolean gameWon        = false;
    boolean gameOver       = false;

    // UI components
    CardLayout cardLayout;
    JPanel     mainPanel;
    JLabel     timerLabel, roomLabel, descLabel, progressLabel, statusLabel, attemptsLabel;
    JTextArea  puzzleArea, logArea;
    JTextField answerField;
    Timer      swingTimer;

    // Particles
    java.util.List<float[]> particles = new ArrayList<>();
    Random rand = new Random();

    // ── Entry point ──────────────────────────
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Game().setVisible(true));
    }

    Game() {
        setTitle("No Escape - Can You Break Free?");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900, 650);
        setMinimumSize(new Dimension(800, 580));
        setLocationRelativeTo(null);

        timerSystem = new TimerSystem(300);
        admin       = new Administrator();

        cardLayout = new CardLayout();
        mainPanel  = new JPanel(cardLayout);
        mainPanel.setBackground(BG_DARK);

        mainPanel.add(buildSplashPanel(), "SPLASH");
        mainPanel.add(buildSetupPanel(),  "SETUP");
        mainPanel.add(buildGamePanel(),   "GAME");
        mainPanel.add(buildEndPanel(),    "END");

        add(mainPanel);
        cardLayout.show(mainPanel, "SPLASH");

        for (int i = 0; i < 60; i++) spawnParticle();
    }

    // ── Splash Screen ────────────────────────
    JPanel buildSplashPanel() {
        JPanel p = new JPanel(null) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0,0,BG_DARK,getWidth(),getHeight(),new Color(18,10,40));
                g2.setPaint(gp);
                g2.fillRect(0,0,getWidth(),getHeight());
                g2.setColor(new Color(80,60,120,30));
                for (int x=0;x<getWidth();x+=40) g2.drawLine(x,0,x,getHeight());
                for (int y=0;y<getHeight();y+=40) g2.drawLine(0,y,getWidth(),y);
                for (int r=200;r>0;r-=10) {
                    float alpha = (200-r)/2000f;
                    g2.setColor(new Color(0f,0.85f,0.78f,alpha));
                    g2.fillOval(getWidth()/2-r,getHeight()/2-r-60,r*2,r*2);
                }
                g2.setColor(new Color(0,220,200,120));
                for (float[] pt : particles)
                    g2.fillOval((int)pt[0],(int)pt[1],2,2);
            }
        };
        p.setBackground(BG_DARK);

        JLabel title = styled("NO ESCAPE", 52, Font.BOLD, ACCENT_CYAN);
        title.setBounds(0, 130, 900, 70);
        title.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel sub = styled("An Escape Room Experience", 18, Font.PLAIN, TEXT_DIM);
        sub.setBounds(0, 210, 900, 30);
        sub.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel tagline = styled("3 Rooms | 5 Minutes | 1 Chance for Survival", 13, Font.PLAIN, new Color(100,90,130));
        tagline.setBounds(0, 248, 900, 24);
        tagline.setHorizontalAlignment(SwingConstants.CENTER);

        JButton start = glowButton("START GAME", ACCENT_CYAN, BG_DARK);
        start.setBounds(330, 320, 240, 52);
        start.addActionListener(e -> cardLayout.show(mainPanel, "SETUP"));

        JLabel credit = styled("Based on NoEscape UML Design", 11, Font.PLAIN, new Color(70,65,90));
        credit.setBounds(0, 560, 900, 20);
        credit.setHorizontalAlignment(SwingConstants.CENTER);

        p.add(title); p.add(sub); p.add(tagline); p.add(start); p.add(credit);

        Timer pt = new Timer(30, e -> {
            for (float[] par : particles) {
                par[0] += par[2]; par[1] += par[3];
                if (par[0]<0||par[0]>900||par[1]<0||par[1]>650) {
                    par[0]=rand.nextInt(900); par[1]=rand.nextInt(650);
                }
            }
            p.repaint();
        });
        pt.start();

        return p;
    }

    // ── Setup / Character Creation Screen ────
    JPanel buildSetupPanel() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(BG_DARK);

        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(BG_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0,180,160,80), 1),
            BorderFactory.createEmptyBorder(36, 44, 36, 44)
        ));

        GridBagConstraints gc = new GridBagConstraints();
        gc.insets    = new Insets(8, 0, 8, 0);
        gc.fill      = GridBagConstraints.HORIZONTAL;
        gc.gridx     = 0;
        gc.gridwidth = 2;

        JLabel h = styled("CREATE YOUR PLAYER", 22, Font.BOLD, ACCENT_CYAN);
        h.setHorizontalAlignment(SwingConstants.CENTER);
        gc.gridy = 0; card.add(h, gc);

        JLabel sub2 = styled("Who dares to escape?", 13, Font.PLAIN, TEXT_DIM);
        sub2.setHorizontalAlignment(SwingConstants.CENTER);
        gc.gridy = 1; card.add(sub2, gc);

        // Name field
        gc.insets = new Insets(14, 0, 4, 0);
        gc.gridy  = 2; card.add(label("Your Name"), gc);
        JTextField nameF = darkField();
        gc.insets = new Insets(0, 0, 10, 0);
        gc.gridy  = 3; card.add(nameF, gc);

        // Course dropdown (2 courses only)
        gc.insets = new Insets(6, 0, 4, 0);
        gc.gridy  = 4; card.add(label("Your Course"), gc);
        JComboBox<String> courseBox = darkCombo(
            "Bachelor of Science in Computer Science",
            "Bachelor of Science in Nursing"
        );
        gc.insets = new Insets(0, 0, 6, 0);
        gc.gridy  = 5; card.add(courseBox, gc);

        // Bonus hint label (updates when course changes)
        JLabel bonusHint = styled("Bonus: +20 seconds to the timer", 11, Font.ITALIC, ACCENT_GOLD);
        bonusHint.setHorizontalAlignment(SwingConstants.CENTER);
        gc.insets = new Insets(0, 0, 16, 0);
        gc.gridy  = 6; card.add(bonusHint, gc);

        courseBox.addActionListener(e -> {
            String sel = (String) courseBox.getSelectedItem();
            if (sel.contains("Computer Science"))
                bonusHint.setText("Bonus: +20 seconds to the timer  (5:20 total)");
            else if (sel.contains("Nursing"))
                bonusHint.setText("Bonus: +2 attempts per room  (5 attempts each)");
            else
                bonusHint.setText("No bonus.");
        });

        JButton go = glowButton("ENTER THE ROOM", ACCENT_GOLD, new Color(40,30,5));
        gc.insets = new Insets(8, 0, 0, 0);
        gc.gridy  = 7; card.add(go, gc);

        go.addActionListener(e -> {
            String nm = nameF.getText().trim();
            if (nm.isEmpty()) { nameF.setBorder(BorderFactory.createLineBorder(ACCENT_RED,2)); return; }
            player = new Player(nm, (String) courseBox.getSelectedItem());
            startGame();
        });

        p.add(card);
        return p;
    }

    // ── Main Game Screen ─────────────────────
    JPanel buildGamePanel() {
        JPanel root = new JPanel(new BorderLayout(0, 0));
        root.setBackground(BG_DARK);

        // TOP BAR
        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(BG_MID);
        top.setBorder(BorderFactory.createEmptyBorder(10,18,10,18));

        roomLabel = styled("Room 1/3", 15, Font.BOLD, ACCENT_CYAN);
        top.add(roomLabel, BorderLayout.WEST);

        timerLabel = styled("Time: 5:00", 17, Font.BOLD, ACCENT_GOLD);
        timerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        top.add(timerLabel, BorderLayout.CENTER);

        progressLabel = styled("[ ] [ ] [ ]", 18, Font.PLAIN, TEXT_DIM);
        progressLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        top.add(progressLabel, BorderLayout.EAST);

        root.add(top, BorderLayout.NORTH);

        // CENTER
        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.X_AXIS));
        center.setBackground(BG_DARK);

        // LEFT: puzzle area
        JPanel leftP = new JPanel(new BorderLayout(0,10));
        leftP.setBackground(BG_DARK);
        leftP.setBorder(BorderFactory.createEmptyBorder(16,16,8,8));

        descLabel = styled("", 13, Font.ITALIC, TEXT_DIM);
        descLabel.setBorder(BorderFactory.createEmptyBorder(0,0,8,0));
        leftP.add(descLabel, BorderLayout.NORTH);

        puzzleArea = new JTextArea();
        puzzleArea.setEditable(false);
        puzzleArea.setBackground(BG_CARD);
        puzzleArea.setForeground(TEXT_MAIN);
        puzzleArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        puzzleArea.setBorder(BorderFactory.createEmptyBorder(12,14,12,14));
        leftP.add(new JScrollPane(puzzleArea), BorderLayout.CENTER);

        JPanel answerRow = new JPanel(new BorderLayout(8,0));
        answerRow.setBackground(BG_DARK);
        answerRow.setBorder(BorderFactory.createEmptyBorder(8,0,0,0));
        answerField = darkField();
        answerField.addActionListener(e -> handleSubmit());
        JButton subBtn = glowButton("Submit", ACCENT_CYAN, BG_DARK);
        subBtn.setPreferredSize(new Dimension(100, 36));
        subBtn.addActionListener(e -> handleSubmit());
        answerRow.add(answerField, BorderLayout.CENTER);
        answerRow.add(subBtn, BorderLayout.EAST);
        leftP.add(answerRow, BorderLayout.SOUTH);
        center.add(leftP);

        // RIGHT: log + buttons
        JPanel rightP = new JPanel(new BorderLayout(0,8));
        rightP.setBackground(BG_DARK);
        rightP.setBorder(BorderFactory.createEmptyBorder(16,8,8,16));
        rightP.setPreferredSize(new Dimension(260, 0));

        statusLabel = styled("Solve the puzzle to advance...", 12, Font.PLAIN, TEXT_DIM);
        statusLabel.setBorder(BorderFactory.createEmptyBorder(0,0,2,0));
        rightP.add(statusLabel, BorderLayout.NORTH);

        // Attempts tracker label
        attemptsLabel = styled("Attempts: 0 / 3", 11, Font.PLAIN, TEXT_DIM);
        attemptsLabel.setBorder(BorderFactory.createEmptyBorder(0,0,4,0));

        JPanel topRight = new JPanel(new BorderLayout());
        topRight.setBackground(BG_DARK);
        topRight.add(statusLabel,   BorderLayout.NORTH);
        topRight.add(attemptsLabel, BorderLayout.SOUTH);
        rightP.add(topRight, BorderLayout.NORTH);

        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setBackground(BG_CARD);
        logArea.setForeground(TEXT_DIM);
        logArea.setFont(new Font("Monospaced", Font.PLAIN, 11));
        logArea.setLineWrap(true);
        logArea.setWrapStyleWord(true);
        logArea.setBorder(BorderFactory.createEmptyBorder(8,8,8,8));
        rightP.add(new JScrollPane(logArea), BorderLayout.CENTER);

        // Action buttons
        JPanel btnPanel = new JPanel(new GridLayout(2, 2, 8, 8));
        btnPanel.setBackground(BG_DARK);

        JButton hintBtn  = sideButton("Hint",      new Color(180,140,0));
        JButton clueBtn  = sideButton("Find Clue", new Color(0,150,180));
        JButton adminBtn = sideButton("Admin Msg", new Color(100,80,160));
        JButton resetBtn = sideButton("Restart",   new Color(160,50,60));

        hintBtn.addActionListener(e -> {
            hintsUsed++;
            log("HINT: " + ROOMS[currentRoom].hint);
            statusLabel.setText("Hint used. Keep thinking...");
        });
        clueBtn.addActionListener(e -> {
            clueFound = true;
            log("CLUE FOUND: " + ROOMS[currentRoom].clue);
            statusLabel.setText("Clue discovered!");
        });
        adminBtn.addActionListener(e -> {
            String[] msgs = {
                "\"The answers are within the puzzles themselves.\"",
                "\"Time is ticking. Focus.\"",
                "\"Think logically. Every cipher has a pattern.\"",
                "\"You're doing better than you think.\""
            };
            String msg = msgs[rand.nextInt(msgs.length)];
            admin.sendMessage(msg);
            log("ADMIN: " + msg);
        });
        resetBtn.addActionListener(e -> {
            int ok = JOptionPane.showConfirmDialog(this, "Restart the entire game?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (ok == JOptionPane.YES_OPTION) { admin.resetGame(); restartGame(); }
        });

        btnPanel.add(hintBtn); btnPanel.add(clueBtn);
        btnPanel.add(adminBtn); btnPanel.add(resetBtn);
        rightP.add(btnPanel, BorderLayout.SOUTH);

        center.add(rightP);
        root.add(center, BorderLayout.CENTER);
        return root;
    }

    JPanel buildEndPanel() {
        JPanel p = new JPanel(null) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D)g;
                g2.setColor(BG_DARK);
                g2.fillRect(0,0,getWidth(),getHeight());
            }
        };
        p.setBackground(BG_DARK);
        return p;
    }

    // ── Game Logic ───────────────────────────

    public void startGame() {
        currentRoom   = 0;
        totalAttempts = 0;
        roomAttempts  = 0;
        hintsUsed     = 0;
        clueFound     = false;
        gameWon       = false;
        gameOver      = false;

        // Apply course bonus to timer
        int baseTime = 300 + player.bonusSeconds; // 300s base + bonus
        timerSystem  = new TimerSystem(baseTime);
        timerSystem.start();

        cardLayout.show(mainPanel, "GAME");
        loadRoom();

        log("=== GAME START ===");
        log("Player  : " + player.name);
        log("Course : " + player.course);
        if (player.bonusSeconds > 0)
            log("Bonus  : +" + player.bonusSeconds + " seconds on the timer!");
        if (player.maxAttempts > 3)
            log("Bonus  : " + player.maxAttempts + " attempts per room!");
        log("Time   : " + (baseTime/60) + ":" + String.format("%02d", baseTime%60));
        log("-----------------");

        swingTimer = new Timer(500, e -> tickTimer());
        swingTimer.start();
    }

    void loadRoom() {
        RoomData r = ROOMS[currentRoom];
        roomAttempts = 0;

        roomLabel.setText("Room " + (currentRoom+1) + " / " + ROOMS.length + "  -  " + r.name);
        descLabel.setText("<html><i>" + r.description + "</i></html>");
        puzzleArea.setText(String.join("\n", r.puzzleLines));
        answerField.setText("");
        answerField.setBorder(BorderFactory.createLineBorder(new Color(60,50,90)));
        clueFound = false;

        String dots = "";
        for (int i=0; i<ROOMS.length; i++)
            dots += (i < currentRoom ? "[X]" : i == currentRoom ? "[*]" : "[ ]") + " ";
        progressLabel.setText(dots.trim());

        updateAttemptsLabel();
        statusLabel.setForeground(TEXT_DIM);
        statusLabel.setText("Solve the puzzle to advance...");
        log("\n[Entering: " + r.name + "]");
        log(r.description);
    }

    void handleSubmit() {
        if (gameOver || gameWon) return;
        String ans = answerField.getText().trim().toUpperCase().replaceAll("\\s+","");
        if (ans.isEmpty()) return;

        roomAttempts++;
        totalAttempts++;
        updateAttemptsLabel();

        RoomData r = ROOMS[currentRoom];

        if (ans.equals(r.answer.toUpperCase())) {
            log("CORRECT! " + r.successMsg);
            statusLabel.setForeground(ACCENT_CYAN);
            statusLabel.setText("Correct! Moving ahead...");
            answerField.setBorder(BorderFactory.createLineBorder(ACCENT_CYAN, 2));
            player.progress = currentRoom + 1;
            currentRoom++;
            Timer delay = new Timer(1000, e2 -> {
                if (currentRoom >= ROOMS.length) winGame();
                else { statusLabel.setForeground(TEXT_DIM); loadRoom(); }
            });
            delay.setRepeats(false);
            delay.start();
        } else {
            // Check if attempts are exhausted for this room
            if (roomAttempts >= player.maxAttempts) {
                log("Wrong: \"" + ans + "\" - No attempts left for this room!");
                swingTimer.stop();
                gameOver = true;
                log("\nOUT OF ATTEMPTS! Game Over.");
                showEndScreen(false);
            } else {
                int left = player.maxAttempts - roomAttempts;
                log("Wrong: \"" + ans + "\" - " + left + " attempt(s) left.");
                statusLabel.setForeground(ACCENT_RED);
                statusLabel.setText("Incorrect. " + left + " attempt(s) remaining.");
                answerField.setBorder(BorderFactory.createLineBorder(ACCENT_RED, 2));
                answerField.setText("");
            }
        }
    }

    void updateAttemptsLabel() {
        attemptsLabel.setText("Attempts this room: " + roomAttempts + " / " + player.maxAttempts);
        if (roomAttempts >= player.maxAttempts - 1 && roomAttempts > 0)
            attemptsLabel.setForeground(ACCENT_RED);
        else
            attemptsLabel.setForeground(TEXT_DIM);
    }

    void tickTimer() {
        if (gameWon || gameOver) return;
        int sec = timerSystem.getSecondsRemaining();
        int m = sec / 60, s = sec % 60;
        timerLabel.setText(String.format("Time: %d:%02d", m, s));
        if      (sec <= 60)  timerLabel.setForeground(ACCENT_RED);
        else if (sec <= 120) timerLabel.setForeground(new Color(255,150,50));
        else                 timerLabel.setForeground(ACCENT_GOLD);

        if (timerSystem.hasTimeExpired()) loseGame();
    }

    public void winGame() {
        gameWon = true;
        swingTimer.stop();
        showEndScreen(true);
    }

    void loseGame() {
        gameOver = true;
        swingTimer.stop();
        log("\nTIME'S UP! Game Over.");
        showEndScreen(false);
    }

    void showEndScreen(boolean won) {
        JPanel ep = (JPanel) mainPanel.getComponent(3);
        ep.removeAll();
        ep.setLayout(new GridBagLayout());

        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(BG_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(won ? ACCENT_CYAN : ACCENT_RED, 2),
            BorderFactory.createEmptyBorder(40, 60, 40, 60)
        ));

        GridBagConstraints g = new GridBagConstraints();
        g.gridx = 0; g.insets = new Insets(8,0,8,0);

        String titleTxt = won ? "YOU ESCAPED!" : "NO ESCAPE...";
        Color  tc       = won ? ACCENT_CYAN : ACCENT_RED;
        g.gridy = 0; card.add(styled(titleTxt, 32, Font.BOLD, tc), g);

        int baseTime = 300 + player.bonusSeconds;
        int timeUsed = baseTime - timerSystem.getSecondsRemaining();
        int m = timeUsed/60, s = timeUsed%60;

        String bonusDesc = player.bonusSeconds > 0
            ? "+" + player.bonusSeconds + "s timer"
            : player.maxAttempts > 3 ? "+" + (player.maxAttempts-3) + " attempts/room" : "None";

        String[] stats = {
            "Player  : " + player.name,
            "Course : " + player.course,
            "Bonus  : " + bonusDesc,
            "Rooms cleared  : " + Math.min(currentRoom, ROOMS.length) + " / " + ROOMS.length,
            "Total attempts : " + totalAttempts,
            "Hints used     : " + hintsUsed,
            "Time " + (won ? "used" : "elapsed") + "   : " + String.format("%d:%02d", m, s)
        };
        for (String stat : stats) {
            JLabel sl = styled(stat, 13, Font.PLAIN, TEXT_MAIN);
            sl.setHorizontalAlignment(SwingConstants.CENTER);
            g.gridy++; card.add(sl, g);
        }

        JButton again = glowButton("PLAY AGAIN", ACCENT_GOLD, new Color(40,30,5));
        g.gridy++; g.insets = new Insets(20,0,0,0); card.add(again, g);
        again.addActionListener(e -> cardLayout.show(mainPanel, "SETUP"));

        JButton exit = glowButton("EXIT", new Color(120,100,140), BG_DARK);
        g.gridy++; g.insets = new Insets(6,0,0,0); card.add(exit, g);
        exit.addActionListener(e -> System.exit(0));

        ep.add(card);
        ep.revalidate();
        ep.repaint();
        cardLayout.show(mainPanel, "END");
    }

    void restartGame() {
        if (swingTimer != null) swingTimer.stop();
        cardLayout.show(mainPanel, "SETUP");
    }

    void log(String msg) {
        logArea.append(msg + "\n");
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }

    // ── Particle helper ──────────────────────
    void spawnParticle() {
        float x  = rand.nextFloat()*900, y  = rand.nextFloat()*650;
        float vx = (rand.nextFloat()-0.5f)*0.8f, vy = (rand.nextFloat()-0.5f)*0.8f;
        particles.add(new float[]{x, y, vx, vy});
    }

    // ── UI helpers ───────────────────────────
    JLabel styled(String text, int size, int style, Color color) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", style, size));
        l.setForeground(color);
        return l;
    }

    JLabel label(String text) {
        return styled(text, 12, Font.BOLD, new Color(160,150,200));
    }

    JTextField darkField() {
        JTextField f = new JTextField(22);
        f.setBackground(new Color(18,14,30));
        f.setForeground(TEXT_MAIN);
        f.setCaretColor(ACCENT_CYAN);
        f.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(60,50,90)),
            BorderFactory.createEmptyBorder(6,10,6,10)
        ));
        return f;
    }

    JComboBox<String> darkCombo(String... items) {
        JComboBox<String> c = new JComboBox<>(items);
        c.setBackground(new Color(18,14,30));
        c.setForeground(TEXT_MAIN);
        c.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        ((JLabel)c.getRenderer()).setHorizontalAlignment(SwingConstants.LEFT);
        return c;
    }

    JButton glowButton(String text, Color fg, Color bg) {
        JButton b = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D)g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if      (getModel().isPressed())  g2.setColor(fg.darker());
                else if (getModel().isRollover()) g2.setColor(fg.darker().darker());
                else                              g2.setColor(bg);
                g2.fillRoundRect(0,0,getWidth(),getHeight(),10,10);
                g2.setColor(fg);
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(1,1,getWidth()-2,getHeight()-2,10,10);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int tx = (getWidth()-fm.stringWidth(getText()))/2;
                int ty = (getHeight()-fm.getHeight())/2 + fm.getAscent();
                g2.setColor(fg);
                g2.drawString(getText(), tx, ty);
            }
        };
        b.setFont(new Font("Segoe UI", Font.BOLD, 14));
        b.setForeground(fg);
        b.setBackground(bg);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setContentAreaFilled(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setPreferredSize(new Dimension(220, 44));
        return b;
    }

    JButton sideButton(String text, Color accent) {
        JButton b = glowButton(text, accent, BG_DARK);
        b.setPreferredSize(null);
        return b;
    }
}
