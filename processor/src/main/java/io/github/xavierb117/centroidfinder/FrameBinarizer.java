package io.github.xavierb117.centroidfinder;
import java.util.List;

/**
 * An interface for receiving frames, colors and thresholds to help prepare the binarization of said frames. Will then be analyzed
 * and return a list of Group records for recording to the CSV. 
 */
public interface FrameBinarizer {
    /**
     * Method used to prepare for frame binarization using the image, color and threshold values provided by FrameGrabber.
     * Executes the analysis for recording centroid coordinates.
     * @return a list of Group records, which holds centroid size, x and y coordinates. Ordered in descending order. Will be used for the TimeCoordinate list in FrameGrabber
     */
    public List<Group> binarizerPasser();
}
