import express from "express";
import VideoRouter from "./routes/videos.js";

const app = express();
app.use(express.json());

app.use("/api/videos", VideoRouter);

const PORT = 3000;

app.listen(PORT, () => console.log(`Server running on http://localhost:${PORT}`));