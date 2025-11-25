import request from "supertest"
import app from "../main.js"
import fs from "fs"

//
//  TEST FOR /PROCESS/:JOBID/STATUS
// To test run in terminal (npm test) -> runs all tests
// To test Individual cases, run (npx jest tests/"testFileName") 
//

beforeAll(() => {
    process.env.JOB = "jobs"
    process.env.ARCHIVE = "archive"
    process.env.OUTPUT_PATH = "output"
})

describe("Process Status Route", () => {

    test("creating the jobId then testing GET /process/:jobId/status", async () => {
        const jobRes = await request(app).get("/process/ensantina.mp4?targetColor=00FF00&threshold=50")
        expect(jobRes.status).toBe(202)
        const jobId = jobRes.body.jobId

        await new Promise((res) => setTimeout(res, 100))

        const statusRes = await request(app).get(`/process/${jobId}/status`)
        expect([200, 202, 404]).toContain(statusRes.status)
    })

    test("GET /process/:jobId/status should return 404 because no ID was given", async () => {
        const res = await request(app).get("/process/:jobId/status")
        expect(res.status).toBe(404)
        expect(res.body.error).toMatch("Job ID not found")
    })

    test("GET /process/:jobId/status should return 500 if the JSON file is invalid", async () => {
        jest.spyOn(fs, "existsSync").mockReturnValue(true)
        jest.spyOn(fs, "readFileSync").mockImplementation(() => "{ invalid JSON }")

        const res = await request(app).get("/process/whatever/status")
        expect(res.status).toBe(500)
        expect(res.body.error).toBe("Error fetching job status")

        jest.restoreAllMocks()
    })

})