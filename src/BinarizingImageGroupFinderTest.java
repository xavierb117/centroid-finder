import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

// Simple test for BinarizingImageGroupFinder
public class BinarizingImageGroupFinderTest {

    // Simple fake versions of the needed interfaces
    private static class FakeBinarizer implements ImageBinarizer {
        boolean called = false;

        @Override
        public int[][] toBinaryArray(BufferedImage image) {
            called = true;
            return new int[][]{
                    {1, 0},
                    {0, 1}
            };
        }
    }

    private static class FakeGroupFinder implements BinaryGroupFinder {
        boolean called = false;
        List<Group> result = new ArrayList<>();

        @Override
        public List<Group> findConnectedGroups(int[][] binaryImage) {
            called = true;
            // Just return one dummy group
            result.add(new Group(2, 2, 1.0, 1.0));
            return result;
        }
    }

    // Create a small test image
    private static BufferedImage makeSmallImage() {
        return new BufferedImage(3, 3, BufferedImage.TYPE_INT_RGB);
    }

    // ✅ Test 1: Basic check — should call both binarizer and groupFinder
    @Test
    void testFindConnectedGroups_CallsBothAndReturnsResult() {
        FakeBinarizer bin = new FakeBinarizer();
        FakeGroupFinder finderStub = new FakeGroupFinder();
        BinarizingImageGroupFinder finder = new BinarizingImageGroupFinder(bin, finderStub);

        List<Group> result = finder.findConnectedGroups(makeSmallImage());

        assertTrue(bin.called, "Binarizer should be called");
        assertTrue(finderStub.called, "GroupFinder should be called");
        assertNotNull(result, "Result should not be null");
        assertEquals(1, result.size(), "Should return one group");
    }

    // ✅ Test 2: Should return the same list from the groupFinder
    @Test
    void testReturnsSameListFromGroupFinder() {
        FakeBinarizer bin = new FakeBinarizer();
        FakeGroupFinder finderStub = new FakeGroupFinder();
        BinarizingImageGroupFinder finder = new BinarizingImageGroupFinder(bin, finderStub);

        List<Group> output = finder.findConnectedGroups(makeSmallImage());

        assertSame(finderStub.result, output, "Should return the exact same list object");
    }

    // ✅ Test 3: If binarizer throws, the method should also throw
    @Test
    void testThrowsIfBinarizerFails() {
        ImageBinarizer badBinarizer = image -> { throw new RuntimeException("binarizer failed"); };
        BinaryGroupFinder dummyFinder = binary -> new ArrayList<>();
        BinarizingImageGroupFinder finder = new BinarizingImageGroupFinder(badBinarizer, dummyFinder);

        assertThrows(RuntimeException.class, () -> finder.findConnectedGroups(makeSmallImage()),
                "Should throw if binarizer fails");
    }

    // ✅ Test 4: If groupFinder throws, the method should also throw
    @Test
    void testThrowsIfGroupFinderFails() {
        ImageBinarizer goodBinarizer = image -> new int[][]{{1}};
        BinaryGroupFinder badFinder = binary -> { throw new RuntimeException("groupFinder failed"); };
        BinarizingImageGroupFinder finder = new BinarizingImageGroupFinder(goodBinarizer, badFinder);

        assertThrows(RuntimeException.class, () -> finder.findConnectedGroups(makeSmallImage()),
                "Should throw if groupFinder fails");
    }

    // ✅ Test 5: Works even if image is empty
    @Test
    void testHandlesEmptyImageGracefully() {
        FakeBinarizer bin = new FakeBinarizer();
        FakeGroupFinder finderStub = new FakeGroupFinder();
        BinarizingImageGroupFinder finder = new BinarizingImageGroupFinder(bin, finderStub);

        BufferedImage emptyImg = new BufferedImage(0, 0, BufferedImage.TYPE_INT_RGB);
        List<Group> result = finder.findConnectedGroups(emptyImg);

        assertNotNull(result, "Should not return null for empty image");
    }
}
