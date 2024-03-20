package Features;

import javax.swing.tree.DefaultMutableTreeNode;
import java.io.File;

public class Features {
    public static String getFileExtension(String fileName) {
        if (fileName.lastIndexOf(".") == -1 || fileName.lastIndexOf(".") == 0 || fileName.lastIndexOf(".") == fileName.length() - 1) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }


    // TODO: Implement the getFullPath method
    public static String getFullPath(DefaultMutableTreeNode node, String ROOT_FOLDER) {
        StringBuilder path = new StringBuilder();
        path.append(node.getUserObject());
        while ((node = (DefaultMutableTreeNode) node.getParent()) != null) {
            if (node.getUserObject() == "D:") {
                path.insert(0, ROOT_FOLDER + File.separator);
            }
            path.insert(0, node.getUserObject() + File.separator);
        }
        return path.toString();
    }
}
