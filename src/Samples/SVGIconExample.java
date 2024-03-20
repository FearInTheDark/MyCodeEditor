package Samples;

import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.apache.batik.transcoder.*;
import org.apache.batik.transcoder.image.ImageTranscoder;
import org.apache.batik.transcoder.image.PNGTranscoder;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.*;

public class SVGIconExample {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("SVG Icon Example");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(300, 200);

            // Load SVG file and convert it to BufferedImage
            BufferedImage svgImage = loadSVGImage("src/icons/frame.svg", 1000, 1000); // Change the dimensions as needed
            if (svgImage == null) {
                System.err.println("Failed to load SVG image");
                return;
            }

            // Create a JLabel and set the SVG image as its icon
            JLabel label = new JLabel(new ImageIcon(svgImage));
            frame.add(label);

            frame.setVisible(true);
        });
    }

    private static BufferedImage loadSVGImage(String filePath, int targetWidth, int targetHeight) {
        try {
            // Create a PNGTranscoder
            Transcoder transcoder = new PNGTranscoder();

            // Set the transcoding hints
            TranscodingHints hints = new TranscodingHints();
            hints.put(ImageTranscoder.KEY_WIDTH, (float) targetWidth);
            hints.put(ImageTranscoder.KEY_HEIGHT, (float) targetHeight);
            transcoder.setTranscodingHints(hints);

            hints.put(ImageTranscoder.KEY_DOM_IMPLEMENTATION, SVGDOMImplementation.getDOMImplementation());
            hints.put(PNGTranscoder.KEY_DOCUMENT_ELEMENT_NAMESPACE_URI, SVGDOMImplementation.SVG_NAMESPACE_URI);
            hints.put(PNGTranscoder.KEY_DOCUMENT_ELEMENT, "svg");
            hints.put(PNGTranscoder.KEY_XML_PARSER_VALIDATING, false);

            // Create a ByteArrayOutputStream to hold the PNG image
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            TranscoderOutput output = new TranscoderOutput(outputStream);

            // Create a TranscoderInput from the SVG file
            InputStream inputStream = new FileInputStream(new File(filePath)); // Use FileInputStream here
            TranscoderInput input = new TranscoderInput(inputStream);

            // Transcode the SVG to PNG
            transcoder.transcode(input, output);

            // Create a BufferedImage from the PNG data
            byte[] pngData = outputStream.toByteArray();
            ByteArrayInputStream pngInputStream = new ByteArrayInputStream(pngData);
            return ImageIO.read(pngInputStream);
        } catch (IOException | TranscoderException e) {
            e.printStackTrace();
        }
        return null;
    }
}

