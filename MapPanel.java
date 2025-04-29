import javax.swing.*;
import java.awt.*;

public class MapPanel extends JPanel {
    private final WSSMap map;
    private static final int CELL_SIZE = 30;
    private final JPanel[][] tilePanels;

    public MapPanel(WSSMap map) {
        this.map = map;
        setLayout(new GridLayout(map.getHeight(), map.getWidth()));
        setPreferredSize(new Dimension(map.getWidth() * CELL_SIZE, map.getHeight() * CELL_SIZE));
        tilePanels = new JPanel[map.getHeight()][map.getWidth()];
        setupTilePanels();
    }

    // Old map display function
    // @Override
    // protected void paintComponent(Graphics g) {
    //     super.paintComponent(g);
    //     for (int r = 0; r < map.getHeight(); r++) {
    //         for (int c = 0; c < map.getWidth(); c++) {
    //             Tile tile = map.getTile(r, c);
    //             g.setColor(tile.getTerrain().getColor());
    //             g.fillRect(c * CELL_SIZE, (map.getHeight() - r - 1) * CELL_SIZE, CELL_SIZE, CELL_SIZE);
    //             g.setColor(Color.BLACK);
    //             g.drawRect(c * CELL_SIZE, (map.getHeight() - r - 1) * CELL_SIZE, CELL_SIZE, CELL_SIZE);
    //         }
    //     }
    // }

    private void setupTilePanels() {
        for (int r = map.getHeight() - 1; r >= 0; r--) {
            for (int c = 0; c < map.getWidth(); c++) {
                tilePanels[r][c] = new JPanel();
                tilePanels[r][c].setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

                add(tilePanels[r][c]);
            }
        }
        redrawTiles();
    }

    private void redrawTiles() {
        for (int r = map.getHeight() - 1; r >= 0; r--) {
            for (int c = 0; c < map.getWidth(); c++) {
                Tile tile = map.getTile(r, c);
                TerrainType terrain = tile.getTerrain();
                tilePanels[r][c].setBackground(tile.getTerrain().getColor());

                String terrainString = terrain.name();
                terrainString = terrainString.substring(0, 1).toUpperCase()
                + terrainString.substring(1).toLowerCase();

                String tooltip = String.format(
                    "<html>%s<br>" +
                    "MoveCost: %d, FoodCost: %d, WaterCost: %d<br>" +
                    "Food: %d, Water: %d, Gold: %d</html>", 
                terrainString, terrain.getMoveCost(), terrain.getFoodCost(), terrain.getWaterCost(),
                tile.getFood(), tile.getWater(), tile.getWater());

                tilePanels[r][c].setToolTipText(tooltip);
            }
        }
    }
}
