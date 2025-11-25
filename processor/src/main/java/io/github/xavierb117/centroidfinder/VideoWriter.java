package io.github.xavierb117.centroidfinder;

/**
 * An interface designed to help write output to a given output path for a file.
 * The output it will write down is the second and coordinates of each frame.
 */
public interface VideoWriter {
    /**
     * Helps record information down the the output path. Values provided by the list of TimeCoordinate records.
     * 
     * @param second an int of the exact second of a frame
     * @param centroid a coordinate record representing the X and Y locations of centroid
     */
    public void write(int second, Coordinate centroid);
}
