// Implements methods to return tile information based
// on the vision range defined in a vision type subclass

public abstract class Vision {
    protected Player player;
    protected WSSMap map;
    protected int[][] offsets;

    public Vision(Player player, WSSMap map) {
        this.player = player;
        this.map = map;
    }

    public Path closestWater() {
        return findClosest("water");
    }

    public Path closestFood() {
        return findClosest("food");
    }

    public Path closestGold() {
        return findClosest("gold");
    }

    public  Path easiestPath() {
        return findLowestCost();
    }

    private Path findClosest(String resource) {
        for (int[] off : offsets) {
            int newRow = map.getPlayerRow() + off[0];
            int newCol = map.getPlayerCol() + off[1];
            Tile tile = map.getTile(newRow, newCol);
            if (tile != null && hasResource(tile, resource)) {
                Direction dir = offsetToDirection(off[0], off[1]);
                return new Path(
                    java.util.List.of(dir),
                    tile.getTerrain().getMoveCost(),
                    tile.getTerrain().getWaterCost(),
                    tile.getTerrain().getFoodCost()
                );
            }
        }
        return null;
    }

    private Direction offsetToDirection(int rowOff, int colOff) {
        if (rowOff == -1 && colOff == 0) return Direction.NORTH;
        if (rowOff == 1 && colOff == 0) return Direction.SOUTH;
        if (rowOff == 0 && colOff == 1) return Direction.EAST;
        if (rowOff == 0 && colOff == -1) return Direction.WEST;
        if (rowOff == -1 && colOff == 1) return Direction.NORTH_EAST;
        if (rowOff == -1 && colOff == -1) return Direction.NORTH_WEST;
        if (rowOff == 1 && colOff == 1) return Direction.SOUTH_EAST;
        if (rowOff == 1 && colOff == -1) return Direction.SOUTH_WEST;
        return Direction.EAST; // fallback
    }

    //returns item bonus on a tile, if any
    private boolean hasResource(Tile tile, String resource) {
        return switch (resource) {
            case "water" -> tile.getWater() > 0;
            case "food" -> tile.getFood() > 0;
            case "gold" -> tile.getGold() > 0;
            default -> false;
        };
    }

    private Path findLowestCost() {
        Path best = null;
        int bestCost = Integer.MAX_VALUE;
        for (int[] off : offsets) {
            Tile tile = map.getTile(map.getPlayerRow() + off[0], map.getPlayerCol() + off[1]);
            if (tile != null) {
                int cost = tile.getTerrain().getMoveCost();
                if (cost < bestCost) {
                    bestCost = cost;
                    best = new Path(java.util.List.of(Direction.EAST), cost,
                            tile.getTerrain().getWaterCost(), tile.getTerrain().getFoodCost());
                }
            }
        }
        return best;
    }
}
