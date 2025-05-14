import java.util.List;

//keeps resources high
public class CautiousBrain extends Brain{

    private Path rememberedWaterPath = null;
    private Path rememberedFoodPath = null;

    public CautiousBrain(Player player, Vision vision, WSSMap map) {
        super(player, vision, map);
    }

    @Override
    protected double getResourceThreshold() {
        return 0.75; // tries to keep above 75% of max resources
    }

    @Override
    protected int getMinStrengthToMove() {
        return 5; // rest if below 5 strength
    }

    @Override
    protected Path choosePath() {
        boolean needsWater = false;
        boolean needsFood = false;

        //determine what resources are low
        if (player.getCurrent_water() < player.getMax_water() * getResourceThreshold()){
            System.out.println("Low on water, searching for water nearby");
            needsWater = true;
        }

        if (player.getCurrent_food() < player.getMax_food() * getResourceThreshold()){
            System.out.println("Low on food, searching for food nearby");
            needsFood = true;
        }

        //scan for resources
        Path rememberedFoodPath = vision.closestFood();
        Path rememberedWaterPath = vision.closestWater();
        if (rememberedWaterPath != null && needsWater) {
            System.out.println("Water Spotted");
        }
        if (rememberedFoodPath != null && needsFood) {
            System.out.println("Food Spotted");
        }

        //determine what to head towards

        if (needsWater && needsFood && rememberedFoodPath != null && rememberedWaterPath != null){
            if (player.getCurrent_water() < player.getCurrent_food()){  //tie-breaker of needing both
                return rememberedWaterPath;
            }
            if (player.getCurrent_food() < player.getCurrent_water()){ //tie-breaker of needing both
                return rememberedFoodPath;
            }
        }
        if (needsWater){
            return rememberedWaterPath;
        }
        if (needsFood) {
            return rememberedFoodPath;
        }

        //if no resources are needed, default to moving east
        List<Direction> validMoves = vision.getValidNextMoves(); //(some combination of EAST. SOUTHEAST, NORTHEAST)
        Tile tileConsideration;  //helper variable to fetch values

        //stores information of lowest move cost tile
        int lowestCost = 100; //sum of total move cost of water+str+food
        int foodCost = 0;
        int waterCost = 0;
        Direction directionToMove = Direction.EAST; // EAST should be a valid move for all vision types
        // NORTHEAST / SOUTHEAST may not be valid for some vision types,
        // so we will check the array for them and consider them if they exist
        for (Direction dir : validMoves){
            if (dir == Direction.EAST){
                tileConsideration = map.getRelativeTile(0, 1);
                if (tileConsideration == null){
                    break;
                }
                if (lowestCost > tileConsideration.getTerrain().getTotal_cost()){
                    lowestCost = tileConsideration.getTerrain().getTotal_cost();
                    foodCost = tileConsideration.getTerrain().getFoodCost();
                    waterCost = tileConsideration.getTerrain().getWaterCost();
                    System.out.println("Considering EAST with a total cost to move of " + tileConsideration.getTerrain().getTotal_cost());
                    directionToMove = Direction.EAST;
                }
            }
            if (dir == Direction.NORTH_EAST) {
                tileConsideration = map.getRelativeTile(1, 1);
                if (tileConsideration == null){
                    break;
                }
                if (lowestCost > tileConsideration.getTerrain().getTotal_cost()) {
                    lowestCost = tileConsideration.getTerrain().getTotal_cost();
                    foodCost = tileConsideration.getTerrain().getFoodCost();
                    waterCost = tileConsideration.getTerrain().getWaterCost();
                    System.out.println("Considering NORTHEAST with a total cost to move of " + tileConsideration.getTerrain().getTotal_cost());
                    directionToMove = Direction.NORTH_EAST;
                }
            }
            if (dir == Direction.SOUTH_EAST) {
                tileConsideration = map.getRelativeTile(-1, 1);
                if (tileConsideration == null){
                    break;
                }
                if (lowestCost > tileConsideration.getTerrain().getTotal_cost()) {
                    lowestCost = tileConsideration.getTerrain().getTotal_cost();
                    foodCost = tileConsideration.getTerrain().getFoodCost();
                    waterCost = tileConsideration.getTerrain().getWaterCost();
                    System.out.println("Considering SOUTHEAST with a total cost to move of " + tileConsideration.getTerrain().getTotal_cost());
                    directionToMove = Direction.SOUTH_EAST;
                }
            }
        }
        //returns direction and costs
        return new Path(
                List.of(directionToMove),
                lowestCost,
                waterCost,
                foodCost);
    }

    // A small helper to ensure we don't reference out-of-bounds targets
    // returns bool false if invalid, true if a valid step
    private boolean isValidPath(Path p) {
        if (p == null || p.getFirstStep() == null) return false;
        Tile target = map.getTileInDirection(p.getFirstStep());
        return (target != null);
    }

    //@Override
    /*public void makeMove() {
        //player is dead; no need to continue with logic
        if (player.getCurrent_food() <= 0 || player.getCurrent_water() <= 0) {
            System.exit(0);
        }


        //maintain enough strength to move across any tile
        if (player.getCurrent_strength() < getMinStrengthToMove()) {
            System.out.println("Not enough strength to move. Resting this turn (+2 strength).");
            player.rest(map);
            return;
        }

        //evaluate food and water options if < 75% of max
        Path waterPath = vision.closestWater();
        if (waterPath != null) {
            rememberedWaterPath = waterPath;
        }
        Path foodPath = vision.closestFood();
        if (foodPath != null) {
            rememberedFoodPath = foodPath;
        }
        Path chosenPath = choosePath();

        //move based on logic above
        if (chosenPath != null && chosenPath.getFirstStep() != null) {
            Direction step = chosenPath.getFirstStep();
            Tile target = map.getTileInDirection(step);
            int moveCost = target.getTerrain().getMoveCost();

            // Record the resources BEFORE collecting them
            int goldBefore = target.getGold();
            int foodBefore = target.getFood();
            int waterBefore = target.getWater();

            player.setCurrent_strength(player.getCurrent_strength() - moveCost);
            player.setCurrent_water(player.getCurrent_water() - target.getTerrain().getWaterCost());
            player.setCurrent_food(player.getCurrent_food() - target.getTerrain().getFoodCost());

            map.movePlayer(step);
            player.collect(target);

            int row = map.getPlayerRow();
            int col = map.getPlayerCol();

            System.out.println("Player enters square " + row + "," + col +
                    ", Strength:" + player.getCurrent_strength() +
                    ", Food:" + player.getCurrent_food() +
                    ", Water:" + player.getCurrent_water() +
                    ", Gold:" + player.getCurrent_gold());
            System.out.println("This location is " + target.getTerrain().name());

            if (goldBefore > 0) System.out.println("Gained +" + goldBefore + " gold!");
            if (foodBefore > 0) System.out.println("Gained +" + foodBefore + " food!");
            if (waterBefore > 0) System.out.println("Gained +" + waterBefore + " water!");

            if (target.hasTrader()) {
                if (player.getCurrent_food() > player.getMax_food() / 2 && player.getCurrent_water() > player.getMax_water() / 2) {
                    System.out.println("There is a trader. I have enough supplies, so I skip the trader.");
                } else {
                    System.out.println("There is a trader. Might consider trading here...");
                    super.makeTrade(target.getTrader(), 0.75);
                }
            }
        }
    }*/
}
