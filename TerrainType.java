
// Defines resource costs of terrain
// Can return resource costs to move on a given terrain type

import java.awt.*;

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
