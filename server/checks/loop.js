import fs from "fs"
import path from "path"

export default function checkStatus() {
    if (process.env.NODE_ENV === "test") return;

    const jobDir = path.join(process.cwd(), process.env.JOB)

    setInterval(() => {
        const files = fs.readdirSync(jobDir)

        for (const file of files) {
            const currentFile = path.join(jobDir, file)

            try {
                const data = JSON.parse(fs.readFileSync(currentFile, "utf-8"))
                const {status, filename, jobId, startTime} = data

                if (status === "processing") {
                    const outputPath = path.join(process.cwd(), process.env.OUTPUT_PATH, `${filename}.csv`)

                    if (fs.existsSync(outputPath)) {
                        data.status = "done";
                        fs.writeFileSync(currentFile, JSON.stringify(data, null, 2))

                        const archiveDir = path.join(process.cwd(), process.env.ARCHIVE)
                        fs.renameSync(currentFile, path.join(archiveDir, `${jobId}`))
                    }
                    else if (Date.now() - startTime > 1 * 60 * 1000) { // THIS IS IN MILLISECONDS, MINUTES TIMES 60 TIMES 1000
                        data.status = "error"
                        fs.writeFileSync(currentFile, JSON.stringify(data, null, 2))

                        const archiveDir = path.join(process.cwd(), process.env.ARCHIVE)
                        fs.renameSync(currentFile, path.join(archiveDir, `${jobId}`))
                    }
                }
            } catch(err) {
                console.log("Error processing job file")    
            }
        }
    }, 5000) // THIS IS SECONDS YOU WANT TIMES 1000, IT IS IN MILLISECONDS
}