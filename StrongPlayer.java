//Player with high strength and lower resources
public class StrongPlayer extends Player {
    int max_food;
    int max_water;
    int max_strength;
    int current_gold;
    int current_food;
    int current_water;
    int current_strength;

    StrongPlayer(int food, int water, int strength){
        super(15, 15, 30,0);
    }
}
