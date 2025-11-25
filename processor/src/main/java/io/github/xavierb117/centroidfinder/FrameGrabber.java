package io.github.xavierb117.centroidfinder;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import java.util.ArrayList;
import java.util.List;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * An implementation of the Grabber interface. It uses a targetColor, threshold, and input path to grab
 * video frames for analysis.
 * 
 * Utilizes JavaCV's FFmpegFrameGrabber to grab a frame from the inputPath, which should be a video. 
 * Methods granted by JavaCV allow for grabbing frames over each second of the video.
 * 
 * The frame gets converted to a BufferedImage for binarization, which is influenced by the targetColor and threshold.
 * The list of groups returned will be used for grabbing the greatest centroid and adding it to the movements list for CSV.
 */

public class FrameGrabber implements Grabber {
    private int targetColor;
    private int threshold;
    private String inputPath;

    /**
     * Constructs a FrameGrabber using targetColor, threshold, and inputPath. 
     * 
     * @param targetColor A hex color used to determine what color the user wants analyzed (0xRRGGBB)
     * @param threshold A tolerance integer for better analyzing specific colors. High tolerance can analyze more colors that resemble targetColor. Low tolerance won't.
     * @param inputPath An input path for the video to analyze. 
     */

    public FrameGrabber(int targetColor, int threshold, String inputPath) {
        this.targetColor = targetColor;
        this.threshold = threshold;
        this.inputPath = inputPath;
    }

    /**
     * Method given by the Grabber interface to analyze the video. It saves a list of TimeCoordinate records that
     * save the specific second, x coordinate and y coordinate of centroid.
     * 
     * A frame gets extracted by JavaCV FFmpegFrameGrabber. A loop is set to grab a frame every second of the video.
     * Each frame will be converted to a BufferedImage for analysis, where it will be binarized and recorded in a list of Groups.
     * The greatest group will be extracted and saved to TimeCoordinate, if there is nothing then -1, -1 is saved.
     * 
     * @return a list of TimeCoordinate records that saves the x and y coordinate over each second of the video
     */

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
            System.out.println("Ffmpeg failed to analyze the video");
        }

        return movements;
    }
}
