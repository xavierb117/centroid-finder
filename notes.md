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
- Takes in resolution origin (0,0) of binarized image
- Returns position of groups of collected values, and the centroid
- The compareTo helps compare the groups for order

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