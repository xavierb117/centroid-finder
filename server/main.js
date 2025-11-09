import express from "express";
import VideoRouter from "./routes/videosRoute.js";
import thumbnailRouter from "./routes/thumbnailRoute.js"
import processRouter from "./routes/processRoute.js"

const app = express();
app.use(express.json());

app.use("/", VideoRouter);
app.use("/", thumbnailRouter);
app.use("/", processRouter);

const PORT = 3000;

app.listen(PORT, () => console.log(`Server running on http://localhost:${PORT}`));