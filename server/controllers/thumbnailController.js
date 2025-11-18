import path from "path";
import {exec} from "child_process"

export const thumbnail = (req, res) => {
    const getVideo = path.join(process.env.VIDEOS, req.params.filename);
    const getOutput = path.join(process.env.OUTPUT_PATH, `${req.params.filename}.jpg`)

    exec(`ffmpeg -i "${getVideo}" -frames:v 1 "${getOutput}" -y`, (err) => {
        if (err) {
            return res.status(500).json({error: "Error generating thumbnail"})
        }
        res.status(200).sendFile(getOutput)
    })
}