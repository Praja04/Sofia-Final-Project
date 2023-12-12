import express from "express";
import {
    Register,
    Login,
    Me,
    Logout
} from "../controllers/Auth.js";
// import { verifyUser } from "../middleware/AuthUser.js";

const router = express.Router();


router.post('/register', Register);
router.get('/me', Me);
router.post('/login', Login);
router.delete('/logout', Logout);

export default router;