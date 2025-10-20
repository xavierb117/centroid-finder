import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;
import java.util.List;

/**
 * Unit tests for BinarizingImageGroupFinder.
 * 
 * These tests verify that the class correctly combines the ImageBinarizer and
 * BinaryGroupFinder to find connected groups in a binarized image.
 * 
 * No mocks or fakes are used here yet — real instances are provided.
 */
public class BinarizingImageGroupFinderTest {

    private BinarizingImageGroupFinder imageGroupFinder;

    @BeforeEach
    public void setUp() {
        // Use real implementations (you can replace with mocks later)
        ColorDistanceFinder distanceFinder = new EuclideanColorDistance();
        ImageBinarizer binarizer = new DistanceImageBinarizer(distanceFinder, 0xFF0000, 100);
        BinaryGroupFinder groupFinder = new DfsBinaryGroupFinder();

        imageGroupFinder = new BinarizingImageGroupFinder(binarizer, groupFinder);
    }

    // ============================================================
    // 🧩 Basic Functionality Tests
    // ============================================================

    @Test
    public void findConnectedGroups_ShouldReturnSingleGroup_ForAllWhiteImage() {
        BufferedImage img = new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB);
        img.setRGB(0, 0, 0xFF0000); // same color as target (red)
        img.setRGB(1, 0, 0xFF0000);
        img.setRGB(0, 1, 0xFF0000);
        img.setRGB(1, 1, 0xFF0000);

        List<Group> groups = imageGroupFinder.findConnectedGroups(img);

        assertEquals(1, groups.size());
        assertEquals(4, groups.get(0).size());
        assertEquals(new Coordinate(0, 0).getClass(), groups.get(0).centroid().getClass());
    }

    @Test
    public void findConnectedGroups_ShouldReturnEmptyList_ForAllBlackImage() {
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
        BufferedImage img = new BufferedImage(3, 3, BufferedImage.TYPE_INT_RGB);

        // Two red pixels separated by blue (not connected)
        img.setRGB(0, 0, 0xFF0000);
        img.setRGB(2, 2, 0xFF0000);
        img.setRGB(1, 1, 0x0000FF);

        List<Group> groups = imageGroupFinder.findConnectedGroups(img);

        assertEquals(2, groups.size());
        assertTrue(groups.get(0).size() == 1 && groups.get(1).size() == 1);
    }

    // ============================================================
    // ⚙️ Integration / Consistency Tests
    // ============================================================

    @Test
    public void findConnectedGroups_ShouldBeConsistent_WithManualBinaryConversion() {
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

    // ============================================================
    // 🚫 Error Handling / Edge Cases
    // ============================================================

    @Test
    public void findConnectedGroups_ShouldThrowException_ForNullImage() {
        assertThrows(NullPointerException.class, () -> imageGroupFinder.findConnectedGroups(null));
    }

    @Test
    public void findConnectedGroups_ShouldHandleEmptyImage() {
        // BufferedImage can't have width=0, but we can simulate minimal (1x1) black pixel
        BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        img.setRGB(0, 0, 0x000000);

        List<Group> groups = imageGroupFinder.findConnectedGroups(img);

        assertTrue(groups.isEmpty());
    }
}
