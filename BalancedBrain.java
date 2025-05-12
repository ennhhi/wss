public class BalancedBrain extends Brain {

    private Path rememberedWaterPath = null;
    private Path rememberedFoodPath = null;

    public BalancedBrain(Player player, Vision vision, WSSMap map) {
        super(player, vision, map);
    }

    @Override
    protected double getResourceThreshold() {
        return 0.5; // tries to keep above 50% of max resources
    }

    @Override
    protected int getMinStrengthToMove() {
        // Return the cost needed to move onto next tile
        Tile east = map.getRelativeTile(0, 1);
        return (east != null) ? east.getTerrain().getMoveCost() : 1;
    }

    @Override
    protected Path choosePath() {
        boolean needsWater = player.getCurrent_water() < (player.getMax_water() * getResourceThreshold());
        boolean needsFood = player.getCurrent_food() < (player.getMax_food() * getResourceThreshold());

        // Use remembered water/food paths if still valid/in-bounds:
        if (needsWater && rememberedWaterPath != null && isValidPath(rememberedWaterPath)) {
            return rememberedWaterPath;
        }
        if (needsFood && rememberedFoodPath != null && isValidPath(rememberedFoodPath)) {
            return rememberedFoodPath;
        }

        // Default to moving east, but only if in-bounds:
        Tile eastTile = map.getRelativeTile(0, 1);
        if (eastTile != null) {
            return new Path(
                java.util.List.of(Direction.EAST),
                eastTile.getTerrain().getMoveCost(),
                eastTile.getTerrain().getWaterCost(),
                eastTile.getTerrain().getFoodCost()
            );
        }

        return null; // No valid move found
    }

    // Simple helper to ensure the path's first step is in-bounds
    private boolean isValidPath(Path p) {
        Direction step = p.getFirstStep();
        Tile target = map.getTileInDirection(step);
        return (target != null);
    }

    @Override
    public void makeMove() {
        if (player.getCurrent_food() <= 0 || player.getCurrent_water() <= 0) {
            //System.out.println("Player has died!"); //the print might be redundant as main() already does this
            System.exit(0);
        }

        // Always update vision and memory
        Path waterPath = vision.closestWater();
        if (waterPath != null) rememberedWaterPath = waterPath;

        Path foodPath = vision.closestFood();
        if (foodPath != null) rememberedFoodPath = foodPath;

        Path chosenPath = choosePath();

        if (chosenPath != null && chosenPath.getFirstStep() != null) {
            Direction step = chosenPath.getFirstStep();
            Tile target = map.getTileInDirection(step);
            int moveCost = target.getTerrain().getMoveCost();

            if (player.getCurrent_strength() < moveCost) {
                System.out.println("Not enough strength to move. Resting this turn (+3 strength).");
                player.rest(map);
                return;
            }

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
                }
            }

        } else {
            System.out.println("BalancedBrain: No valid move. Resting this turn (+3 strength).");
            player.rest(map);
        }
    }
}
