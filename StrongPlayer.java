//Player with high strength and lower resources
public class StrongPlayer extends Player {
    private int max_food;
    private int max_water;
    private int max_strength;
    private int current_gold;
    private int current_food;
    private int current_water;
    private int current_strength;

    StrongPlayer(int food, int water, int strength){
        super(15, 15, 30,0);
    }
}
