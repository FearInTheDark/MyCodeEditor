package CustomizeUI;

import Features.Features;

import javax.swing.*;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;
import java.io.File;

public class MyTreeCellRenderer extends DefaultTreeCellRenderer {
    private final String folderPath = "D:\\Java Learning\\Samples\\FileExplorer\\src\\icons\\folderIcons\\folder.png";
    private final String folderExpandPath = "D:\\Java Learning\\Samples\\FileExplorer\\src\\icons\\folderIcons\\folder-open.png";
    private final String filePath = "D:\\Java Learning\\Samples\\FileExplorer\\src\\icons\\file.png";
    Icon folderIcon, folderExpandIcon;
    Icon fileIcon;
    private String specialFiles = "D:\\Java Learning\\Samples\\FileExplorer\\src\\icons\\fileIcons\\";
    private String specialFolders = "D:\\Java Learning\\Samples\\FileExplorer\\src\\icons\\folderIcons\\";


    public MyTreeCellRenderer() {
        ImageIcon folderII = new ImageIcon(folderPath);
        Image folderImage = folderII.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        this.folderIcon = new ImageIcon(folderImage);
        ImageIcon folderExpandII = new ImageIcon(folderExpandPath);
        Image folderExpandImage = folderExpandII.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        this.folderExpandIcon = new ImageIcon(folderExpandImage);
        ImageIcon fileII = new ImageIcon(filePath);
        Image fileImage = fileII.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        fileIcon = new ImageIcon(fileImage);
    }
//    private Image getScaledImage(Image srcImg, int w, int h){
//        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
//        Graphics2D g2 = resizedImg.createGraphics();
//
//        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
//        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
//        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//
//        g2.drawImage(srcImg, 0, 0, w, h, null);
//        g2.dispose();
//
//        return resizedImg;
//    }


    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
        if (leaf) {
            String fileName = value.toString();
            String fileExtension = Features.getFileExtension(fileName);
            if (new File(specialFiles + fileExtension + ".png").exists()) {
                ImageIcon specialFileIcon = new ImageIcon(specialFiles + fileExtension + ".png");
                Image specialFileImage = specialFileIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
                setIcon(new ImageIcon(specialFileImage));
            } else {
                setIcon(fileIcon);
            }
        } else {
            String fileFolder = specialFolders + "folder-" + value + ".png";
            if (new File(fileFolder).exists()) {
                if (expanded) {
                    String fileFolderExpanded = specialFolders + "folder-" + value + "-open.png";
                    ImageIcon specialFolderIcon = new ImageIcon(fileFolderExpanded);
                    Image specialFolderImage = specialFolderIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
                    setIcon(new ImageIcon(specialFolderImage));
                } else {
                    ImageIcon specialFolderIcon = new ImageIcon(fileFolder);
                    Image specialFolderImage = specialFolderIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
                    setIcon(new ImageIcon(specialFolderImage));
                }
            } else if (expanded) setIcon(folderExpandIcon);
            else setIcon(folderIcon);
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