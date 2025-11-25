package io.github.xavierb117.centroidfinder;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * An implementation of the AutoCloseable and VideWriter interface used for writing a CSV output file. 
 * Will take in a path to write to, then it will write down the second and coordinates provided by the TimeCoordinate list.
 * Requires a closing method to close the writer.
 */
public class VideoResultWriter implements AutoCloseable, VideoWriter {
    private final PrintWriter writer;

    /**
     * Constructs a VideoResultWriter that will take in the output path to write to.
     * Will write "time_seconds,x,y" as the first thing in the output file.
     * Throws an exception if file reading/writing goes wrong.
     * 
     * @param path A string representing the output path to write to
     * @throws IOException Throws Exception if file reading/writing goes wrong based on any wrong input
     */
    public VideoResultWriter(String path) throws IOException {
        writer = new PrintWriter(new FileWriter(path));
        writer.println("time_seconds,x,y");
    }

    /**
     * A method provided by the VideoWriter interface, which will take in the second and Coordinates to write to the output path. 
     * 
     * @param second An integer representing the second of the video.
     * @param centroid A Coordinate record representing the centroid through X and Y.
     */
    @Override
    public void write(int second, Coordinate centroid)
    {
        writer.printf("%d,%d,%d%n", second, centroid.x(), centroid.y());
    }

    /**
     * Method provided by the AutoCloseable interface, which helps close the PrintWriter.
     */
    @Override
    public void close()
    {
        writer.flush();
        writer.close();
    }

}
