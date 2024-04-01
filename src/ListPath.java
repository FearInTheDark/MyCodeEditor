import CustomizeUI.*;
import Features.LineNumberedTextArea;
import Features.MyRSyntaxArea;
import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXLabel;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTree;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import static Features.Features.*;

public class ListPath extends JXFrame {
    private final String ROOT_FOLDER = "Learning Course";
    private final String ROOT_DISK = "D:";
    private final JXTree myTree;
    private int DEFAULT_FONT_SIZE = 20;
    private final Path rootPath;
    private DefaultMutableTreeNode root;
    private JSplitPane splitPane, mainSplitPane;
    private JScrollPane explorerPane;
    private MyTerminal terminal;
    private JTextPane textPane;
    private Object currentRightComponent;
    private MyRSyntaxArea syntaxTextArea;
    private JXLabel title;
    private JXPanel fileExplorerFeatures, TextEditorFeatures, header, footer;
    private LineNumberedTextArea editorPane;
    private String textContent;
    private int WIDTH = 1500, HEIGHT = 1050;
    private File selectedFile;
    private TreePath[] selectedPaths;

    public ListPath() throws Exception {
        root = new DefaultMutableTreeNode(ROOT_DISK);
        myTree = new JXTree(root);
        selectedFile = null;

        rootPath = Paths.get(ROOT_DISK, ROOT_FOLDER);
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
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_X) switchEditor();
            }
        });
        repaint();
        show();
    }

    private void initUI() {
        generateFileExplorerFeatures();
        generateTextEditorFeatures();
        generateEditorPane();
        generateTextPane();
        generateRSyntaxTextArea();
        generateTerminal();
        this.header = fileExplorerFeatures;
        currentRightComponent = editorPane;

        mainSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        mainSplitPane.setBounds(0, 100, WIDTH, HEIGHT - 200);
        mainSplitPane.setBorder(null);


        explorerPane = new JScrollPane(myTree);
        explorerPane.getHorizontalScrollBar().setUI(new MyScrollBarUI());
        explorerPane.getVerticalScrollBar().setUI(new MyScrollBarUI());
        explorerPane.getVerticalScrollBar().setPreferredSize(new Dimension(5, 0));
        explorerPane.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 5));
        explorerPane.setMinimumSize(new Dimension(250, 0));
        explorerPane.setBorder(null);


        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setMinimumSize(new Dimension(WIDTH, HEIGHT - 300));
        splitPane.setBorder(null);
        splitPane.setDividerSize(5);
        splitPane.setUI(new MySplitPaneUI());
        splitPane.setLeftComponent(explorerPane);
        splitPane.setRightComponent(textPane);

        mainSplitPane.setTopComponent(splitPane);
        mainSplitPane.setBottomComponent(terminal.getScrollPane());
        mainSplitPane.setUI(new MySplitPaneUI());
        mainSplitPane.setDividerSize(5);
//        mainSplitPane.setDividerLocation(HEIGHT - 300);

        footer = new JXPanel(null);
        footer.setBounds(0, HEIGHT - 100, WIDTH, 100);
        footer.setBackground(new Color(40, 44, 52));

        myTree.setCellRenderer(new MyTreeCellRenderer());
        myTree.setFont(new Font("Segoe UI", Font.PLAIN, DEFAULT_FONT_SIZE));
        myTree.setLargeModel(true);
        myTree.setClosedIcon(null);
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
//                    popupMenu.setPreferredSize(new Dimension(100, 100));
                    JMenuItem open = getjMenuItem("Open");
                    open.addActionListener(e1 -> {
                        try {
                            selectedFile = new File(getFullPath(node, ROOT_FOLDER));
                            if (selectedFile.isDirectory()) return;
                            String fileExtension = getFileExtension(selectedFile.getName());
                            textContent = Files.readString(selectedFile.toPath());
                            if (splitPane.getRightComponent() == textPane) {
                                splitPane.setRightComponent(editorPane.getScrollPane());
                            }
                            syntaxTextArea.setSyntaxStyle(fileExtension);
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
        myTree.getSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
        myTree.addTreeSelectionListener(e -> selectedPaths = myTree.getSelectionPaths());
        myTree.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                switch (e.getKeyChar()) {
                    case KeyEvent.VK_ENTER -> {
                        for (TreePath path : selectedPaths) {
                            DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
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
                        }
                    }
                    case KeyEvent.VK_DELETE -> {
                        for (TreePath path : selectedPaths) {
                            DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
                            try {
                                selectedFile = new File(getFullPath(node, ROOT_FOLDER));
                                int option = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete " + selectedFile.getName() + "?", "Delete", JOptionPane.YES_NO_OPTION);
                                if (option == JOptionPane.NO_OPTION) return;
                                Files.delete(selectedFile.toPath());
                                DefaultMutableTreeNode parent = (DefaultMutableTreeNode) node.getParent();
                                parent.remove(node);
                                myTree.updateUI();
                            } catch (Exception ioException) {
                                System.err.println("Error: " + ioException.getMessage());
                            }
                        }
                    }
                    case KeyEvent.VK_F2 -> {
                        for (TreePath path : selectedPaths) {
                            DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
                            try {
                                selectedFile = new File(getFullPath(node, ROOT_FOLDER));
                                String newName = JOptionPane.showInputDialog(null, "Enter new name for " + selectedFile.getName());
//                                if (newName == null || newName.trim().isEmpty()) return;
                                if (selectedFile.isDirectory()) {
                                    Files.move(selectedFile.toPath(), selectedFile.toPath().resolveSibling(newName));
                                } else {
                                    String fileExtension = getFileExtension(selectedFile.getName());
                                    Files.move(selectedFile.toPath(), selectedFile.toPath().resolveSibling(newName + "." + fileExtension));
                                }
                                node.setUserObject(newName);
                                myTree.updateUI();
                            } catch (Exception ioException) {
                                System.err.println("Error: " + ioException.getMessage());
                            }
                        }
                    }
                    case KeyEvent.VK_F5 -> {
                        try {
                            root.removeAllChildren();
                            listAllFiles(rootPath, root);
                            myTree.updateUI();
                            System.out.println("Refreshed");
                        } catch (IOException ioException) {
                            System.err.println("Error: " + ioException.getMessage());
                        }
                    }

                }
            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
        add(header);
        add(mainSplitPane);
        add(footer);
        addJMenuBar();
    }



    //These methods are used to generate the components of the UI
    private void generateTerminal() {
        terminal = new MyTerminal();
        terminal.setDEFAULT_PATH("D:\\Java Learning\\Samples\\FileExplorer");
    }

    private void generateTextPane() {
        textPane = new JTextPane();
        textPane.setFont(new Font("JetBrains Mono", Font.PLAIN, 15));
        textPane.setForeground(new Color(0xFFFFFF));
        textPane.setMargin(new Insets(10, 10, 10, 10));
        textPane.setMinimumSize(new Dimension(150, 0));
        textPane.setContentType("text/html");
        textPane.setFocusable(false);
        textPane.setEditable(false);
        String htmlString = "<html><body><h1 style='color: red; font-family: Hey Comic; text-align: center; font-size: 50px'>Welcome</h1><p style='font-size: 30px; color: blue; text-align: center; font-family: Hey Comic'>Double click on any file to open editor.</p></body></html>";
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
                resizeElements();
                repaint();
                revalidate();
            }

            public void focusLost(FocusEvent evt) {
                remove(header);
                header = fileExplorerFeatures;
                add(header);
                resizeElements();
                repaint();
                revalidate();
            }
        });
        // Add key listener to the text area : Ctrl + S to save file
        textComponent.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_S) {
                    if (selectedFile == null) {
                        JOptionPane.showMessageDialog(null, "Please select a file to save", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    try {
                        Files.writeString(selectedFile.toPath(), textComponent.getText());
                        new MyAlertFrame("Success", "File saved successfully");
                    } catch (IOException ioException) {
                        System.err.println("Error: " + ioException.getMessage());
                    }
                }
            }
        });
    }

    private void addJMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        String[] fileItems = {"New", "Open File", "Save", "Save As", "Exit"};
        String[] editItems = {"Undo", "Redo", "Cut", "Copy", "Paste", "Find", "Replace"};
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
        menu.setFont(new Font("Segoe UI", Font.PLAIN, 18));
//        for (String item : items) {
//            JMenuItem menuItem = getjMenuItem(item);
//            menu.add(menuItem);
//        }
        Arrays.stream(items).map(this::getjMenuItem).forEach(menu::add);
        return menu;
    }

    private JMenuItem getjMenuItem(String name) {
        JMenuItem item = new JMenuItem(name);
        item.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        item.setPreferredSize(new Dimension(item.getPreferredSize().width + 20, item.getPreferredSize().height + 5));
        item.setPreferredSize(new Dimension(item.getPreferredSize().width + 20, item.getPreferredSize().height + 5));

        ImageIcon icon = new ImageIcon("D:\\Java Learning\\Samples\\FileExplorer\\src\\icons\\JMenuItem_Icons\\" + name + ".png");
        item.setIcon(icon);

        item.setBackground(Color.WHITE);
        item.setBorder(null);
        switch (name) {
            case "About" ->
                    item.addActionListener(e -> JOptionPane.showMessageDialog(null, "This is a simple text editor", "About", JOptionPane.INFORMATION_MESSAGE));
            case "Copy" -> item.addActionListener(e -> {
                if (currentRightComponent == editorPane) {
                    editorPane.getTextArea().copy();
                } else {
                    syntaxTextArea.getTextArea().copy();
                }
            });
            case "Exit" -> item.addActionListener(e -> System.exit(0));
            case "Full Screen" -> item.addActionListener(e -> {
                if (getExtendedState() == JFrame.MAXIMIZED_BOTH) {
                    setExtendedState(JFrame.NORMAL);
                    resizeElements();
                } else {
                    setExtendedState(JFrame.MAXIMIZED_BOTH);
                    resizeElements();
                }
            });
            case "Switch Editor" -> item.addActionListener(e -> switchEditor());
            case "Cut" -> item.addActionListener(e -> {
                if (currentRightComponent == editorPane) {
                    editorPane.getTextArea().cut();
                } else {
                    syntaxTextArea.getTextArea().cut();
                }
            });
            case "Paste" -> item.addActionListener(e -> {
                if (currentRightComponent == editorPane) {
                    editorPane.getTextArea().paste();
                } else {
                    syntaxTextArea.getTextArea().paste();
                }
            });
            case "Zoom In" -> item.addActionListener(e -> {
                DEFAULT_FONT_SIZE += 2;
                myTree.setFont(new Font("Segoe UI", Font.PLAIN, DEFAULT_FONT_SIZE));
                getJMenu("File", new String[0]).setFont(new Font("Segoe UI", Font.PLAIN, DEFAULT_FONT_SIZE));
                getJMenu("Edit", new String[0]).setFont(new Font("Segoe UI", Font.PLAIN, DEFAULT_FONT_SIZE));
                getJMenu("View", new String[0]).setFont(new Font("Segoe UI", Font.PLAIN, DEFAULT_FONT_SIZE));
                getJMenu("Help", new String[0]).setFont(new Font("Segoe UI", Font.PLAIN, DEFAULT_FONT_SIZE));
                if (currentRightComponent == editorPane) {
                    editorPane.getTextArea().setFont(new Font("JetBrains Mono", Font.PLAIN, editorPane.getTextArea().getFont().getSize() + 2));
                } else {
                    syntaxTextArea.getTextArea().setFont(new Font("JetBrains Mono", Font.PLAIN, syntaxTextArea.getTextArea().getFont().getSize() + 2));
                }
            });
            case "Zoom Out" -> item.addActionListener(e -> {
                DEFAULT_FONT_SIZE -= 2;
                myTree.setFont(new Font("Segoe UI", Font.PLAIN, DEFAULT_FONT_SIZE));
                getJMenu("File", new String[0]).setFont(new Font("Segoe UI", Font.PLAIN, DEFAULT_FONT_SIZE));
                getJMenu("Edit", new String[0]).setFont(new Font("Segoe UI", Font.PLAIN, DEFAULT_FONT_SIZE));
                getJMenu("View", new String[0]).setFont(new Font("Segoe UI", Font.PLAIN, DEFAULT_FONT_SIZE));
                getJMenu("Help", new String[0]).setFont(new Font("Segoe UI", Font.PLAIN, DEFAULT_FONT_SIZE));
                if (currentRightComponent == editorPane) {
                    editorPane.getTextArea().setFont(new Font("JetBrains Mono", Font.PLAIN, editorPane.getTextArea().getFont().getSize() - 2));
                } else {
                    syntaxTextArea.getTextArea().setFont(new Font("JetBrains Mono", Font.PLAIN, syntaxTextArea.getTextArea().getFont().getSize() - 2));
                }
            });
            case "Open" -> item.addActionListener(e -> {
                try {
                    selectedFile = new File(getFullPath((DefaultMutableTreeNode) myTree.getLastSelectedPathComponent(), ROOT_FOLDER));
                    if (selectedFile.isDirectory()) return;
                    String fileExtension = getFileExtension(selectedFile.getName());
                    textContent = Files.readString(selectedFile.toPath());
                    if (splitPane.getRightComponent() == textPane) {
                        splitPane.setRightComponent(editorPane.getScrollPane());
                    }
                    syntaxTextArea.setSyntaxStyle(fileExtension);
                    setEditorContent(textContent);
                } catch (IOException ioException) {
                    System.err.println("Error: " + ioException.getMessage());
                }
            });
        }
        return item;
    }

    private void switchEditor() {
        if (currentRightComponent == syntaxTextArea) {
            textContent = syntaxTextArea.getTextArea().getText();
            splitPane.setRightComponent(editorPane.getScrollPane());
            editorPane.setTextArea(textContent);
            currentRightComponent = editorPane;
        } else {
            textContent = editorPane.getTextArea().getText();
            splitPane.setRightComponent(syntaxTextArea.getSp());
            syntaxTextArea.setTextArea(textContent);
            currentRightComponent = syntaxTextArea;
        }
    }

    private void generateFileExplorerFeatures() {
        fileExplorerFeatures = new JXPanel(null);
        fileExplorerFeatures.setBounds(0, 0, WIDTH, 100);

        title = new JXLabel("File Explorer");
        ImageIcon icon = new ImageIcon("D:\\Java Learning\\Samples\\FileExplorer\\src\\icons\\frame.png");
        title.setIcon(icon);
        title.setBounds(0, 0, 300, 100);
        title.setFont(new Font("Comic Sans MS", Font.BOLD, DEFAULT_FONT_SIZE + 10));
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
        Image icon = new ImageIcon("D:\\Java Learning\\Samples\\FileExplorer\\src\\icons\\Acode.png").getImage().getScaledInstance(100, 100, Image.SCALE_AREA_AVERAGING);
        ImageIcon imageIcon = new ImageIcon(icon);
        title.setIcon(imageIcon);
        title.setBounds(0, 0, 300, 100);
        title.setFont(new Font("Comic Sans MS", Font.BOLD, DEFAULT_FONT_SIZE + 10));
        title.setHorizontalAlignment(SwingConstants.CENTER);

        TextEditorFeatures.add(title);
    }

    private void resizeElements() {
        WIDTH = getWidth();
        HEIGHT = getHeight();
        header.setBounds(0, 0, WIDTH, 100);
        mainSplitPane.setBounds(0, 100, WIDTH, HEIGHT - 200);
        footer.setBounds(0, HEIGHT - 100, WIDTH, 100);
    }

    private void setEditorContent(String textContent) {
        if (currentRightComponent == editorPane) {
            editorPane.setTextArea(textContent);
        } else {
            syntaxTextArea.setTextArea(textContent);
        }
    }
}
