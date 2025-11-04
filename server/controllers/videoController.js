import fs from "fs";
import path from "path";

export const video =  (req, res) => {
    const dir = path.join(process.cwd(), "..", "processor", "videos");
    fs.readdir(dir, (err, files) => {
        if(err)
        {
            console.log(`${err} error here`);
            return res.status(500).json({error: "Error reading directory"})
        }
        res.json(files)
    })
}