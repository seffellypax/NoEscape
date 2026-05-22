package noescape;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

/**
 * OOP:
 *   Inheritance  — extends BasePanel (which extends JPanel)
 *   Abstraction  — implements buildContent() defined in BasePanel
 *   Polymorphism — room.showPuzzle() / room.isLocked() behave differently
 *                  per room type (Classroom, LibraryRoom, TsgRoom, SecurityOfficeRoom)
 */
public class RoomPanel extends BasePanel {
    private final Escapable room;
    private final String controllerMessage;
    private final Escapable[] allRooms;
    private final int activeRoomIndex;

    public RoomPanel(Escapable room, int roomIndex, int totalRooms, Player player, String controllerMessage, Escapable[] allRooms, int activeRoomIndex) {
        this.room = room;
        this.controllerMessage = controllerMessage;
        this.allRooms = allRooms;
        this.activeRoomIndex = activeRoomIndex;
        buildContent();
    }

    @Override
    protected void buildContent() {
        setLayout(new BorderLayout(0, 14));

        JPanel topSection = new JPanel();
        topSection.setOpaque(false);
        topSection.setLayout(new BoxLayout(topSection, BoxLayout.Y_AXIS));

        topSection.add(createTitleLabel(room.getName(), GameWindow.COLOR_PURPLE, 20));
        topSection.add(Box.createVerticalStrut(16));

        if (room.isLocked()) {
            topSection.add(createCenteredLabel("🔒  This room is locked.", GameWindow.COLOR_RED, 15, Font.BOLD));
            topSection.add(Box.createVerticalStrut(8));
            topSection.add(createCenteredLabel("Solve the previous room first.", GameWindow.COLOR_DIMMED, 13, Font.PLAIN));
        } else {
            topSection.add(buildPuzzleBox());
            topSection.add(Box.createVerticalStrut(14));
            topSection.add(createCenteredLabel("Type your answer below and press  Submit.", GameWindow.COLOR_DIMMED, 13, Font.PLAIN));
            topSection.add(Box.createVerticalStrut(4));
            topSection.add(createCenteredLabel("Use the  Clue  or  Hint  buttons if stuck.", GameWindow.COLOR_DIMMED, 12, Font.PLAIN));
        }

        JPanel bottomSection = new JPanel();
        bottomSection.setOpaque(false);
        bottomSection.setLayout(new BoxLayout(bottomSection, BoxLayout.Y_AXIS));
        bottomSection.add(createHorizontalDivider());
        bottomSection.add(Box.createVerticalStrut(8));
        bottomSection.add(createCenteredLabel("[ " + controllerMessage + " ]", GameWindow.COLOR_DIMMED, 12, Font.ITALIC));
        bottomSection.add(Box.createVerticalStrut(10));
        bottomSection.add(buildRoomProgressMap());

        add(topSection, BorderLayout.CENTER);
        add(bottomSection, BorderLayout.SOUTH);
    }

    private JPanel buildPuzzleBox() {
        JPanel puzzleBox = new JPanel();
        puzzleBox.setOpaque(true);
        puzzleBox.setBackground(new Color(30, 20, 55));
        puzzleBox.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(GameWindow.COLOR_PURPLE, 1, true),
            new EmptyBorder(14, 24, 14, 24)
        ));
        puzzleBox.setLayout(new BoxLayout(puzzleBox, BoxLayout.Y_AXIS));

        room.showPuzzle();
        String puzzleText = room.getLastMessage().replace("PUZZLE: ", "");

        puzzleBox.add(createCenteredLabel("📝  PUZZLE", GameWindow.COLOR_YELLOW, 13, Font.BOLD));
        puzzleBox.add(Box.createVerticalStrut(10));

        for (String line : wrapTextToLines(puzzleText, 55)) {
            puzzleBox.add(createCenteredLabel(line, GameWindow.COLOR_TEXT, 14, Font.PLAIN));
        }

        JPanel puzzleWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        puzzleWrapper.setOpaque(false);
        puzzleBox.setMaximumSize(new Dimension(560, 120));
        puzzleWrapper.add(puzzleBox);
        return puzzleWrapper;
    }

    private JPanel buildRoomProgressMap() {
        JPanel mapPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        mapPanel.setOpaque(false);

        for (int i = 0; i < allRooms.length; i++) {
            Color chipColor;
            String statusIcon;

            if (allRooms[i].isSolved()) {
                chipColor  = GameWindow.COLOR_GREEN;  statusIcon = "✓";
            } else if (i == activeRoomIndex) {
                chipColor  = GameWindow.COLOR_PURPLE; statusIcon = "►";
            } else if (allRooms[i].isLocked()) {
                chipColor  = GameWindow.COLOR_DIMMED; statusIcon = "🔒";
            } else {
                chipColor  = GameWindow.COLOR_YELLOW; statusIcon = " ";
            }

            JPanel chip = new JPanel();
            chip.setOpaque(true);
            chip.setBackground(new Color(chipColor.getRed(), chipColor.getGreen(), chipColor.getBlue(), 30));
            chip.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(chipColor, 1, true),
                new EmptyBorder(5, 10, 5, 10)
            ));
            chip.setLayout(new BoxLayout(chip, BoxLayout.Y_AXIS));

            JLabel nameLabel = new JLabel(statusIcon + "  " + allRooms[i].getName(), SwingConstants.CENTER);
            nameLabel.setFont(new Font("Consolas", Font.BOLD, 11));
            nameLabel.setForeground(chipColor);
            nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            chip.add(nameLabel);
            mapPanel.add(chip);
        }
        return mapPanel;
    }
}