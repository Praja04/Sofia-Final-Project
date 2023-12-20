// TransaksiRoute.js

import express from "express";
import {
  getTransaksi,
  getTransaksiById,
  createTransaksi,
  updateTransaksi,
  deleteTransaksi
} from "../controllers/Transaksi.js";
import { verifyUser } from "../middleware/AuthUser.js";

const router = express.Router();

router.get('/transaksi', verifyUser, getTransaksi);
router.get('/transaksi/:id', verifyUser, getTransaksiById);
router.post('/transaksi', verifyUser, createTransaksi);
router.patch('/transaksi/:id', verifyUser, updateTransaksi);
router.delete('/transaksi/:id', verifyUser, deleteTransaksi);

export default router;
