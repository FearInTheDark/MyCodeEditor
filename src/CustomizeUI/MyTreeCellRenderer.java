package CustomizeUI;

import Features.Features;

import javax.swing.*;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;
import java.util.Arrays;

public class MyTreeCellRenderer extends DefaultTreeCellRenderer {
    Icon folderIcon;
    Icon fileIcon;
    private final String folderPath = "D:\\Java Learning\\Samples\\FileExplorer\\src\\icons\\folder.png";
    private final String filePath = "D:\\Java Learning\\Samples\\FileExplorer\\src\\icons\\file.png";
    private String specialFiles = "D:\\Java Learning\\Samples\\FileExplorer\\src\\icons\\fileIcons\\";
    private String[] extensions = {"3d", "abc", "ada", "android", "angular", "apollo", "applescript", "apps-script", "appveyor", "architecture", "arduino", "asciidoc", "assembly", "astro", "astyle", "audio", "aurelia", "authors", "auto", "autoit", "c", "chess", "chrome", "cmake", "coffee", "conduct", "console", "cpp", "credits", "crystal", "csharp", "css", "d", "dart", "database", "disc", "django", "dll", "docker", "document", "drawio", "drone", "email", "exe", "file", "firebase", "font", "forth", "fortran", "foxpro", "fsharp", "fusebox", "gamemaker", "gatsby", "gcp", "gemfile", "gemini", "git", "gitlab", "gitpod", "gleam", "go", "go-mod", "go_gopher", "godot", "godot-assets", "gradle", "grain", "graphcool", "graphql", "gridsome", "groovy", "grunt", "gulp", "h", "hack", "hardhat", "hcl", "hcl_light", "helm", "heroku", "hex", "hjson", "horusec", "hpp", "html", "http", "image", "jar", "java", "javaclass", "javascript", "jsconfig", "json", "kotlin", "laravel", "lib", "lock", "log", "nodejs", "npm", "objective-c", "objective-cpp", "openapi", "parcel", "pascal", "pdf", "perl", "php", "pinejs", "plastic", "powershell", "prettier", "python", "r", "raml", "react", "red", "replit", "riot", "ruby", "rust", "scala", "scheme", "search", "settings", "shader", "shaderlab", "silverstripe", "siyuan", "sketch", "slim", "slug", "sublime", "supabase", "svelte", "svg", "svgo", "svgr", "swagger", "swc", "swift", "syncpack", "table", "tailwindcss", "taskfile", "tauri", "tcl", "teal", "templ", "template", "terraform", "test-js", "test-jsx", "test-ts", "tex", "textlint", "tilt", "tldraw", "tldraw_light", "tobi", "tobimake", "todo", "travis", "tree", "tsconfig", "tune", "turborepo", "turborepo_light", "twig", "twine", "typescript", "typescript-def", "typst", "uml", "uml_light", "unocss", "url", "video", "vim", "visualstudio", "vite", "vscode", "vue", "xaml", "xml", "yaml", "yang", "zip"};


    public MyTreeCellRenderer() {
        ImageIcon folderII = new ImageIcon(folderPath);
        Image folderImage = folderII.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        this.folderIcon = new ImageIcon(folderImage);
        ImageIcon fileII = new ImageIcon(filePath);
        Image fileImage = fileII.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        fileIcon = new ImageIcon(fileImage);

//        ImageIcon folderII = new ImageIcon(folderPath);
//        Image folderImage = getScaledImage(folderII.getImage(), 20, 20);
//        this.folderIcon = new ImageIcon(folderImage);
//        ImageIcon fileII = new ImageIcon(filePath);
//        Image fileImage = getScaledImage(fileII.getImage(), 20, 20);
//        fileIcon = new ImageIcon(fileImage);
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

            if (Arrays.asList(extensions).contains(fileExtension)) {
                ImageIcon specialFileIcon = new ImageIcon(specialFiles + fileExtension + ".png");
                Image specialFileImage = specialFileIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
                setIcon(new ImageIcon(specialFileImage));
            } else {
                setIcon(fileIcon);
            }
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