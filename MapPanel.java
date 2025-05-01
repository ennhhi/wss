import java.awt.*;
import javax.swing.*;

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
        int playerX = map.getPlayerCol();
        int playerY = map.getPlayerRow();

        for (int r = map.getHeight() - 1; r >= 0; r--) {
            for (int c = 0; c < map.getWidth(); c++) {
                Tile tile = map.getTile(r, c);
                tilePanels[r][c].removeAll();

                TerrainType terrain = tile.getTerrain();
                int tileFood = tile.getFood(), tileWater = tile.getWater(), tileGold = tile.getGold();

                // Tooltip
                String terrainString = terrain.name();
                terrainString = terrainString.substring(0, 1).toUpperCase()
                + terrainString.substring(1).toLowerCase();

                String tooltip = String.format(
                    "<html>%s<br>" +
                    "MoveCost: %d, FoodCost: %d, WaterCost: %d<br>" +
                    ((tile.getRepeatableFood() && tileFood > 0) ? "Repeating Food Bonus!<br>" : "") +
                    ((tile.getRepeatableWater() && tileWater > 0) ? "Repeating Water Bonus!<br>" : "") +
                    ((tile.getRepeatableGold() && tileGold > 0) ? "Repeating Gold Bonus!<br>" : "") +
                    ((tile.hasTrader()) ? "Trader<br>" : "FoodBonus: %d, WaterBonus: %d, GoldBonus: %d</html>"), 
                terrainString, terrain.getMoveCost(), terrain.getFoodCost(), terrain.getWaterCost(),
                tileFood, tileWater, tileGold);

                tilePanels[r][c].setToolTipText(tooltip);

                // Item Display
                if (tile.hasTrader()) {
                    tilePanels[r][c].add(new JLabel("T"));
                }
                else if (r == playerY && c == playerX) {
                    JPanel playerPanel = new JPanel();
                    playerPanel.setBackground(Color.RED);
                    playerPanel.setPreferredSize(new Dimension(10, 10));
                    tilePanels[r][c].add(playerPanel);
                }
                else if (tileFood > 0 || tileWater > 0 || tileGold > 0) {
                    String itemLabel = "";
                    if(tileFood > 0){
                        itemLabel += "F";
                        if(tile.getRepeatableFood()){
                            itemLabel += "*";
                        }
                    }
                    if(tileWater > 0){
                        itemLabel += "W";
                        if(tile.getRepeatableWater()){
                            itemLabel += "*";
                        }
                    }
                    if(tileGold > 0){
                        itemLabel += "G";
                        if(tile.getRepeatableGold()){
                            itemLabel += "*";
                        }
                    }

                    tilePanels[r][c].add(new JLabel(itemLabel));
                }

                tilePanels[r][c].revalidate();
                tilePanels[r][c].repaint();
            }
        }
    }
}
