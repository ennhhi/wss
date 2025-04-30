//Player with high resouces and low strength
public class PreparedPlayer extends Player {
    private int max_food;
    private int max_water;
    private int max_strength;
    private int current_gold;
    private int current_food;
    private int current_water;
    private int current_strength;

    PreparedPlayer(){
        super();
    }

    PreparedPlayer(int food, int water, int strength){
        super(30, 30, 15,0);
    }
}
