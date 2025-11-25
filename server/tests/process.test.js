import request from "supertest"
import app from "../main.js"
import fs from "fs"

describe("Process Route", () => {

    //
    // /process/:filename
    // To test run in terminal (npm test) -> runs all tests
    // To test Individual cases, run (npx jest tests/"testFileName") 
    //

    test("GET /process:filename should work properly and as expected", async () => {
        const res = await request(app).get("/process/ensantina.mp4?targetColor=00FF00&threshold=50")
        expect(res.status).toBe(202)
        expect(res.body).toHaveProperty("jobId")
    })

    test("GET /process/:filename should return 400 because of no params", async () => {
        const res = await request(app).get("/process/ensantina.mp4")
        expect(res.status).toBe(400)
        expect(res.body).toHaveProperty("error")
        expect(res.body.error).toMatch("Missing targetColor or threshold query parameter")
    })


})