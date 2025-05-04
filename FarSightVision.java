public class FarSightVision extends Vision {

    public FarSightVision(Player player, WSSMap map) {
        super(player, map);
    }

    @Override
    public Path closestWater() {
        return findClosest("water");
    }

    @Override
    public Path closestFood() {
        return findClosest("food");
    }

    @Override
    public Path closestGold() {
        return findClosest("gold");
    }

    @Override
    public Path easiestPath() {
        return findLowestCost();
    }

    private Path findClosest(String resource) {
        // 2 tiles N/E/S, 1 tile NE/SE, plus knight-move east positions
        int[][] offsets = {
            // 2 tiles N, E, S
            {-2, 0}, {0, 2}, {2, 0},
            // 1 tile NE, SE
            {-1, 1}, {1, 1},
            // Knight moves east (row ±1, col+2) or (row ±2, col+1)
            {-1, 2}, {1, 2}, {-2, 1}, {2, 1}
        };
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

    private boolean hasResource(Tile tile, String resource) {
        return switch (resource) {
            case "water" -> tile.getWater() > 0;
            case "food" -> tile.getFood() > 0;
            case "gold" -> tile.getGold() > 0;
            default -> false;
        };
    }

    private Path findLowestCost() {
        int[][] offsets = {
            {-2, 0}, {0, 2}, {2, 0},
            {-1, 1}, {1, 1},
            {-1, 2}, {1, 2}, {-2, 1}, {2, 1}
        };
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