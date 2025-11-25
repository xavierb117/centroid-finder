package io.github.xavierb117.centroidfinder;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import java.util.ArrayList;
import java.util.List;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class FrameGrabber implements Grabber {
    private int targetColor;
    private int threshold;
    private String inputPath;

    public FrameGrabber(int targetColor, int threshold, String inputPath) {
        this.targetColor = targetColor;
        this.threshold = threshold;
        this.inputPath = inputPath;
    }

    @Override
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
                    FramePasserToBinarizer getGroups = new FramePasserToBinarizer(bufferedImage, threshold, targetColor);
                    List<Group> listOfGroups = getGroups.binarizerPasser();
                    if (listOfGroups.isEmpty()) {
                        movements.add(new TimeCoordinate(sec, new Coordinate(-1, -1)));
                    }
                    else {
                        Group largestCentroid = listOfGroups.get(0);
                        movements.add(new TimeCoordinate(sec, largestCentroid.centroid()));
                    }
                }
                else {
                    System.out.println("Couldn't grab the frame.");
                }
            }
            grab.close();
            convert.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return movements;
    }
}
