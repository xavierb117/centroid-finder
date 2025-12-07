import fs from "fs"
import path from "path"

/**
 * Creates a JSON output to show users (processing, error, done). Utilizes a Interval to recgonize if the process needs to be an Error, or if it has processed correctly.
 * @const jobDir finds the specific job processes are being ran against
 * 
 * Interval time is based off milliseconds and is set based off how long the video will take to be processed
 */

export default function checkStatus() {
    if (process.env.NODE_ENV === "test") return;

    const jobDir = process.env.JOB

    setInterval(() => {
        const files = fs.readdirSync(jobDir)

        for (const file of files) {
            const currentFile = path.join(jobDir, file)

            try {
                const data = JSON.parse(fs.readFileSync(currentFile, "utf-8"))
                const {status, filename, jobId, startTime} = data

                if (status === "processing") {
                    const outputPath = path.join(process.env.OUTPUT_PATH, `${filename}.csv`)

                    if (fs.existsSync(outputPath)) {
                        data.status = "done";
                        fs.writeFileSync(currentFile, JSON.stringify(data, null, 2))

                        const archiveDir = process.env.ARCHIVE
                        fs.renameSync(currentFile, path.join(archiveDir, `${jobId}`))
                    }
                    else if (Date.now() - startTime > 5 * 60 * 1000) { // THIS IS IN MILLISECONDS, MINUTES TIMES 60 TIMES 1000
                        data.status = "error"
                        fs.writeFileSync(currentFile, JSON.stringify(data, null, 2))

                        const archiveDir = process.env.ARCHIVE
                        fs.renameSync(currentFile, path.join(archiveDir, `${jobId}`))
                    }
                }
            } catch(err) {
                console.log("Error processing job file")    
            }
        }
    }, 5000) // THIS IS SECONDS YOU WANT TIMES 1000, IT IS IN MILLISECONDS
}