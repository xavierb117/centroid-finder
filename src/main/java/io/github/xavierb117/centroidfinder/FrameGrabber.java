package io.github.xavierb117.centroidfinder;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import java.util.ArrayList;
import java.util.List;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class FrameGrabber {
    private int targetColor;
    private int threshold;
    private String inputPath;

    public FrameGrabber(int targetColor, int threshold, String inputPath) {
        this.targetColor = targetColor;
        this.threshold = threshold;
        this.inputPath = inputPath;
    }

    public List<TimeCoordinate> analysis() {
        List<TimeCoordinate> movements = new ArrayList<>();

        FFmpegFrameGrabber grab = new FFmpegFrameGrabber(inputPath);
        Java2DFrameConverter convert = new Java2DFrameConverter();
        try {
            grab.start();

            double fps = grab.getFrameRate();
            double durationSec = grab.getLengthInTime() / 1_000_000.0;

            for (int sec = 0; sec < durationSec; sec++) {
                int frameNumber = (int) (sec * fps);
                grab.setFrameNumber(frameNumber);
                Frame frame = grab.grabImage();

                if (frame != null) {
                    BufferedImage bufferedImage = convert.convert(frame);
                }
                else {
                    System.out.println("Couldn't grab the frame.");
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return movements;
    }
}
