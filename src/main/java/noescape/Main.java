import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        
        // Create a player and a room for testing
        Player player = new Player("Alice","Computer Science");
        
        Room room = new Room("Room 1", true) {
            @Override
            public boolean enter(Player player) {
                return true;
            }

            @Override
            public void showPuzzle() {
            }  
            
            @Override
            public void showClue() {
            }
            
            @Override
            public void showHint() {
            }

            @Override
            public void checkAnswer(String answer) {
                if (answer.equals("4")) {
                    System.out.println("Correct!");
                } else {
                    System.out.println("Wrong answer. Try again.");
                }
            }
        };

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run(){
                new Game(player, room);
            }
        });
    }
}