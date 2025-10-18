import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;

public class DistanceImageBinarizerTest {

    private DistanceImageBinarizer binarizer;
    private EuclideanColorDistance distanceFinder;

    @BeforeEach
    public void setup() {
        distanceFinder = new EuclideanColorDistance();
        binarizer = new DistanceImageBinarizer(distanceFinder, 0xFF0000, 100); // Target: red
    }

    // ============================================================
    // 🧩 toBinaryArray() TESTS
    // ============================================================

    @Test
    public void toBinaryArray_ShouldReturnWhiteForExactTargetColor() {
        BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        img.setRGB(0, 0, 0xFF0000); // red → distance 0 → white

        int[][] result = binarizer.toBinaryArray(img);

        assertEquals(1, result[0][0]);
    }

    @Test
    public void toBinaryArray_ShouldReturnBlackForFarColor() {
        BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        img.setRGB(0, 0, 0x0000FF); // blue → far from red

        int[][] result = binarizer.toBinaryArray(img);

        assertEquals(0, result[0][0]);
    }

    @Test
    public void toBinaryArray_ShouldReturnBlackForFarColor_2() {
        BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        img.setRGB(0, 0, 0x00FF00); // green → not so far from red

        int[][] result = binarizer.toBinaryArray(img);

        assertEquals(0, result[0][0]);
    }

    @Test
    public void toBinaryArray_ShouldHandleSmall2x2ImageCorrectly() {
        BufferedImage img = new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB);
        img.setRGB(0, 0, 0xFF0000); // red → white
        img.setRGB(1, 0, 0x0000FF); // blue → black
        img.setRGB(0, 1, 0xFF1000); // close red → white
        img.setRGB(1, 1, 0x00FF00); // green → black

        int[][] result = binarizer.toBinaryArray(img);

        assertEquals(1, result[0][0]);
        assertEquals(0, result[0][1]);
        assertEquals(1, result[1][0]);
        assertEquals(0, result[1][1]);
    }

    @Test
    public void toBinaryArray_ShouldReturnBlackWhenDistanceEqualsThreshold() {
        DistanceImageBinarizer thresholdTest =
                new DistanceImageBinarizer(distanceFinder, 0xFF0000, 0); // zero threshold → everything black
        BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        img.setRGB(0, 0, 0xFF0000); // same color but threshold=0

        int[][] result = thresholdTest.toBinaryArray(img);

        assertEquals(0, result[0][0]); // because 0 is NOT less than 0
    }

    // ============================================================
    // 🎨 toBufferedImage() TESTS
    // ============================================================

    @Test
    public void toBufferedImage_ShouldConvertBinaryToCorrectColors() {
        int[][] binary = {
                {1, 0},
                {0, 1}
        };

        BufferedImage img = binarizer.toBufferedImage(binary);

        assertEquals(0xFFFFFF, img.getRGB(0, 0) & 0xFFFFFF);
        assertEquals(0x000000, img.getRGB(1, 0) & 0xFFFFFF);
        assertEquals(0x000000, img.getRGB(0, 1) & 0xFFFFFF);
        assertEquals(0xFFFFFF, img.getRGB(1, 1) & 0xFFFFFF);
    }

    @Test
    public void toBufferedImage_ShouldProduceSameDimensionsAsArray() {
        int[][] binary = {
                {1, 1, 0},
                {0, 1, 0}
        };

        BufferedImage img = binarizer.toBufferedImage(binary);

        assertEquals(3, img.getWidth());
        assertEquals(2, img.getHeight());
    }

    // ============================================================
    // 🔁 Integration Test — Round Trip
    // ============================================================

    @Test
    public void toBinaryArray_and_toBufferedImage_ShouldRoundTripCorrectly() {
        BufferedImage original = new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB);
        original.setRGB(0, 0, 0xFF0000); // red
        original.setRGB(1, 0, 0x0000FF); // blue
        original.setRGB(0, 1, 0x00FF00); // green
        original.setRGB(1, 1, 0xFF0000); // red

        int[][] binary = binarizer.toBinaryArray(original);
        BufferedImage output = binarizer.toBufferedImage(binary);

        // red → white (1), others → black (0)
        assertEquals(0xFFFFFF, output.getRGB(0, 0) & 0xFFFFFF);
        assertEquals(0x000000, output.getRGB(1, 0) & 0xFFFFFF);
        assertEquals(0x000000, output.getRGB(0, 1) & 0xFFFFFF);
        assertEquals(0xFFFFFF, output.getRGB(1, 1) & 0xFFFFFF);
    }
}
