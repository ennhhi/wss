public enum Direction {
    NORTH(-1, 0),
    SOUTH(1, 0),
    EAST(0, 1),
    WEST(0, -1),
    NORTH_EAST(-1, 1),
    NORTH_WEST(-1, -1),
    SOUTH_EAST(1, 1),
    SOUTH_WEST(1, -1);

    private final int dRow, dCol;

    //offset values to traverse in specified direction
    Direction(int dRow, int dCol) {
        this.dRow = dRow;
        this.dCol = dCol;
    }

    public int deltaRow() { return dRow; }
    public int deltaCol() { return dCol; }
}