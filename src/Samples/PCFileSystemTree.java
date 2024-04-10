package Samples;

import CustomizeUI.MyTreeCellRenderer;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.io.File;

public class PCFileSystemTree {

    public static void main(String[] args) {
        // Create the frame
        JFrame frame = new JFrame("File System JTree");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create the root node for "This PC"
        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("This PC");

        // Get filesystem roots and build tree nodes for each
        File[] roots = File.listRoots();
        for (File root : roots) {
            DefaultMutableTreeNode driveNode = new DefaultMutableTreeNode(root.getPath());
            rootNode.add(driveNode);
            if (root.isDirectory())
                buildDirectoryTree(driveNode, root);
        }

        // Create the tree by passing in the "This PC" node
        JTree tree = new JTree(rootNode);
        tree.setCellRenderer(new MyTreeCellRenderer());
        frame.add(new JScrollPane(tree));

        // Display the frame
        frame.setSize(400, 500);
        frame.setVisible(true);
    }

    private static void buildDirectoryTree(DefaultMutableTreeNode node, File file) {
        if (file.isDirectory() && !file.isHidden()) { // Optionally skip hidden directories
            // List all the files and directories in the current directory
            File[] files = file.listFiles();
            if (files != null) {
                for (File childFile : files) {
                    // Create a new node for file, but avoid entering into recursive directories
                    DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(childFile.getName());
                    node.add(childNode);

                    // Optionally, continue building the tree if it's a directory
                    // Commented out to prevent excessively long loading times
                    // buildDirectoryTree(childNode, childFile);
                }
            }
        }
    }
}
