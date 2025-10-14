public class EuclideanColorDistance implements ColorDistanceFinder {
    /**
     * Returns the euclidean color distance between two hex RGB colors.
     * 
     * Each color is represented as a 24-bit integer in the form 0xRRGGBB, where
     * RR is the red component, GG is the green component, and BB is the blue component,
     * each ranging from 0 to 255.
     * 
     * The Euclidean color distance is calculated by treating each color as a point
     * in 3D space (red, green, blue) and applying the Euclidean distance formula:
     * 
     * sqrt((r1 - r2)^2 + (g1 - g2)^2 + (b1 - b2)^2)
     * 
     * This gives a measure of how visually different the two colors are.
     * 
     * @param colorA the first color as a 24-bit hex RGB integer
     * @param colorB the second color as a 24-bit hex RGB integer
     * @return the Euclidean distance between the two colors
     */
    @Override
    public double distance(int colorA, int colorB) {

        //Define ColorA hex colors
        int redA = (colorA & 0xFF0000) >> 16;
        int greenA = (colorA & 0x00FF00) >> 8;
        int blueA = colorA & 0x0000FF;
        
        //Define ColorB hex colors
        int redB = (colorB & 0xFF0000) >> 16;
        int greenB = (colorB & 0x00FF00) >> 8;
        int blueB = colorB & 0x0000FF;

        //need to work on more
        int redSolve = (redA - redB) * 2;
        int greenSolve = (greenA - greenB) * 2;
        int blueSolve = (blueA - blueB) * 2;

        //not entirely sure if it works lol
        return Math.sqrt(Math.pow(redSolve, 2) + Math.pow(greenSolve, 2) + Math.pow(blueSolve, 2));
    }
}
