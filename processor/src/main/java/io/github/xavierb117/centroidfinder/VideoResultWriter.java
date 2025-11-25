package io.github.xavierb117.centroidfinder;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class VideoResultWriter implements AutoCloseable, VideoWriter {
    private final PrintWriter writer;

    public VideoResultWriter(String path) throws IOException {
        writer = new PrintWriter(new FileWriter(path));
        writer.println("time_seconds,x,y");
    }

    @Override
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
