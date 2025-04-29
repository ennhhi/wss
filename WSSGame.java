import java.awt.FlowLayout;
import javax.swing.*;

public class WSSGame {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            String wStr = JOptionPane.showInputDialog("Enter map width (e.g. 20):");
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
        });
    }
}
