import express from "express";
import VideoRouter from "./routes/videosRoute.js";
import thumbnailRouter from "./routes/thumbnailRoute.js"
import processRouter from "./routes/processRoute.js"
import checkStatus from "./checks/loop.js"
import dotenv from "dotenv";
import cors from 'cors'

/**
 *  Root of express routes and how they are all mounted
 * 
 * @const app main variable that our express server runs on
 * @cors runs as a security measure to make sure we can 1. make requests from an external server, and 2. to ensure security to the server
 * @checkStatus is a function that returns a updated json file with (done, error) regarding the process time and video processed
 * 
 * app always listens on port 3000
 */


if (!process.env.NODE_ENV || process.env.NODE_ENV !== "production") {
  dotenv.config({ path: "../.env" });
}

const app = express();
app.use(express.json());
app.use(cors({
  origin: function (origin, callback) {
    const allowed = ["http://localhost:3000", "http://localhost:3001"];

    // Allow REST tools (Postman, Jest, curl) which send no origin
    if (!origin) return callback(null, true);

    if (allowed.includes(origin)) {
      return callback(null, true);
    }

    return callback(new Error("Not allowed by CORS"));
  }
}));

app.use("/", VideoRouter);
app.use("/", thumbnailRouter);
app.use("/", processRouter);
checkStatus()

const PORT = 3000;

if(process.env.NODE_ENV !== "test")
{
    app.listen(PORT, () => console.log(`Server running on http://localhost:${PORT}`));
}

export default app;