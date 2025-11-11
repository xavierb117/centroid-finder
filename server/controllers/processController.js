import {spawn} from "child_process";
import path from "path";
import { randomUUID } from "crypto";

const jobs = {};

export const startProcess = (req, res) => {
    const {filename} = req.params;
    const {targetColor, threshold} = req.query;

    if(!targetColor || !threshold)
    {
        return res.status(400).json({error: "Need TargetColor and or Threshold"})
    }

    const jobId = randomUUID();
    const input = path.join(process.cwd(), "..", "processor", "videos", filename)
    const output = path.join(process.cwd(), "..", "processor", "sampleOutput", `${filename}.csv`)


    const processJob = spawn("java", [
        "-jar", 
        "../processor/target/videoprocessor-jar-with-dependencies.jar",
        input,
        output,
        targetColor,
        threshold
    ], {
        detached: true,
        stdio: "ignore"
    })

    processJob.unref()

    jobs[jobId] = {status: "processing"}

    processJob.on("close", (code) => {
        if(code === 0)
        {
            console.log(`[JAVA SUCCESS] CSV created: ${output}`);
            jobs[jobId] = {status: "done", result: `/sampleOutput/${filename}.csv`}
        }
        else
        {
            jobs[jobId] = {status: "error", error: "error processing"}
        }
    })

    res.status(202).json({jobId});
}

export const getProcess = (req, res) => {
    const job = jobs[req.params.jobId];
    if(!job) return res.status(404).json({error: "Job ID not found"})
    res.status(202).json(job);
}