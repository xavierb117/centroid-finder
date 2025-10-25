package io.github.xavierb117.centroidfinder;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class JavaVideoTesting {
    public static void main(String[] args) {
        String inputPath = "C:\\Users\\izeco\\OneDrive\\Desktop\\SDEV 334\\centroid-finder\\centroid-finder\\sampleInput\\ensantina.mp4";
        String outputPath = "C:\\Users\\izeco\\OneDrive\\Desktop\\SDEV 334\\centroid-finder\\centroid-finder\\sampleOutput";

        FFmpegFrameGrabber grab = new FFmpegFrameGrabber(inputPath);
        try {
            grab.start();

            int frameToGrab = 0;
            grab.setFrameNumber(frameToGrab);
            Frame frame = grab.grabImage();
            grab.close();

            if (frame != null) {
                Java2DFrameConverter convert = new Java2DFrameConverter();
                BufferedImage bufferedImage = convert.convert(frame);
                convert.close();

                ImageIO.write(bufferedImage, "jpg", new File(outputPath));
            }
            else {
                System.out.println("Couldn't grab the frame");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
