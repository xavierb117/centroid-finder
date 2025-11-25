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

    test("GET /process/:filename should return 500 if video file does not exist", async () => {
        jest.spyOn(fs, "existsSync").mockReturnValue(false);

        const res = await request(app).get("/process/notreal.mp4?targetColor=FFFFFF&threshold=20");

        expect(res.status).toBe(500);
        expect(res.body.error).toBe("Error starting job");

        jest.restoreAllMocks();
    });

    test("GET /process/:filename should delete existing output file", async () => {
        const exists = jest.spyOn(fs, "existsSync")
            .mockReturnValueOnce(true) 
            .mockReturnValue(true);    

        const unlink = jest.spyOn(fs, "unlinkSync").mockImplementation(() => {});
        jest.spyOn(fs, "writeFileSync").mockImplementation(() => {});

        const res = await request(app).get(
            "/process/ensantina.mp4?targetColor=00FF00&threshold=50"
        );

        expect(res.status).toBe(202);
        expect(unlink).toHaveBeenCalled();

        jest.restoreAllMocks();
    });
})