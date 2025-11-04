import path from "path";
import {exec} from "child_process"

export const thumbnail = (req, res) => {
    const getVideo = path.join(process.cwd(), "..", "processor", "videos", req.params.filename);
    const getOutput = path.join(process.cwd(), "..", "processor", "sampleOutput", `${req.params.filename}.jpg`)

    exec(`ffmpeg -i "${getVideo}" -frames:v 1 "${getOutput}" -y`, (err) => {
        if (err) {
            console.log(err, "error here")
            return res.status(500).json({error: "Error generating thumbnail"})
        }
        res.sendFile(getOutput)
    })
}