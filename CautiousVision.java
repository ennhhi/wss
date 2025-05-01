import java.util.ArrayList;
import java.util.List;

public class CautiousVision extends Vision {

    public CautiousVision(Player player, WSSMap map) {
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
        List<Direction> directions = List.of(Direction.NORTH, Direction.SOUTH, Direction.EAST);
        for (Direction dir : directions) {  //scans tiles in cardinal directions
            Tile tile = map.getTileInDirection(dir); //retrieves specified tile object
            if (tile != null && hasResource(tile, resource)) {
                return new Path(List.of(dir),
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

    //returns coordinates and cost of moving to the tile with the lowest traversal cost
    //in adjacent cardinal directions.  TODO: consider ordinal directions? (NORTHEAST, NORTHWEST, etc)
    private Path findLowestCost() {
        List<Direction> directions = List.of(Direction.NORTH, Direction.SOUTH, Direction.EAST);
        Path best = null;
        int bestCost = Integer.MAX_VALUE;

        for (Direction dir : directions) {
            Tile tile = map.getTileInDirection(dir);
            if (tile != null) {
                int cost = tile.getTerrain().getMoveCost();
                if (cost < bestCost) {
                    bestCost = cost;
                    best = new Path(List.of(dir),
                            tile.getTerrain().getMoveCost(),
                            tile.getTerrain().getWaterCost(),
                            tile.getTerrain().getFoodCost());
                }
            }
        }
        return best;
    }
}