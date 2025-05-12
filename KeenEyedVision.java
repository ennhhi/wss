public class KeenEyedVision extends Vision {
    public KeenEyedVision(Player player, WSSMap map) {
        super(player, map);
        offsets = new int[][] {
            {-1, 0}, // NORTH
            {-1, 1}, // NORTHEAST
            {0, 1},  // EAST
            {1, 1},  // SOUTHEAST
            {1, 0},  // SOUTH
            {0, 2}   // 2 tiles EAST
        };
        validNextMoves =  java.util.List.of(
                Direction.NORTH,
                Direction.NORTH_EAST,
                Direction.EAST,
                Direction.SOUTH_EAST,
                Direction.SOUTH);
    }
}
