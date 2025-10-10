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

### Group.java

- Takes in resolution origin (0,0) of binarized image
- Returns position of groups of collected values, and the centroid
- The compareTo helps compare the groups for order