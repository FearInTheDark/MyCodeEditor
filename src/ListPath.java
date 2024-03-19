import CustomizeUI.MySplitPaneUI;
import CustomizeUI.MyTreeCellRenderer;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class ListPath extends JFrame {
    private JTree myTree;
    private DefaultMutableTreeNode root;
    private JSplitPane splitPane;
    private JScrollPane explorerPane;
    private JTextArea textArea;
    private int WIDTH = 1200, HEIGHT = 800;

    public ListPath() throws Exception {
        root = new DefaultMutableTreeNode("D:");
        myTree = new JTree(root);
        Path rootPath = Paths.get("D:", "Java Learning");
        listAllFiles(rootPath, root);


        setTitle("My File Explorer");
        setIconImage(new ImageIcon("src/icons/frame.png").getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH));
        setLayout(null);
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setLocationRelativeTo(null);
        initUI();
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                WIDTH = getWidth();
                HEIGHT = getHeight();
                splitPane.setBounds(0, 100, getWidth(), getHeight() - 150);
            }
        });
        repaint();
        show();
    }

    private static void listAllFiles(Path currentPath, DefaultMutableTreeNode parentNode) throws IOException {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(currentPath)) {
            for (Path entry : stream) {
                DefaultMutableTreeNode node = new DefaultMutableTreeNode(getLastPath(entry));
                parentNode.add(node);
                if (Files.isDirectory(entry)) listAllFiles(entry, node);
            }
        }
    }

    private static Object getLastPath(Path entry) {
        return entry.getName(entry.getNameCount() - 1);
    }

    private void initUI() {
        JLabel title = new JLabel("File Explorer");
        ImageIcon icon = new ImageIcon("src/icons/frame.png");
        title.setIcon(icon);
        title.setBounds(0, 0, 300, 100);
        title.setFont(new Font("Comic Sans MS", Font.BOLD, 30));
        title.setHorizontalAlignment(SwingConstants.CENTER);
//        title.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
//        splitPane.setDividerLocation(150);
        splitPane.setBounds(0, 100, WIDTH, HEIGHT - 150);
        splitPane.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        explorerPane = new JScrollPane(myTree);
        splitPane.setDividerSize(5);
        splitPane.setUI(new MySplitPaneUI());
        explorerPane.getHorizontalScrollBar().setUI((new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = Color.GRAY;
            }

            @Override
            protected JButton createDecreaseButton(int orientation) {
                JButton button = new JButton();
                button.setBackground(new Color(255, 255, 255));
                button.setForeground(null);
                button.setBorder(null);
                return button;
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                JButton button = new JButton();
                button.setBackground(new Color(255, 255, 255));
                button.setForeground(null);
                button.setBorder(null);
                return button;
            }
        }));
        explorerPane.getVerticalScrollBar().setUI((new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = Color.GRAY;
            }

            @Override
            protected JButton createDecreaseButton(int orientation) {
                JButton button = new JButton();
                button.setBackground(new Color(255, 255, 255));
                button.setForeground(null);
                button.setBorder(null);
                return button;
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                JButton button = new JButton();
                button.setBackground(new Color(255, 255, 255));
                button.setForeground(null);
                button.setBorder(null);
                return button;
            }
        }));
        explorerPane.getVerticalScrollBar().setPreferredSize(new Dimension(5, 0));
        explorerPane.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 5));
        explorerPane.setMinimumSize(new Dimension(250, 0));

        textArea = new JTextArea();
        textArea.setFont(new Font("JetBrains Mono", Font.PLAIN, 15));
        textArea.setMargin(new Insets(10, 10, 10, 10));
        textArea.setMinimumSize(new Dimension(150, 0));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        splitPane.setLeftComponent(explorerPane);
        splitPane.setRightComponent(textArea);

        myTree.setCellRenderer(new MyTreeCellRenderer());
        myTree.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        myTree.addTreeSelectionListener(e -> {
        });
        myTree.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) myTree.getLastSelectedPathComponent();
                if (e.getClickCount() == 2) {
                    if (node == null) return;
                    Object nodeInfo = node.getUserObject();
                    textArea.setText(nodeInfo.toString());
                    System.out.println(Arrays.toString(node.getPath()));
                }
                if (SwingUtilities.isRightMouseButton(e)) {
                    if (node == null) return;
                    JPopupMenu popupMenu = new JPopupMenu();
                    popupMenu.setPreferredSize(new Dimension(100, 100));
                    JMenuItem open = getjMenuItem("Open");
                    JMenuItem delete = getjMenuItem("Delete");
                    JMenuItem rename = getjMenuItem("Rename");
                    popupMenu.add(open);
                    popupMenu.add(delete);
                    popupMenu.add(rename);
                    popupMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });
        add(title);
        add(splitPane);

//        myTree.setUI(new CustomizeUI.MyTreeUI());
    }

    private JMenuItem getjMenuItem(String name) {
        JMenuItem item = new JMenuItem(name);
        item.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        item.addActionListener(e -> {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) myTree.getLastSelectedPathComponent();
            if (node == null) return;
            Object nodeInfo = node.getUserObject();
            textArea.setText(nodeInfo.toString());
            System.out.println(Arrays.toString(node.getPath()));
        });
        return item;

    }
}
