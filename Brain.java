public abstract class Brain {
    protected Player player;
    protected Vision vision;
    protected WSSMap map;

    public Brain(Player player, Vision vision, WSSMap map) {
        this.player = player;
        this.vision = vision;
        this.map = map;
    }

    public abstract void makeMove();
}