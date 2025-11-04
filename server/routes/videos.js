import express from "express";
import fs from "fs";
import path from "path";

const router = express.Router();

router.get("/", (req, res) => {
    const dir = path.join(process.cwd(), "..", "processor", "videos");
    fs.readdir(dir, (err, files) => {
        if(err)
        {
            console.log(`${err} error here`);
            return res.status(500).json({error: "Error reading directory"})
        }
        res.json(files)
    })
})

export default router;