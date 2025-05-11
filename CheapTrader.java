//Trader with low prices but low stock
public class CheapTrader extends Trader{
    private int patienceLevel;
    private int food;
    private int water;
    private int gold;
    private boolean isTrading=false;

    public CheapTrader(){
        this.patienceLevel = 10;
        this.food = 999;
        this.water = 999;
        this.gold = 999;
        isTrading = true;
    }
}
