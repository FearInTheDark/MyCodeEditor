package Features;

import javax.swing.tree.DefaultMutableTreeNode;
import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class Features {

    public static void listAllFiles(Path currentPath, DefaultMutableTreeNode parentNode) throws IOException {
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
