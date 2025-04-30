public class Tile {
    private TerrainType terrain;
    private int food, water, gold;
    private boolean repeatableBonus;
    private Trader trader;

    public Tile(TerrainType terrain) {
        this.terrain = terrain;
        food = 0; water = 0; gold = 0;
        repeatableBonus = false;
        trader = null;
    }

    public TerrainType getTerrain() {
        return terrain;
    }

    public void setTerrain(TerrainType terrain) {
        this.terrain = terrain;
    }

    public void setBonuses(int foodBonus, int waterBonus, int goldBonus, boolean repeatable) {
        food = foodBonus;
        water = waterBonus;
        gold = goldBonus;
        repeatableBonus = repeatable;
    }

    public void setBonuses(int foodBonus, int waterBonus, int goldBonus) {
        food = foodBonus;
        water = waterBonus;
        gold = goldBonus;
    }

    public void setTrader(Trader trader) {
        this.trader = trader;
    }

    public int getFood() { 
        return food; 
    }

    public int getWater() { 
        return water; 
    }

    public int getGold() { 
        return gold; 
    }
    public boolean hasRepeatableBonus() { 
        return repeatableBonus; 
    }

    public boolean hasTrader() {
        return trader != null;
    }

    public Trader getTrader() {
        return trader;
    }


}
