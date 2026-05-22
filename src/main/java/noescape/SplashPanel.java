package noescape;

import javax.swing.*;
import java.awt.*;

/**
 * Animated title splash screen rendered via {@code paintComponent()}.
 * Displayed as a centered modal dialog before the main window appears.
 *
 * OOP: Inheritance — extends JPanel and overrides {@code paintComponent()}.
 */
public class SplashPanel extends JPanel {

    private static final int PANEL_WIDTH  = 560;
    private static final int PANEL_HEIGHT = 340;

    private final String playerName;
    private final String playerCourse;
    private float glowAlpha = 0f;
    private Timer glowAnimationTimer;

    public SplashPanel(String playerName, String playerCourse) {
        this.playerName   = playerName;
        this.playerCourse = playerCourse;
        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        setBackground(new Color(10, 10, 20));
        startGlowAnimation();
    }

    private void startGlowAnimation() {
        glowAnimationTimer = new Timer(30, event -> {
            glowAlpha = Math.min(1f, glowAlpha + 0.04f);
            repaint();
        });
        glowAnimationTimer.start();
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D g2d = (Graphics2D) graphics;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,      RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int panelWidth  = getWidth();
        int panelHeight = getHeight();

        drawBackground(g2d, panelWidth, panelHeight);
        drawGlowOverlay(g2d, panelWidth, panelHeight);
        drawTitle(g2d, panelWidth);
        drawSubtitle(g2d, panelWidth);
        drawDividerLine(g2d, panelWidth);
        drawPlayerInfoBox(g2d, panelWidth);
        drawLoadingInstruction(g2d, panelWidth, panelHeight);
        drawBorderOutline(g2d, panelWidth, panelHeight);
    }

    private void drawBackground(Graphics2D g2d, int panelWidth, int panelHeight) {
        g2d.setColor(new Color(10, 10, 20));
        g2d.fillRect(0, 0, panelWidth, panelHeight);
    }

    private void drawGlowOverlay(Graphics2D g2d, int panelWidth, int panelHeight) {
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, glowAlpha * 0.25f));
        g2d.setColor(new Color(140, 60, 220));
        g2d.fillOval(panelWidth / 2 - 120, 10, 240, 140);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
    }

    private void drawTitle(Graphics2D g2d, int panelWidth) {
        g2d.setFont(new Font("Consolas", Font.BOLD, 44));
        g2d.setColor(new Color(80, 0, 140, 120));
        drawStringCentered(g2d, "NO ESCAPE", panelWidth, 82);

        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, glowAlpha));
        g2d.setColor(new Color(180, 90, 240));
        drawStringCentered(g2d, "NO ESCAPE", panelWidth, 80);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
    }

    private void drawSubtitle(Graphics2D g2d, int panelWidth) {
        g2d.setFont(new Font("Consolas", Font.PLAIN, 13));
        g2d.setColor(new Color(120, 110, 150));
        drawStringCentered(g2d, "An endless campus time-loop adventure", panelWidth, 108);
    }

    private void drawDividerLine(Graphics2D g2d, int panelWidth) {
        g2d.setColor(new Color(70, 30, 110));
        g2d.setStroke(new BasicStroke(1.5f));
        g2d.drawLine(60, 124, panelWidth - 60, 124);
    }

    private void drawPlayerInfoBox(Graphics2D g2d, int panelWidth) {
        g2d.setColor(new Color(20, 16, 36));
        g2d.fillRoundRect(60, 138, panelWidth - 120, 90, 12, 12);
        g2d.setColor(new Color(70, 40, 100));
        g2d.setStroke(new BasicStroke(1f));
        g2d.drawRoundRect(60, 138, panelWidth - 120, 90, 12, 12);

        g2d.setFont(new Font("Consolas", Font.BOLD, 14));
        g2d.setColor(new Color(200, 200, 60));
        drawStringCentered(g2d, "Player :  " + playerName, panelWidth, 168);

        g2d.setFont(new Font("Consolas", Font.PLAIN, 13));
        g2d.setColor(new Color(160, 180, 220));
        drawStringCentered(g2d, "Course :  " + playerCourse, panelWidth, 192);
        drawStringCentered(g2d, "Get ready to escape the loop!", panelWidth, 214);
    }

    private void drawLoadingInstruction(Graphics2D g2d, int panelWidth, int panelHeight) {
        g2d.setFont(new Font("Consolas", Font.BOLD, 13));
        g2d.setColor(new Color(60, 200, 100));
        drawStringCentered(g2d, "Loading your campus...", panelWidth, panelHeight - 24);
    }

    private void drawBorderOutline(Graphics2D g2d, int panelWidth, int panelHeight) {
        g2d.setColor(new Color(100, 40, 160));
        g2d.setStroke(new BasicStroke(2f));
        g2d.drawRoundRect(8, 8, panelWidth - 16, panelHeight - 16, 18, 18);
    }

    private void drawStringCentered(Graphics2D g2d, String text, int containerWidth, int yPosition) {
        FontMetrics fontMetrics = g2d.getFontMetrics();
        int xPosition = (containerWidth - fontMetrics.stringWidth(text)) / 2;
        g2d.drawString(text, xPosition, yPosition);
    }

    /**
     * Displays the splash screen as a timed, undecorated modal dialog.
     * Automatically closes after approximately 2.8 seconds.
     */
    public static void showSplashDialog(String playerName, String playerCourse) {
        JDialog splashDialog = new JDialog();
        splashDialog.setTitle("NO ESCAPE");
        splashDialog.setModal(true);
        splashDialog.setUndecorated(true);
        splashDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        splashDialog.setBackground(new Color(10, 10, 20));

        SplashPanel splashPanel = new SplashPanel(playerName, playerCourse);
        splashDialog.add(splashPanel);
        splashDialog.pack();
        splashDialog.setLocationRelativeTo(null);

        Timer autoCloseTimer = new Timer(2800, event -> {
            splashPanel.glowAnimationTimer.stop();
            splashDialog.dispose();
        });
        autoCloseTimer.setRepeats(false);
        autoCloseTimer.start();

        splashDialog.setVisible(true);
    }
}