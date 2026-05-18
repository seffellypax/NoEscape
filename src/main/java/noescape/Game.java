import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Game {

    public static void main(String[] args) {
        JFrame aFrame = new JFrame("NoEscape!!");
        aFrame.setSize(600, 400);
        aFrame.setVisible(true);
    }   

        private JFrame window;
        private JTextArea displayArea;
        private JTextField inputField;
        private JButton submitButton;

        private Room currentRoom;
        private Player player;

        public Game(Player player, Room room) {
            this.player = player;
            this.currentRoom = room;

         // Set up the GUI
            window = new JFrame("NoEscape!!");
            window.setSize(600, 400);
            window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            window.setLayout(new BorderLayout());

            displayArea = new JTextArea();
            displayArea.setEditable(false);
            displayArea.setFont(new Font("Consolas", Font.PLAIN,14));
            displayArea.setBackground(Color.BLACK);
            displayArea.setForeground(Color.GREEN);
            JScrollPane scrollPane = new JScrollPane(displayArea);
            window.add(scrollPane, BorderLayout.CENTER);
        
          //bottom panel for user input
            JPanel inputPanel = new JPanel();
            inputPanel.setLayout(new BorderLayout());
            inputField = new JTextField();
            inputField.setFont(new Font("Arial", Font.PLAIN,14));
            submitButton = new JButton("Submit");
            inputPanel.add(inputField, BorderLayout.CENTER);
            inputPanel.add(submitButton, BorderLayout.EAST);
            window.add(inputPanel, BorderLayout.SOUTH);

          //happens when the player submits an answer
            ActionListener submitAction = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    processPlayerInput();
                }
            };

          // allow pressing Enter to submit
            submitButton.addActionListener(submitAction);
            inputField.addActionListener(submitAction); 

            window.setVisible(true);
            startGame();
        } 

        private void startGame() {
          // we append text to our GUI area.
            displayArea.append(player.getName() + " has entered " + currentRoom.getName() + "\n");
            displayArea.append("============================\n");

            displayArea.append("Puzzle: What is 2 + 2?\n\n");
            displayArea.append("Type your answer below and press Submit:\n");
        }

        private void processPlayerInput() {
            String answer = inputField.getText().trim();

            if (answer.isEmpty()) {
                return; // ignore empty input
            }

            if(answer.equals("4")){
                displayArea.append("Correct! You have escaped the room!\n");
                inputField.setEditable(false);
                submitButton.setEnabled(false);
            } else {
                displayArea.append("Incorrect! Hint: It's the same as 2 * 2.\n");
            }
        }
    }
