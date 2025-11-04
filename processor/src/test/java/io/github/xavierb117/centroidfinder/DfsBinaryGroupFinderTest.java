package io.github.xavierb117.centroidfinder;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

public class DfsBinaryGroupFinderTest {
    @Test
    public void testSinglePixelGroup() {
        int[][] image = {
                {0, 0, 0},
                {0, 1, 0},
                {0, 0, 0}
        };

        DfsBinaryGroupFinder finder = new DfsBinaryGroupFinder();
        List<Group> groups = finder.findConnectedGroups(image);

        assertEquals(1, groups.size());
        Group g = groups.get(0);
        assertEquals(1, g.size());
        assertEquals(new Coordinate(1, 1), g.centroid());
    }

    @Test
    public void testTwoSeparatedGroups() {
        int[][] image = {
                {1, 0, 0},
                {0, 0, 0},
                {0, 0, 1}
        };

        DfsBinaryGroupFinder finder = new DfsBinaryGroupFinder();
        List<Group> groups = finder.findConnectedGroups(image);

        assertEquals(2, groups.size());
        assertTrue(groups.stream().allMatch(g -> g.size() == 1));
        assertTrue(groups.contains(new Group(1, new Coordinate(0, 0))));
        assertTrue(groups.contains(new Group(1, new Coordinate(2, 2))));
    }

    @Test
    public void testAllZerosImage() {
        int[][] image = {
            {0, 0, 0},
            {0, 0, 0},
            {0, 0, 0}
        };

        DfsBinaryGroupFinder finder = new DfsBinaryGroupFinder();
        List<Group> groups = finder.findConnectedGroups(image);

        assertNotNull(groups);
        assertTrue(groups.isEmpty());
    }

    @Test
    public void testAllOnesImage() {
        int[][] image = {
            {1, 1, 1},
            {1, 1, 1},
            {1, 1, 1}
        };

        DfsBinaryGroupFinder finder = new DfsBinaryGroupFinder();
        List<Group> groups = finder.findConnectedGroups(image);

        assertEquals(1, groups.size());

        Group g = groups.get(0);
        int expectedSize = 9; // 3x3 image → 9 pixels total

        // Compute expected centroid manually:
        // x: (0+1+2 + 0+1+2 + 0+1+2)/9 = 9/9 = 1
        // y: (0+0+0 + 1+1+1 + 2+2+2)/9 = 9/9 = 1
        Coordinate expectedCentroid = new Coordinate(1, 1);

        assertEquals(expectedSize, g.size());
        assertEquals(expectedCentroid, g.centroid());
    }

    @Test
    public void testMultipleGroupsSortedDescending() {
        int[][] image = {
                {1, 1, 0, 1},
                {1, 0, 0, 0},
                {0, 0, 1, 1}
        };

        DfsBinaryGroupFinder finder = new DfsBinaryGroupFinder();
        List<Group> groups = finder.findConnectedGroups(image);

        assertEquals(3, groups.size());

        // Check descending order by size
        assertTrue(groups.get(0).size() >= groups.get(1).size());
        assertTrue(groups.get(1).size() >= groups.get(2).size());

         // Verify sizes explicitly
        assertEquals(3, groups.get(0).size());
        assertEquals(2, groups.get(1).size());
        assertEquals(1, groups.get(2).size());

        // Verify centroids
        assertEquals(new Coordinate(0, 0), groups.get(0).centroid());
        assertEquals(new Coordinate(2, 2), groups.get(1).centroid());
        assertEquals(new Coordinate(3, 0), groups.get(2).centroid());
    }

    @Test
    public void testTieBreakingByX() {
        int[][] image = {
            {1, 0, 1},  // groups at (0,0) and (2,0)
        };

        DfsBinaryGroupFinder finder = new DfsBinaryGroupFinder();
        List<Group> groups = finder.findConnectedGroups(image);

        assertEquals(2, groups.size());

        // Both groups have size 1
        assertEquals(1, groups.get(0).size());
        assertEquals(1, groups.get(1).size());

        // Expected centroids: (2,0) first, (0,0) second (descending x)
        assertEquals(new Coordinate(2, 0), groups.get(0).centroid());
        assertEquals(new Coordinate(0, 0), groups.get(1).centroid());
    }

    @Test
    public void testTieBreakingByY() {
        int[][] image = {
            {0, 1, 0},  // (x=1, y=0)
            {0, 0, 0},
            {0, 1, 0}   // (x=1, y=2)
        };

        DfsBinaryGroupFinder finder = new DfsBinaryGroupFinder();
        List<Group> groups = finder.findConnectedGroups(image);

        assertEquals(2, groups.size());

        // Both groups have size 1 and same x
        assertEquals(1, groups.get(0).size());
        assertEquals(1, groups.get(1).size());

        // Expected centroids: (1,2) first, (1,0) second (descending y)
        assertEquals(new Coordinate(1, 2), groups.get(0).centroid());
        assertEquals(new Coordinate(1, 0), groups.get(1).centroid());
    }

    @Test
    public void testNullImageThrowsException() {
        DfsBinaryGroupFinder finder = new DfsBinaryGroupFinder();
        assertThrows(NullPointerException.class, () -> finder.findConnectedGroups(null));
    }

    @Test
    public void testNullSubarrayThrowsException() {
        int[][] image = {
                null,
                new int[]{1, 0}
        };

        DfsBinaryGroupFinder finder = new DfsBinaryGroupFinder();
        assertThrows(NullPointerException.class, () -> finder.findConnectedGroups(image));
    }

    @Test
    public void testNonRectangularArrayThrowsException() {
        int[][] image = {
                {1, 0, 1},
                {0, 1}
        };

        DfsBinaryGroupFinder finder = new DfsBinaryGroupFinder();
        assertThrows(IllegalArgumentException.class, () -> finder.findConnectedGroups(image));
    }

    @Test
    public void testInvalidValuesThrowsException() {
        int[][] image = {
                {0, 2},
                {1, -1}
        };

        DfsBinaryGroupFinder finder = new DfsBinaryGroupFinder();
        assertThrows(IllegalArgumentException.class, () -> finder.findConnectedGroups(image));
    }

    @Test
    public void testEmptyArrayThrowsException() {
        int[][] image = new int[0][0];
        DfsBinaryGroupFinder finder = new DfsBinaryGroupFinder();
        assertThrows(IllegalArgumentException.class, () -> finder.findConnectedGroups(image));
    }
}
