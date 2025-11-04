package io.github.xavierb117.centroidfinder;

import java.io.FileWriter;
import java.io.PrintWriter;

public class VideoResultWriter implements AutoCloseable {
    private final PrintWriter writer;

    public VideoResultWriter(String path) throws Exception {
        writer = new PrintWriter(new FileWriter(path));
        writer.println("time_seconds,x,y");
    }

    public void write(int second, Coordinate centroid)
    {
        writer.printf("%d,%d,%d%n", second, centroid.x(), centroid.y());
    }

    @Override
    public void close()
    {
        writer.flush();
        writer.close();
    }

}
