package Samples;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CommandExecutor {
    public static void main(String[] args) {
        JTextArea textArea = new JTextArea();
        textArea.setBackground(Color.BLACK);
        textArea.setForeground(Color.WHITE);
        textArea.setFont(new Font("JetBrains Mono", Font.PLAIN, 14));
        textArea.setCaretColor(Color.WHITE);
        textArea.setMargin(new Insets(10, 10, 10, 10));

        textArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    if (textArea.getText().split("\n").length > 1) {
                        textArea.setText("");
                        return;
                    }
                    String command = textArea.getText().split("\n")[0]; // get the first line
                    System.out.println(command);
                    textArea.append("\n");

                    try {
                        Process process = Runtime.getRuntime().exec("cmd /c " + command);
                        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                        String line;
                        while ((line = reader.readLine()) != null) {
                            textArea.append(line + "\n");
                        }
                    } catch (IOException ex) {
                        System.out.println("Error executing command: " + ex.getMessage());
                    }
                }
            }
        });

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new JScrollPane(textArea));
        frame.setSize(500, 500);
        frame.setVisible(true);
    }
}