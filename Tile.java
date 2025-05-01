public class Tile {
    private TerrainType terrain;
    private int food, water, gold;
    private boolean repeatableFood;
    private boolean repeatableWater;
    private boolean repeatableGold;

    private Trader trader;

    public Tile(TerrainType terrain) {
        this.terrain = terrain;
        food = 0; water = 0; gold = 0;
        repeatableFood = false;
        repeatableWater = false;
        repeatableGold = false;
        trader = null;
    }

    public TerrainType getTerrain() {
        return terrain;
    }

    public void setTerrain(TerrainType terrain) {
        this.terrain = terrain;
    }

    public void setBonuses(int foodBonus, int waterBonus, int goldBonus, boolean repeatableFood, boolean repeatableWater, boolean repeatableGold) {
        food = foodBonus;
        water = waterBonus;
        gold = goldBonus;
        this.repeatableFood = repeatableFood;
        this.repeatableWater = repeatableWater;
        this.repeatableGold = repeatableGold;
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
    
    public boolean hasTrader() {
        return (trader != null);
    }

    public Trader getTrader() {
        return trader;
    }

    public void setFood(int food) {
        this.food = food;
    }

    public void setWater(int water) {
        this.water = water;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public boolean getRepeatableFood(){
        return repeatableFood;
    }

    public boolean getRepeatableWater(){
        return repeatableWater;
    }

    public boolean getRepeatableGold(){
        return repeatableGold;
    }

    public void setRepeatableFood(boolean repeatableFood) {
        this.repeatableFood = repeatableFood;
    }

    public void setRepeatableWater(boolean repeatableWater) {
        this.repeatableWater = repeatableWater;
    }

    public void setRepeatableGold(boolean repeatableGold) {
        this.repeatableGold = repeatableGold;
    }

}
