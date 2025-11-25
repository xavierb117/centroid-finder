package io.github.xavierb117.centroidfinder;

import java.util.List;
import java.awt.image.BufferedImage;

public class FramePasserToBinarizer implements FrameBinarizer {
    private BufferedImage bufferedImage;
    private int threshold;
    private int targetColor;

    public FramePasserToBinarizer(BufferedImage bufferedImage, int threshold, int targetColor) {
        this.bufferedImage = bufferedImage;
        this.threshold = threshold;
        this.targetColor = targetColor;
    }

    @Override
    public List<Group> binarizerPasser() {
        DfsBinaryGroupFinder finder = new DfsBinaryGroupFinder();
        EuclideanColorDistance colorDistance = new EuclideanColorDistance();
        DistanceImageBinarizer binarizer = new DistanceImageBinarizer(colorDistance, targetColor, threshold);
        BinarizingImageGroupFinder groupsToReturn = new BinarizingImageGroupFinder(binarizer, finder);

        List<Group> groups = groupsToReturn.findConnectedGroups(bufferedImage);
        return groups;
    }
}
