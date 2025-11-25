package io.github.xavierb117.centroidfinder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * The Video Application processor that runs the entire Video processor logic based on a command line.
 * This path takess in the JAR, an input path representing the video, an outputCSV representing the file to write to, a targetColor hex, and a threshold.
 * Validates input for each line in the command.
 * 
 * Overall, it will call FrameGrabber to grab the specific frame, which will then be analyzed and binarized. These frames gets saved to a TimeCoordinate list,
 * which will be used in VideoResultWriter to actually write to the file the user typed in from outputCsv.
 * 
 * @param args The array of strings that holds each of the 4 parts of the JAR command. This will be validated and converted appropriately to write a CSV file of time and coordinates.
 */
public class VideoApp {
    public static void main(String[] args) {
        if (args.length < 4) {
            System.out.println("Usage: java -jar ../processor/target/videoprocessor-jar-with-dependencies.jar inputPath outputCsv targetColor threshold");
            return;
        }

        String inputPath = args[0];
        String outputCsv = args[1];
        String hexTargetColor = args[2];
        String colorThreshold = args[3];

        try {
            Path path = Paths.get(inputPath);
            if (!path.isAbsolute()) {
                path = Path.of(System.getProperty("user.dir")).resolve(inputPath);
            }

            if (!Files.exists(path) || !Files.isRegularFile(path)) {
                System.out.println("Error: The inputPath is invalid. Please input again.");
                return;
            }
        } catch (InvalidPathException e) {
            System.out.println("Error: The path is invalid. Please input again.");
            return;
        }

        try {
        Path outputPath = Paths.get(outputCsv);
        Path parentDir = outputPath.getParent();

        // If no parent (e.g., user just typed a filename), use current directory
            if (parentDir == null) {
                parentDir = Path.of(System.getProperty("user.dir"));
            }
        
            // Create folder if it doesn’t exist
            if (!Files.exists(parentDir)) {
                Files.createDirectories(parentDir);
            }
        
            // Final safety check
            if (!Files.isDirectory(parentDir) || !Files.isWritable(parentDir)) {
                System.out.println("Error: The output directory is invalid: " + parentDir.toAbsolutePath());
                return;
            }
        } catch (IOException e) {
            System.out.println("Error: The output path is invalid or cannot be created.");
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

        try {
            FrameGrabber grabber = new FrameGrabber(targetColor, threshold, inputPath);
            var results = grabber.analysis();

            Path outputPath = Paths.get(outputCsv);
            if (Files.isDirectory(outputPath)) {
                outputPath = outputPath.resolve("results.csv");
            }
        
            try (VideoResultWriter writer = new VideoResultWriter(outputPath.toString())) {
                for (TimeCoordinate tc : results) {
                    writer.write(tc.sec(), tc.centroid());
                }
            }
            catch (IOException e) {
                System.out.println("Error: Failed to create or write to the output CSV");
                return;
            }
        } catch (Exception e) {
            System.out.println("Error: Failed to read files and write to CSV");
            e.printStackTrace();
            return;
        } 
    }
}
