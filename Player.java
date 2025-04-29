public abstract class Player {
    int max_food;
    int max_water;
    int max_strength;
    int current_gold;
    int current_food;
    int current_water;
    int current_strength;

    Player(){
        
    }

    public int getMax_food() {
        return max_food;
    }

    public void setMax_food(int max_food) {
        this.max_food = max_food;
    }

    public int getMax_water() {
        return max_water;
    }

    public void setMax_water(int max_water) {
        this.max_water = max_water;
    }

    public int getMax_strength() {
        return max_strength;
    }

    public void setMax_strength(int max_strength) {
        this.max_strength = max_strength;
    }

    public int getCurrent_gold() {
        return current_gold;
    }

    public void setCurrent_gold(int current_gold) {
        this.current_gold = current_gold;
    }

    public int getCurrent_food() {
        return current_food;
    }

    public void setCurrent_food(int current_food) {
        this.current_food = current_food;
    }

    public int getCurrent_water() {
        return current_water;
    }

    public void setCurrent_water(int current_water) {
        this.current_water = current_water;
    }

    public int getCurrent_strength() {
        return current_strength;
    }

    public void setCurrent_strength(int current_strength) {
        this.current_strength = current_strength;
    }

}
