package Samples;

import CustomizeUI.MyScrollBarUI;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LineNumberedTextArea extends JFrame {

    private JTextArea textArea;
    private JTextArea lineNumberArea;
    private JScrollPane scrollPane;

    public LineNumberedTextArea() {
        setTitle("Line Numbered JTextArea");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        initComponents();
        setVisible(true);
    }


    private void initComponents() {
        textArea = new JTextArea();
        textArea.setFont(new Font("JetBrains Mono", Font.PLAIN, 12));
        textArea.setMargin(new Insets(10, 10, 10, 10));

        lineNumberArea = new JTextArea("1\n");
        lineNumberArea.setBackground(Color.LIGHT_GRAY);
//        lineNumberArea.setEditable(false);
//        lineNumberArea.setFocusable(false);
        lineNumberArea.setMargin(new Insets(10, 10, 10, 10));
        lineNumberArea.setFont(new Font("JetBrains Mono", Font.BOLD, 12));
        lineNumberArea.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    int lineNumber = lineNumberArea.getLineOfOffset(lineNumberArea.viewToModel(e.getPoint()));
                    Element root = textArea.getDocument().getDefaultRootElement();
                    Element lineElement = root.getElement(lineNumber);
                    int startOffset = lineElement.getStartOffset();
                    int endOffset = lineElement.getEndOffset();
                    textArea.select(startOffset, endOffset - 1);
                } catch (BadLocationException ex) {
                    System.out.println(ex.getMessage() + " at " + ex.getStackTrace()[0].toString());
                }
            }
        });


        scrollPane = new JScrollPane(textArea);
        scrollPane.setRowHeaderView(lineNumberArea);
        scrollPane.getVerticalScrollBar().setUI(new MyScrollBarUI());
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(5, 0));
        scrollPane.getHorizontalScrollBar().setUI(new MyScrollBarUI());
        scrollPane.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 5));

        textArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateLineNumbers();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateLineNumbers();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateLineNumbers();
            }
        });

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(scrollPane, BorderLayout.CENTER);
    }


    private void updateLineNumbers() {
        int caretPosition = textArea.getDocument().getLength();
        Element root = textArea.getDocument().getDefaultRootElement();
        String text = "1\n";
        for (int i = 2; i < root.getElementIndex(caretPosition) + 2; i++) {
            text += i + "\n";
        }
        lineNumberArea.setText(text);
    }
//    public static void main(String[] args) {
//        new LineNumberedTextArea();
//    }

    public static JScrollPane getScrollPane() {
        return new LineNumberedTextArea().scrollPane;
    }
}

