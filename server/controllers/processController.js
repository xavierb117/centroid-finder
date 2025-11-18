import fs from "fs"
import {spawn} from "child_process";
import path from "path";
import { randomUUID } from "crypto";

export const startProcess = (req, res) => {
    try {
        const {filename} = req.params;
        const {targetColor, threshold} = req.query;

        let output = path.join(process.env.OUTPUT_PATH, `${filename}.csv`)
        if (fs.existsSync(output)) {
            fs.unlinkSync(output)
        }

        if (!targetColor || !threshold)
        {
            return res.status(400).json({error: "Missing targetColor or threshold query parameter"})
        }

        const jobId = randomUUID();
        const input = path.join(process.env.VIDEOS, filename)

        if (!fs.existsSync(input)) {
            return res.status(500).json({error: "Error starting job"})
        }
        
        const jobDir = path.join(process.env.JOB, `${jobId}`)

        fs.writeFileSync(jobDir, JSON.stringify({
            status: "processing",
            filename,
            jobId,
            startTime: Date.now()
        }, null, 2))


        const processJob = spawn("java", [
            "-jar", 
            process.env.TARGET,
            input,
            output,
            targetColor,
            threshold
        ], {
            detached: true,
            stdio: "ignore"
        })

        processJob.unref()

        res.status(202).json({jobId});
    } catch (err) {
        return res.status(500).json({error: "Error starting job"})
    }
}
// if(!job) return res.status(404).json({error: "Job ID not found"})
export const getProcess = (req, res) => {
    try {
        const {jobId} = req.params;
        const currentJobFile = path.join(process.env.JOB, `${jobId}`)
        let currentFile = currentJobFile;

        if (!fs.existsSync(currentFile)) {
            const currentArchiveFile = path.join(process.env.ARCHIVE, `${jobId}`)
            if (fs.existsSync(currentArchiveFile)) {
                currentFile = currentArchiveFile;
            }
            else {
                return res.status(404).json({error: "Job ID not found"})
            }
        }
        
        const data = JSON.parse(fs.readFileSync(currentFile, "utf-8"))

        if (data.status === "processing") {
            return res.status(200).json({status: "processing"})
        }
        else if (data.status === "done") {
            return res.status(200).json({status: "done", result: path.join(process.env.OUTPUT_PATH, `${data.filename}.csv`)})
        }
        else if (data.status === "error") {
            return res.status(200).json({status: "error", error: "Error processing video: Unexpected ffmpeg error"})
        }
        else {
            return res.status(500).json({error: "Error fetching job status"})
        }
        
    } catch (err) {
        return res.status(500).json({error: "Error fetching job status"})
    }
}