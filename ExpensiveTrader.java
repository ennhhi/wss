//Trader with higher prices but high stock
public class ExpensiveTrader {
    private int patienceLevel;
    private int food;
    private int water;
    private int gold;
    private boolean isTrading=false;

    public ExpensiveTrader(){
        this.patienceLevel = 10;
        this.food = 999;
        this.water = 999;
        this.gold = 999;
        isTrading = true;
    }
}
