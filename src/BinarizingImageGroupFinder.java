import java.awt.image.BufferedImage;
import java.util.List;

public class BinarizingImageGroupFinder implements ImageGroupFinder {
    private final ImageBinarizer binarizer;
    private final BinaryGroupFinder groupFinder;

    public BinarizingImageGroupFinder(ImageBinarizer binarizer, BinaryGroupFinder groupFinder) {
        this.binarizer = binarizer;
        this.groupFinder = groupFinder;
    }

    @Override
    public List<Group> findConnectedGroups(BufferedImage image) {
        return null;
    }
    
}
