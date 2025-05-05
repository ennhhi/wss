import java.awt.*;
import java.awt.event.*;
import java.util.Hashtable;
import javax.swing.*;

public class WSSGame {
    private static Player player;
    private static Vision vision;
    private static Brain brain;
    private static WSSMap map;
    private static MapPanel mapPanel;
    private static int turnSpeed;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame jfrm = new JFrame("WSS");
            JPanel contentPane = (JPanel) jfrm.getContentPane();
            contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
            contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            jfrm.setSize(600,500);

            jfrm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            // Creating menu components.
            JSpinner colSpinner = new JSpinner(new SpinnerNumberModel(20, 5, 50, 1));
            JSpinner rowSpinner = new JSpinner(new SpinnerNumberModel(10, 5, 50, 1));

            ButtonGroup difficultyButtons = new ButtonGroup();
            JRadioButton easyButton = new JRadioButton("EASY");
            JRadioButton mediumButton = new JRadioButton("MEDIUM");
            JRadioButton hardButton = new JRadioButton("HARD");
            mediumButton.setSelected(true);
            difficultyButtons.add(easyButton);
            difficultyButtons.add(mediumButton);
            difficultyButtons.add(hardButton);

            ButtonGroup playerButtons = new ButtonGroup();
            JRadioButton balancedPlayerButton = new JRadioButton("Balanced");
            JRadioButton preparedButton = new JRadioButton("Prepared");
            JRadioButton strongButton = new JRadioButton("Strong");
            JRadioButton richButton = new JRadioButton("Rich");
            balancedPlayerButton.setSelected(true);
            playerButtons.add(balancedPlayerButton);
            playerButtons.add(preparedButton);
            playerButtons.add(strongButton);
            playerButtons.add(richButton);

            ButtonGroup visionButtons = new ButtonGroup();
            JRadioButton focusedButton = new JRadioButton("Focused");
            JRadioButton keenEyedButton = new JRadioButton("Keen Eyed");
            JRadioButton farSightButton = new JRadioButton("Far Sight");
            JRadioButton cautiousButton = new JRadioButton("Cautious");
            focusedButton.setSelected(true);
            visionButtons.add(focusedButton);
            visionButtons.add(keenEyedButton);
            visionButtons.add(farSightButton);
            visionButtons.add(cautiousButton);

            // TODO: Once more brain subtypes are added, add more corresponding buttons.
            ButtonGroup brainButtons = new ButtonGroup();
            JRadioButton balancedBrainButton = new JRadioButton("Balanced");
            balancedBrainButton.setSelected(true);
            brainButtons.add(balancedBrainButton);

            JSlider speedSlider = new JSlider(0, 3000, 2000);
            speedSlider.setMajorTickSpacing(500);
            speedSlider.setSnapToTicks(true);
            speedSlider.setPaintTicks(true);
            speedSlider.setPaintLabels(true);

            Hashtable<Integer, JLabel> labelTable = new Hashtable<>();
            for (int i = 0; i <= 3000; i += 500) {
                labelTable.put(i, new JLabel(String.format("%.1f", i / 1000.0)));
            }
            speedSlider.setLabelTable(labelTable);

            JButton startButton = new JButton("Start");

            // Adding and arranging menu components to frame.
            jfrm.add(createRow(new JLabel("Set Map Dimensions")));
            jfrm.add(createRow(new JLabel("Number of Columns:"), colSpinner, new JLabel("Number of Rows:"), rowSpinner));

            jfrm.add(createRow(new JLabel("Select Difficulty:")));
            jfrm.add(createRow(easyButton, mediumButton, hardButton));

            jfrm.add(createRow(new JLabel("Select Player Type:")));
            jfrm.add(createRow(balancedPlayerButton, preparedButton, strongButton, richButton));

            jfrm.add(createRow(new JLabel("Select Vision Type:")));
            jfrm.add(createRow(focusedButton, keenEyedButton, farSightButton, cautiousButton));

            // TODO: Once more brain subtypes are added, add their buttons here.
            jfrm.add(createRow(new JLabel("Select Brain Type:")));
            jfrm.add(createRow(balancedBrainButton));

            jfrm.add(createRow(new JLabel("Set Game Speed (in seconds per turn):")));
            jfrm.add(createRow(speedSlider));

            jfrm.add(createRow(startButton));


            startButton.addActionListener((ActionEvent ae) -> {
                int width = (int)colSpinner.getValue();
                int height = (int)rowSpinner.getValue();

                WSSMap.Difficulty diff;

                if (easyButton.isSelected())
                    diff = WSSMap.Difficulty.EASY;
                else if (mediumButton.isSelected())
                    diff = WSSMap.Difficulty.MEDIUM;
                else
                    diff = WSSMap.Difficulty.HARD;

                if (balancedPlayerButton.isSelected())
                    player = new BalancedPlayer();
                else if (preparedButton.isSelected())
                    player = new PreparedPlayer();
                else if (strongButton.isSelected())
                    player = new StrongPlayer(15, 15, 30);
                else
                    player = new RichPlayer(10, 10, 15);

                map = new WSSMap(width, height, diff);
                mapPanel = new MapPanel(map);

                if (focusedButton.isSelected())
                    vision = new FocusedVision(player, map);
                else if (keenEyedButton.isSelected())
                    vision = new KeenEyedVision(player, map);
                else if (farSightButton.isSelected())
                    vision = new FarSightVision(player, map);
                else
                    vision = new CautiousVision(player, map);

                // TODO: Once more brain subtypes are added, add selection of brains.
                brain = new BalancedBrain(player, vision, map);

                turnSpeed = speedSlider.getValue();

                jfrm.setVisible(false);
                runGame();
            });
            
            jfrm.setLocationRelativeTo(null);
            jfrm.setVisible(true);
        });
    }

    private static void runGame() {
        JFrame frame = new JFrame("Wilderness Survival Map");
        frame.getContentPane().setLayout(new FlowLayout());
        frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        frame.add(mapPanel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        Tile startingTile = map.getTile(map.getPlayerRow(), map.getPlayerCol());
        player.collect(startingTile);
        //Game Loop Starts

        new Thread(() -> {
            try {
                for (int turn = 1; turn <= 50; turn++) {
                    Thread.sleep(turnSpeed);
                
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
    }

    // Helper method for display
    private static JPanel createRow(Component... components) {
        JPanel row = new JPanel();
        row.setLayout(new FlowLayout());
        for (Component comp : components) {
            row.add(comp);
        }
        return row;
    }
}
