import CustomizeUI.MyScrollBarUI;
import CustomizeUI.MySplitPaneUI;
import CustomizeUI.MyTreeCellRenderer;
import Features.LineNumberedTextArea;
import Features.MyRSyntaxArea;
import org.jdesktop.swingx.JXLabel;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTree;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static Features.Features.*;

public class ListPath extends JFrame {
    private final String ROOT_FOLDER = "Learning Course";
    private JXTree myTree;
    private DefaultMutableTreeNode root;
    private JSplitPane splitPane;
    private JScrollPane explorerPane;
    private JTextPane textPane;
    private MyRSyntaxArea syntaxTextArea;
    private JXLabel title;
    private JXPanel fileExplorerFeatures, TextEditorFeatures, header, footer;
    private LineNumberedTextArea editorPane;
    private String textContent;
    private int WIDTH = 1200, HEIGHT = 850;
    private File selectedFile;

    public ListPath() throws Exception {
        root = new DefaultMutableTreeNode("D:");
        myTree = new JXTree(root);
        selectedFile = null;

        Path rootPath = Paths.get("D:", ROOT_FOLDER);
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
                resizeElements();
            }
        });
        repaint();
        show();
    }

    private void resizeElements() {
        WIDTH = getWidth();
        HEIGHT = getHeight();
        header.setBounds(0, 0, WIDTH, 100);
        splitPane.setBounds(0, 100, WIDTH, HEIGHT - 180);
        footer.setBounds(0, HEIGHT - 80, WIDTH, 20);
    }

    private void initUI() {
        generateFileExplorerFeatures();
        generateTextEditorFeatures();
        generateEditorPane();
        generateTextPane();
        generateRSyntaxTextArea();
        this.header = fileExplorerFeatures;

        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setBounds(0, 100, WIDTH, HEIGHT - 180);
        splitPane.setBorder(null);

        explorerPane = new JScrollPane(myTree);
        splitPane.setDividerSize(5);
        splitPane.setUI(new MySplitPaneUI());
        explorerPane.getHorizontalScrollBar().setUI(new MyScrollBarUI());
        explorerPane.getVerticalScrollBar().setUI(new MyScrollBarUI());
        explorerPane.getVerticalScrollBar().setPreferredSize(new Dimension(5, 0));
        explorerPane.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 5));
        explorerPane.setMinimumSize(new Dimension(250, 0));


        splitPane.setLeftComponent(explorerPane);
        splitPane.setRightComponent(textPane);

        footer = new JXPanel(null);
        footer.setBounds(0, HEIGHT - 80, WIDTH, 20);
        footer.setBackground(new Color(40, 44, 52));

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
                    try {
                        if (node == null) return;
                        String filePath = getFullPath(node, ROOT_FOLDER);
//                        System.out.println(filePath);
                        selectedFile = new File(filePath);
                        System.out.print(selectedFile.isFile() ? "File - " : "Directory - " + selectedFile.getName() + "\n");
                        if (selectedFile.isFile()) {
                            String fileExtension = getFileExtension(selectedFile.getName());
                            System.out.println("File Extension: " + fileExtension);
                            syntaxTextArea.setSyntaxStyle(fileExtension);
                            if (splitPane.getRightComponent() == textPane) {
                                splitPane.setRightComponent(editorPane.getScrollPane());
                            }
                            textContent = Files.readString(selectedFile.toPath());
                            setEditorContent(textContent);
                        }
                    } catch (IOException ioException) {
                        System.err.println("Error: " + ioException.getMessage());

                    }
                }
                if (SwingUtilities.isRightMouseButton(e)) {
                    if (node == null) return;
                    JPopupMenu popupMenu = new JPopupMenu();
                    popupMenu.setPreferredSize(new Dimension(100, 100));
                    JMenuItem open = getjMenuItem("Open");
                    open.addActionListener(e1 -> {
                        try {
                            selectedFile = new File(getFullPath(node, ROOT_FOLDER));
                            if (selectedFile.isDirectory()) return;
                            textContent = Files.readString(selectedFile.toPath());
                            if (splitPane.getRightComponent() == textPane) {
                                splitPane.setRightComponent(editorPane.getScrollPane());
                            }
                            setEditorContent(textContent);
                        } catch (IOException ioException) {
                            System.err.println("Error: " + ioException.getMessage());
                        }
                    });
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


    //These methods are used to generate the components of the UI
    private void generateTextPane() {
        textPane = new JTextPane();
        textPane.setFont(new Font("JetBrains Mono", Font.PLAIN, 15));
        textPane.setForeground(new Color(0xFFFFFF));
        textPane.setMargin(new Insets(10, 10, 10, 10));
        textPane.setMinimumSize(new Dimension(150, 0));
        textPane.setContentType("text/html");
        textPane.setFocusable(false);
        textPane.setEditable(false);
        String htmlString = "<html><body><h1 style='color: red; font-family: Segoe UI; text-align: center; font-size: 50px'>Welcome</h1><p style='font-size: 20px; color: blue;'>This is a paragraph.</p></body></html>";
        textPane.setText(htmlString);
    }

    private void generateEditorPane() {
        this.editorPane = new LineNumberedTextArea();
        editorPane.setMinimumSize(new Dimension(150, 0));
        addFocus(editorPane.getTextArea());
    }

    private void generateRSyntaxTextArea() {
        syntaxTextArea = new MyRSyntaxArea();
        syntaxTextArea.setSyntaxEditingStyle("text/java");
        addFocus(syntaxTextArea.getTextArea());
    }

    private void addFocus(JTextComponent textComponent) {
        textComponent.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent evt) {
                remove(header);
                header = TextEditorFeatures;
                add(header);
                repaint();
                revalidate();
            }

            public void focusLost(FocusEvent evt) {
                remove(header);
                header = fileExplorerFeatures;
                add(header);
                repaint();
                revalidate();
            }
        });
    }

    private void addJMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        String[] fileItems = {"New", "Open", "Save", "Save As", "Exit"};
        String[] editItems = {"Undo", "Redo", "Cut", "Copy", "Paste", "Delete", "Find", "Replace"};
        String[] viewItems = {"Zoom In", "Zoom Out", "Full Screen", "Switch Editor"};
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
        switch (name) {
            case "Exit" -> item.addActionListener(e -> System.exit(0));
            case "Switch Editor" -> item.addActionListener(e -> {
                if (splitPane.getRightComponent() == syntaxTextArea) {
                    splitPane.setRightComponent(editorPane.getScrollPane());
                    textContent = syntaxTextArea.getTextArea().getText();
                    editorPane.setTextArea(textContent);
                } else {
                    splitPane.setRightComponent(syntaxTextArea.getSp());
                    textContent = editorPane.getTextArea().getText();
                    syntaxTextArea.setTextArea(textContent);
                }
            });
            case "About" -> item.addActionListener(e -> {
                JOptionPane.showMessageDialog(null, "This is a simple text editor", "About", JOptionPane.INFORMATION_MESSAGE);
            });
            case "Full Screen" -> item.addActionListener(e -> {
                if (getExtendedState() == JFrame.MAXIMIZED_BOTH) {
                    setExtendedState(JFrame.NORMAL);
                    resizeElements();
                } else {
                    setExtendedState(JFrame.MAXIMIZED_BOTH);
                    resizeElements();
                }
            });
        }
        return item;
    }

    private void generateFileExplorerFeatures() {
        fileExplorerFeatures = new JXPanel(null);
        fileExplorerFeatures.setBounds(0, 0, WIDTH, 100);

        title = new JXLabel("File Explorer");
        ImageIcon icon = new ImageIcon("src/icons/frame.png");
        title.setIcon(icon);
        title.setBounds(0, 0, 300, 100);
        title.setFont(new Font("Comic Sans MS", Font.BOLD, 30));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setToolTipText("<html><span style='font-family: Segoe UI'>This is a tooltip</span></html>");

        title.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
            }
        });

        fileExplorerFeatures.add(title);
    }

    private void generateTextEditorFeatures() {
        TextEditorFeatures = new JXPanel(null);
        TextEditorFeatures.setBounds(0, 0, WIDTH, 100);
        TextEditorFeatures.setBackground(new Color(0xadadad));

        title = new JXLabel("Text Editor");
        Image icon = new ImageIcon("src/icons/Acode.png").getImage().getScaledInstance(100, 100, Image.SCALE_AREA_AVERAGING);
        ImageIcon imageIcon = new ImageIcon(icon);
        title.setIcon(imageIcon);
        title.setBounds(0, 0, 300, 100);
        title.setFont(new Font("Comic Sans MS", Font.BOLD, 30));
        title.setHorizontalAlignment(SwingConstants.CENTER);

        TextEditorFeatures.add(title);
    }

    private void setEditorContent(String textContent) {
        if (splitPane.getRightComponent() == editorPane) {
            editorPane.setTextArea(textContent);
        } else {
            syntaxTextArea.setTextArea(textContent);
        }
    }
}
