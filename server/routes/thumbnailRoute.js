import express from 'express'
import {thumbnail} from '../controllers/thumbnailController.js'

/**
 * Mount :filename to process the 1st frame of the video
 */

const router = express.Router();

router.get("/thumbnail/:filename", thumbnail)

export default router