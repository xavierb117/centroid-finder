### squares.jpg
- The input image for the project.
- Consists of 4 sticky notes.

### binarized.png
- The output after using the sticky note image
- Includes the 4 sticky notes as white squares with pixels

### groups.csv
- The size of the found group of pixels
- Comes with x and y coordinates

### ImageSummaryApp.java
- Takes in command lines for image input, target hex color, and integer threshold
- Takes in the input image path (the sticky notes)
- Parses the target color from hex to a 24-bit integer
- Will then binarize the image by comparing each pixels color distance to the target color by marking 1 and 0.
- Converts the binary array and outputs the "binarized.png"
- Finds the connected group of pixels vertically and horizontally while computing group size and coordinates
- Creates the csv file containing one row per connected group with the format "size, x, y"

### DfsBinaryGroupFinder.java
- Finds the connected pixel groups of 1's in the integer array representing the binarized image
- Null array = NullPointerException, invalid array = IllegalArgumentException
- Returns a list of sorted Groups, could use matrix search to traverse and set 1's and 0's.
- Pixels connected vertically and horizontally, goes from top-left to bottom-right in matrix input

### Group.java
- A record that represents a group of contiguous pixels in an image, with size being the number of pixels and centroid as average of pixel coodinates.
- Takes in resolution origin (0,0) of binarized image
- Returns position of groups of collected values, and the centroid
- The compareTo helps compare the groups for order

### Coordinate.java
- A record, just like Group
- Represents a location in an image or array using (x, y)

### BinaryGroupFinder.java
- An interface that DfsBinaryGroupFinder implements, giving the findConnectedGroups method

### EuclideanColorDistance.java
- Takes in two 24 bit integers
- Converts to Hex Integers *(eg)*
```
128,64,32,16,8,4,2,1 - 8 Bit Binary

00000000 00000000 00000000 - 24 bits
01010110 00111100 10010101 - Random Example

86,60,149 - Converted to Base-10
56,3C,95 - Converted to Hex (Expected parameter)
```
- Compare distance between two values given
- Returns an integer for how far the color distance is from the given values

### ColorDistanceFinder.java
- The interface that EuclideanColorDistance implements, giving the distance method

### DistanceImageBinarizer.java
- Makes use of java.awt.image.BufferedImage. Will need to research and convert RRGGBB values respectively
- Helps determine whether each pixel should be black or white in the binarized image
- Helps convert a given BufferedImage into a binary 2d array of 1's and 0's, using color distance and threshold
- Helps convert binary 2d array into a buffered image
- This class will take in a ColorDistanceFinder, int targetColor, and int threshold
- Must use the other methods, ColorDistanceFinder and BinaryGroupFinder, to accomplish this...

### ImageBinarizer.java
- The interface that DistanceImageBinarizer implements, giving the methods toBinaryArray and toBufferedImage

### BinarizingImageGroupFinder.java
- Takes in an ImageBinarizer to convert an image to a 2d binary array
- Takes in a BinaryGroupFinder to find connected white pixel groups in the binary array
- Identified groups are returned in descending order

### ImageGroupFinder.java
- The interface that BinarizingImageGroupFinder implements, giving the findConnectedGroups method