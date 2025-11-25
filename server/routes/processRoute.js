import express from 'express'
import {startProcess, getProcess} from '../controllers/processController.js'



const router = express.Router();

router.get("/process/:filename", startProcess)
router.get("/process/:jobId/status", getProcess)

export default router;