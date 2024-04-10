package Samples;

import CustomizeUI.MyTreeCellRenderer;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.io.File;

public class TreeExample extends JFrame {
    private JTree tree;

    public TreeExample() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        File rootDirectory = new File("D:\\");
        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("D:");
        buildDirectoryTree(rootNode, rootDirectory);

        File rootDirectory2 = new File("C:\\Users\\Vinh.Lap\\Downloads\\Documents\\");
        DefaultMutableTreeNode rootNode2 = new DefaultMutableTreeNode(rootDirectory2.getName());
        buildDirectoryTree(rootNode2, rootDirectory2);

        DefaultMutableTreeNode root = new DefaultMutableTreeNode("This PC");
        root.add(rootNode);
        root.add(rootNode2);

        JTree tree = new JTree(root);
        tree.setCellRenderer(new MyTreeCellRenderer());
        add(new JScrollPane(tree));

        setSize(400, 500);
        setVisible(true);
    }

    public static void buildDirectoryTree(DefaultMutableTreeNode node, File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File childFile : files) {
                    DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(childFile.getName());
                    node.add(childNode);
                    buildDirectoryTree(childNode, childFile);
                }
            }
        }
    }

    public static void main(String[] args) {
        new TreeExample();
    }
}