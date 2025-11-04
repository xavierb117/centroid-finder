// SATISFIED WITH USING JAVACV

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
        String inputPath = "sampleInput/ensantina.mp4";
        String outputPath = "sampleOutput";

        FFmpegFrameGrabber grab = new FFmpegFrameGrabber(inputPath);
        Java2DFrameConverter convert = new Java2DFrameConverter();

        try {
            grab.start();

            int frameToGrab = 10000;
            grab.setFrameNumber(frameToGrab);
            Frame frame = grab.grabImage();

            if (frame != null) {
                BufferedImage bufferedImage = convert.convert(frame);
                String fileName = "testImage1.jpg";
                File file = new File(outputPath, fileName);
                ImageIO.write(bufferedImage, "jpg", file);
            }
            else {
                System.out.println("Couldn't grab the frame");
            }

            convert.close();
            grab.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
