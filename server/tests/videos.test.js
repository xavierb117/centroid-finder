import request from "supertest"
import app from "../main.js"

//
//  TEST FOR /API/VIDEOS ROUTE
// To test run in terminal (npm test) -> runs all tests
// To test Individual cases, run (npx jest tests/"testFileName") 
//

describe("Videos Route", () => {

    test("GET /Videos should return a list of videos available to use", async () => {
        const res = await request(app).get("/api/videos")
        expect(res.status).toBe(200)
        expect(Array.isArray(res.body)).toBe(true)
    })

    test("GET /Videos should return error", async () => {
        const res = await request(app).get("/videos")
        expect(res.status).toBe(404)
    })

})