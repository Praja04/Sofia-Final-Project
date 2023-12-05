import express from "express";
import {
    getUsers,
    getUsersById,
    createUsers,
    updateUsers,
    deleteUsers
} from "../controllers/Users.js";
import { verifyUser } from "../middleware/AuthUser.js";

const router = express.Router();

router.get('/users', verifyUser, getUsers);
router.get('/users/:id', verifyUser, getUsersById);
router.post('/users', verifyUser, createUsers);
router.patch('/users/:id', verifyUser, updateUsers);
router.delete('/users/:id', verifyUser, deleteUsers);

export default router;