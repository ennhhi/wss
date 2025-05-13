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
        boolean needsWater = player.getCurrent_water() < player.getMax_water() * getResourceThreshold();
        boolean needsFood = player.getCurrent_food() < player.getMax_food() * getResourceThreshold();

        if (needsWater && rememberedWaterPath != null) {
            if (isValidPath(rememberedWaterPath)) {
                return rememberedWaterPath;
            }
        }
        if (needsFood && rememberedFoodPath != null) {
            if (isValidPath(rememberedFoodPath)) {
                return rememberedFoodPath;
            }
        }
        // Default to moving east
        Tile east = map.getRelativeTile(0, 1);
        if (east == null) {
            return null; // out-of-bounds, no move
        }
        return new Path(
            java.util.List.of(Direction.EAST),
            east.getTerrain().getMoveCost(),
            east.getTerrain().getWaterCost(),
            east.getTerrain().getFoodCost()
        );
    }

    // A small helper to ensure we don't reference out-of-bounds targets
    private boolean isValidPath(Path p) {
        if (p == null || p.getFirstStep() == null) return false;
        Tile target = map.getTileInDirection(p.getFirstStep());
        return (target != null);
    }

    @Override
    public void makeMove() {
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

        //evaluate food and water options if < 50% of max
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
    }
}
