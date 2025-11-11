import fs from "fs"
import path from "path"

export default function checkStatus() {
    const jobDir = path.join(process.cwd(), process.env.JOB)

    setInterval(() => {
        const files = fs.readdirSync(jobDir)

        for (const file of files) {
            const currentFile = path.join(jobDir, file)
            const data = JSON.parse(fs.readFileSync(currentFile, "utf8"))
            const {status, filename, jobId, time} = data

            if (status === "processing") {
                const outputPath = path.join(process.cwd(), process.env.OUTPUT_PATH, `${filename}.csv`)

                if (fs.existsSync(outputPath)) {
                    data.status = "done";
                    fs.writeFileSync(currentFile, JSON.stringify(data))

                    const archiveDir = path.join(process.env.ARCHIVE)
                    fs.renameSync(currentFile, path.join(archiveDir, `${jobId}`))
                }
                else if (Date.now() - time > 3 * 60 * 1000) { // THIS IS IN MILLISECONDS, MINUTES TIMES 60 TIMES 1000
                    data.status = "error"
                    fs.writeFileSync(currentFile, JSON.stringify(data))

                    const archiveDir = path.join(process.env.ARCHIVE)
                    fs.renameSync(currentFile, path.join(archiveDir, `${jobId}`))
                }
            }
        }
    }, 3000) // THIS IS SECONDS YOU WANT TIMES 1000, IT IS IN MILLISECONDS
}