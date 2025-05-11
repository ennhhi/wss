public abstract class Brain {
    protected Player player;
    protected Vision vision;
    protected WSSMap map;
    protected Path rememberedWaterPath;
    protected Path rememberedFoodPath;

    public Brain(Player player, Vision vision, WSSMap map) {
        this.player = player;
        this.vision = vision;
        this.map = map;
        this.rememberedWaterPath = null;
        this.rememberedFoodPath = null;
    }

    // Let each subtype specify its own resource threshold and minimum strength
    protected abstract double getResourceThreshold();
    protected abstract int getMinStrengthToMove();

    public void makeMove() {
        if (player.getCurrent_food() <= 0 || player.getCurrent_water() <= 0) {
            System.exit(0);
        }

        updatePaths();

        // Check if we need to rest due to low strength
        if (shouldRest()) {
            restPlayer();
            return;
        }

        // Choose path and move
        Path chosenPath = choosePath();
        if (chosenPath != null && chosenPath.getFirstStep() != null) {
            handleMovement(chosenPath);
        } else {
            handleNoPath();
        }
    }

    private void updatePaths() {
        Path waterPath = vision.closestWater();
        if (waterPath != null) rememberedWaterPath = waterPath;
        Path foodPath = vision.closestFood();
        if (foodPath != null) rememberedFoodPath = foodPath;
    }

    private boolean shouldRest() {
        // Cautious might rest if strength < 5; Balanced checks cost to move, etc.
        return player.getCurrent_strength() < getMinStrengthToMove();
    }

    private void restPlayer() {
        System.out.println("Not enough strength to move. Resting this turn (+2 strength).");
        player.rest(map);
    }

    private void handleMovement(Path path) {
        Direction step = path.getFirstStep();
        Tile target = map.getTileInDirection(step);
        int moveCost = target.getTerrain().getMoveCost();

        // Record before collecting
        int goldBefore = target.getGold();
        int foodBefore = target.getFood();
        int waterBefore = target.getWater();

        player.setCurrent_strength(player.getCurrent_strength() - moveCost);
        player.setCurrent_water(player.getCurrent_water() - target.getTerrain().getWaterCost());
        player.setCurrent_food(player.getCurrent_food() - target.getTerrain().getFoodCost());

        map.movePlayer(step);
        player.collect(target);

        System.out.println("Player enters square " + map.getPlayerRow() + "," + map.getPlayerCol() +
                ", Strength:" + player.getCurrent_strength() +
                ", Food:" + player.getCurrent_food() +
                ", Water:" + player.getCurrent_water() +
                ", Gold:" + player.getCurrent_gold());

        if (goldBefore > 0) System.out.println("Gained +" + goldBefore + " gold!");
        if (foodBefore > 0) System.out.println("Gained +" + foodBefore + " food!");
        if (waterBefore > 0) System.out.println("Gained +" + waterBefore + " water!");

        if (target.hasTrader()) {
            System.out.println("There is a trader here...");
        }
    }

    private void handleNoPath() {
        System.out.println("No valid path. Resting (+2 strength).");
        restPlayer();
    }

    // Let each subclass decide how to pick a path
    protected abstract Path choosePath();


    //Call function when there is a tile with a trader and is trading
    public void makeTrade(Trader trader, int threshold){
        int food_threshold = player.getMax_food()*threshold;
        int water_threshold = player.getMax_water()*threshold;
        int gold = player.getCurrent_gold();
        Offer offer=new Offer();
        //If brain is reckless (threshold = 0)
        if(gold>0){
            if(food_threshold==0 && water_threshold==0){
                    if( (gold/2) > (player.getMax_food() - player.getCurrent_food()) ){
                        offer.setWantFood(player.getMax_food()-player.getCurrent_food());
                        offer.setOfferGold( offer.getWantFood() );
                    } else {
                        offer.setWantFood(gold/2);
                        offer.setOfferGold(gold/2);
                    }
                    gold-= offer.getWantFood();
                    if( (gold) > (player.getMax_water() - player.getCurrent_water()) ){
                        offer.setWantWater(player.getMax_water()-player.getCurrent_water());
                        offer.setOfferGold(offer.getOfferGold() + offer.getWantFood() );
                    } else {
                        offer.setWantWater(gold);
                        offer.setOfferGold(offer.getOfferGold() + gold);
                    }
                    gold-= offer.getWantWater();
            } else if(player.getCurrent_food() < food_threshold && player.getCurrent_water() < water_threshold){
                if( (gold/2) > food_threshold ){
                    offer.setWantFood(player.getMax_food()-player.getCurrent_food());
                    offer.setOfferGold( offer.getWantFood() );
                } else {
                    offer.setWantFood(gold/2);
                    offer.setOfferGold(gold/2);
                }
                gold-= offer.getWantFood();
                if( (gold) > water_threshold){
                    offer.setWantWater(player.getMax_water()-player.getCurrent_water());
                    offer.setOfferGold(offer.getOfferGold() + offer.getWantFood() );
                } else {
                    offer.setWantWater(gold);
                    offer.setOfferGold(offer.getOfferGold() + gold);
                }
                gold-= offer.getWantWater();
            } else if(player.getCurrent_food() < food_threshold){

            } else if(player.getCurrent_water() < water_threshold) {

            } else{

            }
        }
    }
}