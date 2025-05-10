public class RecklessBrain extends Brain {
    public RecklessBrain(Player player, Vision vision, WSSMap map) {
        super(player, vision, map);
    }

    @Override
    protected double getResourceThreshold() {
        return 0.0; // never cares about resources
    }

    @Override
    protected int getMinStrengthToMove() {
        return 1; // always tries to move
    }

    @Override
    protected Path choosePath() {
        // Always just move east, ignoring resources
        Tile east = map.getRelativeTile(0, 1);
        if (east != null) {
            return new Path(
                java.util.List.of(Direction.EAST),
                east.getTerrain().getMoveCost(),
                east.getTerrain().getWaterCost(),
                east.getTerrain().getFoodCost()
            );
        }
        return null;
    }
}