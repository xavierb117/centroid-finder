import fs from "fs"
import {spawn} from "child_process";
import path from "path";
import { randomUUID } from "crypto";

export const startProcess = (req, res) => {
    try {
        const {filename} = req.params;
        const {targetColor, threshold} = req.query;

        if (!targetColor || !threshold)
        {
            return res.status(400).json({error: "Missing targetColor or threshold query parameter"})
        }

        const jobId = randomUUID();
        const input = path.join(process.cwd(), process.env.VIDEOS, filename)

        if (!fs.existsSync(input)) {
            return res.status(500).json({error: "Error starting job"})
        }

        const output = path.join(process.cwd(), process.env.OUTPUT_PATH, `${filename}.csv`)
        const jobDir = path.join(process.cwd(), process.env.JOB, `${jobId}.status`)

        fs.writeFileSync(jobDir, JSON.stringify({
            status: "processing",
            filename,
            jobId,
            startTime: Date.now()
        }))


        const processJob = spawn("java", [
            process.env.JAR, 
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

export const getProcess = (req, res) => {
    const job = jobs[req.params.jobId];
    if(!job) return res.status(404).json({error: "Job ID not found"})
    res.status(202).json(job);
}