import express from "express";
import {
    getProducts,
    getProductsById,
    createProducts,
    updateProducts,
    deleteProducts,
    getStokProducts
} from "../controllers/Produk.js";
import { verifyUser } from "../middleware/AuthUser.js";

const router = express.Router();


router.get('/products', verifyUser, getProducts);
router.get('/products/:id', verifyUser, getProductsById);
router.post('/products', verifyUser, createProducts);
router.patch('/products/:id', verifyUser, updateProducts);
router.delete('/products/:id', verifyUser, deleteProducts);
router.get('/products/stok/:id',verifyUser, getStokProducts);

export default router;