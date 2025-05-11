import java.util.List;

public class RecklessBrain extends Brain {
    public RecklessBrain(Player player, Vision vision, WSSMap map) {
        super(player, vision, map);
    }

    @Override
    protected double getResourceThreshold() {
        return 0.0; // never cares about resource management
    }

    @Override
    protected int getMinStrengthToMove() {
        return 1; // always tries to move without banking strength
    }

    @Override
    protected Path choosePath() {
        List<Direction> validMoves = vision.getValidNextMoves();
        int lowestCost = 100;
        int foodCost = 0;
        int waterCost = 0;
        Direction directionToMove = Direction.EAST;

        for (Direction dir : validMoves){
            // Get the tile for each valid direction, skip if null
            Tile candidate = switch (dir) {
                case EAST -> map.getRelativeTile(0, 1);
                case NORTH_EAST -> map.getRelativeTile(-1, 1);
                case SOUTH_EAST -> map.getRelativeTile(1, 1);
                default -> null; // ignoring other directions if not relevant
            };
            if (candidate == null) continue; // out-of-bounds or invalid

            int moveCost = candidate.getTerrain().getMoveCost();
            if (moveCost < lowestCost) {
                lowestCost = moveCost;
                foodCost = candidate.getTerrain().getFoodCost();
                waterCost = candidate.getTerrain().getWaterCost();
                directionToMove = dir;
            }
        }

        return new Path(
            List.of(directionToMove),
            lowestCost,
            waterCost,
            foodCost
        );
    }
}