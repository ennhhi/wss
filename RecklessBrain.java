import java.util.List;

public class RecklessBrain extends Brain {
    public RecklessBrain(Player player, Vision vision, WSSMap map) {
        super(player, vision, map);
    }

    @Override
    protected double getResourceThreshold() {
        return 0.0; // never cares about resource management
    }

    @Override
    protected int getMinStrengthToMove() {
        return 1; // always tries to move without banking strength
    }

    @Override
    protected Path choosePath() {
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
}