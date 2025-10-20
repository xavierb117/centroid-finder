import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class BinarizingImageGroupFinderTest {

    @Test
    public void testFindConnectedGroupsNotNull() {
        ImageGroupFinder finder = new ImageGroupFinder();
        BufferedImage img = new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB);

        List<Group> result = finder.findConnectedGroups(img);
        assertNotNull(result, "Result should not be null");
    }

    @Test
    public void testFindConnectedGroupsSortedDescending() {
        ImageGroupFinder finder = new StubImageGroupFinder();
        BufferedImage img = new BufferedImage(3, 3, BufferedImage.TYPE_INT_RGB);

        List<Group> result = finder.findConnectedGroups(img);

        assertEquals(3, result.size(), "Should return 3 groups");
        assertTrue(result.get(0).size() >= result.get(1).size(), "Groups should be sorted in descending order");
        assertTrue(result.get(1).size() >= result.get(2).size(), "Groups should be sorted in descending order");
    }

    @Test
    public void testFindConnectedGroupsReturnsExpectedSizes() {
        ImageGroupFinder finder = new StubImageGroupFinder();
        BufferedImage img = new BufferedImage(4, 4, BufferedImage.TYPE_INT_RGB);

        List<Group> result = finder.findConnectedGroups(img);
        List<Integer> expectedSizes = Arrays.asList(5, 3, 1);

        assertEquals(expectedSizes.get(0), result.get(0).size());
        assertEquals(expectedSizes.get(1), result.get(1).size());
        assertEquals(expectedSizes.get(2), result.get(2).size());
    }

    @Test
    public void testFindConnectedGroupsThrowsExceptionForNullImage() {
        ImageGroupFinder finder = new StubImageGroupFinder();
        assertThrows(NullPointerException.class, () -> finder.findConnectedGroups(null),
                "Should throw NullPointerException for null image");
    }

    
}
