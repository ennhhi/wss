public class FarSightVision extends Vision {
    public FarSightVision(Player player, WSSMap map) {
        super(player, map);
        offsets = new int[][] {
            // 1 tile to the NORTH, EAST, SOUTH
            {-1, 0}, {0, 1}, {1, 0},
            // 2 tiles to the NORTH, EAST, SOUTH
            {-2, 0}, {0, 2}, {2, 0},
            // 1 tile NORTHEAST, SOUTHEAST
            {-1, 1}, {1, 1},
            // additional EAST facing diagonal tiles (row ±1, col+2) or (row ±2, col+1)
            {-1, 2}, {1, 2}, {-2, 1}, {2, 1}
        };
        validNextMoves = java.util.List.of(
                Direction.NORTH,
                Direction.EAST,
                Direction.SOUTH,
                Direction.NORTH_EAST,
                Direction.SOUTH_EAST);
    }
}
