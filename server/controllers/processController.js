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
}

export const getProcess = (req, res) => {

}