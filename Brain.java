import java.util.List;
import java.util.Random;

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

    //Calculate random trade amount based on brain type
    protected int calculateRandomTradeAmount(int baseAmount, String brainType) {
        Random random = new Random();
        double multiplier;
        
        switch (brainType) {
            case "RecklessBrain":
                // Reckless brain has high variance (0.5x to 2.0x)
                multiplier = 0.5 + random.nextDouble() * 1.5;
                break;
            case "BalancedBrain":
                // Balanced brain has moderate variance (0.7x to 1.3x)
                multiplier = 0.7 + random.nextDouble() * 0.6;
                break;
            case "CautiousBrain":
                // Cautious brain has low variance (0.8x to 1.2x)
                multiplier = 0.8 + random.nextDouble() * 0.4;
                break;
            default:
                multiplier = 1.0;
        }
        
        return (int) Math.round(baseAmount * multiplier);
    }

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
            System.out.println(chosenPath.getFirstStep());
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
        System.out.println("Player stats: " +
                ", Strength:" + player.getCurrent_strength() +
                ", Food:" + player.getCurrent_food() +
                ", Water:" + player.getCurrent_water() +
                ", Gold:" + player.getCurrent_gold());
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
        checkTrade(target, player);

        System.out.println("Player enters square (" + map.getPlayerRow() + "," + map.getPlayerCol() +
                "): Strength:" + player.getCurrent_strength() +
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
        System.out.println("No valid path to resources.");
        Tile target = map.getTileInDirection(Direction.EAST);

        if (target.getTerrain().getMoveCost() < player.getCurrent_strength()){
            handleMovement(new Path(
                    List.of(Direction.EAST),
                    target.getTerrain().getMoveCost(),
                    target.getTerrain().getWaterCost(),
                    target.getTerrain().getFoodCost()));
        }
        else {
            restPlayer();
        }

    }

    // Let each subclass decide how to pick a path
    protected abstract Path choosePath();


    //Call function when there is a tile with a trader and is trading
    public void makeTrade(Trader trader, double threshold) {
        //The values we don't want to exceed
        double food_threshold = player.getMax_food()*threshold;
        double water_threshold = player.getMax_water()*threshold;
        int gold = player.getCurrent_gold();
        Offer offer = new Offer();
        if(gold>0 && trader.isTrading()) {
            String brainType = this.getClass().getSimpleName();
            
            //If brain is reckless (threshold = 0)
            if(food_threshold==0 && water_threshold==0) {
                if((gold/2) > (player.getMax_food() - player.getCurrent_food())) {
                    int baseAmount = player.getMax_food() - player.getCurrent_food();
                    offer.setWantFood(calculateRandomTradeAmount(baseAmount, brainType));
                    offer.setOfferGold(offer.getWantFood());
                } else {
                    int baseAmount = gold/2;
                    offer.setWantFood(calculateRandomTradeAmount(baseAmount, brainType));
                    offer.setOfferGold(offer.getWantFood());
                }
                gold -= offer.getWantFood();
                if((gold) > (player.getMax_water() - player.getCurrent_water())) {
                    int baseAmount = player.getMax_water() - player.getCurrent_water();
                    offer.setWantWater(calculateRandomTradeAmount(baseAmount, brainType));
                    offer.setOfferGold(offer.getOfferGold() + offer.getWantWater());
                } else {
                    int baseAmount = gold;
                    offer.setWantWater(calculateRandomTradeAmount(baseAmount, brainType));
                    offer.setOfferGold(offer.getOfferGold() + offer.getWantWater());
                }
                gold -= offer.getWantWater();
            }
            //If we need food and water
            else if(player.getCurrent_food() < food_threshold && player.getCurrent_water() < water_threshold) {
                if((gold/2) > food_threshold) {
                    int baseAmount = (int)(food_threshold - player.getCurrent_food());
                    offer.setWantFood(calculateRandomTradeAmount(baseAmount, brainType));
                    offer.setOfferGold(offer.getWantFood());
                } else {
                    int baseAmount = gold/2;
                    offer.setWantFood(calculateRandomTradeAmount(baseAmount, brainType));
                    offer.setOfferGold(offer.getWantFood());
                }
                gold -= offer.getWantFood();
                if((gold) > water_threshold) {
                    int baseAmount = (int)(water_threshold - player.getCurrent_water());
                    offer.setWantWater(calculateRandomTradeAmount(baseAmount, brainType));
                    offer.setOfferGold(offer.getOfferGold() + offer.getWantWater());
                } else {
                    int baseAmount = gold;
                    offer.setWantWater(calculateRandomTradeAmount(baseAmount, brainType));
                    offer.setOfferGold(offer.getOfferGold() + offer.getWantWater());
                }
                gold -= offer.getWantWater();
            }
            //If we need food
            else if(player.getCurrent_food() < food_threshold) {
                if((gold) > (food_threshold - player.getCurrent_food())) {
                    int baseAmount = (int)(food_threshold - player.getCurrent_food());
                    offer.setWantFood(calculateRandomTradeAmount(baseAmount, brainType));
                    offer.setOfferGold(offer.getWantFood());
                } else {
                    int baseAmount = (int)(gold + (player.getCurrent_water() - water_threshold));
                    offer.setWantFood(calculateRandomTradeAmount(baseAmount, brainType));
                    offer.setOfferGold(gold);
                    offer.setOfferWater((int)(player.getCurrent_water() - water_threshold));
                }
            }
            //If we need water
            else if(player.getCurrent_water() < water_threshold) {
                if((gold) > (water_threshold - player.getCurrent_water())) {
                    int baseAmount = (int)(water_threshold - player.getCurrent_water());
                    offer.setWantWater(calculateRandomTradeAmount(baseAmount, brainType));
                    offer.setOfferGold(offer.getWantWater());
                } else {
                    int baseAmount = (int)(gold + (player.getCurrent_food() - food_threshold));
                    offer.setWantWater(calculateRandomTradeAmount(baseAmount, brainType));
                    offer.setOfferGold(gold);
                    offer.setOfferFood((int)(player.getCurrent_food() - food_threshold));
                }
            } else {
                //We don't need any resources
                offer = null;
            }
        }
        //If offer != we needed resouces and made a trade
        if(offer != null){
            Offer trade = trader.evaluateTrade(offer);
            if(trade == null){
                System.out.println("Trader rejected trade");
            } else {
                System.out.printf("Resources before Trading: Food: %d, Water: %d, Gold: %d\n", 
                    player.getCurrent_food(), player.getCurrent_water(), player.getCurrent_gold());
                
                System.out.println("Trading with trader");
                int add_Food = player.getCurrent_food() + trade.getWantFood();
                int add_Water = player.getCurrent_water() + trade.getWantWater();
                player.setCurrent_food(add_Food);
                player.setCurrent_water(add_Water);
                player.setCurrent_gold(player.getCurrent_gold() - trade.getOfferGold());
        
                player.checkValues(player.getCurrent_food(), player.getCurrent_water(), player.getCurrent_strength(), player.getCurrent_gold());
        
                System.out.println("Finished Trading");
                System.out.printf("Resources after Trading: Food: %d, Water: %d, Gold: %d\n",
                    player.getCurrent_food(), player.getCurrent_water(), player.getCurrent_gold());
            }
         }

    }

    public void checkTrade(Tile tile, Player player){
        if (tile.hasTrader() && player.getCurrent_gold() > 0){
            makeTrade(tile.getTrader(), getResourceThreshold());
        }
    }
}
