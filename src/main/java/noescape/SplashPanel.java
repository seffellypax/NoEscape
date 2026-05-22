package noescape;

import javax.swing.*;
import java.awt.*;

/**
 * OOP: Inheritance — extends JPanel and overrides {@code paintComponent()}.
 */
public class SplashPanel extends JPanel {
    private static final int PANEL_WIDTH = 560;
    private static final int PANEL_HEIGHT = 340;

    private final String playerName;
    private final String playerCourse;
    private float glowAlpha = 0f;
    private Timer glowAnimationTimer;

    public SplashPanel(String playerName, String playerCourse) {
        this.playerName = playerName;
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
        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int panelWidth = getWidth();
        int panelHeight = getHeight();

        drawBackground(graphics2D, panelWidth, panelHeight);
        drawGlowOverlay(graphics2D, panelWidth, panelHeight);
        drawTitle(graphics2D, panelWidth);
        drawSubtitle(graphics2D, panelWidth);
        drawDividerLine(graphics2D, panelWidth);
        drawPlayerInfoBox(graphics2D, panelWidth);
        drawLoadingInstruction(graphics2D, panelWidth, panelHeight);
        drawBorderOutline(graphics2D, panelWidth, panelHeight);
    }

    private void drawBackground(Graphics2D graphics2D, int panelWidth, int panelHeight) {
        graphics2D.setColor(new Color(10, 10, 20));
        graphics2D.fillRect(0, 0, panelWidth, panelHeight);
    }

    private void drawGlowOverlay(Graphics2D graphics2D, int panelWidth, int panelHeight) {
        graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, glowAlpha * 0.25f));
        graphics2D.setColor(new Color(140, 60, 220));
        graphics2D.fillOval(panelWidth / 2 - 120, 10, 240, 140);
        graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
    }

    private void drawTitle(Graphics2D graphics2D, int panelWidth) {
        graphics2D.setFont(new Font("Consolas", Font.BOLD, 44));
        graphics2D.setColor(new Color(80, 0, 140, 120));
        drawStringCentered(graphics2D, "NO ESCAPE", panelWidth, 82);

        graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, glowAlpha));
        graphics2D.setColor(new Color(180, 90, 240));
        drawStringCentered(graphics2D, "NO ESCAPE", panelWidth, 80);
        graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
    }

    private void drawSubtitle(Graphics2D graphics2D, int panelWidth) {
        graphics2D.setFont(new Font("Consolas", Font.PLAIN, 13));
        graphics2D.setColor(new Color(120, 110, 150));
        drawStringCentered(graphics2D, "An endless campus time-loop adventure", panelWidth, 108);
    }

    private void drawDividerLine(Graphics2D graphics2D, int panelWidth) {
        graphics2D.setColor(new Color(70, 30, 110));
        graphics2D.setStroke(new BasicStroke(1.5f));
        graphics2D.drawLine(60, 124, panelWidth - 60, 124);
    }

    private void drawPlayerInfoBox(Graphics2D graphics2D, int panelWidth) {
        graphics2D.setColor(new Color(20, 16, 36));
        graphics2D.fillRoundRect(60, 138, panelWidth - 120, 90, 12, 12);
        graphics2D.setColor(new Color(70, 40, 100));
        graphics2D.setStroke(new BasicStroke(1f));
        graphics2D.drawRoundRect(60, 138, panelWidth - 120, 90, 12, 12);

        graphics2D.setFont(new Font("Consolas", Font.BOLD, 14));
        graphics2D.setColor(new Color(200, 200, 60));
        drawStringCentered(graphics2D, "Player :  " + playerName, panelWidth, 168);

        graphics2D.setFont(new Font("Consolas", Font.PLAIN, 13));
        graphics2D.setColor(new Color(160, 180, 220));
        drawStringCentered(graphics2D, "Course :  " + playerCourse, panelWidth, 192);
        drawStringCentered(graphics2D, "Get ready to escape the loop!", panelWidth, 214);
    }

    private void drawLoadingInstruction(Graphics2D graphics2D, int panelWidth, int panelHeight) {
        graphics2D.setFont(new Font("Consolas", Font.BOLD, 13));
        graphics2D.setColor(new Color(60, 200, 100));
        drawStringCentered(graphics2D, "Loading your campus...", panelWidth, panelHeight - 24);
    }

    private void drawBorderOutline(Graphics2D graphics2D, int panelWidth, int panelHeight) {
        graphics2D.setColor(new Color(100, 40, 160));
        graphics2D.setStroke(new BasicStroke(2f));
        graphics2D.drawRoundRect(8, 8, panelWidth - 16, panelHeight - 16, 18, 18);
    }

    private void drawStringCentered(Graphics2D graphics2D, String text, int containerWidth, int yPosition) {
        FontMetrics fontMetrics = graphics2D.getFontMetrics();
        int xPosition = (containerWidth - fontMetrics.stringWidth(text)) / 2;
        graphics2D.drawString(text, xPosition, yPosition);
    }

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