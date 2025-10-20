import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class BinarizingImageGroupFinderTest {

    // Helper stub for ImageBinarizer
    private static class StubBinarizer implements ImageBinarizer {
        int calls = 0;
        int[][] toReturn;
        @Override
        public int[][] toBinaryArray(BufferedImage image) {
            calls++;
            return toReturn;
        }
    }

    // Helper stub for BinaryGroupFinder
    private static class StubGroupFinder implements BinaryGroupFinder {
        int calls = 0;
        int[][] lastInput;
        List<Group> toReturn = new ArrayList<>();
        @Override
        public List<Group> findConnectedGroups(int[][] binaryImage) {
            calls++;
            lastInput = binaryImage;
            return toReturn;
        }
    }

    private static BufferedImage smallImage() {
        return new BufferedImage(3, 3, BufferedImage.TYPE_INT_RGB);
    }

    // --- Test 1: Calls both collaborators once and returns the same list ---
    @Test
    void callsBinarizerAndGroupFinderOnce() {
        StubBinarizer bin = new StubBinarizer();
        StubGroupFinder gf = new StubGroupFinder();
        bin.toReturn = new int[][]{{1, 0}, {0, 1}};
        gf.toReturn.add(new Group(1, 1, 1.0, 1.0)); // dummy group

        BinarizingImageGroupFinder finder = new BinarizingImageGroupFinder(bin, gf);
        List<Group> result = finder.findConnectedGroups(smallImage());

        assertEquals(1, bin.calls, "binarizer should be called once");
        assertEquals(1, gf.calls, "groupFinder should be called once");
        assertSame(gf.toReturn, result, "should return the same list from groupFinder");
    }

    // --- Test 2: Passes binary array from binarizer to groupFinder ---
    @Test
    void passesBinaryArrayFromBinarizerToGroupFinder() {
        StubBinarizer bin = new StubBinarizer();
        StubGroupFinder gf = new StubGroupFinder();
        int[][] expectedArray = {{1, 1}, {0, 0}};
        bin.toReturn = expectedArray;

        BinarizingImageGroupFinder finder = new BinarizingImageGroupFinder(bin, gf);
        finder.findConnectedGroups(smallImage());

        assertSame(expectedArray, gf.lastInput, "should pass binary array from binarizer to groupFinder");
    }

    // --- Test 3: Propagates exception from binarizer ---
    @Test
    void propagatesExceptionFromBinarizer() {
        ImageBinarizer badBin = image -> { throw new IllegalStateException("binarizer error"); };
        BinaryGroupFinder gf = binary -> new ArrayList<>();

        BinarizingImageGroupFinder finder = new BinarizingImageGroupFinder(badBin, gf);

        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> finder.findConnectedGroups(smallImage()));
        assertEquals("binarizer error", ex.getMessage());
    }

    // --- Test 4: Propagates exception from groupFinder ---
    @Test
    void propagatesExceptionFromGroupFinder() {
        ImageBinarizer bin = image -> new int[][]{{1}};
        BinaryGroupFinder badFinder = binary -> { throw new IllegalArgumentException("groupFinder error"); };

        BinarizingImageGroupFinder finder = new BinarizingImageGroupFinder(bin, badFinder);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> finder.findConnectedGroups(smallImage()));
        assertEquals("groupFinder error", ex.getMessage());
    }

    // --- Test 5: Returns empty list safely ---
    @Test
    void returnsEmptyListSafely() {
        StubBinarizer bin = new StubBinarizer();
        StubGroupFinder gf = new StubGroupFinder();
        bin.toReturn = new int[][]{{0}};
        gf.toReturn = new ArrayList<>();

        BinarizingImageGroupFinder finder = new BinarizingImageGroupFinder(bin, gf);
        List<Group> result = finder.findConnectedGroups(smallImage());

        assertNotNull(result, "result should never be null");
        assertTrue(result.isEmpty(), "result should be empty when no groups found");
    }
}
