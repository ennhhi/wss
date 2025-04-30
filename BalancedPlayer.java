//Player that has average stats 
public class BalancedPlayer extends Player{
    private int max_food;
    private int max_water;
    private int max_strength;
    private int current_gold;
    private int current_food;
    private int current_water;
    private int current_strength;

    BalancedPlayer(){
        super(15, 15, 15, 0);
    }
}
