import  request  from "supertest";
import app from "../main.js";

/*
supertest is a JS libary
Jest is the testing framework

    #describe = groups together similar or related tests

    #test = defines individual tests to run
        #utilizes a asynchronous function
        #makes a call to the main app
        #test the endpoint of the route

    #expect = makes assertions about what should be returned or the result 
        #toBe = small assertion
*/

describe("Express API routes", () => {
    test("GET /Videos should return a list of videos available to use", async () => {
        const res = await request(app).get("/api/videos");
        expect(res.status).toBe(200);
        expect(Array.isArray(res.body)).toBe(true);
    })

    test("GET /Videos should return error", async () => {
        const res = await request(app).get("/videos");
        expect(res.status).toBe(404);
    })
})