package Samples;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Element;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class LineNumberedTextArea extends JScrollPane {
    private JTextArea textArea;
    private JTextPane lineNumbers;

    public LineNumberedTextArea(JTextArea textArea) {
        super(textArea);
        this.textArea = textArea;

        // Create line number component
        lineNumbers = new JTextPane();
        lineNumbers.setBackground(Color.LIGHT_GRAY);
        lineNumbers.setEditable(false);
        lineNumbers.setFont(new Font("JetBrains Mono", Font.PLAIN, 16));

        // Add line numbers to scroll pane
        setRowHeaderView(lineNumbers);

        // Update line numbers when text area changes
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
        textArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                if (e.getKeyChar() == '{') {
                    SwingUtilities.invokeLater(() -> {
                        textArea.insert("}", textArea.getCaretPosition());
                        textArea.setCaretPosition(textArea.getCaretPosition() - 1);
                    });
                }
                if (e.getKeyChar() == '(') {
                    SwingUtilities.invokeLater(() -> {
                        textArea.insert(")", textArea.getCaretPosition());
                        textArea.setCaretPosition(textArea.getCaretPosition() - 1);
                    });
                }

            }
        });
    }

    private void updateLineNumbers() {
        StringBuilder lineNums = new StringBuilder();
        Element root = textArea.getDocument().getDefaultRootElement();
        for (int i = 2; i < root.getElementCount() + 2; i++) {
            lineNums.append(i).append(System.lineSeparator());
        }
        lineNumbers.setText(lineNums.toString());
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Line Numbered Text Area");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new BorderLayout());

        JTextArea textArea = new JTextArea();
        textArea.setFont(new Font("JetBrains Mono", Font.PLAIN, 16));
        textArea.setTabSize(2);
        textArea.setMargin(new Insets(10, 10, 10, 10));

        LineNumberedTextArea lineNumberedTextArea = new LineNumberedTextArea(textArea);
        frame.add(lineNumberedTextArea, BorderLayout.CENTER);

        frame.setVisible(true);
    }
}
