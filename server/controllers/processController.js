import fs from "fs"
import {spawn} from "child_process";
import path from "path";
import { randomUUID } from "crypto";

/**
 * 
 *  Process vidoes, creating job ids and binarizing the video
 * 
 * @param req Express Request
 * @param res Express Response
 * @const targetcolor The Inputted targetcolor the process will be searching for
 * @const threshold How different the colors can be (foundColor vs targetColor)
 * @const jobId random UUID for each independent process
 * @const input video we will be processing against
 * @returns CSV file with values that repersent the threshold between the targetcolor and found colors/values
 */

export const startProcess = (req, res) => {
    try {
        const {filename} = req.params;
        const {targetColor, threshold} = req.query;

        if (!targetColor || !threshold)
        {
            return res.status(400).json({error: "Missing targetColor or threshold query parameter"})
        }


        function isValidHex(hex) {
            return /^([0-9A-Fa-f]{6})$/.test(hex);
        }

        function isValidThreshold(tolerance) {
            const num = Number(tolerance)
            return Number.isInteger(num);
        }

        if (!isValidHex(targetColor)) {
            return res.status(400).json({error: "Invalid targetColor. Color must be 6-digit hex RGB (00FF00)."})
        }

        if (!isValidThreshold(threshold)) {
            return res.status(400).json({error: "Invalid threshold. Threshold must be a number"})
        }

        let output = path.resolve(process.env.OUTPUT_PATH, `${filename}.csv`)
        if (fs.existsSync(output)) {
            fs.unlinkSync(output)
        }

        const jobId = randomUUID();
        const input = path.resolve(process.env.VIDEOS, filename)

        if (!fs.existsSync(input)) {
            return res.status(500).json({error: "Error starting job"})
        }
        
        const jobDir = path.resolve(process.env.JOB, `${jobId}`)

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

/**
 *  Finds the status of the given job Id above to show status of processing (error, done)
 * 
 * @param req Express Request
 * @param res Express Response
 * 
 * Finds current jobid within the jobs folder and processes it to view the status 
 *  
 */


export const getProcess = (req, res) => {
    try {
        const {jobId} = req.params;
        const currentJobFile = path.resolve(process.env.JOB, `${jobId}`)
        let currentFile = currentJobFile;

        if (!fs.existsSync(currentFile)) {
            const currentArchiveFile = path.resolve(process.env.ARCHIVE, `${jobId}`)
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
        else if (data.status === "done") {
            const out = path.resolve(process.env.OUTPUT_PATH, `${data.filename}.csv`);
            const size = fs.statSync(out).size;

            if (size < 10) {
                return res.status(200).json({ status: "error", error: "Output file empty → FFmpeg failed" });
            }

            return res.status(200).json({
                status: "done",
                result: out
            });
        }
        else {
            return res.status(500).json({error: "Error fetching job status"})
        }
        
    } catch (err) {
        return res.status(500).json({error: "Error fetching job status"})
    }
}