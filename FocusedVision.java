public class FocusedVision extends Vision {
    public FocusedVision(Player player, WSSMap map) {
        super(player, map);
        offsets = new int[][] {
            {1, 1},    // NORTHEAST
            {0, 1},     // EAST
            {-1, 1},     // SOUTHEAST
        };
        validNextMoves = java.util.List.of(
                Direction.NORTH_EAST,
                Direction.EAST,
                Direction.SOUTH_EAST);
    }
}
