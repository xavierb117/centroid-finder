package io.github.xavierb117.centroidfinder;

import java.util.List;
import java.awt.image.BufferedImage;

/**
 * An implementation of the FrameBinarizer interface. This saves the information given from FrameGrabber
 * to eventually set up the methods required for binarization. Will return a list of Group records.
 */
public class FramePasserToBinarizer implements FrameBinarizer {
    private BufferedImage bufferedImage;
    private int threshold;
    private int targetColor;

    /**
     * Constructs a FramePasserToBinarizer using a BufferedImage, an integer threshold, and a hex color
     * 
     * @param bufferedImage A BufferedImage of a frame from the chosen video. Will be used for binarization
     * @param threshold A tolerance level for choosing colors. A high tolerance can pick similar colors to targetColor, a low tolerance is strictly only targetColor
     * @param targetColor The chosen color that the user wants analyzed, any color in BufferedImage equal to this will be in white
     */
    public FramePasserToBinarizer(BufferedImage bufferedImage, int threshold, int targetColor) {
        this.bufferedImage = bufferedImage;
        this.threshold = threshold;
        this.targetColor = targetColor;
    }

    /**
     * A method given by the FrameBinarizer interface, which sets up the required methods to analyze the BufferedImage frame.
     * Each object created here plays a part in converting the frame to a black and white binarized image along with 
     * recording its centroid groups.
     * 
     * @return a list of Group records, which holds the centroid sizes, x and y coordinates. Will be used for the list of TimeCoordinate records.
     */
    @Override
    public List<Group> binarizerPasser() {
        DfsBinaryGroupFinder finder = new DfsBinaryGroupFinder();
        EuclideanColorDistance colorDistance = new EuclideanColorDistance();
        DistanceImageBinarizer binarizer = new DistanceImageBinarizer(colorDistance, targetColor, threshold);
        BinarizingImageGroupFinder groupsToReturn = new BinarizingImageGroupFinder(binarizer, finder);

        List<Group> groups = groupsToReturn.findConnectedGroups(bufferedImage);
        return groups;
    }
}
