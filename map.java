import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

/**
 * Enum representing terrain types, each with movement, water, and food costs.
 */
public enum TerrainType {
    PLAINS(1, 1, 1, Color.GREEN),
    FOREST(2, 1, 2, new Color(34, 139, 34)),
    MOUNTAIN(3, 2, 2, Color.GRAY),
    DESERT(2, 3, 1, Color.YELLOW),
    SWAMP(2, 2, 3, new Color(47, 79, 79));

    private final int moveCost;
    private final int waterCost;
    private final int foodCost;
    private final Color color;

    TerrainType(int moveCost, int waterCost, int foodCost, Color color) {
        this.moveCost = moveCost;
        this.waterCost = waterCost;
        this.foodCost = foodCost;
        this.color = color;
    }

    public int getMoveCost() { return moveCost; }
    public int getWaterCost() { return waterCost; }
    public int getFoodCost() { return foodCost; }
    public Color getColor() { return color; }
}

/**
 * Represents a single cell on the map.
 */
class Tile {
    private TerrainType terrain;
    // TODO: later add items, trader, etc.

    public Tile(TerrainType terrain) {
        this.terrain = terrain;
    }

    public TerrainType getTerrain() {
        return terrain;
    }

    public void setTerrain(TerrainType terrain) {
        this.terrain = terrain;
    }
}

/**
 * The map model containing a grid of Tiles.
 */
class WSSMap {
    private final int width;
    private final int height;
    private final Tile[][] grid;
    private final Random random = new Random();

    public enum Difficulty { EASY, MEDIUM, HARD }

    public WSSMap(int width, int height, Difficulty difficulty) {
        this.width = width;
        this.height = height;
        grid = new Tile[height][width];
        generateMap(difficulty);
    }

    private void generateMap(Difficulty difficulty) {
        for (int r = 0; r < height; r++) {
            for (int c = 0; c < width; c++) {
                grid[r][c] = new Tile(randomTerrain(difficulty));
            }
        }
    }

    private TerrainType randomTerrain(Difficulty difficulty) {
        int roll = random.nextInt(100);
        switch (difficulty) {
            case EASY:
                if (roll < 50) return TerrainType.PLAINS;
                if (roll < 70) return TerrainType.FOREST;
                if (roll < 85) return TerrainType.DESERT;
                if (roll < 95) return TerrainType.SWAMP;
                return TerrainType.MOUNTAIN;
            case MEDIUM:
                if (roll < 30) return TerrainType.PLAINS;
                if (roll < 55) return TerrainType.FOREST;
                if (roll < 75) return TerrainType.DESERT;
                if (roll < 90) return TerrainType.SWAMP;
                return TerrainType.MOUNTAIN;
            case HARD:
                if (roll < 20) return TerrainType.PLAINS;
                if (roll < 40) return TerrainType.FOREST;
                if (roll < 65) return TerrainType.DESERT;
                if (roll < 90) return TerrainType.SWAMP;
                return TerrainType.MOUNTAIN;
            default:
                return TerrainType.PLAINS;
        }
    }

    public Tile getTile(int row, int col) {
        if (row < 0 || row >= height || col < 0 || col >= width) return null;
        return grid[row][col];
    }

    public int getWidth() { return width; }
    public int getHeight() { return height; }
}

/**
 * Swing panel to render the map grid.
 */
class MapPanel extends JPanel {
    private final WSSMap map;
    private static final int CELL_SIZE = 30;

    public MapPanel(WSSMap map) {
        this.map = map;
        setPreferredSize(new Dimension(map.getWidth() * CELL_SIZE, map.getHeight() * CELL_SIZE));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int r = 0; r < map.getHeight(); r++) {
            for (int c = 0; c < map.getWidth(); c++) {
                Tile tile = map.getTile(r, c);
                g.setColor(tile.getTerrain().getColor());
                g.fillRect(c * CELL_SIZE, r * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                g.setColor(Color.BLACK);
                g.drawRect(c * CELL_SIZE, r * CELL_SIZE, CELL_SIZE, CELL_SIZE);
            }
        }
    }
}

/**
 * Main application to launch the map UI.
 */
public class WSSGame {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            String wStr = JOptionPane.showInputDialog("Enter map width (e.g. 20):");
            String hStr = JOptionPane.showInputDialog("Enter map height (e.g. 10):");
            String[] options = {"EASY", "MEDIUM", "HARD"};
            int diffChoice = JOptionPane.showOptionDialog(
                    null, "Select difficulty:", "Difficulty",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                    null, options, options[0]);

            int width = Integer.parseInt(wStr);
            int height = Integer.parseInt(hStr);
            WSSMap.Difficulty diff = WSSMap.Difficulty.valueOf(options[diffChoice]);

            WSSMap map = new WSSMap(width, height, diff);
            JFrame frame = new JFrame("Wilderness Survival Map");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(new MapPanel(map));
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
