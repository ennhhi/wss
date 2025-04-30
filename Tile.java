public class Tile {
    private TerrainType terrain;
    private int food, water, gold, strength;
    private boolean repeatableFood;
    private boolean repeatableWater;
    private boolean repeatableGold;
    private boolean repeatableStrength;

    private Trader trader;

    public Tile(TerrainType terrain) {
        this.terrain = terrain;
        food = 0; water = 0; gold = 0; strength = 0;
        repeatableFood = false;
        repeatableWater = false;
        repeatableGold = false;
        repeatableStrength = false;
        trader = null;
    }

    public TerrainType getTerrain() {
        return terrain;
    }

    public void setTerrain(TerrainType terrain) {
        this.terrain = terrain;
    }

    public void setBonuses(int foodBonus, int waterBonus, int goldBonus, int strengthBonus, boolean repeatableFood, boolean repeatableWater, boolean repeatableGold, boolean repeatableStrength) {
        food = foodBonus;
        water = waterBonus;
        gold = goldBonus;
        strength = strengthBonus;
        this.repeatableFood = repeatableFood;
        this.repeatableWater = repeatableWater;
        this.repeatableGold = repeatableGold;
        this.repeatableStrength = repeatableStrength;
    }

    public void setBonuses(int foodBonus, int waterBonus, int goldBonus, int strengthBonus) {
        food = foodBonus;
        water = waterBonus;
        gold = goldBonus;
        strength = strengthBonus;
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
    
      public int getStrength() {
          return strength;
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

    public void setStrength(int strength) {
        this.strength = strength;
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

    public boolean getRepeatableStrength(){
        return repeatableStrength;
    }

}
