## Understanding
- Make HTTP requests and responses with express, seems to be 3 get requests and 1 post request. Might have to touch up on it again...
- These requests are going to have to interact with out processor folder that holds our Java logic. How would we start this out? How does express connect to Java? Something we will have to figure out...
- Start Video Processing Job is the post request, it seems to take in the targetColor and the threshold, both fields required in the JAR command. Based on this information, this is where it will analyze the video just like the VideoApp. What will jobId ultimately do?
- The Get Processing Job status seems to be related to the post request as it gets the status for the post request. It has 5 responses in total, with 3 OK's that return "processing" "done" or "error". Then there is a 404 and 500 error. 
- Generating thumbnail will extract and return the first frame. The 200 response will return the JPEG binary data, this is where ffmpeg will come in handy. But how can we use it in express?
- List Available Videos will return a list of all video files in the directory, this is where we could use the salamander video. It says videos/VIDEO_NAME so will we have to add a folder that holds the video? Since videos can't be uploaded, this route will probably solve that issue since this could be where express accesses the video. 
- How will express call the JAR? From the API instructions, it could lie in the post request? Here, this is where the targetColor and threshold is written, so the post request may have to interact with the JAR, but how? Does express have commands to call it?

## Server Plan

- Utilize built-in javascript imports such as **randomUUID, path, spawn, exec and fs**
- Main point of connection **router/app** will be used to make routes towards existing logic within VideoApp.java
- The first GET method will return all the videos found within the sampleInput Directory, we will need to access the directoy and view its contents with a router GET method.
- The second GET method extracts a single frame from the video and returns it as a JPEG(image). We could utilize **ffmpeg** within the **exec** built-in javascript method to run against our VideoApp.java to grab a single frame and return it.
- The Third method will be a POST method. We need it as a POST method to pass queries and parameters. This method will utilize **randomUUID** to make unique identifiers for each time we run the method. We can also utilize the **spawn** built-in method to call VideoApp.java to gather our output. This should be when the **JAR** command will run.
- Our final Get will need the unique identifiers given from the **randomUUID** from the previous POST method. This will enable us to view if a process has failed or succeeded.
- Our plan will utilize a file-system rather than a Database. We will use the unique Identfiers provided by **randomUUID** to distingusish between different processes ran.