import java.util.List;

public class BalancedBrain extends Brain {

    private Path rememberedWaterPath = null;
    private Path rememberedFoodPath = null;

    public BalancedBrain(Player player, Vision vision, WSSMap map) {
        super(player, vision, map);
    }

    @Override
    public void makeMove() {
        if (player.getCurrent_food() <= 0 || player.getCurrent_water() <= 0) {
            System.out.println("Player has died!");
            System.exit(0);
        }

        // Always update vision and memory
        Path waterPath = vision.closestWater();
        if (waterPath != null) rememberedWaterPath = waterPath;

        Path foodPath = vision.closestFood();
        if (foodPath != null) rememberedFoodPath = foodPath;

        Path chosenPath = null;

        boolean needsWater = player.getCurrent_water() < player.getMax_water() / 3;
        boolean needsFood = player.getCurrent_food() < player.getMax_food() / 3;

        if (needsWater && rememberedWaterPath != null) {
            chosenPath = rememberedWaterPath;
        } else if (needsFood && rememberedFoodPath != null) {
            chosenPath = rememberedFoodPath;
        } else {
            Tile east = map.getRelativeTile(0, 1);
            if (east != null) {
                chosenPath = new Path(List.of(Direction.EAST),
                    east.getTerrain().getMoveCost(),
                    east.getTerrain().getWaterCost(),
                    east.getTerrain().getFoodCost());
            }
        }

        if (chosenPath != null && chosenPath.getFirstStep() != null) {
            Direction step = chosenPath.getFirstStep();
            Tile target = map.getTileInDirection(step);

            if (target != null) {
                int prevFood = player.getCurrent_food();
                int prevWater = player.getCurrent_water();
                int prevStrength = player.getCurrent_strength();

                player.setCurrent_strength(player.getCurrent_strength() - target.getTerrain().getMoveCost());
                player.setCurrent_water(player.getCurrent_water() - target.getTerrain().getWaterCost());
                player.setCurrent_food(player.getCurrent_food() - target.getTerrain().getFoodCost());

                map.movePlayer(step);
                player.collect(target);

                System.out.println("Player enters square " + map.getPlayerRow() + "," + map.getPlayerCol() +
                        ", Strength:" + player.getCurrent_strength() +
                        ", Food:" + player.getCurrent_food() +
                        ", Water:" + player.getCurrent_water() +
                        ", Gold:" + player.getCurrent_gold());
                System.out.println("This location is " + target.getTerrain().name());

                int foodGain = player.getCurrent_food() - prevFood;
                int waterGain = player.getCurrent_water() - prevWater;
                int strengthGain = player.getCurrent_strength() - prevStrength;

                if (foodGain > 0) System.out.println("Gained +" + foodGain + " food!");
                if (waterGain > 0) System.out.println("Gained +" + waterGain + " water!");
                if (strengthGain > 0) System.out.println("Gained +" + strengthGain + " strength!");

                if (target.getGold() > 0) System.out.println("I see some gold here!");
                if (target.hasTrader()) {
                    if (player.getCurrent_food() > player.getMax_food() / 2 && player.getCurrent_water() > player.getMax_water() / 2) {
                        System.out.println("There is a trader.\nI have enough supplies, so I skip the trader.");
                    } else {
                        System.out.println("There is a trader.\nMight consider trading here...");
                    }
                }
            }
        } else {
            System.out.println("Strength is zero, player rests this turn (+2 strength).");
            player.rest();
            player.setCurrent_strength(Math.min(player.getMax_strength(), player.getCurrent_strength() + 2));
            player.setCurrent_water(Math.max(0, player.getCurrent_water() - 1));
            player.setCurrent_food(Math.max(0, player.getCurrent_food() - 1));
        }
    }
}