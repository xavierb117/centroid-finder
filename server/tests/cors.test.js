import request from "supertest";
import app from "../main.js";

describe("CORS", () => {

test("allows requests from localhost:3000", async () => {
    const res = await request(app)
      .get("/videos")
      .set("Origin", "http://localhost:3000");

    expect(res.status).not.toBe(403);
    expect(res.headers["access-control-allow-origin"]).toBe("http://localhost:3000");
  });

})