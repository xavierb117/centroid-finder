package io.github.xavierb117.centroidfinder;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class FramePasserToBinarizerTest {

    @Test
    @DisplayName("Empty image: no near-target pixels -> empty groups")
    void noTargetPixels_returnsEmpty() {
        BufferedImage img = new BufferedImage(4, 3, BufferedImage.TYPE_INT_RGB);
        int red = new Color(255, 0, 0).getRGB();

        FramePasserToBinarizer passer = new FramePasserToBinarizer(img, 10, red);
        List<Group> groups = passer.binarizerPasser();

        assertNotNull(groups);
        assertTrue(groups.isEmpty(), "Expected no groups for fully black image");
    }

    @Test
    @DisplayName("Single target pixel -> one group")
    void singlePixel_oneGroup() {
        BufferedImage img = new BufferedImage(5, 5, BufferedImage.TYPE_INT_RGB);
        int red = new Color(255, 0, 0).getRGB();
        img.setRGB(2, 3, red);

        FramePasserToBinarizer passer = new FramePasserToBinarizer(img, 10, red);
        List<Group> groups = passer.binarizerPasser();

        assertEquals(1, groups.size(), "Expected exactly one group for one red pixel");
    }

    @Test
    @DisplayName("4-directional connectivity only (diagonals don't connect)")
    void diagonalsAreSeparate() {
        BufferedImage img = new BufferedImage(3, 3, BufferedImage.TYPE_INT_RGB);
        int red = new Color(255, 0, 0).getRGB();

        img.setRGB(0, 0, red);
        img.setRGB(1, 1, red); // diagonal only

        FramePasserToBinarizer passer = new FramePasserToBinarizer(img, 10, red);
        List<Group> groups = passer.binarizerPasser();

        assertEquals(2, groups.size(), "Diagonal-only pixels should form separate groups");
    }

    @Test
    @DisplayName("Adjacent target pixels -> merged into one group")
    void adjacencyMerges() {
        BufferedImage img = new BufferedImage(3, 1, BufferedImage.TYPE_INT_RGB);
        int red = new Color(255, 0, 0).getRGB();

        img.setRGB(0, 0, red);
        img.setRGB(1, 0, red); // horizontally adjacent

        FramePasserToBinarizer passer = new FramePasserToBinarizer(img, 10, red);
        List<Group> groups = passer.binarizerPasser();

        assertEquals(1, groups.size(), "Adjacent red pixels should merge into one group");
    }

    @Test
    @DisplayName("Threshold controls inclusion: near-red included only when high enough")
    void thresholdControlsInclusion() {
        BufferedImage img = new BufferedImage(4, 1, BufferedImage.TYPE_INT_RGB);
        int red = new Color(255, 0, 0).getRGB();
        int nearRed = new Color(250, 5, 5).getRGB(); // close to red

        img.setRGB(0, 0, red);
        img.setRGB(1, 0, nearRed);

        // Low threshold excludes nearRed
        List<Group> low = new FramePasserToBinarizer(img, 5, red).binarizerPasser();
        assertEquals(1, low.size(), "Low threshold should exclude near-red pixel");

        // Higher threshold includes nearRed
        List<Group> high = new FramePasserToBinarizer(img, 15, red).binarizerPasser();
        assertEquals(1, high.size(), "High threshold should include near-red (merged group)");
    }
}
