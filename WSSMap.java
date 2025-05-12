
//Gets player location, map dimensions, tile information, and move the player

import java.util.Random;

public class WSSMap {
    public enum Difficulty { EASY, MEDIUM, HARD }

    private final int width;
    private final int height;
    private final Tile[][] grid;
    private final Random random = new Random();
    private int playerRow;
    private int playerCol;
    private final Difficulty difficulty;

    public WSSMap(int width, int height, Difficulty difficulty) {
        this.width = width;
        this.height = height;
        this.playerRow = height / 2; // roughly centers player on y-axis
        this.playerCol = 0;          // always starts at first column
        this.difficulty = difficulty;
        grid = new Tile[height][width];
        generateMap(difficulty);
    }

    private void generateMap(Difficulty difficulty) {
        for (int r = 0; r < height; r++) {
            for (int c = 0; c < width; c++) {
                grid[r][c] = new Tile(randomTerrain(difficulty));
                generateItems(grid[r][c], difficulty);
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

    private void generateItems(Tile tile, Difficulty difficulty) {
        if (random.nextInt(100) < 1) {
            // tile.setTrader(new Trader()); // Placeholder for Trader feature
            return;
        }

        int foodBonus = 0, waterBonus = 0, goldBonus = 0;
        boolean repeatableFood = false, repeatableWater = false, repeatableGold = false;
        int itemRate;

        switch (difficulty) {
            case EASY -> itemRate = 8;
            case MEDIUM -> itemRate = 5;
            case HARD -> itemRate = 3;
            default -> itemRate = 0;
        }

        if (random.nextInt(100) < itemRate) foodBonus = random.nextInt(3) + 3;
        if (random.nextInt(100) < itemRate) waterBonus = random.nextInt(3) + 3;
        if (random.nextInt(100) < itemRate) goldBonus = random.nextInt(3) + 3;

        if ((foodBonus != 0 || waterBonus != 0 || goldBonus != 0) && random.nextInt(100) < 20) repeatableFood = true;
        if ((foodBonus != 0 || waterBonus != 0 || goldBonus != 0) && random.nextInt(100) < 20) repeatableWater = true;
        if ((foodBonus != 0 || waterBonus != 0 || goldBonus != 0) && random.nextInt(100) < 20) repeatableGold = true;

        tile.setBonuses(foodBonus, waterBonus, goldBonus, repeatableFood, repeatableWater, repeatableGold);
    }

    // Returns a tile object at the given coordinates
    public Tile getTile(int row, int col) {
        // Bounds check
        if (row < 0 || row >= height || col < 0 || col >= width) return null;
        return grid[row][col];
    }

    // Returns the tile object relative to the player position by offset
    public Tile getRelativeTile(int dRow, int dCol) {
        int newRow = playerRow + dRow;
        int newCol = playerCol + dCol;
        // Bounds check
        if (newRow >= 0 && newRow < height && newCol >= 0 && newCol < width) {
            return grid[newRow][newCol];
        }
        return null;
    }

    // Returns 1 adjacent tile in a given direction
    public Tile getTileInDirection(Direction dir) {
        return getRelativeTile(dir.deltaRow(), dir.deltaCol());
    }

    // Updates player position
    public void movePlayer(Direction dir) {
        int newRow = playerRow + dir.deltaRow();
        int newCol = playerCol + dir.deltaCol();
        if (newRow >= 0 && newRow < height && newCol >= 0 && newCol < width) {
            playerRow = newRow;
            playerCol = newCol;
        }
    }

    public int getPlayerRow() {
        return playerRow;
    }

    public int getPlayerCol() {
        return playerCol;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }
}
