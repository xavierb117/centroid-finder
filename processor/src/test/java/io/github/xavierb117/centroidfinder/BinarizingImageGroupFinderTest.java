package io.github.xavierb117.centroidfinder;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;
import java.util.List;

public class BinarizingImageGroupFinderTest {

    // Tests below here are FAKE TESTS

    static class FakeBinarizer implements ImageBinarizer {
        @Override
        public int[][] toBinaryArray(BufferedImage image) {
            // Always return a small 2x2 binary pattern
            return new int[][] {
                    {1, 0},
                    {0, 1}
            };
        }

        @Override
        public BufferedImage toBufferedImage(int[][] image) {
            throw new UnsupportedOperationException();
        }
    }

    @Test
    public void findConnectedGroups_UsesBinaryArrayFromBinarizer() {
        // Arrange
        ImageBinarizer fakeBinarizer = new FakeBinarizer();
        BinaryGroupFinder realGroupFinder = new DfsBinaryGroupFinder(); // or any valid implementation
        BinarizingImageGroupFinder finder = new BinarizingImageGroupFinder(fakeBinarizer, realGroupFinder);

        BufferedImage dummyImage = new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB);

        // Act
        List<Group> groups = finder.findConnectedGroups(dummyImage);

        // Assert
        assertNotNull(groups);
        assertFalse(groups.isEmpty());
        assertEquals(2, groups.size());
        assertEquals(1, groups.get(0).size());
        assertEquals(1, groups.get(1).size());
    }

    // Tests below here are MOCK TESTS

    static class MockGroupFinder implements BinaryGroupFinder {
        private boolean called = false;
        private int[][] receivedImage;
        private List<Group> resultToReturn;

        public MockGroupFinder(List<Group> resultToReturn) {
            this.resultToReturn = resultToReturn;
        }

        @Override
        public List<Group> findConnectedGroups(int[][] image) {
            called = true;
            receivedImage = image;
            return resultToReturn;
        }

        public boolean wasCalled() {
            return called;
        }

        public int[][] getReceivedImage() {
            return receivedImage;
        }
    }

    @Test
    public void findConnectedGroups_DelegatesToBinaryGroupFinder() {
        // Arrange
        ImageBinarizer stubBinarizer = new ImageBinarizer() {
            @Override
            public int[][] toBinaryArray(BufferedImage image) {
                return new int[][] { {1, 0}, {0, 1} };
            }

            @Override
            public BufferedImage toBufferedImage(int[][] image) {
                throw new UnsupportedOperationException();
            }
        };

        List<Group> expectedGroups = List.of(
                new Group(1, new Coordinate(0, 0)),
                new Group(1, new Coordinate(1, 1))
        );

        MockGroupFinder mockFinder = new MockGroupFinder(expectedGroups);
        BinarizingImageGroupFinder imageGroupFinder = new BinarizingImageGroupFinder(stubBinarizer, mockFinder);

        BufferedImage dummyImage = new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB);

        // Act
        List<Group> result = imageGroupFinder.findConnectedGroups(dummyImage);

        // Assert
        assertTrue(mockFinder.wasCalled());
        assertArrayEquals(new int[][]{{1, 0}, {0, 1}}, mockFinder.getReceivedImage());
        assertEquals(expectedGroups, result);
    }

    // Tests below here are REAL IMPLENTATION TESTS

    @Test
    public void findConnectedGroups_ShouldReturnSingleGroup_ForAllWhiteImage() {
        ColorDistanceFinder distanceFinder = new EuclideanColorDistance();
        ImageBinarizer binarizer = new DistanceImageBinarizer(distanceFinder, 0xFF0000, 100);
        BinaryGroupFinder groupFinder = new DfsBinaryGroupFinder();
        BinarizingImageGroupFinder imageGroupFinder = new BinarizingImageGroupFinder(binarizer, groupFinder);

        BufferedImage img = new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB);
        img.setRGB(0, 0, 0xFF0000); // same color as target (red)
        img.setRGB(1, 0, 0xFF0000);
        img.setRGB(0, 1, 0xFF0000);
        img.setRGB(1, 1, 0xFF0000);

        List<Group> groups = imageGroupFinder.findConnectedGroups(img);

        assertEquals(1, groups.size());
        assertEquals(4, groups.get(0).size());
        assertEquals(new Coordinate(0, 0), groups.get(0).centroid());
    }

    @Test
    public void findConnectedGroups_ShouldReturnEmptyList_ForAllBlackImage() {
        ColorDistanceFinder distanceFinder = new EuclideanColorDistance();
        ImageBinarizer binarizer = new DistanceImageBinarizer(distanceFinder, 0xFF0000, 100);
        BinaryGroupFinder groupFinder = new DfsBinaryGroupFinder();
        BinarizingImageGroupFinder imageGroupFinder = new BinarizingImageGroupFinder(binarizer, groupFinder);

        BufferedImage img = new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB);
        img.setRGB(0, 0, 0x0000FF);
        img.setRGB(1, 0, 0x0000FF);
        img.setRGB(0, 1, 0x0000FF);
        img.setRGB(1, 1, 0x0000FF);

        List<Group> groups = imageGroupFinder.findConnectedGroups(img);

        assertTrue(groups.isEmpty());
    }

    @Test
    public void findConnectedGroups_ShouldDetectMultipleSeparateGroups() {
        ColorDistanceFinder distanceFinder = new EuclideanColorDistance();
        ImageBinarizer binarizer = new DistanceImageBinarizer(distanceFinder, 0xFF0000, 100);
        BinaryGroupFinder groupFinder = new DfsBinaryGroupFinder();
        BinarizingImageGroupFinder imageGroupFinder = new BinarizingImageGroupFinder(binarizer, groupFinder);

        BufferedImage img = new BufferedImage(3, 3, BufferedImage.TYPE_INT_RGB);

        // Two red pixels separated by blue (not connected)
        img.setRGB(0, 0, 0xFF0000);
        img.setRGB(2, 2, 0xFF0000);
        img.setRGB(1, 1, 0x0000FF);

        List<Group> groups = imageGroupFinder.findConnectedGroups(img);

        assertEquals(2, groups.size());
        assertTrue(groups.get(0).size() == 1 && groups.get(1).size() == 1);
        assertEquals(new Coordinate(2, 2), groups.get(0).centroid());
        assertEquals(new Coordinate(0, 0), groups.get(1).centroid());
    }

    @Test
    public void findConnectedGroups_ShouldDetectTwoLargeSeparateGroups() {
        ColorDistanceFinder distanceFinder = new EuclideanColorDistance();
        ImageBinarizer binarizer = new DistanceImageBinarizer(distanceFinder, 0xFF0000, 100);
        BinaryGroupFinder groupFinder = new DfsBinaryGroupFinder();
        BinarizingImageGroupFinder imageGroupFinder = new BinarizingImageGroupFinder(binarizer, groupFinder);

        BufferedImage img = new BufferedImage(6, 2, BufferedImage.TYPE_INT_RGB);

        // Left 2×2 red group
        img.setRGB(0, 0, 0xFF0000);
        img.setRGB(1, 0, 0xFF0000);
        img.setRGB(0, 1, 0xFF0000);
        img.setRGB(1, 1, 0xFF0000);

        // Right 2×2 red group
        img.setRGB(4, 0, 0xFF0000);
        img.setRGB(5, 0, 0xFF0000);
        img.setRGB(4, 1, 0xFF0000);
        img.setRGB(5, 1, 0xFF0000);

        // Separation columns (black/blue)
        img.setRGB(2, 0, 0x0000FF);
        img.setRGB(3, 0, 0x0000FF);
        img.setRGB(2, 1, 0x0000FF);
        img.setRGB(3, 1, 0x0000FF);

        List<Group> groups = imageGroupFinder.findConnectedGroups(img);

        assertEquals(2, groups.size());
        assertEquals(4, groups.get(0).size());
        assertEquals(4, groups.get(1).size());
        assertEquals(new Coordinate(4, 0), groups.get(0).centroid());
        assertEquals(new Coordinate(0, 0), groups.get(1).centroid());
    }

    @Test
    public void findConnectedGroups_ShouldBeConsistent_WithManualBinaryConversion() {
        ColorDistanceFinder distanceFinder = new EuclideanColorDistance();
        ImageBinarizer binarizer = new DistanceImageBinarizer(distanceFinder, 0xFF0000, 100);
        BinaryGroupFinder groupFinder = new DfsBinaryGroupFinder();
        BinarizingImageGroupFinder imageGroupFinder = new BinarizingImageGroupFinder(binarizer, groupFinder);

        // Same idea: red = white pixel, blue = black pixel
        BufferedImage img = new BufferedImage(2, 1, BufferedImage.TYPE_INT_RGB);
        img.setRGB(0, 0, 0xFF0000);
        img.setRGB(1, 0, 0x0000FF);

        List<Group> groups = imageGroupFinder.findConnectedGroups(img);

        // The binarized version would be [1, 0] -> one single white pixel group
        assertEquals(1, groups.size());
        assertEquals(1, groups.get(0).size());
        assertEquals(0, groups.get(0).centroid().x());
        assertEquals(0, groups.get(0).centroid().y());
    }

    @Test
    public void findConnectedGroups_ShouldBeConsistent_WithVerticalConversion() {
        ColorDistanceFinder distanceFinder = new EuclideanColorDistance();
        ImageBinarizer binarizer = new DistanceImageBinarizer(distanceFinder, 0xFF0000, 100);
        BinaryGroupFinder groupFinder = new DfsBinaryGroupFinder();
        BinarizingImageGroupFinder imageGroupFinder = new BinarizingImageGroupFinder(binarizer, groupFinder);

        BufferedImage img = new BufferedImage(1, 2, BufferedImage.TYPE_INT_RGB);
        img.setRGB(0, 0, 0xFF0000);
        img.setRGB(0, 1, 0x0000FF);

        List<Group> groups = imageGroupFinder.findConnectedGroups(img);

        assertEquals(1, groups.size());
        assertEquals(1, groups.get(0).size());
        assertEquals(0, groups.get(0).centroid().x());
        assertEquals(0, groups.get(0).centroid().y());
    }

    @Test
    public void findConnectedGroups_ShouldThrowException_ForNullImage() {
        ColorDistanceFinder distanceFinder = new EuclideanColorDistance();
        ImageBinarizer binarizer = new DistanceImageBinarizer(distanceFinder, 0xFF0000, 100);
        BinaryGroupFinder groupFinder = new DfsBinaryGroupFinder();
        BinarizingImageGroupFinder imageGroupFinder = new BinarizingImageGroupFinder(binarizer, groupFinder);

        assertThrows(NullPointerException.class, () -> imageGroupFinder.findConnectedGroups(null));
    }
}
