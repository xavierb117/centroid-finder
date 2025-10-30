## Centroid Finder Part 2 Plan
- Chosen library is JavaCV, which can help with grabbing frames PER SECOND and grabbing the time of said frame.
- Frame is also able to be converted to BufferedImage, which is what centroid finder uses. 
- The 2 methods that could potentially help is getTimeStamp and getFrameRate. 
- getFrameRate will get the frames per second, we can use some conditions to only render one frame per second. 
- getTimeSamp will get the current timestamp. This is in microseconds which will be converted.
- New CSV will have to be made with time and coordinates. Time will be new from library methods, but the coordinates can be used from the largest centroid.

(Subject to change!)

1. Create the main app for video processing. This will be used to execute the command and grab the arguments. 
2. Object-Oriented: Create new files for converting to BufferedImage, result writer for the new csv, or using loop to only grab one frame. 
3. Classes will be created and used in the main.
4. When converted to BufferedImage, this is when we can use the old methods. Old methods require BufferedImage.
5. Older methods will handle the conversion to binary array and return a list of Groups for largest centroid and coordinates. 
6. Get the first row from CSV, should be the largest centroid. Will contain the coordinates needed for new CSV. Pass to result writer
7. Grab the time from the loop (may need to change), then pass to result writer.
8. Result writer will add to new CSV file (could need a list of new record just like group?) Should eventually return the CSV file.
9. Main method should return the new CSV file that contains time and coordinates.

TESTING: I got the first picture of the Salamander video, then I put it into an online tool called "imagecolorpicker.com" (https://imagecolorpicker.com/). Here, it would give me the color of the Salamander as a hex string. With the ImageSummaryApp, I inputted the picture into it to determine if the Salamander picture was good. It was good if the Salamander was big and white, while the edges of the picture were not white. I would go through the threshold and see if making it lower or higher would be better. Then, I settled on 43060a 80 which gave the Salamander a good white picture while also not catching the edges as much.