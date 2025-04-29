public abstract class Trader {
    int patienceLevel;
    int food;
    int water;
    int gold;

    boolean isTrading=true;

    public Trader(){
        
    }

    public void counter_offer(){
        
    }
    
    public void quitNegotiation(){
        
    }

    public int getPatienceLevel() {
        return patienceLevel;
    }
    
    public void setPatienceLevel(int patienceLevel) {
        this.patienceLevel = patienceLevel;
    }

    public int getFood() {
        return food;
    }

    public void setFood(int food) {
        this.food = food;
    }

    public int getWater() {
        return water;
    }

    public void setWater(int water) {
        this.water = water;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }
}
