import path from "path";
import {exec} from "child_process"

/**
 * Capture the 1st frame from the inputted video and display to the user
 * 
 * @param req Express Request 
 * @param res Express Response
 * @const getVideo obtains the input with the same name passed in with the (:filename)
 * @const getOutput creates a new image to display to the user
 * 
 * @exec calls to the command line
 */

export const thumbnail = (req, res) => {
    const getVideo = path.resolve(process.env.VIDEOS, req.params.filename);
    const getOutput = path.resolve(process.env.OUTPUT_PATH, `${req.params.filename}.jpg`)

    exec(`ffmpeg -i "${getVideo}" -frames:v 1 "${getOutput}" -y`, (err) => {
        if (err) {
            return res.status(500).json({error: "Error generating thumbnail"})
        }
        res.status(200).sendFile(getOutput)
    })
}