//Player with high resouces and low strength
public class PreparedPlayer extends Player {
    int max_food;
    int max_water;
    int max_strength;
    int current_gold;
    int current_food;
    int current_water;
    int current_strength;

    PreparedPlayer(){
        super();
    }

    PreparedPlayer(int food, int water, int strength){
        super(30, 30, 15,0);
    }
}
