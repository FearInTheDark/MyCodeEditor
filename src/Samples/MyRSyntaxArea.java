package Samples;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;

public class MyRSyntaxArea extends RSyntaxTextArea {

    private RSyntaxTextArea textArea;
    private RTextScrollPane sp;

    public MyRSyntaxArea() {
        textArea = new RSyntaxTextArea(20, 60);
        textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
        textArea.setCodeFoldingEnabled(true);
        sp = new RTextScrollPane(textArea);
        sp.setFoldIndicatorEnabled(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                MyRSyntaxArea app = new MyRSyntaxArea();
                JFrame frame = new JFrame("RSyntaxTextArea");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setContentPane(app.sp);
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }

}