package io.github.xavierb117.centroidfinder;

import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class VideoApp {
    // WILL TAKE IN: inputPath, outputCsv, targetColor, and threshold
    // MUST GENERATE A CSV THAT TRACKS THE LARGEST CENTROID OVER TIME. FIRST COLUMN IS SECONDS< THEN X AND Y COORDINATES
    // NO CENTROID RETURNS -1, -1
    public static void main(String[] args) {
        if (args.length < 4) {
            System.out.println("Usage: java -jar videoprocessor.jar inputPath outputCsv targetColor threshold");
            return;
        }

        String inputPath = args[0];
        String outputCsv = args[1];
        String hexTargetColor = args[2];
        String colorThreshold = args[3];

        try {
            Path path = Paths.get(inputPath);
            if (!Files.exists(path) || !Files.isRegularFile(path)) {
                System.out.println("Error: The inputPath is invalid. Please input again.");
                return;
            }
        } catch (InvalidPathException e) {
            System.out.println("Error: The path is invalid. Please input again.");
            return;
        }

        try {
            Path path = Paths.get(outputCsv);
            if (!Files.exists(path) || !Files.isDirectory(path) || !Files.isWritable(path)) {
                System.out.println("Error: The outputCsv is invalid. Please input again.");
                return;
            }
        } catch (InvalidPathException e) {
            System.out.println("Error: The outputCsv is invalid. Please input again.");
            return;
        }

        int targetColor = 0;
        try {
            targetColor = Integer.parseInt(hexTargetColor, 16);
        } catch(NumberFormatException e) {
            System.out.println("Error: The targetColor is not a valid hexadecimal RGB string (6-digit). Please try in the RRGGBB format.");
            return;
        }

        int threshold = 0;
        try {
            threshold = Integer.parseInt(colorThreshold);
        } catch (NumberFormatException e) {
            System.out.println("Error: The threshold is not a valid integer. Please input again as an integer.");
            return;
        }

        
    }
}
