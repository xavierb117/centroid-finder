import express from 'express'
import {thumbnail} from '../controllers/thumbnailController.js'

const router = express.Router();

router.get("/thumbnail/:filename", thumbnail)

export default router