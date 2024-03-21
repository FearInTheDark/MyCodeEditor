package Converters;

import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.TranscodingHints;
import org.apache.batik.transcoder.image.ImageTranscoder;
import org.apache.batik.transcoder.image.PNGTranscoder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class SVGToIconConverter {

    public static void convertSvgToIcon(String svgFilePath, String outputIconPath, int width, int height) {
        try {
            PNGTranscoder transcoder = new PNGTranscoder();
            TranscodingHints hints = new TranscodingHints();
            hints.put(PNGTranscoder.KEY_WIDTH, (float) width);
            hints.put(PNGTranscoder.KEY_HEIGHT, (float) height);
            transcoder.setTranscodingHints(hints);

            hints.put(ImageTranscoder.KEY_DOM_IMPLEMENTATION, SVGDOMImplementation.getDOMImplementation());
            hints.put(PNGTranscoder.KEY_DOCUMENT_ELEMENT_NAMESPACE_URI, SVGDOMImplementation.SVG_NAMESPACE_URI);
            hints.put(PNGTranscoder.KEY_DOCUMENT_ELEMENT, "svg");
            hints.put(PNGTranscoder.KEY_XML_PARSER_VALIDATING, false);


            TranscoderInput input = new TranscoderInput(new File(svgFilePath).toURI().toString());
            OutputStream outputStream = new FileOutputStream(outputIconPath);
            TranscoderOutput output = new TranscoderOutput(outputStream);
            transcoder.transcode(input, output);
            outputStream.close();

            System.out.println("Biểu tượng đã được tạo thành công.");
        } catch (Exception e) {
            System.err.println("Lỗi khi chuyển đổi SVG thành biểu tượng: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
//        String svgFilePath = "src/icons/icons_svg/java.svg";
//        String outputIconPath = "src/icons/java.png";
//        int width = 100;
//        int height = 100;
//        convertSvgToIcon(svgFilePath, outputIconPath, width, height);
//        File file = new File("src/icons/frame.svg");
//        System.out.println(file.exists() ? "File exists" : "File not found");


        // Convert all svg files in icons_svg folder to folder fileIcons with size = 20x20 and folder path = D:\Java Learning\Samples\FileExplorer\src\icons\icons_svg

        File file = new File("src/icons/icons_svg");
        File[] files = file.listFiles();
        assert files != null;
        for (File f : files) {
            String fileName = f.getName();
            String outputIconPath = "src/icons/fileIcons/" + fileName.substring(0, fileName.lastIndexOf(".")) + ".png";
            convertSvgToIcon(f.getAbsolutePath(), outputIconPath, 100, 100);
        }

        System.out.println("All icons have been created successfully.");
    }
}

