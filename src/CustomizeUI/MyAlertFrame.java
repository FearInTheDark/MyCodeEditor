package CustomizeUI;

import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXLabel;

import javax.swing.*;
import java.awt.*;

public class MyAlertFrame extends JXFrame {

    private JXLabel titleLabel, messageLabel;


    public MyAlertFrame(String title, String message) {
        super(title);
        setLayout(null);
        this.setSize(500, 400);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setVisible(true);
        initUI(title, message);
    }

    private void initUI(String title, String message) {
        JXLabel titleLabel = new JXLabel(title);
        titleLabel.setBounds(10, 10, 480, 50);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(titleLabel);

        JXLabel messageLabel = new JXLabel(message);
        messageLabel.setBounds(10, 70, 480, 200);
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(messageLabel);


        JButton okButton = new JButton("OK");
        okButton.setFocusable(false);
//        okButton.setBackground();
        okButton.setBounds(200, 300, 100, 50);
        okButton.addActionListener(e -> dispose());
        add(okButton);
    }
}
