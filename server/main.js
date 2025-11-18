import express from "express";
import VideoRouter from "./routes/videosRoute.js";
import thumbnailRouter from "./routes/thumbnailRoute.js"
import processRouter from "./routes/processRoute.js"
import checkStatus from "./checks/loop.js"
import dotenv from "dotenv";

if (!process.env.NODE_ENV || process.env.NODE_ENV !== "production") {
  dotenv.config({ path: "../.env" });
}

const app = express();
app.use(express.json());

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