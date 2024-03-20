package Samples;

import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXLabel;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.border.DropShadowBorder;

import javax.swing.border.AbstractBorder;
import java.awt.*;

public class RoundedBorder extends AbstractBorder {
    private final int radius;

    public RoundedBorder(int radius) {
        this.radius = radius;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(this.radius + 1, this.radius + 1, this.radius + 2, this.radius);
    }

    @Override
    public Insets getBorderInsets(Component c, Insets insets) {
        insets.left = insets.right = insets.top = insets.bottom = this.radius + 1;
        return insets;
    }

    public static void main(String[] args) {
        JXFrame frame = new JXFrame("Rounded Border Example");
        frame.setDefaultCloseOperation(JXFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);
        frame.setLayout(new FlowLayout());

        // Create a JPanel with a RoundedBorder
        JXPanel panel = new JXPanel();
//        panel.setBorder(new RoundedBorder(30));
        panel.setBorder(new DropShadowBorder());
//        panel.setBackground(new Color(34, 143, 61, 234));
        JXLabel label = new JXLabel("This is a JXPanel with a RoundedBorder");
        label.setOpaque(true);
        label.setBackground(null);
        panel.add(label);
        frame.add(panel);

        frame.setVisible(true);
    }
}