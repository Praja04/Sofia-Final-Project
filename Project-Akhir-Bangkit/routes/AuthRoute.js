import express from "express";

import { verifyUser } from "../middleware/AuthUser.js";
import Saldo from "../models/SaldoModel.js";
import {
    Register,
    Login,
    Me,
    Logout,
    getSaldo,
    withdrawSaldo 
} from "../controllers/Auth.js";
// import { verifyUser } from "../middleware/AuthUser.js";

const router = express.Router();


router.post('/register', Register);
router.get('/me', Me);
router.get('/saldo', getSaldo);
router.post('/withdraw', verifyUser, withdrawSaldo);
router.post('/login', Login);
router.delete('/logout', Logout);

router.get('/saldo-masuk/', verifyUser, async (req, res) => {
    const user_id = req.session.userId;
    
    try {
        const saldoMasuk = await Saldo.findAll({
            where: {
                userId: user_id,
                jenisSaldo: 'masuk',
            },
            attributes: ['id', 'totalSaldo','jenisSaldo'],
        });

        res.status(200).json(saldoMasuk);
    } catch (error) {
        res.status(500).json({ msg: error.message });
    }
});

router.get('/saldo-keluar/', verifyUser, async (req, res) => {
    const user_id = req.session.userId;
    
    try {
        const saldoKeluar = await Saldo.findAll({
            where: {
                userId: user_id,
                jenisSaldo: 'keluar',
            },
            attributes: ['id', 'totalSaldo','jenisSaldo'],
        });

        res.status(200).json(saldoKeluar);
    } catch (error) {
        res.status(500).json({ msg: error.message });
    }
});
export default router;