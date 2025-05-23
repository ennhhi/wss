import java.awt.*;
import javax.swing.*;

public class MapPanel extends JPanel {
    private final WSSMap map;
    private static final int CELL_SIZE = 30;
    private final JPanel[][] tilePanels;
    private int oldPlayerRow;
    private int oldPlayerCol;

    public MapPanel(WSSMap map) {
        this.map = map;
        this.oldPlayerRow = map.getPlayerRow();
        this.oldPlayerCol = map.getPlayerCol();
        setLayout(new GridLayout(map.getHeight(), map.getWidth()));
        setPreferredSize(new Dimension(map.getWidth() * CELL_SIZE, map.getHeight() * CELL_SIZE));
        tilePanels = new JPanel[map.getHeight()][map.getWidth()];
        setupTilePanels();
    }

    private void setupTilePanels() {
        for (int r = map.getHeight() - 1; r >= 0; r--) {
            for (int c = 0; c < map.getWidth(); c++) {
                tilePanels[r][c] = new JPanel(new FlowLayout());
                tilePanels[r][c].setBackground(map.getTile(r, c).getTerrain().getColor());
                tilePanels[r][c].setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

                add(tilePanels[r][c]);
            }
        }
        redrawTiles();
    }

    public void redrawTiles() {
        for (int r = map.getHeight() - 1; r >= 0; r--) {
            for (int c = 0; c < map.getWidth(); c++) {
                redrawSingleTile(r, c);
            }
        }
    }

    public void redrawPlayerMove(int newRow, int newCol) {
        // Re-render only old and new tiles
        redrawSingleTile(oldPlayerRow, oldPlayerCol);
        redrawSingleTile(newRow, newCol);
        oldPlayerRow = newRow;
        oldPlayerCol = newCol;
    }

    private void redrawSingleTile(int r, int c) {
        if (r < 0 || r >= map.getHeight() || c < 0 || c >= map.getWidth()) return;

        Tile tile = map.getTile(r, c);
        JPanel panel = tilePanels[r][c];
        panel.removeAll();
        panel.setBackground(tile.getTerrain().getColor());

        // Rebuild tooltip:
        String terrainString = tile.getTerrain().name();
        terrainString = terrainString.substring(0,1).toUpperCase() + terrainString.substring(1).toLowerCase();
        int tileFood = tile.getFood(), tileWater = tile.getWater(), tileGold = tile.getGold();
        String tooltip = String.format(
            "<html>%s<br>MoveCost: %d, FoodCost: %d, WaterCost: %d<br>%s%s%s%s</html>",
            terrainString, tile.getTerrain().getMoveCost(), tile.getTerrain().getFoodCost(), tile.getTerrain().getWaterCost(),
            (tile.getRepeatableFood() && tileFood > 0) ? "Repeating Food Bonus!<br>" : "",
            (tile.getRepeatableWater() && tileWater > 0) ? "Repeating Water Bonus!<br>" : "",
            (tile.getRepeatableGold() && tileGold > 0) ? "Repeating Gold Bonus!<br>" : "",
            tile.hasTrader() ? "Trader<br>" : String.format("FoodBonus: %d, WaterBonus: %d, GoldBonus: %d", tileFood, tileWater, tileGold)
        );
        panel.setToolTipText(tooltip);

        // Render player, trader, or resource
        int playerRow = map.getPlayerRow(), playerCol = map.getPlayerCol();
        if (r == playerRow && c == playerCol) {
            JPanel playerPanel = new JPanel();
            playerPanel.setBackground(Color.RED);
            playerPanel.setPreferredSize(new Dimension(10, 10));
            panel.add(playerPanel);
        } else if (tile.hasTrader()) {
            panel.add(new JLabel("T"));
        } else if (tileFood > 0 || tileWater > 0 || tileGold > 0) {
            String itemLabel = "";
            if (tileFood > 0) {
                itemLabel += "F" + (tile.getRepeatableFood() ? "*" : "");
            }
            if (tileWater > 0) {
                itemLabel += "W" + (tile.getRepeatableWater() ? "*" : "");
            }
            if (tileGold > 0) {
                itemLabel += "G" + (tile.getRepeatableGold() ? "*" : "");
            }
            panel.add(new JLabel(itemLabel));
        }

        panel.revalidate();
        panel.repaint();
    }
}
