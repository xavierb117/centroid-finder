import fs from "fs";
import path from "path";

/**
 * Read a directory and return the results
 * 
 * @param req Express Request
 * @param res Express Response
 * @const dir passes a path from the .env file to the interal input (Videos) file
 */

export const video =  (req, res) => {
    const dir = process.env.VIDEOS
    fs.readdir(dir, (err, files) => {
        if(err)
        {
            return res.status(500).json({error: "Error reading video directory"})
        }
        res.status(200).json(files)
    })
}