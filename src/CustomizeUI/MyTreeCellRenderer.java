package CustomizeUI;

import javax.swing.*;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;

public class MyTreeCellRenderer extends DefaultTreeCellRenderer {
    Icon folderIcon;
    Icon fileIcon;

    public MyTreeCellRenderer() {
        ImageIcon folderII = new ImageIcon("src/icons/folder.png");
        Image folderImage = folderII.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        this.folderIcon = new ImageIcon(folderImage);
        ImageIcon fileII = new ImageIcon("src/icons/file.png");
        Image fileImage = fileII.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        fileIcon = new ImageIcon(fileImage);

    }


    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
        if (leaf) {
            setIcon(fileIcon);
        } else {
            setIcon(folderIcon);
        }
        if (sel) {
            String text = getText();
            setText("<html><u style='text-decoration: underline; text-underline-offset: 3px;'>" + text + "</u></html>");
            setOpaque(true);
            setBackground(null);
        } else {
            setForeground(Color.BLACK);
        }
        return this;
    }
}