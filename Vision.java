
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
            Tile tile = map.getTile(map.getPlayerRow() + off[0], map.getPlayerCol() + off[1]);
            if (tile != null && hasResource(tile, resource)) {
                return new Path(java.util.List.of(Direction.EAST),
                        tile.getTerrain().getMoveCost(),
                        tile.getTerrain().getWaterCost(),
                        tile.getTerrain().getFoodCost());
            }
        }
        return null;
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
