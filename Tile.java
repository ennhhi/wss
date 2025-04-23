public class Tile {
    private TerrainType terrain;

    public Tile(TerrainType terrain) {
        this.terrain = terrain;
    }

    public TerrainType getTerrain() {
        return terrain;
    }

    public void setTerrain(TerrainType terrain) {
        this.terrain = terrain;
    }
}
