import java.util.List;

public class Path {
    private final List<Direction> steps;
    private final int totalMovementCost;
    private final int totalWaterCost;
    private final int totalFoodCost;

    public Path(List<Direction> steps, int moveCost, int waterCost, int foodCost) {
        this.steps = steps;
        this.totalMovementCost = moveCost;
        this.totalWaterCost = waterCost;
        this.totalFoodCost = foodCost;
    }

    public Direction getFirstStep() {
        return steps.isEmpty() ? null : steps.get(0);
    }

    public int getTotalMovementCost() { return totalMovementCost; }
    public int getTotalWaterCost() { return totalWaterCost; }
    public int getTotalFoodCost() { return totalFoodCost; }
}