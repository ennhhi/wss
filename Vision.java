public abstract class Vision {
    protected Player player;
    protected WSSMap map;

    public Vision(Player player, WSSMap map) {
        this.player = player;
        this.map = map;
    }

    public abstract Path closestWater();
    public abstract Path closestFood();
    public abstract Path closestGold();
    public abstract Path easiestPath();
}