package io.github.xavierb117.centroidfinder;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class EuclideanColorDistanceTest {
    @Test
    public void testSameColor() {
        EuclideanColorDistance colorUtils = new EuclideanColorDistance();
        double distance = colorUtils.distance(0xFFFFFF, 0xFFFFFF);
        assertEquals(0.0, distance, 1e-9);
    }

    @Test
    public void testBlackAndWhite() {
        EuclideanColorDistance colorUtils = new EuclideanColorDistance();
        double distance = colorUtils.distance(0x000000, 0xFFFFFF);
        // sqrt((255)^2 * 3) = sqrt(195075) ≈ 441.6729
        assertEquals(441.6729, distance, 1e-4);
    }

    @Test
    public void testRedAndGreen() {
        EuclideanColorDistance colorUtils = new EuclideanColorDistance();
        double distance = colorUtils.distance(0xFF0000, 0x00FF00);
        // sqrt((255)^2 + (255)^2) = sqrt(130050) ≈ 360.6245
        assertEquals(360.6245, distance, 1e-4);
    }

    @Test
    public void testRedAndBlue() {
        EuclideanColorDistance colorUtils = new EuclideanColorDistance();
        double distance = colorUtils.distance(0xFF0000, 0x0000FF);
        assertEquals(360.6245, distance, 1e-4);
    }

    @Test
    public void testGreenAndBlue() {
        EuclideanColorDistance colorUtils = new EuclideanColorDistance();
        double distance = colorUtils.distance(0x00FF00, 0x0000FF);
        assertEquals(360.6245, distance, 1e-4);
    }

    @Test
    public void testSymmetry() {
        EuclideanColorDistance colorUtils = new EuclideanColorDistance();
        double d1 = colorUtils.distance(0x123456, 0x654321);
        double d2 = colorUtils.distance(0x654321, 0x123456);
        assertEquals(d1, d2, 1e-9);
    }

    @Test
    public void testRedChannelDifferenceOnly() {
        EuclideanColorDistance colorUtils = new EuclideanColorDistance();
        // Only red differs by 100 (from 0x640000 to 0x000000)
        double distance = colorUtils.distance(0x640000, 0x000000);
        assertEquals(100.0, distance, 1e-9);
    }

    @Test
    public void testGreenChannelDifferenceOnly() {
        // 0x006400 = green = 100, red = 0, blue = 0    
        EuclideanColorDistance colorUtils = new EuclideanColorDistance();
        double distance = colorUtils.distance(0x006400, 0x000000);
        assertEquals(100.0, distance, 1e-9);
    }

    @Test
    public void testBlueChannelDifferenceOnly() {
        // 0x000064 = blue = 100, red = 0, green = 0
        EuclideanColorDistance colorUtils = new EuclideanColorDistance();
        double distance = colorUtils.distance(0x000064, 0x000000);
        assertEquals(100.0, distance, 1e-9);
    }

    @Test
    public void testArbitraryColorDistance() {
        // Arbitrary colors: #336699 (R=51, G=102, B=153) and #6699CC (R=102, G=153, B=204)
        int colorA = 0x336699;
        int colorB = 0x6699CC;

        // Manual expected calculation:
        // ΔR = 102 - 51 = 51
        // ΔG = 153 - 102 = 51
        // ΔB = 204 - 153 = 51
        // distance = sqrt(51² + 51² + 51²) = sqrt(3 * 2601) = sqrt(7803) ≈ 88.3345912
        double expected = 88.335;

        EuclideanColorDistance colorUtils = new EuclideanColorDistance();
        double distance = colorUtils.distance(colorA, colorB);

        assertEquals(expected, distance, 1e-3);
    }
}
