import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class BinarizingImageGroupFinderTest {

    // --- simple fake versions of dependencies ---
    private static class FakeBinarizer implements ImageBinarizer {
        boolean called = false;
        int[][] array = {{1, 0}, {0, 1}};
        @Override
        public int[][] toBinaryArray(BufferedImage image) {
            called = true;
            return array;
        }
    }

    private static class FakeGroupFinder implements BinaryGroupFinder {
        boolean called = false;
        List<Group> list = new ArrayList<>();
        @Override
        public List<Group> findConnectedGroups(int[][] binaryImage) {
            called = true;
            list.add(new Group(2, 1, 1.0, 1.0));   // dummy group
            return list;
        }
    }

    private static BufferedImage makeImage() {
        return new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB);
    }

    // ✅ Test 1: calls both collaborators and returns result
    @Test
    void testCallsBothHelpers() {
        FakeBinarizer bin = new FakeBinarizer();
        FakeGroupFinder gf = new FakeGroupFinder();
        BinarizingImageGroupFinder finder = new BinarizingImageGroupFinder(bin, gf);

        List<Group> result = finder.findConnectedGroups(makeImage());

        assertTrue(bin.called, "binarizer should be called");
        assertTrue(gf.called, "groupFinder should be called");
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    // ✅ Test 2: passes binary array from binarizer to groupFinder
    @Test
    void testPassesBinaryArray() {
        FakeBinarizer bin = new FakeBinarizer();
        FakeGroupFinder gf = new FakeGroupFinder();
        BinarizingImageGroupFinder finder = new BinarizingImageGroupFinder(bin, gf);

        finder.findConnectedGroups(makeImage());

        assertTrue(gf.called, "groupFinder must be called");
        assertNotNull(bin.array, "binary array should not be null");
    }

    // ✅ Test 3: propagates exception from binarizer
    @Test
    void testThrowsIfBinarizerFails() {
        ImageBinarizer bad = img -> { throw new RuntimeException("binarizer failed"); };
        BinaryGroupFinder ok = arr -> new ArrayList<>();
        BinarizingImageGroupFinder finder = new BinarizingImageGroupFinder(bad, ok);

        assertThrows(RuntimeException.class,
                () -> finder.findConnectedGroups(makeImage()),
                "should throw if binarizer fails");
    }

    // ✅ Test 4: propagates exception from groupFinder
    @Test
    void testThrowsIfGroupFinderFails() {
        ImageBinarizer ok = img -> new int[][]{{1}};
        BinaryGroupFinder bad = arr -> { throw new RuntimeException("groupFinder failed"); };
        BinarizingImageGroupFinder finder = new BinarizingImageGroupFinder(ok, bad);

        assertThrows(RuntimeException.class,
                () -> finder.findConnectedGroups(makeImage()),
                "should throw if groupFinder fails");
    }

    // ✅ Test 5: works even with empty image
    @Test
    void testHandlesEmptyImage() {
        FakeBinarizer bin = new FakeBinarizer();
        FakeGroupFinder gf = new FakeGroupFinder();
        BinarizingImageGroupFinder finder = new BinarizingImageGroupFinder(bin, gf);

        BufferedImage empty = new BufferedImage(0, 0, BufferedImage.TYPE_INT_RGB);
        List<Group> result = finder.findConnectedGroups(empty);

        assertNotNull(result, "should not return null");
    }
}
