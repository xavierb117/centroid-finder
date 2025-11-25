import express from 'express'
import {startProcess, getProcess} from '../controllers/processController.js'

/**
 * Mount :filename to be given as a input
 * Mount :jobId to be searched after processed
 */

const router = express.Router();

router.get("/process/:filename", startProcess)
router.get("/process/:jobId/status", getProcess)

export default router;