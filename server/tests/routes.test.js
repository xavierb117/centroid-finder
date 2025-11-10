import  request  from "supertest";
import app from "../main.js";

/*
Supertest is a JS libary
Jest is the testing framework
Babel is JS libary 

#Supertest Libary

    #request = Acts as a Virtual Machine almost and makes GETS and POST requests to the browser just as if we were typing it in ourselves
        -sends fake HTTP requests to the browser
-----------------------------------------------------------------------------------------------------------------------------------

#Jest Framework

    #describe = groups together similar or related tests

    #test = defines individual tests to run
        -utilizes a asynchronous function
        -makes a call to the main app
        -test the endpoint of the route

    #expect = makes assertions about what should be returned or the result 
        -toBe = small assertion

-----------------------------------------------------------------------------------------------------------------------------------

#Babel Libary

    #Babel = Acts as a translator for Jest
        -transforms our code into older code or more readable syntax that can run on any system
-----------------------------------------------------------------------------------------------------------------------------------
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