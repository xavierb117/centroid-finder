import java.awt.image.BufferedImage;

public interface ImageBinarizer {
    public int[][] toBinaryArray(BufferedImage image);

    public BufferedImage toBufferedImage(int[][] image);
}
