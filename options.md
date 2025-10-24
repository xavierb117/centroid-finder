## BoofCV
Pros:
- Covers low-level image processing.
- Has detection/tracking.
- Organized into several packages, use different packages for different functionalities. Comes with JavaDoc.
Cons:
- No mention of mp4 videos...

## JavaCV
Pros:
- Comes with geometric color calibration. Could be useful for salamander color tracking?
- Contains detection and matching of feature points. Could be useful for tracking salamnder.
- Has the Maven dependency! Functionality/methods also seems very broad.
Cons:
- Lacks documentation, must use the sample section to understand.
- Seems to be a steeper learning curve compared to BoofCV from the samples. Ex. Will need to convert BufferedImage using methods.
- Potentially might need to download more apps if needed.

## JCodec
Pros:
- Supports many formats, including MP4
- Includes Maven Dependency, and is purely Java which could help with simplicity.
- Can extract specific frames in videos, may be useful for frames of salamanders
Cons: 
- There is no documentation, just sample code
- There are performance and quality issues to consider
- Methods seem limited compared to JavaCV and BoofCV. It contains methods that capture frames, Tape Timecode, and DPX metadata. There could be more hidden methods, but the ones in the sample code haven't shown too much. 