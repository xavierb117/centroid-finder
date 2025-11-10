import  request  from "supertest";
import app from "../main.js";

describe("Express API routes", () => {
    test("GET /Videos should return a list of videos available to use", async () => {
        const res = await request(app).get("/api/videos");
        expect(res.status).toBe(200);
        expect(Array.isArray(res.body)).toBe(true);
    })
})