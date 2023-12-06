import express from "express";
import {
    getUsers,
    getUsersById,
    createUsers,
    updateUsers,
    deleteUsers
} from "../controllers/Users.js";
import { verifyUser, isAdmin } from "../middleware/AuthUser.js";

const router = express.Router();

router.get('/users', verifyUser, isAdmin, getUsers);
router.get('/users/:id', verifyUser, isAdmin, getUsersById);
router.post('/users', verifyUser, isAdmin, createUsers);
router.patch('/users/:id', verifyUser, isAdmin, updateUsers);
router.delete('/users/:id', verifyUser, isAdmin, deleteUsers);

export default router;