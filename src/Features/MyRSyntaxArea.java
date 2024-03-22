package Features;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class MyRSyntaxArea extends RSyntaxTextArea {

    private RSyntaxTextArea textArea;
    private RTextScrollPane sp;

    public MyRSyntaxArea() {
        textArea = new RSyntaxTextArea();
        textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_CPLUSPLUS);
        textArea.setCodeFoldingEnabled(true);

        sp = new RTextScrollPane(textArea);
        sp.setFoldIndicatorEnabled(true);
        sp.getVerticalScrollBar().setUI(new CustomizeUI.MyScrollBarUI());
        sp.getVerticalScrollBar().setPreferredSize(new Dimension(5, 0));
        sp.getHorizontalScrollBar().setUI(new CustomizeUI.MyScrollBarUI());
        sp.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 5));
        textArea.setFont(new Font("JetBrains Mono", Font.PLAIN, 20));
        textArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_EQUALS) {
                    textArea.setFont(textArea.getFont().deriveFont(textArea.getFont().getSize() + 1f));
                    sp.getGutter().setLineNumberFont(textArea.getFont().deriveFont(textArea.getFont().getSize() - 2f));
                }
                if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_MINUS) {
                    textArea.setFont(textArea.getFont().deriveFont(textArea.getFont().getSize() - 1f));
                    sp.getGutter().setLineNumberFont(textArea.getFont().deriveFont(textArea.getFont().getSize() - 2f));
                }
            }
        });
    }

    public RTextScrollPane getSp() {
        return sp;
    }

    public RSyntaxTextArea getTextArea() {
        return textArea;
    }

    public void setTextArea(String text) {
        textArea.setText(text);
    }

    public void setSyntaxStyle(String fileExtension) {
        switch (fileExtension) {
            case "java" -> textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
            case "c" -> textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_C);
            case "cpp" -> textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_CPLUSPLUS);
            case "cs" -> textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_CSHARP);
            case "html" -> textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_HTML);
            case "css" -> textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_CSS);
            case "js" -> textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVASCRIPT);
            case "php" -> textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_PHP);
            case "py" -> textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_PYTHON);
            case "rb" -> textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_RUBY);
            case "sql" -> textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_SQL);
            case "xml" -> textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_XML);
            default -> textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_NONE);
        }
    }

//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(new Runnable() {
//            public void run() {
//                MyRSyntaxArea app = new MyRSyntaxArea();
//                JFrame frame = new JFrame("RSyntaxTextArea");
//                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//                frame.setContentPane(app.sp);
//                frame.pack();
//                frame.setLocationRelativeTo(null);
//                frame.setVisible(true);
//            }
//        });
//    }

}