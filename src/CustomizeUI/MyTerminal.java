package CustomizeUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MyTerminal {

    private JTextArea terminal;
    private JScrollPane scrollPane;
    private ProcessBuilder processBuilder;
    private Process process;
    private String DEFAULT_PATH;

    public MyTerminal() {
        super();
        initUI();
    }

    private void initUI() {
        terminal = new JTextArea();
        terminal.setFont(new java.awt.Font("JetBrains Mono", Font.PLAIN, 20));
        terminal.setText(DEFAULT_PATH + ">");
        terminal.setMargin(new Insets(10, 10, 10, 10));
        terminal.setCaretColor(new java.awt.Color(0, 255, 0));
        terminal.setForeground(new java.awt.Color(0, 255, 0));
        terminal.setBackground(new java.awt.Color(0, 0, 0));
        terminal.setLineWrap(true);
        terminal.setWrapStyleWord(true);
        terminal.setAutoscrolls(true);
        addEvents(terminal);

        scrollPane = new JScrollPane(terminal);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUI(new MyScrollBarUI());
        scrollPane.getHorizontalScrollBar().setUI(new MyScrollBarUI());
        scrollPane.getVerticalScrollBar().setPreferredSize(new java.awt.Dimension(5, 0));
        scrollPane.getHorizontalScrollBar().setPreferredSize(new java.awt.Dimension(0, 5));
    }

    private void addEvents(JTextArea terminal) {
        terminal.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent evt) {
                if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
//                    String command = terminal.getText().substring(DEFAULT_PATH.length() + 1);
                    String command = terminal.getText().substring(terminal.getText().lastIndexOf(">") + 1);
                    System.out.println(command);
                    try {
                        process = Runtime.getRuntime().exec("cmd /c " + command);
                        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                        String line;
                        while ((line = reader.readLine()) != null) {
                            terminal.append("\n" + line);
                        }
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                    }
                    terminal.append("\n" + DEFAULT_PATH + ">");
                }
            }
        });
    }

    public void setDEFAULT_PATH(String path) {
        this.DEFAULT_PATH = path;
        terminal.setText(DEFAULT_PATH + ">");
    }

    public JScrollPane getTerminal() {
        return scrollPane;
    }

    public JScrollPane getScrollPane() {
        return scrollPane;
    }
}
