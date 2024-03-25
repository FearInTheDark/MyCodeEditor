package Converters;

import com.google.gson.Gson;
import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXLabel;

import javax.swing.*;
import java.awt.*;
import java.io.FileReader;


public class GifToIcon extends JXFrame {
    private JXLabel gifLabel;

    public GifToIcon() {
        setTitle("GIF Display");
        setDefaultCloseOperation(JXFrame.EXIT_ON_CLOSE);
        setSize(400, 400);
        setLayout(null);

        // Initialize components
        gifLabel = new JXLabel("This is a GIF Display");
        gifLabel.setBounds(0, 0, 400, 400);
        gifLabel.setBackground(Color.cyan);
        add(gifLabel, BorderLayout.CENTER);

        // Load configuration from JSON
        try {
            Gson gson = new Gson();
            FileReader reader = new FileReader("D:\\Java Learning\\Samples\\FileExplorer\\src\\icons\\JMenuItem_Icons\\new.json"); // Adjust path as needed
            GifConfig config = gson.fromJson(reader, GifConfig.class);
            reader.close();

            // Load and display GIF
            ImageIcon gifIcon = new ImageIcon(config.gifPath);
            gifLabel.setIcon(gifIcon);
            repaint();
        } catch (Exception ex) {
            System.out.println("Error loading GIF: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GifToIcon gifDisplay = new GifToIcon();
            gifDisplay.setVisible(true);
        });
    }
}

class GifConfig {
    String gifPath;

    public GifConfig(String gifPath) {
        this.gifPath = gifPath;
    }
}
