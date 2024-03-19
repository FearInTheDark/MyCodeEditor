package CustomizeUI;

import javax.swing.*;
import javax.swing.plaf.basic.BasicTreeUI;
import javax.swing.tree.TreePath;
import java.awt.*;

public class MyTreeUI extends BasicTreeUI {
    @Override
    protected void paintVerticalPartOfLeg(Graphics g, Rectangle clipBounds, Insets insets, TreePath path) {
        if (path != null) {
            int depth = path.getPathCount() - 1;
            int lineX = getRowX(tree.getRowForPath(path), depth);

            if (clipBounds == null || (clipBounds != null && lineX >= clipBounds.x && lineX < (clipBounds.x + clipBounds.width))) {
                paintVerticalLine(g, tree, lineX, clipBounds.y, clipBounds.y + clipBounds.height);
            }
        }
    }

    @Override
    protected void paintHorizontalPartOfLeg(Graphics g, Rectangle clipBounds, Insets insets, Rectangle bounds, TreePath path, int row, boolean isExpanded, boolean hasBeenExpanded, boolean isLeaf) {
        // Do nothing, this will prevent the horizontal lines from being drawn
    }

    @Override
    protected void paintVerticalLine(Graphics g, JComponent c, int x, int top, int bottom) {
        g.setColor(Color.RED); // Change the color as per your requirement
        drawDashedVerticalLine(g, x, top, bottom);
    }

    @Override
    protected void drawDashedVerticalLine(Graphics g, int x, int y1, int y2) {
        // Change the line style as per your requirement
        for (int i = y1; i <= y2; i += 2) {
            g.drawLine(x, i, x, i);
        }
    }
}