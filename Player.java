public abstract class Player {
    private int max_food;
    private int max_water;
    private int max_strength;
    private int current_food;
    private int current_water;
    private int current_strength;
    private int current_gold;

    Player(){
        max_food = 0;
        max_water = 0;
        max_strength = 0;
        current_food = 0;
        current_water = 0;
        current_strength = 0;
        current_gold = 0;
    }
        
    Player(int food, int water, int strength, int gold){
        max_food = food;
        max_water = water;
        max_strength = strength;
        current_food = food;
        current_water = water;
        current_strength = strength;
        current_gold = gold;
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

    public void collect(Tile tile){
        current_food += tile.getFood();
        current_water += tile.getWater();
        current_gold += tile.getGold();

        if(!tile.getRepeatableFood()){
            tile.setFood(0);
        }

        if(!tile.getRepeatableWater()){
            tile.setWater(0);
        }

        if(!tile.getRepeatableGold()){
            tile.setGold(0);
        }

        checkValues(current_food, current_water, current_strength);
    }

public void checkValues(int food, int water, int strength) {
    if (food > max_food) food = max_food;
    if (water > max_water) water = max_water;
    if (strength > max_strength) strength = max_strength;
    if (food < 0) food = 0;
    if (water < 0) water = 0;

    setCurrent_food(food);
    setCurrent_water(water);
    setCurrent_strength(strength);
}

    public Offer suggestTrade(Trader trader, int offerFood, int offerWater, int offerGold, int wantFood, int wantWater, int wantGold ){
        Offer trade = null;
        if(trader.isTrading()){
            Offer offer = new Offer(offerFood, offerWater, offerGold, wantFood, wantWater, wantGold );
            trade = trader.evaluateTrade(offer);
            // acceptTrade(trade);
        }
        return trade;
    }



    public void acceptTrade(Offer trade){
            if(trade != null){
                setCurrent_food(getCurrent_food() + trade.getOfferFood());
                setCurrent_water(getCurrent_water() + trade.getOfferWater());
                setCurrent_gold(getCurrent_gold() + trade.getOfferGold());
                checkValues(getCurrent_food(), getCurrent_water(), getCurrent_gold());
            }
    }

    public void rest(WSSMap map){
        setCurrent_strength(current_strength + 3);
        Tile currentTile = map.getTile(map.getPlayerRow(), map.getPlayerRow());

        //Reduce food and water by half of current tile's cost
        setCurrent_food(current_food-(currentTile.getTerrain().getFoodCost()/2));
        setCurrent_food(current_water-(currentTile.getTerrain().getWaterCost()/2));

    }

    public void move(){

    }

}
