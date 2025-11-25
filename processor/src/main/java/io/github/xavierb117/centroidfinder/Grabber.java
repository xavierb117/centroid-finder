package io.github.xavierb117.centroidfinder;
import java.util.List;

/**
 * Interface used for analying a video frame by frame, ultimately returning a list of TimeCoordinate Records.
 */

public interface Grabber {
    /**
     * Method used for analyzing a video frame by frame. Grabs each frame and converts them to a BufferedImage before
     * sending them over for binarization. Results returned will be saved.
     * 
     * @return a list of TimeCoordinate records that record the x and y coordinate derived from a list of Groups
     *  over each second of the video.
     */
    public List<TimeCoordinate> analysis();
}
