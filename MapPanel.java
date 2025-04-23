import javax.swing.*;
import java.awt.*;

public class MapPanel extends JPanel {
    private final WSSMap map;
    private static final int CELL_SIZE = 30;

    public MapPanel(WSSMap map) {
        this.map = map;
        setPreferredSize(new Dimension(map.getWidth() * CELL_SIZE, map.getHeight() * CELL_SIZE));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int r = 0; r < map.getHeight(); r++) {
            for (int c = 0; c < map.getWidth(); c++) {
                Tile tile = map.getTile(r, c);
                g.setColor(tile.getTerrain().getColor());
                g.fillRect(c * CELL_SIZE, r * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                g.setColor(Color.BLACK);
                g.drawRect(c * CELL_SIZE, r * CELL_SIZE, CELL_SIZE, CELL_SIZE);
            }
        }
    }
}
