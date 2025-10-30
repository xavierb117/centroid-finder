package io.github.xavierb117.centroidfinder;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

    @Test
    void shouldRecordMinusOneCoordinatesWhenNoBlobsFound() {
        FrameGrabber grabber = new FrameGrabber(0xFF0000, 30, "fake.mp4") {
            @Override
            public List<TimeCoordinate> analysis() {
                return List.of(new TimeCoordinate(0, new Coordinate(-1, -1)));
            }
        };

        List<TimeCoordinate> result = grabber.analysis();
        assertEquals(1, result.size());
        assertEquals(-1, result.get(0).centroid().x());
        assertEquals(-1, result.get(0).centroid().y());
    }

    @Test
    void RecordsOneElementCorrectly() {
        FrameGrabber grabber = new FrameGrabber(0xFF0000, 30, "fake.mp4") {
            @Override
            public List<TimeCoordinate> analysis() {
                return List.of(new TimeCoordinate(0, new Coordinate(200, 150)));
            }
        };

        List<TimeCoordinate> result = grabber.analysis();
        assertEquals(1, result.size());
        assertEquals(200, result.get(0).centroid().x());
        assertEquals(150, result.get(0).centroid().y());
    }

    @Test
    void RecordsOneElementPerSecond() {
        FrameGrabber grabber = new FrameGrabber(0xFF0000, 30, "fake.mp4") {
            @Override
            public List<TimeCoordinate> analysis() {
                return List.of(
                    new TimeCoordinate(0, new Coordinate(200, 150)),
                    new TimeCoordinate(1, new Coordinate(200, 150)),
                    new TimeCoordinate(2, new Coordinate(200, 150))
                
                );
            }
        };

        List<TimeCoordinate> result = grabber.analysis();
        assertEquals(3, result.size());
        
    }

}
