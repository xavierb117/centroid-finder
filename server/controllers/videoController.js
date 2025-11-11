import fs from "fs";
import path from "path";

export const video =  (req, res) => {
    const dir = path.join(process.cwd(), process.env.VIDEOS);
    fs.readdir(dir, (err, files) => {
        if(err)
        {
            return res.status(500).json({error: "Error reading directory"})
        }
        res.status(200).json(files)
    })
}