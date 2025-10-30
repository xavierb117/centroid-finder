package io.github.xavierb117.centroidfinder;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

public class FrameGrabberTest {
    @Test
    void shouldReturnEmptyListWhenVideoFileMissing() {
        FrameGrabber grabber = new FrameGrabber(0xFF0000, 30, "nonexistent.mp4");
        List<TimeCoordinate> result = grabber.analysis();

        assertTrue(result.isEmpty(), "Should return an empty list for a missing video");
    }
}
