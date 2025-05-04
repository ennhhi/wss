public class KeenEyedVision extends Vision {

    public KeenEyedVision(Player player, WSSMap map) {
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
        // Offsets: 1 tile N, NE, SE, S and 2 tiles E
        int[][] offsets = {
            {-1, 0}, // NORTH
            {-1, 1}, // NE
            {1, 1},  // SE
            {1, 0},  // SOUTH
            {0, 2}   // 2 tiles EAST
        };
        for (int[] off : offsets) {
            Tile tile = map.getTile(map.getPlayerRow() + off[0], map.getPlayerCol() + off[1]);
            if (tile != null && hasResource(tile, resource)) {
                Direction dummyStep = Direction.EAST; // Not exact, but used for Path
                return new Path(java.util.List.of(dummyStep),
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
            {-1, 0}, {-1, 1}, {1, 1}, {1, 0}, {0, 2}
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