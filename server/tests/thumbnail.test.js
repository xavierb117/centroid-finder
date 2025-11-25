import request from "supertest"
import app from "../main.js"

//
//  TEST FOR /THUMBNAIL/:FILENAME ROUTE
// To test run in terminal (npm test) -> runs all tests
// To test Individual cases, run (npx jest tests/"testFileName") 
//

describe("Thumbnail Route", () => {

    test("GET /thumbnail/:filename should return a 500 error for trouble generating", async () => {
        const res = await request(app).get("/thumbnail/:filename")
        expect(res.status).toBe(500)
    })

    test("GET /thumbnail/:filename should return a 200 code for processing", async () => {
        const res = await request(app).get("/thumbnail/ensantina.mp4")
        expect(res.status).toBe(200)
    })

})