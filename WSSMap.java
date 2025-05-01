import java.util.Random;

public class WSSMap {
    public enum Difficulty { EASY, MEDIUM, HARD }

    private final int width;
    private final int height;
    private final Tile[][] grid;
    private final Random random = new Random();
    Difficulty difficulty;

    public WSSMap(int width, int height, Difficulty difficulty) {
        this.width = width;
        this.height = height;
        grid = new Tile[height][width];
        this.difficulty = difficulty;
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

    public Tile getTile(int row, int col) {
        if (row < 0 || row >= height || col < 0 || col >= width) return null;
        return grid[row][col];
    }

    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public Difficulty getDifficulty(){ return difficulty; }



    private void generateItems(Tile tile, Difficulty difficulty) {
        if (random.nextInt(100) < 1) {
            //tile.setTrader(new Trader()); //Cannot create a trader, replace with trader subclass once finished
            return;
        }

        int foodBonus = 0, waterBonus = 0, goldBonus = 0;
        boolean repeatableFood = false;
        boolean repeatableWater = false;
        boolean repeatableGold = false;
        int itemRate;

        switch (difficulty) {
            case EASY -> itemRate = 8;
            case MEDIUM -> itemRate = 5;
            case HARD -> itemRate = 3;
            default -> itemRate = 0;
        }

        if (random.nextInt(100) < itemRate)
            foodBonus = random.nextInt(3) + 3;
        if (random.nextInt(100) < itemRate)
            waterBonus = random.nextInt(3) + 3;
        if (random.nextInt(100) < itemRate)
            goldBonus = random.nextInt(3) + 3;

        if ((foodBonus != 0 || waterBonus != 0 || goldBonus != 0 ) && random.nextInt(100) < 20){
            repeatableFood = true;
        }
        
        if ((foodBonus != 0 || waterBonus != 0 || goldBonus != 0 ) && random.nextInt(100) < 20){
            repeatableWater = true;
        }
        
        if ((foodBonus != 0 || waterBonus != 0 || goldBonus != 0 ) && random.nextInt(100) < 20){
            repeatableGold = true;
        }
        
        tile.setBonuses(foodBonus, waterBonus, goldBonus, repeatableFood, repeatableWater, repeatableGold);
    }
}
