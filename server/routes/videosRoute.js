import express from "express";
import {video} from '../controllers/videoController.js'

/**
 * Mount videos to return the list of videos within the given directory
 */

const router = express.Router();

router.get("/api/videos", video)

export default router;