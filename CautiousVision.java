public class CautiousVision extends Vision {

    public CautiousVision(Player player, WSSMap map) {
        super(player, map);
        offsets = new int[][] {
            {1, 0},     // SOUTH
            {-1, 0},    // NORTH
            {0, 1}      // EAST
        };
    }
}
