import java.awt.FlowLayout;
import javax.swing.*;

public class WSSGame {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            String wStr = JOptionPane.showInputDialog("Enter map length (e.g. 20):");
            String hStr = JOptionPane.showInputDialog("Enter map height (e.g. 10):");
            String[] options = { "EASY", "MEDIUM", "HARD" };
            int diffChoice = JOptionPane.showOptionDialog(
                null, "Select difficulty:", "Difficulty",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, options, options[0]);

            int width = Integer.parseInt(wStr);
            int height = Integer.parseInt(hStr);
            WSSMap.Difficulty diff = WSSMap.Difficulty.valueOf(options[diffChoice]);

            WSSMap map = new WSSMap(width, height, diff);
            MapPanel mapPanel = new MapPanel(map);

            JFrame frame = new JFrame("Wilderness Survival Map");
            frame.getContentPane().setLayout(new FlowLayout());
            frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
            frame.add(mapPanel);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

            Player player = new PreparedPlayer();  
            Vision vision = new CautiousVision(player, map);
            Brain brain = new BalancedBrain(player, vision, map);

            Tile startingTile = map.getTile(map.getPlayerRow(), map.getPlayerCol());
            player.collect(startingTile);
//Game Loop Starts
 new Thread(() -> {
    try {
        for (int turn = 1; turn <= 50; turn++) {
            Thread.sleep(2000);

            System.out.println("Turn " + turn);

            int row = map.getPlayerRow();
            int col = map.getPlayerCol();
            Tile tile = map.getTile(row, col);

            // === Death check (only food or water) ===
            if (player.getCurrent_food() <= 0 || player.getCurrent_water() <= 0) {
                System.out.println("Player has died!");
                break;
            }

            if (player.getCurrent_strength() <= 0) {
                System.out.println("Strength is zero, player rests this turn (+2 strength).");
                player.setCurrent_strength(player.getCurrent_strength() + 2);
                
            //continue making moves
            } else { 
                brain.makeMove();
            }

            mapPanel.redrawTiles();

            // Check if player has reached the last column
            if (map.getPlayerCol() == map.getWidth() - 1) {
                System.out.println("Congratulations! You survived and reached the end!");
                break;
            }

        } // end of Game Loop
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
}).start();
          
        });
    }
}
