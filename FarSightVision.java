public class FarSightVision extends Vision {
    public FarSightVision(Player player, WSSMap map) {
        super(player, map);
        offsets = new int[][] {
            // 1 tile N, E, S
            {-1, 0}, {0, 1}, {1, 0},
            // 2 tiles N, E, S
            {-2, 0}, {0, 2}, {2, 0},
            // 1 tile NE, SE
            {-1, 1}, {1, 1},
            // Knight moves east (row ±1, col+2) or (row ±2, col+1)
            {-1, 2}, {1, 2}, {-2, 1}, {2, 1}
        };
    }
}
