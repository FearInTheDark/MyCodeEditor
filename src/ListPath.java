import CustomizeUI.MyScrollBarUI;
import CustomizeUI.MySplitPaneUI;
import CustomizeUI.MyTreeCellRenderer;
import Samples.LineNumberedTextArea;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.jdesktop.swingx.JXLabel;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTree;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class ListPath extends JFrame {
    private JXTree myTree;
    private DefaultMutableTreeNode root;
    private JSplitPane splitPane;
    private JScrollPane explorerPane, editorPane, textAreaScrollPane;
    private JTextPane textArea;
    private RSyntaxTextArea syntaxTextArea;
    private JXPanel header;
    private JXPanel fileExplorerFeatures, TextEditorFeatures, footer;
    private int WIDTH = 1200, HEIGHT = 850;

    public ListPath() throws Exception {
        root = new DefaultMutableTreeNode("D:");
        myTree = new JXTree(root);
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
                splitPane.setBounds(0, 100, getWidth(), getHeight() - 180);
                footer.setBounds(0, getHeight() - 80, getWidth(), 80);
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
        generateFileExplorerFeatures();
        generateTextEditorFeatures();
        generateEditorPane();
        generateTextArea();
        this.header = fileExplorerFeatures;


        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
//        splitPane.setDividerLocation(150);
        splitPane.setBounds(0, 100, WIDTH, HEIGHT - 180);
//        splitPane.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        explorerPane = new JScrollPane(myTree);
        splitPane.setDividerSize(5);
        splitPane.setUI(new MySplitPaneUI());
        explorerPane.getHorizontalScrollBar().setUI(new MyScrollBarUI());
        explorerPane.getVerticalScrollBar().setUI(new MyScrollBarUI());
        explorerPane.getVerticalScrollBar().setPreferredSize(new Dimension(5, 0));
        explorerPane.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 5));
        explorerPane.setMinimumSize(new Dimension(250, 0));


        splitPane.setLeftComponent(explorerPane);
        splitPane.setRightComponent(textArea);

        footer = new JXPanel(null);
        footer.setBounds(0, HEIGHT - 80, WIDTH, 80);
        footer.setBackground(Color.CYAN);

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
                    // If folder return
                    File file = new File(nodeInfo.toString());
                    if (file.isDirectory()) return;
                    splitPane.setRightComponent(editorPane);
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
        add(header);
        add(splitPane);
        add(footer);
        addJMenuBar();
    }

    private void generateTextArea() {
        textArea = new JTextPane();
        textArea.setFont(new Font("JetBrains Mono", Font.PLAIN, 15));
        textArea.setForeground(new Color(0xFFFFFF));
        textArea.setMargin(new Insets(10, 10, 10, 10));
        textArea.setMinimumSize(new Dimension(150, 0));
//        textArea.setLineWrap(true);
//        textArea.setWrapStyleWord(true);
//        textArea.setUI(new MyTextAreaUI());
        textArea.setContentType("text/html");
        // Welcome by a html formatted text
        // Text Align center, font size 30, font family Comic Sans MS, red color
        String htmlString = "<html><body><h1 style='color: red; font-family: Segoe UI; text-align: center; font-size: 50px'>Welcome</h1><p style='font-size: 20px; color: blue;'>This is a paragraph.</p></body></html>";
        textArea.setText(htmlString);
        textArea.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                remove(header);
                header = TextEditorFeatures;
                add(header);
                textArea.setSelectionStart(0);
                textArea.setSelectionEnd(textArea.getText().length());
                repaint();
                revalidate();
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                remove(header);
                header = fileExplorerFeatures;
                add(header);
                repaint();
                revalidate();
            }
        });
    }

    private void generateEditorPane() {
        this.editorPane = LineNumberedTextArea.getScrollPane();
        editorPane.setMinimumSize(new Dimension(150, 0));
    }


    private void addJMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        String[] fileItems = {"New", "Open", "Save", "Save As", "Exit"};
        String[] editItems = {"Undo", "Redo", "Cut", "Copy", "Paste", "Delete", "Find", "Replace"};
        String[] viewItems = {"Zoom In", "Zoom Out", "Full Screen"};
        String[] helpItems = {"About", "Contact"};
        JMenu fileMenu = getJMenu("File", fileItems);
        JMenu edit = getJMenu("Edit", editItems);
        JMenu view = getJMenu("View", viewItems);
        JMenu help = getJMenu("Help", helpItems);

        menuBar.add(fileMenu);
        menuBar.add(edit);
        menuBar.add(view);
        menuBar.add(help);
        setJMenuBar(menuBar);
    }

    private JMenu getJMenu(String name, String[] items) {
        String fixedName = "<html> <span style='text-align: center;'>" + name + " </span> </html>";
        JMenu menu = new JMenu(fixedName);
        menu.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        for (String item : items) {
            JMenuItem menuItem = getjMenuItem(item);
            menu.add(menuItem);
        }
//        menu.addSeparator();
//        Dimension dimension = new Dimension(menu.getPreferredSize().width + 10, menu.getPreferredSize().height);
//        menu.setPreferredSize(dimension);
        return menu;
    }

    private JMenuItem getjMenuItem(String name) {
        JMenuItem item = new JMenuItem(name);
        item.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        item.setPreferredSize(new Dimension(item.getPreferredSize().width + 20, item.getPreferredSize().height + 5));
        item.setBorder(null);
        item.addActionListener(e -> {
            if (name.equals("Exit")) System.exit(0);
        });
        return item;
    }

    private void generateFileExplorerFeatures() {
        fileExplorerFeatures = new JXPanel(null);
        fileExplorerFeatures.setBounds(0, 0, getWidth(), 100);
//        fileExplorerFeatures.setBackground(Color.CYAN);

        JLabel title = new JLabel("File Explorer");
        ImageIcon icon = new ImageIcon("src/icons/frame.png");
        title.setIcon(icon);
        title.setBounds(0, 0, 300, 100);
        title.setFont(new Font("Comic Sans MS", Font.BOLD, 30));
        title.setHorizontalAlignment(SwingConstants.CENTER);

        fileExplorerFeatures.add(title);
    }

    private void generateTextEditorFeatures() {
        TextEditorFeatures = new JXPanel(null);
        TextEditorFeatures.setBounds(0, 0, getWidth(), 100);
        TextEditorFeatures.setBackground(new Color(0xadadad));

        JXLabel title = new JXLabel("Text Editor");
        Image icon = new ImageIcon("src/icons/Acode.png").getImage().getScaledInstance(100, 100, Image.SCALE_AREA_AVERAGING);
        ImageIcon imageIcon = new ImageIcon(icon);
        title.setIcon(imageIcon);
        title.setBounds(0, 0, 300, 100);
        title.setFont(new Font("Comic Sans MS", Font.BOLD, 30));
        title.setHorizontalAlignment(SwingConstants.CENTER);

        TextEditorFeatures.add(title);
    }
}
