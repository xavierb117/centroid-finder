package io.github.xavierb117.centroidfinder;

import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class VideoResultWriterTest {
    @Test
    void shouldCreateFileWithHeader() throws Exception {
        Path tempFile = Files.createTempFile("videoresult-", ".csv");

        try (VideoResultWriter writer = new VideoResultWriter(tempFile.toString())) {
            // don't write any data yet
        }

        String content = Files.readString(tempFile);
        Assertions.assertTrue(content.startsWith("time_seconds,x,y"),
                "File should start with header line");
    }

    @Test
    void shouldWriteSingleLineAfterHeader() throws Exception {
        Path tempFile = Files.createTempFile("videoresult-", ".csv");
        Coordinate c = new Coordinate(100, 200);

        try (VideoResultWriter writer = new VideoResultWriter(tempFile.toString())) {
            writer.write(5, c);
        }

        var lines = Files.readAllLines(tempFile);

        Assertions.assertEquals(2, lines.size(), "Should have header + one data line");
        Assertions.assertEquals("5,100,200", lines.get(1));
    }

    @Test
    void shouldWriteMultipleLinesAfterHeader() throws Exception {
        Path tempFile = Files.createTempFile("videoresult-", ".csv");
        Coordinate c = new Coordinate(100, 200);

        try (VideoResultWriter writer = new VideoResultWriter(tempFile.toString())) {
            writer.write(5, c);
            writer.write(5, c);
            writer.write(5, c);
            writer.write(5, c);
            writer.write(5, c);
        }

        var lines = Files.readAllLines(tempFile);

        Assertions.assertEquals(6, lines.size(), "Should have header + five data line");
    }

}
