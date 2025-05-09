public class FocusedVision extends Vision {
    public FocusedVision(Player player, WSSMap map) {
        super(player, map);
        offsets = new int[][] {
            {-1, 1}, // NE
            {0, 1},  // EAST
            {1, 1},  // SE
        };
    }
}
